package btill.terminal;

import btill.terminal.values.*;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import org.bitcoin.protocols.payments.Protos;
import org.bitcoin.protocols.payments.Protos.Payment;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.protocols.payments.PaymentProtocolException;
import org.bitcoinj.protocols.payments.PaymentSession;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestController {

    private final Menu fakeMenu = new Menu(asList(new MenuItem("lager", new GBP(375), "drink")));
    private final Menu returnedOrder = new Menu(asList(new MenuItem("cider", new GBP(350), "drink")));
    private final Till fakeTill = new FakeTill();
    private final Order fakeOrder = new Order(returnedOrder);
    private Controller controller = new Controller(fakeMenu, fakeTill);

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
//    public Menu deserialize(String body) {
//        return new Gson().fromJson(body, Menu.class);
//    }


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







}
