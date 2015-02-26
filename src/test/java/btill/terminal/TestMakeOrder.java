package btill.terminal;

import btill.terminal.values.*;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertThat;

public class TestMakeOrder {
    private final MenuItem bitter = new MenuItem("bitter", new GBP(325), 1);
    private final MenuItem lager = new MenuItem("lager", new GBP(375), 1);
    private final MenuItem cider = new MenuItem("cider", new GBP(350), 2);
    private final MenuItem burger = new MenuItem("burger", new GBP(800), 1);
    private final MenuItem fries = new MenuItem("fries", new GBP(250));


    @Test
    public void totalBillIsCorrect() {
        String total = total(create(fakeOrder()));
        assertThat(total, is("£22.00"));
    }

//    @Test
//    public void orderContainsAllThingsOrdered() {
//    }

    private String total(Order order) {
        return order.total().toString();
    }

    private Order create(Menu order) {
        return new Order(order);
    }

    private BtillResponse runTest() {
        Menu fakeMenu = new Menu(asList(cider, bitter, burger, lager));
        Till fakeTill = new FakeTill();
        Controller controller = new Controller(fakeMenu, fakeTill);
        return controller.processRequest(Command.toCommand("MAKE_ORDER"), new TestController().serialize(fakeOrder()));
    }

    private Menu fakeOrder() {

        return new Menu(asList(burger, cider, lager, bitter));
    }


}
