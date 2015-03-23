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
                //Integer tableNumber = restaurant.tableNumberFromData(signedBill.getLocationData());
                Integer tableNumber = restaurant.tableNumberFromData(newLocationData(2));
                System.out.println("Created receipt");
                System.out.println("Location Data: " + signedBill.getLocationData().toString());
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


    // TODO this is just for a trial
    public static ArrayList<Table> newTables() {
        ArrayList<Table> tables = new ArrayList<Table>();
        tables.add(new Table(1, new Position(1.0, 4.0), 1.0));
        tables.add(new Table(2, new Position(4.0, 3.0), 1.0));
        tables.add(new Table(3, new Position(2.0, 1.0), 1.0));
        return tables;
    }

    public static BeaconData newBeacons() {
        ArrayList<EstimoteBeacon> beacons = new ArrayList<EstimoteBeacon>();
        beacons.add(new EstimoteBeacon(1, "EB1", new Position(0.0, 5.0)));
        beacons.add(new EstimoteBeacon(2, "EB2", new Position(5.0, 5.0)));
        beacons.add(new EstimoteBeacon(3, "EB3", new Position(5.0, 0.0)));
        beacons.add(new EstimoteBeacon(4, "EB4", new Position(0.0, 0.0)));

        return new BeaconData(beacons);
    }

    private LocationData newLocationData(int table) {
        LocationData locationData = new LocationData(new TreeMap<String, Double>());
        if (table == 1) {
            locationData.add("EB1", Math.sqrt(18.25));
            locationData.add("EB2", Math.sqrt(28.25));
            locationData.add("EB3", Math.sqrt(13.25));
            locationData.add("EB4", Math.sqrt(3.25));
        }
        else if (table == 2) {
            locationData.add("EB1", Math.sqrt(21.25));
            locationData.add("EB2", Math.sqrt(11.25));
            locationData.add("EB3", Math.sqrt(6.25));
            locationData.add("EB4", Math.sqrt(16.25));
        }
        else if (table == 3) {
            locationData.add("EB1", Math.sqrt(8));
            locationData.add("EB2", Math.sqrt(13));
            locationData.add("EB3", Math.sqrt(18));
            locationData.add("EB4", Math.sqrt(13));
        }

        return locationData;
    }
}
