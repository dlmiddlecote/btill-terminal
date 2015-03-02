package btill.terminal;

import btill.terminal.values.*;
import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
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

    @Test
    public void headerIsOKIfCommandIsMakeOrder() throws InvalidProtocolBufferException {
        assertThat(runMakeOrderSequence().getHeader(), is("OK"));
    }

    public BTMessage runMakeOrderSequence() throws InvalidProtocolBufferException {
        fakeItem.setQuantity(1);
        final Menu fakeMenu2 = new Menu(asList(fakeItem));
        return controller.processRequest(Command.MAKE_ORDER, getBytes(fakeMenu2));
    }

    @Test
    public void makeOrderCommandReturnsAbillForAmount() throws InvalidProtocolBufferException {
        fakeItem.setQuantity(3);
        final Menu fakeMenu2 = new Menu(asList(fakeItem));
        BTMessage message = controller.processRequest(Command.MAKE_ORDER, getBytes(fakeMenu2));
        NewBill bill = new Gson().fromJson(message.getBodyString(), NewBill.class);
        assertThat(bill.toString(), is("Bill for £3.00"));
    }


    //
//    // Doesn't work with the PaymentRequest? Why?
//    @Test
//    public void headerIsOkIfCommandIsValid() {
//        BtillResponse fakeMenuResponse = controller.processRequest(Command.REQUEST_MENU, "");
//        BtillResponse fakeReceiptResponse = controller.processRequest(Command.SETTLE_BILL, "");
//        assertThat(fakeMenuResponse.status(), is("OK"));
//        assertThat(fakeReceiptResponse.status(), is("OK"));
//    }
//
//    @Test
//    public void returnMenuWhenRequested() {
//        String serializedMenu = serialize(fakeMenu);
//        BtillResponse fakeMenuResponse = controller.processRequest(Command.REQUEST_MENU, "");
//        assertThat(fakeMenuResponse.body(), is(serializedMenu));
//    }
//
//    @Test
//    public void returnPaymentRequestWhenOrderReceived() {
//        Bill fakeBill = fakeTill.createBillForAmount(fakeOrder.total());
//        String serializedOrder = serialize(returnedOrder);
//        String serializedBill = serialize(fakeBill);
//        BtillResponse fakeBillResponse = controller.processRequest(Command.MAKE_ORDER, serializedOrder);
//        assertThat(fakeBillResponse.body(), is(serializedBill));
//    }
//
//
//    public String serialize(Bill bill) {
//        return new Gson().toJson(bill);
//    }
//
//    public String serialize(Menu menu) {
//        return new Gson().toJson(menu);
//    }
//
    public NewBill deserialize(String body) {
        return new Gson().fromJson(body, NewBill.class);
    }


//    How can I create a fakePayment for testing?
//    @Test public void returnReceiptWhenPaymentReceived() {
//
//
//        Receipt fakeReceipt = Receipt.receipt(new GBP(100));
//
//        String serializedPayment = serialize(fakePayment);
//        String serializedReceipt = serialize(fakeReceipt);
//        BtillResponse fakeReceiptResponse = controller.processRequest(Command.SETTLE_BILL, serializedPayment);
//        assertThat(fakeReceiptResponse.body(), is(serializedReceipt));
//    }
//    For unused test
//    private String serialize(Payment payment) {
//        return new Gson().toJson(payment);
//    }
//
//    private String serialize(Receipt receipt) {
//        return new Gson().toJson(receipt);
//    }


    private byte[] getBytes(Object object) {
        String json = new Gson().toJson(object, object.getClass());
        return json.getBytes();
    }


}
