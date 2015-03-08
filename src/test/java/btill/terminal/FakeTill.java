package btill.terminal;

import btill.terminal.values.Bill;
import btill.terminal.values.GBP;
import btill.terminal.values.Receipt;
import btill.terminal.values.SignedBill;

public class FakeTill implements Till {
    @Override
    public Bill createBillForAmount(GBP amount) {
        return new Bill(null, null, null, null, amount, null, true);
    }

    @Override
    public Receipt settleBillUsing(SignedBill signedBill) {
        return null;
    }

    /*@Override
    public GBP getGBP(Order order) {
        return order.total();
    }

    @Override
    public String getAmount(GBP amount) {
        return null;
    }*/

}