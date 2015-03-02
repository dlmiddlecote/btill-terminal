package btill.terminal;

import btill.terminal.values.*;
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
    public GBP getGBP(Order order) {
        return order.total();
    }

    @Override
    public String getAmount(GBP amount) {
        return null;
    }

}
