package btill.terminal;

import btill.terminal.bitcoin.BitcoinTill;
import btill.terminal.values.GBP;
import btill.terminal.values.Location.Table;
import btill.terminal.values.Location.TableData;
import btill.terminal.values.Menu;
import btill.terminal.values.MenuItem;
import btill.terminal.values.Restaurant;

import javax.bluetooth.UUID;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class BtillTerminal implements AutoCloseable {
    //private static final double CONVERSION_RATE = 5854.8;
    //private static final double CONVERSION_RATE = penceExchangeRate();
    public static final UUID SERVICE_ID = new UUID("0000110100001000800000805F9B34FB", false);
    public static final String SERVICE_NAME = "helloService";
    public static final String SERVICE_HOST = "btspp://localhost:";

    private Server server;

    public void startUsing(Server server, Till till) {
        this.server = server;
        ArrayList<Table> tableArrayList = Controller.newTables();
        TableData tables = new TableData(tableArrayList);
        Restaurant restaurant = new Restaurant("Dan's Mexican Restaurant", Controller.newBeacons(tables), tableArrayList);
        Menu menu = createMenu(restaurant.getName());
        Controller controller = new Controller(menu, till, restaurant);
        server.use(controller);

        server.start();
    }

    private static Till createTill() {
        return new BitcoinTill(null, "bitcoin");
    }

    public Menu createMenu(String restaurantName) {
        return new Menu(restaurantName, asList(
                new MenuItem("Burrito", new GBP(600), "Mains"),
                new MenuItem("Nachos", new GBP(500), "Sides"),
                new MenuItem("Quesadilla", new GBP(400), "Mains"),
                new MenuItem("Tacos", new GBP(600), "Mains"),
                new MenuItem("Guacamole", new GBP(200), "Dips"),
                new MenuItem("Garlic & Herb", new GBP(50), "Dips"),
                new MenuItem("Spicy Rice", new GBP(300), "Sides"),
                new MenuItem("Horchata", new GBP(350), "Drinks"),
                new MenuItem("Mojito", new GBP(700), "Drinks"),
                new MenuItem("Tostada", new GBP(420), "Mains"),
                new MenuItem("Ketchup", new GBP(1), "Mains")

        ));
    }

    private static String serviceUri() {
        return String.format("%s%s;name=%s", SERVICE_HOST, SERVICE_ID, SERVICE_NAME);
    }

    @Override
    public void close() throws Exception {
        if (server != null) server.close();
    }

    public static void main(String[] args) throws Exception {
        try (final BtillTerminal terminal = new BtillTerminal()) {
            BtillServer server = new BtillServer(serviceUri());
            terminal.startUsing(server, createTill());
        }
    }
}
