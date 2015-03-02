package btill.terminal;

import btill.terminal.values.GBP;
import btill.terminal.values.Menu;
import btill.terminal.values.MenuItem;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertThat;

//public class TestRequestMenu {
//    private final MenuItem bitter = new MenuItem("bitter", new GBP(325), "drink");
//    private final MenuItem lager = new MenuItem("lager", new GBP(375), "drink");
//    private final MenuItem cider = new MenuItem("cider", new GBP(350), "drink");
//    private final MenuItem burger = new MenuItem("burger", new GBP(800), "drink");
//
//    @Test
//    public void menuContainingItemsIsReturned() {
//        assertThat(menuFromResultOf(runTest()), containsInAnyOrder(bitter, burger, lager, cider));
//    }
//
//    private Menu menuFromResultOf(BtillResponse response) {
//        return new TestController().deserialize(response.body());
//    }
//
////    private BtillResponse runTest() {
////        Menu fakeMenu = new Menu(asList(cider, bitter, burger, lager));
////        Till fakeTill = new FakeTill();
////        Controller controller = new Controller(fakeMenu, fakeTill);
////        return controller.processRequest(Command.toCommand("REQUEST_MENU"), "");
////    }

//}
