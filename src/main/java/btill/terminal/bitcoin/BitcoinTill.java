package btill.terminal.bitcoin;

import btill.terminal.Till;
import btill.terminal.values.Bill;
import btill.terminal.values.GBP;
import btill.terminal.values.Receipt;
import btill.terminal.values.SignedBill;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.bitcoinj.core.*;
import org.bitcoinj.params.TestNet3Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.test.Test;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * BitcoinTill deals with the financial transactions of the program.
 * It runs a {@link btill.terminal.bitcoin.WalletKitThread}, builds a {@link btill.terminal.values.Bill} which
 * contains a payment request pointing and settles a {@link btill.terminal.values.SignedBill} by pushing a transaction
 * to the Bitcoin blockchain, returning a {@link btill.terminal.values.Receipt}.
 */

public class BitcoinTill implements Till, WalletKitThreadInterface {

    protected static final Logger Log = LoggerFactory.getLogger(BitcoinTill.class);
    private static final double CONVERSION_RATE = penceExchangeRate();
    private final GBP2SatoshisExchangeRate exchange;
    private WalletKitThread walletKitThread = null;
    private String walletKitThreadFilePrefix = null;
    private String walletKitThreadFolder = null;
    private String memo = "Thank you for your order!";
    private String paymentURL = "www.b-till.com";
    private byte[] merchantData = null;
    private BillBuilder billBuilder = null;
    private Receipt receipt = null;

    /**
     * Constructs a BitcoinTill with the specified exchange rate, starting the associated {@link WalletKitThread}.
     */
    public BitcoinTill(@Nullable String walletKitThreadFolder, @Nullable String walletKitThreadFilePrefix) {
        this.exchange = new GBP2SatoshisExchangeRate(CONVERSION_RATE);
        if (walletKitThreadFolder != null)
            this.walletKitThreadFolder = walletKitThreadFolder;
        if (walletKitThreadFilePrefix != null)
            this.walletKitThreadFilePrefix = walletKitThreadFilePrefix;
        this.setUpWalletThread();
        this.startWalletThread();
        while (!this.walletKitThread.isRunning())   // BitcoinTill won't start up until wallet is running
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.error("\nWallet set-up interrupted!");
                e.printStackTrace();
            }

    }

    /**
     * Sets the conversion rate between GBP pence and BTC satoshis using a web call.
     */
    private static double penceExchangeRate() {
        String gbp = null;
        try {
            gbp = Unirest.get("https://community-bitcointy.p.mashape.com/price/GBP")
                    .header("X-Mashape-Key", "lkv6KPZ302mshKYByfU1gD0iqSsqp1OV2l0jsnlc4oIyhtpR8p")
                    .header("Accept", "text/plain")
                    .asString().getBody();
        } catch (UnirestException e) {
            Log.info("Error");
        }
        if (gbp != null) {
            Double pence = Double.parseDouble(gbp);
            Log.info("Exchange rate: " + (100000000.0 / pence) + " satoshis / GBP");
            return 1000000.0 / pence;
        }
        Log.info("Exchange rate: 0.0");
        return 0.0;
    }

    public static double getConversionRate() {
        return CONVERSION_RATE;
    }

    public String getMemo() {
        return memo;
    }

    public String getPaymentURL() {
        return paymentURL;
    }

    public byte[] getMerchantData() {
        return merchantData;
    }

    /**
     * Creates a {@link Bill} for the specified amount in {@link GBP}.
     */
    public Bill createBillForAmount(GBP amount) {

        Log.info("Amount: " + amount.toString() + ": Bitcoin: " + exchange.getSatoshis(amount).toString());

        billBuilder = new BillBuilder();
        billBuilder.setAmount(exchange.getSatoshis(amount));
        billBuilder.setGBPAmount(amount);
        billBuilder.setMemo(memo);
        billBuilder.setPaymentURL(paymentURL);
        billBuilder.setMerchantData(merchantData);
        billBuilder.setWallet(getWallet());

        return billBuilder.build();
    }

    /**
     * Creates a {@link Bill} for the specified amount in {@link GBP}.
     */
    public Bill createBillForAmount(GBP amount, String order) {

        Log.info("Amount: " + amount.toString() + ": Bitcoin: " + exchange.getSatoshis(amount).toString());

        billBuilder = new BillBuilder();
        billBuilder.setAmount(exchange.getSatoshis(amount));
        billBuilder.setGBPAmount(amount);
        memo = order;
        billBuilder.setMemo(order);
        billBuilder.setPaymentURL(paymentURL);
        billBuilder.setMerchantData(merchantData);
        billBuilder.setWallet(getWallet());

        return billBuilder.build();
    }

    /**
     * Creates a {@link Bill} for the specified amount in {@link Coin}.
     */
    public Bill createBillForAmount(Coin amount) {

        billBuilder = new BillBuilder();
        billBuilder.setAmount(amount);
        billBuilder.setGBPAmount(exchange.getGBP(amount));
        billBuilder.setMemo(memo);
        billBuilder.setPaymentURL(paymentURL);
        billBuilder.setMerchantData(merchantData);
        billBuilder.setWallet(getWallet());

        return billBuilder.build();
    }

    /**
     * Returns the {@link Receipt} from pushing the transaction in the {@link SignedBill} to the Bitcoin blockchain.
     * Uses {@link java.util.concurrent.Future} to await the result of broadcasting the transaction, so will not return
     * immediately.
     * Make sure not to block I/O by calling this method!
     */
    public Receipt settleBillUsing(SignedBill signedBill) {

        Log.info("\nReceived Bill");
        Transaction tx = null;

        // extract the transaction from the SignedBill - assumes only one transaction!
        tx = new Transaction(walletKitThread.getWalletAppKit().params(),
                signedBill.getPayment().getTransactions(0).toByteArray());

        // awaits confirmation of broadcast to blockchain
        Future<Transaction> transactionFuture = walletKitThread.getWalletAppKit().peerGroup().broadcastTransaction(tx);
        Log.info("\nBroadcast Transaction");
        Transaction txReturn = null;
        try {
            txReturn = transactionFuture.get(15, TimeUnit.SECONDS);
            Log.info("\nTransaction Broadcast Returned!");

            // build receipt for signedBill
            receipt = new Receipt(signedBill.getPayment(), signedBill.getGbpAmount(), signedBill.getBtcAmount(), new Date(System.currentTimeMillis()), signedBill.getOrderId());

        } catch (InterruptedException e) {
            Log.error("\nReceipt retrieval interrupted");
            receipt = null;
           // e.printStackTrace();
        } catch (ExecutionException e) {
            Log.error("\nReceipt retrieval execution interrupted!");
            receipt = null;
            //e.printStackTrace();
        } catch (TimeoutException e) {
            Log.error("\nReceipt retrieval timed out");
            receipt = null;
            //e.printStackTrace();
        }

        // commits transaction to the blockchain
        if (receipt != null) {
            //getWallet().maybeCommitTx(tx);
            if (getWallet().maybeCommitTx(tx)) {
                Log.info("\nCommitted Transaction");
                Log.info(getWallet().toString(false,false,false,null));
                Log.info("\nPending Transactions");
                Log.info(getWallet().getPendingTransactions().toString());
            }
        }

        return receipt;
    }

    /**
     * Sets the Memo contained in the {@link Bill} - probably a list of
     * purchased items
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * Sets the PaymentURL contained in the {@link Bill} - a secure (HTTPS)
     * location to send a Payment message to receive a PaymentACK
     */
    public void setPaymentURL(String paymentURL) {
        this.paymentURL = paymentURL;
    }

    /**
     * Sets the Merchant Data contained in the {@link Bill} - a means to
     * identify the Payment Request
     */
    public void setMerchantData(byte[] merchantData) {
        this.merchantData = merchantData;
    }

    /**
     * Sets up the {@link btill.terminal.bitcoin.WalletKitThread}.
     */
    public void setUpWalletThread() {
        walletKitThread = new WalletKitThread(walletKitThreadFolder, walletKitThreadFilePrefix);
    }

    /**
     * Starts the {@link btill.terminal.bitcoin.WalletKitThread}.
     */
    public void startWalletThread() {
        walletKitThread.start();
    }

    /**
     * Stops the {@link btill.terminal.bitcoin.WalletKitThread}.
     */
    public void stopWalletThread() {
        while (walletKitThread.isRunning())
            walletKitThread.terminate();
    }

    public Boolean isRunning(){
        return walletKitThread.isRunning();
    }

    /**
     * Returns the {@link org.bitcoinj.core.Wallet} wrapped inside the {@link btill.terminal.bitcoin.WalletKitThread}.
     */
    public Wallet getWallet() {
        return walletKitThread.getWalletAppKit().wallet();
    }
}
