package btill.terminal.values;

import com.google.protobuf.InvalidProtocolBufferException;
import org.bitcoin.protocols.payments.Protos;
import org.bitcoinj.core.Coin;
import org.bitcoinj.protocols.payments.PaymentProtocol;

public class Receipt {

    private final GBP gbp;
    private final Coin bitcoins;
    private byte[] paymentACKbytes = null;

    public Receipt(Protos.Payment forPayment, GBP amount, Coin btcAmount) {
        paymentACKbytes = PaymentProtocol.createPaymentAck(forPayment, "TRANSACTION SUCCEEDED:\n" + forPayment.getMemo()).toByteArray();
        gbp = amount;
        bitcoins = btcAmount;
    }

    public GBP getGbp() {
        return gbp;
    }

    public Coin getBitcoins() {
        return bitcoins;
    }

    public Protos.PaymentACK getPaymentACK() {
        try {
            return Protos.PaymentACK.parseFrom(paymentACKbytes);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }
}