package btill.terminal;

import btill.terminal.values.Bill;
import btill.terminal.values.GBP;
import btill.terminal.values.Menu;
import btill.terminal.values.Receipt;
import org.bitcoin.protocols.payments.Protos.Payment;

public interface Till {
    Bill createBillForAmount(GBP amount);
    Receipt settleBillUsing(Payment payment, GBP amount);
    GBP getGBP(Menu menu);
    String getAmount(GBP amount);
}
