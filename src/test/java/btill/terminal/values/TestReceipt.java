package btill.terminal.values;


import btill.terminal.bitcoin.WalletKitThread;
import org.bitcoin.protocols.payments.Protos;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.protocols.payments.PaymentProtocol;
import org.bitcoinj.protocols.payments.PaymentProtocolException;
import org.bitcoinj.protocols.payments.PaymentSession;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class TestReceipt {

    private Protos.Payment testPayment;
    private WalletKitThread testWalletKitThread = new WalletKitThread("resources/bitcoin_test_files", "testReceipt");

    private Coin testAmount = Coin.valueOf(17);
    private GBP testGbpAmount = new GBP(29);
    private String testMemo = "testMemo";
    private String testPaymentURL = "testPaymentURL";
    private byte[] testMerchantData = String.format("testMerchantData").getBytes();

    @Test
    public void receiptAAAConstructor() throws InsufficientMoneyException, PaymentProtocolException, IOException {
        receiptHelper();

        Receipt test = new Receipt(testPayment, testGbpAmount, testAmount, new Date(System.currentTimeMillis()), 1);

        assertThat(test, notNullValue());
    }

    @Test
    public void receiptGetBTCAmount() throws InsufficientMoneyException, IOException, PaymentProtocolException {
        receiptHelper();

        Receipt test = new Receipt(testPayment, testGbpAmount, testAmount, new Date(System.currentTimeMillis()), 1);

        assertThat(test.getBitcoins(), equalTo(testAmount));
    }

    @Test
    public void receiptGetGBPAmount() throws InsufficientMoneyException, IOException, PaymentProtocolException {
        receiptHelper();

        Receipt test = new Receipt(testPayment, testGbpAmount, testAmount, new Date(System.currentTimeMillis()), 1);

        assertThat(test.getGbp(), equalTo(testGbpAmount));
    }

    @Test
    public void receiptGetPaymentACK() throws InsufficientMoneyException, IOException, PaymentProtocolException {
        receiptHelper();

        Receipt test = new Receipt(testPayment, testGbpAmount, testAmount, new Date(System.currentTimeMillis()), 1);

        Protos.PaymentACK testPaymentACK = PaymentProtocol.createPaymentAck(testPayment, "TRANSACTION SUCCEEDED:\n" + testPayment.getMemo());

        assertThat(test.getPaymentACK(), equalTo(testPaymentACK));
    }

    public void receiptHelper() throws InsufficientMoneyException, IOException, PaymentProtocolException {
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
