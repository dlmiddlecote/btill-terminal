package btill.terminal;

import btill.terminal.values.Bill;
import btill.terminal.values.GBP;
import btill.terminal.values.Menu;
import btill.terminal.values.Receipt;
import org.bitcoin.protocols.payments.Protos;

public interface Till {
    Bill createBillForAmount(Menu menu);
    Receipt settleBillUsing(Protos.Payment payment);
}
