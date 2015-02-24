package btill.terminal.bluetooth;

import btill.terminal.Till;
import btill.terminal.values.*;
import com.google.gson.Gson;
import org.bitcoin.protocols.payments.Protos.Payment;

import static btill.terminal.bluetooth.Status.NOT_FOUND;
import static btill.terminal.bluetooth.Status.OK;

public class Controller {
    private final Menu menu;
    private final Till till;

    public Controller(Menu menu, Till till) {
        this.menu = menu;
        this.till = till;
    }

    public BTMessage processRequest(Command command, byte[] content) {

        switch (command) {
            case REQUEST_MENU: {
                return new BTMessageBuilder(OK, menu).build();
            }
            case MAKE_ORDER: {
                Menu menu = deserializeMenu(content);
                Bill bill = till.createBillForAmount(menu);
                return new BTMessageBuilder(OK, bill).build();
            }
            case SETTLE_BILL: {
                Payment payment = deserializePayment(content);
                Receipt receipt = till.settleBillUsing(payment);
                return new BTMessageBuilder(OK, receipt).build();
            }
            default: {
                return new BTMessageBuilder(NOT_FOUND).build();
            }
        }

    }

    private Menu deserializeMenu(byte[] content) {
        return new Gson().fromJson(new String(content, 0, content.length), Menu.class);
    }

    private Payment deserializePayment(byte[] content) {
        try {
            return Payment.parseFrom(content);
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}
