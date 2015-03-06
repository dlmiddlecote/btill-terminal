package btill.terminal.values;

import com.google.protobuf.InvalidProtocolBufferException;
import org.bitcoin.protocols.payments.Protos;
import org.bitcoinj.core.Coin;

/**
 * SignedBill acts as a wrapper class for a {@link org.bitcoin.protocols.payments.Protos.Payment}. It is constructed by
 * the Phone in response to a {@ Bill} and sent to the {@link btill.terminal.bitcoin.BitcoinTill}  to be pushed to the
 * blockchain.
 */
public class SignedBill {

    private final GBP gbpAmount;
    private final Coin btcAmount;
    private byte[] serialisedPayment;

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
