package btill.terminal;

import btill.terminal.bitcoin.WalletKitThreadInterface;
import btill.terminal.bluetooth.SignedBill;
import btill.terminal.values.Bill;
import btill.terminal.values.GBP;
import btill.terminal.values.Menu;
import btill.terminal.values.Receipt;
import org.bitcoin.protocols.payments.Protos;
import org.bitcoinj.core.Address;

import java.util.concurrent.Future;

public interface Till {
    Bill createBillForAmount(Menu menu);
    Future<Receipt> settleBillUsing(SignedBill signedBill);

    GBP getGBP(Menu menu);
    String getAmount(GBP amount);
}
