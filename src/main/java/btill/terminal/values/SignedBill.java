package btill.terminal.values;

import com.google.protobuf.InvalidProtocolBufferException;
import org.bitcoin.protocols.payments.Protos.Payment;

public class SignedBill {

    private byte[] serialisedPayment;

    public SignedBill(Payment payment) {
        serialisedPayment = payment.toByteArray();
    }

    public byte[] getSerialisedPayment() {
        return serialisedPayment;
    }

    public Payment getPayment() throws InvalidProtocolBufferException {
        return Payment.parseFrom(serialisedPayment);
    }
}
