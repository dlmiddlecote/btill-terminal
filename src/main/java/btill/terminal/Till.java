package btill.terminal;

import btill.terminal.values.Bill;
import btill.terminal.values.GBP;
import btill.terminal.values.Receipt;
import org.bitcoin.protocols.payments.Protos;

public interface Till {
    Bill createBillForAmount(GBP amount);
    Receipt settleBillUsing(Protos.Payment payment);
}
