package btill.terminal.values;

import com.google.protobuf.InvalidProtocolBufferException;
import org.bitcoin.protocols.payments.Protos;
import org.bitcoin.protocols.payments.Protos.Payment;
import org.bitcoinj.core.Coin;

public class SignedBill {

    private byte[] serialisedPayment;
    private GBP gbpAmount;
    private Coin btcAmount;

    public SignedBill(Protos.Payment payment, GBP gbpAmount, Coin btcAmount) {
        serialisedPayment = payment.toByteArray();
        this.gbpAmount = gbpAmount;
        this.btcAmount = btcAmount;
    }

    public byte[] getSerialisedPayment() {
        return serialisedPayment;
    }

    public Protos.Payment getPayment() throws InvalidProtocolBufferException {
        return Protos.Payment.parseFrom(serialisedPayment);
    }

    public GBP getGbpAmount() {
        return gbpAmount;
    }

    public Coin getBtcAmount() {
        return btcAmount;
    }
}
