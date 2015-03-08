package btill.till;

import btill.terminal.bitcoin.WalletKitThread;
import btill.terminal.values.Bill;
import btill.terminal.values.GBP;
import btill.terminal.values.SignedBill;
import org.bitcoin.protocols.payments.Protos;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.protocols.payments.PaymentProtocolException;
import org.bitcoinj.protocols.payments.PaymentSession;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by Adam Kent on 08/03/2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSignedBill {

    private Protos.Payment testPayment;
    private WalletKitThread testWalletKitThread = new WalletKitThread("./bitcoin_test_files", "testSignedBill");

    private Coin testAmount = Coin.valueOf(17);
    private GBP testGbpAmount = new GBP(29);
    private String testMemo = "testMemo";
    private String testPaymentURL = "testPaymentURL";
    private byte[] testMerchantData = String.format("testMerchantData").getBytes();

    @Test
    public void signedBillAAAConstructor() throws InsufficientMoneyException, IOException, PaymentProtocolException {
        testWalletKitThread.run();

        signedBillHelper();

        SignedBill test = new SignedBill(testPayment, testGbpAmount, testAmount);

        while (testWalletKitThread.isRunning())
            testWalletKitThread.terminate();

        assertThat(test, notNullValue());
    }

    @Test
    public void signedBillGetBTCAmount() throws InsufficientMoneyException, IOException, PaymentProtocolException {
        testWalletKitThread.run();

        signedBillHelper();

        SignedBill test = new SignedBill(testPayment, testGbpAmount, testAmount);

        while (testWalletKitThread.isRunning())
            testWalletKitThread.terminate();

        assertThat(test.getBtcAmount(), equalTo(testAmount));
    }

    @Test
    public void signedBillGetGBPAmount() throws InsufficientMoneyException, IOException, PaymentProtocolException {
        testWalletKitThread.run();

        signedBillHelper();

        SignedBill test = new SignedBill(testPayment, testGbpAmount, testAmount);

        while (testWalletKitThread.isRunning())
            testWalletKitThread.terminate();

        assertThat(test.getGbpAmount(), equalTo(testGbpAmount));
    }

    @Test
    public void signedBillGetSerialisedPayment() throws InsufficientMoneyException, IOException, PaymentProtocolException {
        testWalletKitThread.run();

        signedBillHelper();

        SignedBill test = new SignedBill(testPayment, testGbpAmount, testAmount);

        while (testWalletKitThread.isRunning())
            testWalletKitThread.terminate();

        assertThat(test.getSerialisedPayment(), equalTo(testPayment.toByteArray()));
    }

    @Test
    public void signedBillGetPayment() throws InsufficientMoneyException, IOException, PaymentProtocolException {
        testWalletKitThread.run();

        signedBillHelper();

        SignedBill test = new SignedBill(testPayment, testGbpAmount, testAmount);

        while (testWalletKitThread.isRunning())
            testWalletKitThread.terminate();

        assertThat(test.getPayment(), equalTo(testPayment));
    }

    public void signedBillHelper() throws InsufficientMoneyException, IOException, PaymentProtocolException {
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
    }
}
