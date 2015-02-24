package btill.terminal.bluetooth;

import btill.terminal.Till;
import btill.terminal.values.Bill;
import btill.terminal.values.Menu;
import btill.terminal.values.MenuItem;
import btill.terminal.values.Receipt;
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

    public BtillResponse processRequest(Command command, String content) {

        switch (command) {
            case REQUEST_MENU: {
                return new BtillResponse(OK, serialize(menu));
            }
            case MAKE_ORDER: {
                Menu menu = deserializeMenu(content);
                Bill bill = till.createBillForAmount(menu);
                return new BtillResponse(OK, serialize(bill));
            }
            case SETTLE_BILL: {
                Payment payment = deserializePayment(content);
                Receipt receipt = till.settleBillUsing(payment);
                return new BtillResponse(OK, serialize(receipt));
            }
            default: {
                return new BtillResponse(NOT_FOUND, "");
            }
        }

    }

    private Menu deserializeMenu(String content) {
        return new Gson().fromJson(content, Menu.class);
    }

    private Payment deserializePayment(String content) {
        return new Gson().fromJson(content, Payment.class);
    }

    private String serialize(Bill bill) {
        return new Gson().toJson(bill);
    }

    private String serialize(Menu menu) {
        return new Gson().toJson(menu);
    }

    private String serialize(Receipt receipt) {
        return new Gson().toJson(receipt);
    }


}
