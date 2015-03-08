package btill.till;

import btill.terminal.bitcoin.WalletKitThread;
import btill.terminal.values.Bill;
import btill.terminal.values.GBP;
import btill.terminal.values.SignedBill;
import com.google.protobuf.InvalidProtocolBufferException;
import org.bitcoin.protocols.payments.Protos;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.protocols.payments.PaymentProtocol;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.lang.reflect.Field;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by Adam Kent on 08/03/2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBill {

    private Wallet testWallet = new Wallet(TestNet3Params.get());
    private String testMemo = "testMemo";
    private String testPaymentURL = "testPaymentURL";
    private byte[] testMerchantData = String.format("testMerchantData").getBytes();
    private Coin testAmount = Coin.valueOf(17);
    private GBP testGbpAmount = new GBP(23);

    @Test
    public void billAAAConstructor() {
        Bill test = new Bill(testMemo,testPaymentURL,testMerchantData,testAmount,testGbpAmount,testWallet,false);
        assertThat(test, notNullValue());
    }

    @Test(expected = NullPointerException.class)
    public void billAAAConstructorAllNull() {
        new Bill(null,null,null,null,null,null,false);
    }

    @Test
    public void billToString() {
        Bill test = new Bill(testMemo,testPaymentURL,testMerchantData,testAmount,testGbpAmount,testWallet,false);
        assertThat(test.toString(), equalTo("Bill for " + test.getCoinAmount().toFriendlyString()));
    }

    @Test
    public void billToStringNull() {
        Bill test = new Bill(null, null, null, null, null, testWallet, true);
        assertThat(test.toString(), equalTo("Bill for ???"));
    }

    @Test
    public void billEquals() {
        Bill test1 = new Bill(testMemo,testPaymentURL,testMerchantData,testAmount,testGbpAmount,testWallet,false);
        assertThat(test1, equalTo(new Bill(testMemo,testPaymentURL,testMerchantData,testAmount,testGbpAmount,testWallet,false)));
        assertThat(test1, not(new Bill("NOT EQUAL!",testPaymentURL,testMerchantData,testAmount,testGbpAmount,testWallet,false)));
    }

    @Test
    public void billHashCode() {
        Bill test1 = new Bill(testMemo,testPaymentURL,testMerchantData,testAmount,testGbpAmount,testWallet,false);
        Bill test2 = new Bill(testMemo,testPaymentURL,testMerchantData,testAmount,testGbpAmount,testWallet,false);
        assertThat(test1, equalTo(test2));
        int code1 = test1.hashCode();
        assertThat(code1, equalTo(test2.hashCode()));
        assertThat(code1, equalTo(test1.hashCode()));
    }

    @Test
    public void billSetWallet() {
        Bill test = new Bill(null, null, null, null, null, testWallet, true);
        assertThat(test.getWallet(), is(testWallet));
    }

    @Test
    public void billSetMemo() {
        Bill test = new Bill(testMemo, null, null, null, null, testWallet, true);
        assertThat(test.getMemo(), is(testMemo));
    }

    @Test
    public void billSetPaymentURL() {
        Bill test = new Bill(null, testPaymentURL, null, null, null, testWallet, true);
        assertThat(test.getPaymentURL(), is(testPaymentURL));
    }

    @Test
    public void billSetMerchantData() {
        Bill test = new Bill(null, null, testMerchantData, null, null, testWallet, true);
        assertThat(test.getMerchantData(), is(testMerchantData));
    }

    @Test
    public void billSetCoinAmount() {
        Bill test = new Bill(null, null, null, testAmount, null, testWallet, true);
        assertThat(test.getCoinAmount(), is(testAmount));
    }

    @Test
    public void billSetGBPAmount() {
        Bill test = new Bill(null, null, null, null, testGbpAmount, testWallet, true);
        assertThat(test.getGbpAmount(), is(testGbpAmount));
    }

    // TODO not working, weirdly. Assertion hits before fresh address does...
    @Test
    public void billSetFreshAddressTrue() {
        Bill test = new Bill(null, null, null, null, null, testWallet, true);
        Address expectedAddress = testWallet.currentReceiveAddress();
        assertThat(test.getWallet().currentReceiveAddress(), not(expectedAddress));
    }

    @Test
    public void billSetFreshAddressFalse() {
        Bill test = new Bill(null, null, null, null, null, testWallet, false);
        Address expectedAddress = testWallet.currentReceiveAddress();
        assertThat(test.getWallet().currentReceiveAddress(), is(expectedAddress));
    }

    @Test
    public void billGetRequest() throws InvalidProtocolBufferException {
        Bill test = new Bill(testMemo,testPaymentURL,testMerchantData,testAmount,testGbpAmount,testWallet,false);
        assertThat(test.getRequest(), is(test.getPaymentRequest()));

        Protos.PaymentRequest testRequest = (PaymentProtocol.createPaymentRequest(testWallet.getParams(),
                testAmount, testWallet.currentReceiveAddress(), testMemo, testPaymentURL,
                testMerchantData)).build();
        assertThat(test.getRequest(), is(testRequest));
    }

    @Test
    public void billPay() throws InsufficientMoneyException {
        WalletKitThread testWalletKitThread = new WalletKitThread("./bitcoin_test_files", "testBillPay");
        testWalletKitThread.run();

        Bill test = new Bill(testMemo,testPaymentURL,testMerchantData,testAmount,testGbpAmount,testWalletKitThread.getWalletAppKit().wallet(),false);

        SignedBill testSignedBill = test.pay();

        try {
            testWalletKitThread.terminate();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        assertThat(testSignedBill, notNullValue());
        assertThat(testSignedBill.getPayment().getMemo(), equalTo(testMemo));
        assertThat(testSignedBill.getBtcAmount(), equalTo(testAmount));
        assertThat(testSignedBill.getGbpAmount(), equalTo(testGbpAmount));

    }

    @Test(expected = InsufficientMoneyException.class)
    public void billPayInsufficient() throws InsufficientMoneyException {
        Bill test = new Bill(testMemo,testPaymentURL,testMerchantData,testAmount,testGbpAmount,testWallet,false);

        test.pay();
    }

    @Test(expected = InvalidProtocolBufferException.class)
    public void billGetRequestException() throws NoSuchFieldException, IllegalAccessException, InvalidProtocolBufferException {
        Bill test = new Bill(testMemo,testPaymentURL,testMerchantData,testAmount,testGbpAmount,testWallet,false);

        Field field = Bill.class.getDeclaredField("request");
        field.setAccessible(true);
        field.set(test, String.format("broken").getBytes());

        test.getRequest();
    }

    @Test
    public void tillZZZNotReallyATestDeleteTmpFiles() {
        final File dir = new File("./bitcoin_test_files");
        final String[] allFiles = dir.list();
        for (final String file : allFiles) {
            if (file.endsWith(".tmp")) {
                new File("./bitcoin_test_files/" + file).delete();
            }
        }
    }

}
