package btill.terminal;

import btill.terminal.values.*;
import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import org.bitcoin.protocols.payments.Protos.Payment;

import static btill.terminal.Status.NOT_FOUND;
import static btill.terminal.Status.OK;

public class Controller {
    private final Menu menu;
    private final Till till;
    private GBP amount;

    public Controller(Menu menu, Till till) {
        this.menu = menu;
        this.till = till;
    }

    public BTMessage processRequest(Command command, byte[] content) throws InvalidProtocolBufferException {

        switch (command) {
            case REQUEST_MENU: {
                return new BTMessageBuilder(OK, menu).build();
            }
            case MAKE_ORDER: {
                Order order = new Order(deserializeMenu(content));
                amount = order.total();
                NewBill bill = new NewBill(amount);


//                THIS DOES NOT WORK
//                bill.setRequest(bill.getRequest("bitcoin:mhKuHFtbzF5khjNSDDbM8z6x18avzt4EgY?amount="
//                                + till.getAmount(amount) + "&r=http://www.b-till.com&message=Payment%20for%20coffee"));


                BTMessage message = new BTMessageBuilder(OK, bill).build();
                return message;
            }
            case SETTLE_BILL: {
                Payment payment = deserializePayment(content);
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

    private Payment deserializePayment(byte[] content) throws InvalidProtocolBufferException {
        return new Gson().fromJson(new String(content, 0, content.length), SignedBill.class).getPayment();
    }
}
