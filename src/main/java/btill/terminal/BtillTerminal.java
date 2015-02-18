package btill.terminal;

import javax.bluetooth.UUID;

import static btill.terminal.Request.Command.MAKE_ORDER;
import static btill.terminal.Request.Command.REQUEST_MENU;
import static btill.terminal.Request.Command.SETTLE_BILL;
import static java.util.Arrays.asList;

public class BtillTerminal implements AutoCloseable {
    public static final UUID SERVICE_ID = new UUID("0000110100001000800000805F9B34FB", false);
    public static final String SERVICE_NAME = "helloService";
    public static final String SERVICE_HOST = "btspp://localhost:";

    private Server server;

    public void startUsing(Server server, Till till) {
        this.server = server;


        Menu menu = createMenu();
        Controller controller = new Controller();
        controller.register(REQUEST_MENU, new RequestMenuHandler(menu));
        controller.register(MAKE_ORDER, new MakeOrderHandler(till));
        controller.register(SETTLE_BILL, new SettleBillHandler(till));
        server.use(controller);

        server.start();
    }

    private static Till createTill() {
        return new BitcoinTill();
    }

    private Menu createMenu() {
        return new Menu(asList(
                new MenuItem("Bitter", new GBP(3, 75)),
                new MenuItem("Lager", new GBP(4, 25))
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
