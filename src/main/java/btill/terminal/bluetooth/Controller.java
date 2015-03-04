package btill.terminal.bluetooth;

import btill.terminal.Till;
import btill.terminal.values.*;
import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import org.bitcoin.protocols.payments.Protos.Payment;

import java.util.Random;

import static btill.terminal.bluetooth.Status.NOT_FOUND;
import static btill.terminal.bluetooth.Status.OK;

public class Controller {
    private final Menu menu;
    private final Till till;
    private GBP amount;

    public Controller(Menu menu, Till till) {
        this.menu = menu;
        this.till = till;
    }

    public BTMessage processRequest(Command command, byte[] content) {
        Menu newMenu = null;
        switch (command) {
            case REQUEST_MENU: {
                BTMessage message =  new BTMessageBuilder(OK, menu).build();
                //System.out.println("Read Count: " + message.getReadCount());
                return message;
            }
            case MAKE_ORDER: {
                newMenu = deserializeMenu(content);
                //Bill bill = till.createBillForAmount(menu);
                amount = till.getGBP(newMenu);
                NewBill bill = new NewBill(amount);
                bill.setRequest(bill.getRequest("bitcoin:mhKuHFtbzF5khjNSDDbM8z6x18avzt4EgY?amount=" + till.getAmount(amount) + "&r=http://www.b-till.com&message=Payment%20for%20coffee"));
                BTMessage message = new BTMessageBuilder(OK, bill).build();
                return message;
            }
            case SETTLE_BILL: {
                Payment payment = deserializePayment(content);
                System.out.println("Amount: " + amount.toString());
                Receipt receipt = till.settleBillUsing(payment, amount);
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
            return new Gson().fromJson(new String(content, 0, content.length), SignedBill.class).getPayment();
        } catch (InvalidProtocolBufferException e) {
            System.out.println("Error reading payment");
            return null;
        }
    }
}
