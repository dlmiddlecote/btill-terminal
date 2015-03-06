package btill.terminal.values;

import org.bitcoin.protocols.payments.Protos;
import org.bitcoinj.core.Coin;
import org.bitcoinj.protocols.payments.PaymentProtocol;

/**
 * Receipt acts as a wrapper class for a {@link org.bitcoin.protocols.payments.Protos.PaymentACK}. It is created by the
 * {@link btill.terminal.bitcoin.BitcoinTill} in response to the transaction of a
 * {@link btill.terminal.values.SignedBill} being pushed to the blockchain, and sent to the phone.
 */
public class Receipt {

    private final GBP gbp;
    private final Coin bitcoins;
    private byte[] paymentACKbytes = null;

    public Receipt(Protos.Payment forPayment, GBP amount, Coin btcAmount) {
        paymentACKbytes = PaymentProtocol.createPaymentAck(forPayment, "TRANSACTION SUCCEEDED:\n" + forPayment.getMemo()).toByteArray();
        gbp = amount;
        bitcoins = btcAmount;
    }
}