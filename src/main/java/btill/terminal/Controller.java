package btill.terminal;

import btill.terminal.values.*;
import btill.terminal.values.Location.*;
import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.TreeMap;

import static btill.terminal.Status.NOT_FOUND;
import static btill.terminal.Status.OK;

public class Controller {
    private final Menu menu;
    private final Till till;
    private final Restaurant restaurant;
    private GBP amount;

    public Controller(Menu menu, Till till, Restaurant restaurant) {
        this.menu = menu;
        this.till = till;
        this.restaurant = restaurant;
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
                Integer tableNumber = restaurant.tableNumberFromData(signedBill.getLocationData());
                //Integer tableNumber = -1;
                System.out.println("Created receipt");
                System.out.println("Location Data: " + signedBill.getLocationData().toString());
                if (receipt != null) {// && tableNumber > 0) {
                    return new BTMessageBuilder(OK, receipt, tableNumber).build();
                }
                else {
                    return new BTMessageBuilder(NOT_FOUND).build();
                }
            }
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


    // TODO this is just for a trial
    public static ArrayList<Table> newTables() {
        ArrayList<Table> tables = new ArrayList<Table>();
        // Table(Table Number) - could add in extra features later on
        tables.add(new Table(1));
        tables.add(new Table(2));
        tables.add(new Table(3));
        tables.add(new Table(4));
        tables.add(new Table(5));
        tables.add(new Table(6));
        return tables;
    }

    public static BeaconData newBeacons(TableData tables) {
        ArrayList<EstimoteBeacon> beacons = new ArrayList<EstimoteBeacon>();

        beacons.add(new EstimoteBeacon(1, "E6:AC:03:43:A3:CD", 1)); // Green Old Box
        beacons.add(new EstimoteBeacon(2, "C7:9B:45:5F:A4:67", 2)); // Green New Box
        beacons.add(new EstimoteBeacon(3, "D6:17:6A:43:12:FD", 3)); // Dark Blue Old Box
        beacons.add(new EstimoteBeacon(4, "D9:A5:1D:9B:40:AE", 4)); // Dark Blue New Box
        beacons.add(new EstimoteBeacon(5, "D0:41:C0:E9:87:79", 5)); // Light Blue Old Box
        beacons.add(new EstimoteBeacon(6, "F7:15:68:F9:01:4F", 6)); // Light Blue New Box

        return new BeaconData(beacons);
    }
}
