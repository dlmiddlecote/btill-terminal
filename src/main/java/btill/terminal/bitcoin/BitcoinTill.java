package btill.terminal.bitcoin;

import btill.terminal.*;
import btill.terminal.values.*;
import org.bitcoin.protocols.payments.Protos.Payment;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Wallet;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * BitcoinTill currently builds a {@link btill.terminal.values.Bill} which contains a payment request pointing
 * to the Till's assigned {@link Wallet}.
 */

public class BitcoinTill implements Till {

    private final GBP2SatoshisExchangeRate exchange;

    private Wallet wallet = null;
    private String memo = null;
    private String paymentURL = null;
    private byte[] merchantData = null;
    private BillBuilder billBuilder;

    public BitcoinTill(GBP2SatoshisExchangeRate exchange) {

        this.exchange = exchange;
    }

    public Bill createBillForAmount(GBP amount) {
        System.out.println("Amount: " + amount.toString() + ": Bitcoin: " + exchange.getSatoshis(amount).toString());
        billBuilder = new BillBuilder();
        billBuilder.setAmount(exchange.getSatoshis(amount));
        billBuilder.setMemo(memo);
        billBuilder.setPaymentURL(paymentURL);
        billBuilder.setMerchantData(merchantData);
        billBuilder.setWallet(wallet);
        return billBuilder.build();
    }

    public Bill createBillForAmount(Coin amount) {
        billBuilder = new BillBuilder();
        billBuilder.setAmount(amount);
        billBuilder.setMemo(memo);
        billBuilder.setPaymentURL(paymentURL);
        billBuilder.setMerchantData(merchantData);
        billBuilder.setWallet(wallet);
        return billBuilder.build();
    }

    public String getAmount(GBP amount) {
        return exchange.getSatoshis(amount).toPlainString();
    }

    public GBP getGBP(Menu menu) {
        GBP totalCost = new GBP(0);

        for (MenuItem item: menu) {
            totalCost = totalCost.plus(item.getPrice().times(item.getQuantity()));
            System.out.println("Item: " + item.getName() + "\n" + "Quantity: " + item.getQuantity() + "\n");
        }

        return totalCost;
    }
    @Override
    public Bill createBillForAmount(Menu menu) {
        GBP totalCost = new GBP(0);

        for (MenuItem item: menu) {
            totalCost = totalCost.plus(item.getPrice().times(item.getQuantity()));
            System.out.println("Item: " + item.getName() + "\n" + "Quantity: " + item.getQuantity() + "\n");
        }

        return createBillForAmount(totalCost);
    }

    public Receipt settleBillUsing(Payment withPayment, GBP amount) {
        // TODO Pay for Bill using withPayment and return Receipt of transaction
        // Probably want merchant info
        // bitcoins & pounds transacted

        // Receipt receipt = new Receipt().build(withPayment.getGBPamount(), withPayment.getBitcoinAmount(),
        // withPayment.getMemo());
        // To make a receipt we need amount transacted in GBP and bitcoins, and order ID from memo
        //return receipt;
        return new Receipt(new Date(), amount, exchange.getSatoshis(amount));
    }


    /**
     * Sets the {@link Wallet} to which payment requests are directed - probably
     * not a security hazard?
     */
    public void setWallet(Wallet wallet) {
        wallet = wallet;
    }

    /**
     * Returns the current receiving Address of the {@link Wallet} to which
     * payment requests are directed.
     */
    public Address getWalletAddress() {
        return wallet.currentReceiveAddress();
    }

    /**
     * Returns the current Balance of the {@link Wallet} to which payment
     * requests are directed.
     */
    public Coin getWalletBalance() {
        return wallet.getBalance();
    }

    /**
     * Saves the {@link Wallet} to file.
     */
    public void saveWallet(File walletFile) {
        try {
            wallet.saveToFile(walletFile);
            System.err.println("Wallet saved to " + walletFile.getPath());
        } catch (IOException e) {
            System.err.println("Cannot save wallet to file "
                    + walletFile.getPath());
            e.printStackTrace();
        }
    }

    /**
     * Sets the Memo contained in the {@link Bill} - probably a list of
     * purchased items
     */
    public void setMemo(String memo) {
        memo = memo;
    }

    /**
     * Sets the PaymentURL contained in the {@link Bill} - a secure (HTTPS)
     * location to send a Payment message to receive a PaymentACK
     */
    public void setPaymentURL(String paymentURL) {
        paymentURL = paymentURL;
    }

    /**
     * Sets the Merchant Data contained in the {@link Bill} - a means to
     * identify the Payment Request
     */
    public void setMerchantData(byte[] merchantData) {
        merchantData = merchantData;
    }
}
