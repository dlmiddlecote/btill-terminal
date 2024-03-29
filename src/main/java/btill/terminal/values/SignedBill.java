package btill.terminal.values;

import btill.terminal.values.Location.LocationData;
import com.google.protobuf.InvalidProtocolBufferException;
import org.bitcoin.protocols.payments.Protos;
import org.bitcoinj.core.Coin;

/**
 * SignedBill acts as a wrapper class for a {@link org.bitcoin.protocols.payments.Protos.Payment}. It is constructed by
 * the Phone in response to a {@ Bill} and sent to the {@link btill.terminal.bitcoin.BitcoinTill}  to be pushed to the
 * blockchain.
 */
public class SignedBill {

    private byte[] serialisedPayment;
    private GBP gbpAmount;
    private Coin btcAmount;
    private int orderId;
    private LocationData locationData;

    public SignedBill(int orderId, Protos.Payment payment, GBP gbpAmount, Coin btcAmount, LocationData locationData) {
        this.orderId = orderId;
        serialisedPayment = payment.toByteArray();
        this.gbpAmount = gbpAmount;
        this.btcAmount = btcAmount;
        this.locationData = locationData;
    }

    public byte[] getSerialisedPayment() {
        return serialisedPayment;
    }

    public Protos.Payment getPayment() {
        try {
            return Protos.Payment.parseFrom(serialisedPayment);
        } catch (InvalidProtocolBufferException e) {
            return null;
        }
    }

    public GBP getGbpAmount() {
        return gbpAmount;
    }

    public Coin getBtcAmount() {
        return btcAmount;
    }

    public int getOrderId() {
        return orderId;
    }

    public LocationData getLocationData() {
        return locationData;
    }
}