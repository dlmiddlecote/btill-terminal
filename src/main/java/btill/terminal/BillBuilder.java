package btill.terminal;

import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Wallet;

public class BillBuilder {

    private String memo = null;
    private String paymentURL = null;
    private byte[] merchantData = null;
    private Coin amount = null;
    private Wallet wallet = null;

    Bill build() {
        return new Bill(memo, paymentURL, merchantData, amount, wallet);
    }

    BillBuilder setMemo(String memo) {
        this.memo = memo;
        return this;
    }

    BillBuilder setPaymentURL(String paymentURL) {
        this.paymentURL = paymentURL;
        return this;
    }

    BillBuilder setMerchantData(byte[] merchantData) {
        this.merchantData = merchantData;
        return this;
    }

    BillBuilder setAmount(Coin amount) {
        this.amount = amount;
        return this;
    }

    BillBuilder setAmount(GBP amount) {
        // currently, I just convert these to BTC...
        this.amount = GBP.getSatoshis(amount);
        return this;
    }

    BillBuilder setWallet(Wallet wallet) {
        this.wallet = wallet;
        return this;
    }

}
