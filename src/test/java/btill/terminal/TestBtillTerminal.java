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

    private Menu createMenu() {
        return new Menu(asList(
                fakeItem));
    }
}
