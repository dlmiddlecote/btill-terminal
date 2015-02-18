package btill.terminal;

import org.bitcoin.protocols.payments.Protos;

public interface Till {
    Bill createBillForAmount(GBP amount);
    Receipt settleBillUsing(Protos.Payment payment);
}
