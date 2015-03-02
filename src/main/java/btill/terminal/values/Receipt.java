package btill.terminal.values;

import org.bitcoin.protocols.payments.Protos;
import org.bitcoinj.core.Coin;
import org.bitcoinj.protocols.payments.PaymentProtocol;

import java.util.Date;

public class Receipt {
    private Protos.PaymentACK _paymentACK = null;

    public Receipt(Protos.Payment forPayment) {
        _paymentACK = PaymentProtocol.createPaymentAck(forPayment, "TRANSACTION SUCCEEDED:\n" + forPayment.getMemo());
    }
}