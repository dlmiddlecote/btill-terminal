package btill.terminal;

import btill.terminal.values.GBP;
import btill.terminal.values.Menu;
import btill.terminal.values.MenuItem;

import static java.util.Arrays.asList;

//import com.google.gson.Gson;
//import org.bitcoin.protocols.payments.Protos.Payment;
//import org.junit.Test;

public class TestBtillTerminal {

    private GBP price = new GBP(375);
    MenuItem fakeItem = new MenuItem("fakeItem", price, "category");
    private Menu fakeMenu = createMenu();

//
//    @Test public void something() {
//        FakeServer server = new FakeServer();
//        FakeTill till = new FakeTill();
//        BtillTerminal terminal = new BtillTerminal();
//        terminal.startUsing(server, till);
//        server.sendRequest();
//    }
//
//    private class FakeServer implements Server {
//        private Controller controller;
//        @Override public void start() {}
//        @Override public void use(Controller controller) {
//            this.controller = controller;
//        }
//        @Override public void close() throws Exception {}
//
//        public void sendRequest() {
//            // Order order = new Order(fakeMenu);
////            String serializedItem = new Gson().toJson(new Order());
//
//        }
//    }

    private Menu createMenu() {
        return new Menu(asList(
                fakeItem));
    }
}
