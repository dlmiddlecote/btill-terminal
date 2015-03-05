package btill.terminal.values;

import org.bitcoin.protocols.payments.Protos;
import org.bitcoinj.core.Coin;
import org.bitcoinj.protocols.payments.PaymentProtocol;

public class Receipt {
    //private Protos.PaymentACK paymentACK = null;



    private byte[] paymentACKbytes = null;
    private final GBP gbp;
    private Coin bitcoins;

    public Receipt(Protos.Payment forPayment, GBP amount, Coin btcAmount) {
        //paymentACK = PaymentProtocol.createPaymentAck(forPayment, "TRANSACTION SUCCEEDED:\n" + forPayment.getMemo());
        paymentACKbytes = PaymentProtocol.createPaymentAck(forPayment, "TRANSACTION SUCCEEDED:\n" + forPayment.getMemo()).toByteArray();
        gbp = amount;
        bitcoins = btcAmount;
    }
}