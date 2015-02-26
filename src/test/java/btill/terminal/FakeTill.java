package btill.terminal;

import btill.terminal.values.Bill;
import btill.terminal.values.GBP;
import btill.terminal.values.Receipt;
import org.bitcoin.protocols.payments.Protos.Payment;

public class FakeTill implements Till {
    @Override
    public Bill createBillForAmount(GBP amount) {
        return new Bill(amount);
    }

    @Override
    public Receipt settleBillUsing(Payment payment) {

        return Receipt.receipt(new GBP(100));
    }
}
