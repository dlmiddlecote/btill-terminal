package btill.terminal;

import btill.terminal.bitcoin.BitcoinTill;
import btill.terminal.bitcoin.GBP2SatoshisExchangeRate;
import btill.terminal.values.GBP;
import btill.terminal.values.Menu;
import btill.terminal.values.MenuItem;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import javax.bluetooth.UUID;

import static java.util.Arrays.asList;

public class BtillTerminal implements AutoCloseable {
    //private static final double CONVERSION_RATE = 5854.8;
    private static final double CONVERSION_RATE = penceExchangeRate();
    public static final UUID SERVICE_ID = new UUID("0000110100001000800000805F9B34FB", false);
    public static final String SERVICE_NAME = "helloService";
    public static final String SERVICE_HOST = "btspp://localhost:";

    private Server server;

    private static double penceExchangeRate() {

        String gbp = null;
        try {
            gbp = Unirest.get("https://community-bitcointy.p.mashape.com/price/GBP")
                    .header("X-Mashape-Key", "lkv6KPZ302mshKYByfU1gD0iqSsqp1OV2l0jsnlc4oIyhtpR8p")
                    .header("Accept", "text/plain")
                    .asString().getBody();
        } catch (UnirestException e) {
            System.out.println("Error");
        }
        if (gbp != null) {
            Double pence = Double.parseDouble(gbp);
            return 1000000.0 / pence;
        }
        return 0.0;
    }

    public void startUsing(Server server, Till till) {
        this.server = server;


        Menu menu = createMenu();
        Controller controller = new Controller(menu, till);
        server.use(controller);

        server.start();
    }

    private static Till createTill() {
        return new BitcoinTill(new GBP2SatoshisExchangeRate(CONVERSION_RATE));
    }

    private Menu createMenu() {
        return new Menu(asList(
                new MenuItem("Burrito", new GBP(600), "Mains"),
                new MenuItem("Nachos", new GBP(500), "Sides"),
                new MenuItem("Quesadilla", new GBP(400), "Mains"),
                new MenuItem("Tacos", new GBP(600), "Mains"),
                new MenuItem("Guacamole", new GBP(200), "Dips"),
                new MenuItem("Garlic & Herb", new GBP(50), "Dips"),
                new MenuItem("Ketchup", new GBP(1), "Sauces"),
                new MenuItem("Spicy Rice", new GBP(300), "Sides"),
                new MenuItem("Horchata", new GBP(350), "Drinks"),
                new MenuItem("Mojito", new GBP(700), "Drinks")

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
