package btill.terminal;

import btill.terminal.values.*;
import org.bitcoin.protocols.payments.Protos.Payment;

public interface Till {
    Bill createBillForAmount(GBP amount);
    Receipt settleBillUsing(Payment payment, GBP amount);
    GBP getGBP(Order order);
    String getAmount(GBP amount);
}
