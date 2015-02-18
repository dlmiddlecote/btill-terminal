package btill.terminal;

import org.bitcoin.protocols.payments.Protos.Payment;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Wallet;

import java.io.File;
import java.io.IOException;

/**
 * BitcoinTill currently builds a {@link Bill} which contains a payment request pointing
 * to the Till's assigned {@link Wallet}.
 */

public class BitcoinTill implements Till {

    private Wallet wallet = null;
    private String memo = null;
    private String paymentURL = null;
    private byte[] merchantData = null;
    private BillBuilder billBuilder;

    @Override
    public Bill createBillForAmount(GBP amount) {
        billBuilder = new BillBuilder();
        billBuilder.setAmount(amount);
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

    public Receipt settleBillUsing(Payment withPayment) {
        // TODO Pay for Bill using withPayment and return Receipt of transaction
        // Probably want merchant info
        // bitcoins & pounds transacted

        // Receipt receipt = new Receipt().build(withPayment.getGBPamount(), withPayment.getBitcoinAmount(),
        // withPayment.getMemo());
        // To make a receipt we need amount transacted in GBP and bitcoins, and order ID from memo
        // return receipt;
        return null;
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
