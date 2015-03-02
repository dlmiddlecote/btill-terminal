package btill.terminal;

import btill.terminal.values.Bill;
import btill.terminal.values.GBP;
import btill.terminal.values.Menu;
import btill.terminal.values.Receipt;
import org.bitcoin.protocols.payments.Protos;
import org.bitcoin.protocols.payments.Protos.Payment;

public class FakeTill implements Till {
    @Override
    public Bill createBillForAmount(GBP amount) {
        return new Bill(amount);
    }

    @Override
    public Receipt settleBillUsing(Payment payment, GBP amount) {
        return null;
    }

    @Override
    public GBP getGBP(Menu menu) {
        return null;
    }

    @Override
    public String getAmount(GBP amount) {
        return null;
    }
    
}
