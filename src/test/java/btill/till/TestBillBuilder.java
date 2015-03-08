package btill.till;

import btill.terminal.bitcoin.BillBuilder;
import btill.terminal.values.Bill;
import btill.terminal.values.GBP;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.params.TestNet3Params;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by Adam Kent on 07/03/2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBillBuilder {

    private Wallet testWallet = new Wallet(TestNet3Params.get());
    private String testMemo = "testMemo";
    private String testPaymentURL = "testPaymentURL";
    private byte[] testMerchantData = String.format("testMerchantData").getBytes();
    private Coin testAmount = Coin.valueOf(17);
    private GBP testGbpAmount = new GBP(23);

    @Test
    public void builderAAAConstructor() {
        BillBuilder test = new BillBuilder();
        assertThat(test, notNullValue());
    }

    @Test(expected = NullPointerException.class)
    public void builderAAABuildAllNull() {
        BillBuilder test = new BillBuilder();
        test.build();
    }

    @Test
    public void builderSetWallet() {
        BillBuilder test = new BillBuilder();
        test.setWallet(testWallet);
        Bill testBill = test.build();
        Bill expectedBill = new Bill(null, null, null, null, null, testWallet, true);
        assertThat(testBill.getWallet(), is(expectedBill.getWallet()));
    }

    @Test
    public void builderSetMemo() {
        BillBuilder test = new BillBuilder();
        test.setWallet(testWallet);
        test.setMemo(testMemo);
        Bill testBill = test.build();
        Bill expectedBill = new Bill(testMemo, null, null, null, null, testWallet, true);
        assertThat(testBill.getMemo(), is(expectedBill.getMemo()));
    }

    @Test
    public void builderSetPaymentURL() {
        BillBuilder test = new BillBuilder();
        test.setWallet(testWallet);
        test.setPaymentURL(testPaymentURL);
        Bill testBill = test.build();
        Bill expectedBill = new Bill(null, testPaymentURL, null, null, null, testWallet, true);
        assertThat(testBill.getPaymentURL(), is(expectedBill.getPaymentURL()));
    }

    @Test
    public void builderSetMerchantData() {
        BillBuilder test = new BillBuilder();
        test.setWallet(testWallet);
        test.setMerchantData(testMerchantData);
        Bill testBill = test.build();
        Bill expectedBill = new Bill(null, null, testMerchantData, null, null, testWallet, true);
        assertThat(testBill.getMerchantData(), is(expectedBill.getMerchantData()));
    }

    @Test
    public void builderSetCoinAmount() {
        BillBuilder test = new BillBuilder();
        test.setWallet(testWallet);
        test.setAmount(testAmount);
        Bill testBill = test.build();
        Bill expectedBill = new Bill(null, null, null, testAmount, null, testWallet, true);
        assertThat(testBill.getCoinAmount(), is(expectedBill.getCoinAmount()));
    }

    @Test
    public void builderSetGBPAmount() {
        BillBuilder test = new BillBuilder();
        test.setWallet(testWallet);
        test.setGBPAmount(testGbpAmount);
        Bill testBill = test.build();
        Bill expectedBill = new Bill(null, null, null, null, testGbpAmount, testWallet, true);
        assertThat(testBill.getGbpAmount(), is(expectedBill.getGbpAmount()));
    }

    // TODO not working, weirdly. Assertion hits before fresh address does...
    @Test
    public void builderSetFreshAddressTrue() {
        BillBuilder test = new BillBuilder();
        test.setFreshAddress(true);
        test.setWallet(testWallet);
        Address expectedAddress = testWallet.currentReceiveAddress();
        Bill testBill = test.build();
        assertThat(testBill.getWallet().currentReceiveAddress(), not(expectedAddress));
    }

    @Test
    public void builderSetFreshAddressFalse() {
        BillBuilder test = new BillBuilder();
        test.setFreshAddress(false);
        test.setWallet(testWallet);
        Address expectedAddress = testWallet.currentReceiveAddress();
        Bill testBill = test.build();
        assertThat(testBill.getWallet().currentReceiveAddress(), is(expectedAddress));
    }

}
