package btill.terminal;

import btill.terminal.values.*;
import org.bitcoin.protocols.payments.Protos.Payment;

import java.util.concurrent.Future;

public interface Till {
    Bill createBillForAmount(GBP amount);
    Future<Receipt> settleBillUsing(SignedBill signedBill);
    GBP getGBP(Order order);
    String getAmount(GBP amount);
}
