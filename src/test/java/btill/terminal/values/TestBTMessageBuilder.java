package btill.terminal.values;

import btill.terminal.Command;
import btill.terminal.Status;
import btill.terminal.bitcoin.WalletKitThread;
import com.google.gson.Gson;
import org.bitcoin.protocols.payments.Protos;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.protocols.payments.PaymentProtocolException;
import org.bitcoinj.protocols.payments.PaymentSession;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

public class TestBTMessageBuilder {

    private Protos.Payment testPayment;
    private WalletKitThread testWalletKitThread = new WalletKitThread("resources/bitcoin_test_files", "testBTMessageBuilder");

    private Coin testAmount = Coin.valueOf(17);
    private GBP testGbpAmount = new GBP(29);
    private String testMemo = "testMemo";
    private String testPaymentURL = "testPaymentURL";
    private byte[] testMerchantData = String.format("testMerchantData").getBytes();

    @Test
    public void testBuildHeaderOnly() throws Exception {
        BTMessageBuilder builder = new BTMessageBuilder("OK");
        BTMessage message = builder.build();
        assertEquals(Status.OK.toString(), message.getHeader());
        assertNull(message.getBody());
    }

    @Test
    public void testBuildMenu() throws Exception {
        ArrayList<MenuItem> testList = new ArrayList<MenuItem>();
        testList.add(new MenuItem("Coke", new GBP(150), "Drinks"));
        Menu testMenu = new Menu(testList);
        BTMessage message = new BTMessageBuilder(testMenu).build();
        assertEquals(Command.MAKE_ORDER.toString(), message.getHeader());
        assertNotNull(message.getBody());
        Menu testMenu2 = new Gson().fromJson(new String(message.getBody(), 0, message.getBody().length), Menu.class);
        assertEquals(testMenu.toString(), testMenu2.toString());
    }

    @Test
    public void testBuildSignedBill() throws InsufficientMoneyException, PaymentProtocolException, IOException {
        BTMessageBuilderHelper();
        BTMessage message = new BTMessageBuilder(1, testPayment, testGbpAmount, testAmount, null).build();
        SignedBill testSignedBill = new SignedBill(1, testPayment, testGbpAmount, testAmount, null);
        assertEquals(Command.SETTLE_BILL.toString(), message.getHeader());
        assertNotNull(message.getBody());
        SignedBill testSignedBill2 = new Gson().fromJson(new String(message.getBody(), 0, message.getBody().length), SignedBill.class);
        assertEquals(testSignedBill.getBtcAmount().getValue(), testSignedBill2.getBtcAmount().getValue());
        assertEquals(testSignedBill.getGbpAmount().getPence(), testSignedBill2.getGbpAmount().getPence());
        assertEquals(testSignedBill.getPayment().getMemo(), testSignedBill2.getPayment().getMemo());
    }

    public void BTMessageBuilderHelper() throws InsufficientMoneyException, IOException, PaymentProtocolException {
        testWalletKitThread.start();
        while (!testWalletKitThread.isRunning())
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        Bill testBill = new Bill(testMemo, testPaymentURL, testMerchantData, testAmount, testGbpAmount, testWalletKitThread.getWalletAppKit().wallet(), false);

        PaymentSession testPaymentSession = new PaymentSession(testBill.getRequest(), false);

        Wallet.SendRequest testSendRequest = testPaymentSession.getSendRequest();

        testSendRequest.ensureMinRequiredFee = false;
        testSendRequest.fee = Coin.ZERO;
        testSendRequest.feePerKb = Coin.ZERO;
        testSendRequest.signInputs = false;

        testWalletKitThread.getWalletAppKit().wallet().completeTx(testSendRequest);
        testWalletKitThread.getWalletAppKit().wallet().signTransaction(testSendRequest);
        List<Transaction> txil = new ArrayList<Transaction>();
        txil.add(testSendRequest.tx);

        testPayment = testPaymentSession.getPayment(txil, testWalletKitThread.getWalletAppKit().wallet().currentReceiveAddress(), testPaymentSession.getMemo());

        while (testWalletKitThread.isRunning())
            testWalletKitThread.terminate();
    }
}
