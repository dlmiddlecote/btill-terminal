package btill.terminal;

import btill.terminal.values.*;
import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import org.bitcoin.protocols.payments.Protos;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestController {
    private MenuItem fakeItem = new MenuItem("fakeItem", new GBP(100), "category");
    private final Menu fakeMenu = new Menu(asList(new MenuItem("lager", new GBP(375), "drink")));
    private final Menu returnedOrder = new Menu(asList(new MenuItem("cider", new GBP(350), "drink")));
    private final Till fakeTill = new FakeTill();
    private final Order fakeOrder = new Order(returnedOrder);
    private Controller controller = new Controller(fakeMenu, fakeTill);
    private byte[] emptyBytes = new byte[0];

    @Test
    public void headerIsOkIfCommandIsRequestMenu() throws InvalidProtocolBufferException {
        BTMessage message = controller.processRequest(Command.REQUEST_MENU, emptyBytes);
        assertThat(message.getHeader(), is("OK"));
    }

    @Test
    public void requestMenuCommandReturnsAmenu() throws InvalidProtocolBufferException {
        BTMessage message = controller.processRequest(Command.REQUEST_MENU, emptyBytes);
        byte[] menuBytes = getBytes(fakeMenu);
        assertThat(message.getBody(), is(menuBytes));
    }

    public BTMessage runMakeOrderSequence() throws InvalidProtocolBufferException {
        fakeItem.setQuantity(1);
        final Menu fakeMenu2 = new Menu(asList(fakeItem));
        return controller.processRequest(Command.MAKE_ORDER, getBytes(fakeMenu2));
    }

    @Test
    public void headerIsOKIfCommandIsMakeOrder() throws InvalidProtocolBufferException {
        assertThat(runMakeOrderSequence().getHeader(), is("OK"));
    }

    @Test
    public void makeOrderCommandReturnsAbillForAmount() throws InvalidProtocolBufferException {
        fakeItem.setQuantity(3);
        final Menu fakeMenu2 = new Menu(asList(fakeItem));
        BTMessage message = controller.processRequest(Command.MAKE_ORDER, getBytes(fakeMenu2));
        Bill bill = new Gson().fromJson(message.getBodyString(), Bill.class);
        assertThat(bill.toString(), is("Bill for £3.00"));
    }

    // not sure how to create a fake payment to put into this test
//    @Test
//    public void headerISOKIfCommandIsSettleBill() {
//        controller.processRequest(Command.SETTLE_BILL, );
//    }

//
    private Bill deserialize(String body) {
        return new Gson().fromJson(body, Bill.class);
    }

    private byte[] getBytes(Object object) {
        String json = new Gson().toJson(object, object.getClass());
        return json.getBytes();
    }


}
