package btill.terminal.values;

import org.bitcoin.protocols.payments.Protos;
import org.bitcoinj.protocols.payments.PaymentProtocol;

public class Receipt {
    private Protos.PaymentACK paymentACK = null;

    public Receipt(Protos.Payment forPayment) {
        paymentACK = PaymentProtocol.createPaymentAck(forPayment, "TRANSACTION SUCCEEDED:\n" + forPayment.getMemo());
    }
}