package btill.terminal.bitcoin;

import btill.terminal.values.Bill;
import btill.terminal.values.GBP;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Wallet;

/**
 * BillBuilder creates a {@link btill.terminal.values.Bill}. Set each of the BIP70 fields, then build!
 */
public class BillBuilder {

    private String memo = null;
    private String paymentURL = null;
    private byte[] merchantData = null;
    private Coin amount = null;
    private GBP gbpAmount = null;
    private Wallet wallet = null;
    private Boolean freshAddress = true;

    public Bill build() {
        return new Bill(memo, paymentURL, merchantData, amount, gbpAmount, wallet, freshAddress);
    }

    public BillBuilder setMemo(String memo) {
        this.memo = memo;
        return this;
    }

    public BillBuilder setPaymentURL(String paymentURL) {
        this.paymentURL = paymentURL;
        return this;
    }

    public BillBuilder setMerchantData(byte[] merchantData) {
        this.merchantData = merchantData;
        return this;
    }

    public BillBuilder setAmount(Coin amount) {
        this.amount = amount;
        return this;
    }

    public BillBuilder setGBPAmount(GBP amount) {
        this.gbpAmount = amount;
        return this;
    }

    public BillBuilder setWallet(Wallet wallet) {
        this.wallet = wallet;
        return this;
    }

    public BillBuilder setFreshAddress(Boolean freshAddress) {
        this.freshAddress = freshAddress;
        return this;
    }


}
