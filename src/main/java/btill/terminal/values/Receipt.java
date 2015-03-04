package btill.terminal.values;

import org.bitcoin.protocols.payments.Protos;
import org.bitcoinj.core.Coin;
import org.bitcoinj.protocols.payments.PaymentProtocol;

public class Receipt {
    private Protos.PaymentACK paymentACK = null;
    private Coin bitcoins;

    public Receipt(Protos.Payment forPayment) {
        paymentACK = PaymentProtocol.createPaymentAck(forPayment, "TRANSACTION SUCCEEDED:\n" + forPayment.getMemo());
    }
}