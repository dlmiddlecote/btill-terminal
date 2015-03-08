package btill.till;

import btill.terminal.bitcoin.BillBuilder;
import btill.terminal.values.Bill;
import btill.terminal.values.GBP;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.params.TestNet3Params;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by Adam Kent on 07/03/2015.
 */
public class TestBillBuilder {

    private Wallet testWallet = new Wallet(TestNet3Params.get());
    private String testMemo = "testMemo";
    private String testPaymentURL = "testPaymentURL";
    private byte[] testMerchantData = new String("testMerchantData").getBytes();
    private Coin testAmount = Coin.valueOf(17);
    private GBP testGbpAmount = new GBP(23);

    @Test
    public void Constructor() {
        BillBuilder test = new BillBuilder();
        assertThat(test, notNullValue());
    }

    @Test(expected = NullPointerException.class)
    public void BuilderAllNull() {
        BillBuilder test = new BillBuilder();
        assertThat(test.build(), is(new Bill(null, null, null, null, null, null, true)));
    }

    @Test
    public void BuilderSetWallet() {
        BillBuilder test = new BillBuilder();
        test.setWallet(testWallet);
        Bill testBill = test.build();
        Bill expectedBill = new Bill(null, null, null, null, null, testWallet, true);
        assertThat(testBill.getWallet(), is(expectedBill.getWallet()));
    }

    @Test
    public void BuilderSetMemo() {
        BillBuilder test = new BillBuilder();
        test.setWallet(testWallet);
        test.setMemo(testMemo);
        Bill testBill = test.build();
        Bill expectedBill = new Bill(testMemo, null, null, null, null, testWallet, true);
        assertThat(testBill.getMemo(), is(expectedBill.getMemo()));
    }

    @Test
    public void BuilderSetPaymentURL() {
        BillBuilder test = new BillBuilder();
        test.setWallet(testWallet);
        test.setPaymentURL(testPaymentURL);
        Bill testBill = test.build();
        Bill expectedBill = new Bill(null, testPaymentURL, null, null, null, testWallet, true);
        assertThat(testBill.getPaymentURL(), is(expectedBill.getPaymentURL()));
    }

    @Test
    public void BuilderSetMerchantData() {
        BillBuilder test = new BillBuilder();
        test.setWallet(testWallet);
        test.setMerchantData(testMerchantData);
        Bill testBill = test.build();
        Bill expectedBill = new Bill(null, null, testMerchantData, null, null, testWallet, true);
        assertThat(testBill.getMerchantData(), is(expectedBill.getMerchantData()));
    }

    @Test
    public void BuilderSetCoinAmount() {
        BillBuilder test = new BillBuilder();
        test.setWallet(testWallet);
        test.setAmount(testAmount);
        Bill testBill = test.build();
        Bill expectedBill = new Bill(null, null, null, testAmount, null, testWallet, true);
        assertThat(testBill.getCoinAmount(), is(expectedBill.getCoinAmount()));
    }

    @Test
    public void BuilderSetGBPAmount() {
        BillBuilder test = new BillBuilder();
        test.setWallet(testWallet);
        test.setGBPAmount(testGbpAmount);
        Bill testBill = test.build();
        Bill expectedBill = new Bill(null, null, null, null, testGbpAmount, testWallet, true);
        assertThat(testBill.getGbpAmount(), is(expectedBill.getGbpAmount()));
    }

    // TODO not working, weirdly. Assertion hits before fresh address does...
    @Test
    public void BuilderSetFreshAddressTrue() {
        BillBuilder test = new BillBuilder();
        test.setFreshAddress(true);
        test.setWallet(testWallet);
        Address expectedAddress = testWallet.currentReceiveAddress();
        Bill testBill = test.build();
        assertThat(testBill.getWallet().currentReceiveAddress(), not(expectedAddress));
    }

    @Test
    public void BuilderSetFreshAddressFalse() {
        BillBuilder test = new BillBuilder();
        test.setFreshAddress(false);
        test.setWallet(testWallet);
        Address expectedAddress = testWallet.currentReceiveAddress();
        Bill testBill = test.build();
        assertThat(testBill.getWallet().currentReceiveAddress(), is(expectedAddress));
    }

}
