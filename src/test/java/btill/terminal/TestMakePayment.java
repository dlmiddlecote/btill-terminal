package btill.terminal;

import btill.terminal.values.Menu;

import static java.util.Arrays.asList;

public class TestMakePayment {

    private Menu orderFromResultOf(BtillResponse response) {
        return new TestController().deserialize(response.body());
    }

//    private BtillResponse runTest() {
//        Menu fakeMenu = new Menu(asList(cider, bitter, burger, lager));
//        Till fakeTill = new FakeTill();
//        Controller controller = new Controller(fakeMenu, fakeTill);
//        return controller.processRequest(Command.toCommand("MAKE_ORDER"), new TestController().serialize(fakeOrder()));
//    }

}
