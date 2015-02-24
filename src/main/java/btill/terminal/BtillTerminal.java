package btill.terminal;

import btill.terminal.bitcoin.BitcoinTill;
import btill.terminal.bitcoin.GBP2SatoshisExchangeRate;
import btill.terminal.bluetooth.BtillServer;
import btill.terminal.bluetooth.Controller;
import btill.terminal.values.GBP;
import btill.terminal.values.Menu;
import btill.terminal.values.MenuItem;

import javax.bluetooth.UUID;

import static java.util.Arrays.asList;

public class BtillTerminal implements AutoCloseable {
    private static final double CONVERSION_RATE = 6534.589;
    public static final UUID SERVICE_ID = new UUID("0000110100001000800000805F9B34FB", false);
    public static final String SERVICE_NAME = "helloService";
    public static final String SERVICE_HOST = "btspp://localhost:";

    private Server server;

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
                new MenuItem("Bitter", new GBP(375)),
                new MenuItem("Lager", new GBP(425)),
                new MenuItem("Chicken", new GBP(200)),
                new MenuItem("More Chicken", new GBP(100))
                //new MenuItem("Hot Wings", new GBP(250))
                //new MenuItem("Chicken Burger", new GBP(300))
                //new MenuItem("Popcorn Chicken", new GBP(150))
        ));
    }

    private static String serviceUri() {
        return String.format("%s%s;name=%s", SERVICE_HOST, SERVICE_ID, SERVICE_NAME);
    }

    @Override public void close() throws Exception {
        if (server != null) server.close();
    }

    public static void main(String[] args) throws Exception {
        try (final BtillTerminal terminal = new BtillTerminal()) {
            BtillServer server = new BtillServer(serviceUri());
            terminal.startUsing(server, createTill());
        }
    }
}
