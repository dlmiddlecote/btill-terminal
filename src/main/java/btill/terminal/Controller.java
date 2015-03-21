package btill.terminal;

import btill.terminal.values.*;
import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
                Bill bill = till.createBillForAmount(amount);
                if (bill != null) {
                    return new BTMessageBuilder(OK, bill).build();
                }
                else {
                    return new BTMessageBuilder(NOT_FOUND).build();
                }
            }
            case SETTLE_BILL: {
                SignedBill signedBill = deserializeSignedBill(content);
                Receipt receipt = till.settleBillUsing(signedBill);
                Integer tableNumber = tableNumberFromData(signedBill.getLocationData());
                System.out.println("Created receipt");
                if (receipt != null && tableNumber > 0) {
                    return new BTMessageBuilder(OK, receipt, tableNumber).build();
                }
                else {
                    return new BTMessageBuilder(NOT_FOUND).build();
                }
            }
            /*case LOCATION: {
                LocationData locationData = deserializeLocation(content);
                Integer tableNumber = tableNumberFromData(locationData);
                if (tableNumber > 0) {
                    return new BTMessageBuilder(OK, tableNumber).build();
                }
                else {
                    return new BTMessageBuilder(NOT_FOUND).build();
                }
            }*/
            default: {
                return new BTMessageBuilder(NOT_FOUND).build();
            }
        }

    }

    private Menu deserializeMenu(byte[] content) {
        return new Gson().fromJson(new String(content, 0, content.length), Menu.class);
    }

    private SignedBill deserializeSignedBill(byte[] content) {
        return new Gson().fromJson(new String(content, 0, content.length), SignedBill.class);
    }

    private LocationData deserializeLocation(byte[] content) {
        return new Gson().fromJson(new String(content, 0, content.length), LocationData.class);
    }

    private Integer tableNumberFromData(LocationData locationData) {
        return 4;
    }

}
