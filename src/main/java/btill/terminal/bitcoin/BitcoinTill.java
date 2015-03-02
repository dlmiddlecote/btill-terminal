package btill.terminal.bitcoin;

import btill.terminal.*;
import btill.terminal.bluetooth.SignedBill;
import btill.terminal.values.*;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.protobuf.InvalidProtocolBufferException;
import org.bitcoinj.core.*;

import java.util.concurrent.*;

import static java.lang.System.exit;

/**
 * BitcoinTill currently builds a {@link btill.terminal.values.Bill} which contains a payment request pointing
 * to the Till's assigned {@link Wallet}.
 */

public class BitcoinTill implements Till {

    private final GBP2SatoshisExchangeRate exchange;

    private WalletKitThread walletKitThread = null;
    private String memo = "Thank you for your order!";
    private String paymentURL = "www.b-till.com";
    private byte[] merchantData = null;
    private BillBuilder billBuilder;

    private ExecutorService pool = Executors.newFixedThreadPool(10);
    private Receipt receipt = null;

    public BitcoinTill(GBP2SatoshisExchangeRate exchange) {
        this.exchange = exchange;
        this.setUpWalletThread();
        this.startWalletThread();
        while (!this.walletKitThread.isRunning())
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("\nWallet set-up interrupted!"); // TODO CHANGE TO LOGGING FORMAT
                e.printStackTrace();
            }

    }

    public Bill createBillForAmount(GBP amount) {
        // TODO CHANGE TO LOGGING FORMAT (OR DELETE!)
        System.out.println("Amount: " + amount.toString() + ": Bitcoin: " + exchange.getSatoshis(amount).toString());
        billBuilder = new BillBuilder();
        billBuilder.setAmount(exchange.getSatoshis(amount));
        billBuilder.setMemo(memo);
        billBuilder.setPaymentURL(paymentURL);
        billBuilder.setMerchantData(merchantData);
        billBuilder.setWallet(getWallet());
        return billBuilder.build();
    }

    // Adam says: Never used, but might be used in a future where people actually use Bitcoin
    public Bill createBillForAmount(Coin amount) {
        billBuilder = new BillBuilder();
        billBuilder.setAmount(amount);
        billBuilder.setMemo(memo);
        billBuilder.setPaymentURL(paymentURL);
        billBuilder.setMerchantData(merchantData);
        billBuilder.setWallet(getWallet());
        return billBuilder.build();
    }

    public String getAmount(GBP amount) {
        return exchange.getSatoshis(amount).toPlainString();
    }

    public GBP getGBP(Menu menu) {
        GBP totalCost = new GBP(0);

        for (MenuItem item: menu) {
            totalCost = totalCost.plus(item.getPrice().times(item.getQuantity()));
            System.out.println("Item: " + item.getName() + "\n" + "Quantity: " + item.getQuantity() + "\n"); // TODO CHANGE TO LOGGING FORMAT?
        }

        return totalCost;
    }
    @Override
    public Bill createBillForAmount(Menu menu) {
        GBP totalCost = new GBP(0);

        for (MenuItem item: menu) {
            totalCost = totalCost.plus(item.getPrice().times(item.getQuantity()));
            System.out.println("Item: " + item.getName() + "\n" + "Quantity: " + item.getQuantity() + "\n"); // TODO CHANGE TO LOGGING FORMAT
        }

        return createBillForAmount(totalCost);
    }

    public Future<Receipt> settleBillUsing(SignedBill signedBill) {

        receipt = null;

        // SEND TRANSACTION TO BLOCKCHAIN
        try {
            Transaction tx = new Transaction(walletKitThread.getWalletAppKit().params(),
                    signedBill.getPayment().getTransactions(0).toByteArray());
            getWallet().commitTx(tx);
            Futures.addCallback(walletKitThread.getWalletAppKit()
                            .peerGroup().broadcastTransaction(tx),
                    new FutureCallback<Transaction>() {
                        public void onSuccess(Transaction tx) {
                            System.out.println("Transaction succeeded"); // TODO CHANGE TO LOGGING FORMAT

                            // CREATE RECEIPT
                            try {
                                receipt = new Receipt(signedBill.getPayment());
                            } catch (InvalidProtocolBufferException e) {
                                System.err.println("\nCould not extract payment!"); // TODO CHANGE TO LOGGING FORMAT
                                e.printStackTrace();
                                exit(1);
                            }


                        }

                        public void onFailure(Throwable t) {
                            receipt = null;
                            System.out.println("Transaction failed"); // TODO CHANGE TO LOGGING FORMAT
                        }
                    });
        } catch (VerificationException e) {
            System.err.println("\nPayment Verification Failed!"); // TODO CHANGE TO LOGGING FORMAT
            e.printStackTrace();
            exit(1);
        } catch (InvalidProtocolBufferException e) {
            System.err.println("\nCould not extract payment!"); // TODO CHANGE TO LOGGING FORMAT
            e.printStackTrace();
            exit(1);
        }

        return pool.submit(new Callable<Receipt>() {
            @Override
            public Receipt call() throws Exception {
                // TODO I have no idea if this will work - seems a little hacky!
                while (receipt == null)
                    Thread.sleep(1000);
                return receipt;
            }
        });
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

    public void setUpWalletThread(){
        walletKitThread = new WalletKitThread(/*new File("tillWallet"),*/"till");
    }

    public void startWalletThread(){
        walletKitThread.start();
    }

    public Wallet getWallet(){
        return walletKitThread.getWalletAppKit().wallet();
    }
}
