package btill.till;

import btill.terminal.bitcoin.BillBuilder;
import btill.terminal.values.Bill;
import btill.terminal.values.GBP;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.params.TestNet3Params;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
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
        assertThat(test.build(), is(new Bill(null, null, null, null, null, null)));
    }

    @Test
    public void BuilderSetWallet() {
        BillBuilder test = new BillBuilder();
        test.setWallet(testWallet);
        Bill testBill = test.build();
        Bill expectedBill = new Bill(null, null, null, null, null, testWallet);
        assertThat(testBill.getWallet(), is(expectedBill.getWallet()));
    }

    @Test
    public void BuilderSetMemo() {
        BillBuilder test = new BillBuilder();
        test.setWallet(testWallet);
        test.setMemo(testMemo);
        Bill testBill = test.build();
        Bill expectedBill = new Bill(testMemo, null, null, null, null, testWallet);
        assertThat(testBill.getMemo(), is(expectedBill.getMemo()));
    }

    @Test
    public void BuilderSetPaymentURL() {
        BillBuilder test = new BillBuilder();
        test.setWallet(testWallet);
        test.setPaymentURL(testPaymentURL);
        Bill testBill = test.build();
        Bill expectedBill = new Bill(null, testPaymentURL, null, null, null, testWallet);
        assertThat(testBill.getPaymentURL(), is(expectedBill.getPaymentURL()));
    }

    @Test
    public void BuilderSetMerchantData() {
        BillBuilder test = new BillBuilder();
        test.setWallet(testWallet);
        test.setMerchantData(testMerchantData);
        Bill testBill = test.build();
        Bill expectedBill = new Bill(null, null, testMerchantData, null, null, testWallet);
        assertThat(testBill.getMerchantData(), is(expectedBill.getMerchantData()));
    }

    @Test
    public void BuilderSetCoinAmount() {
        BillBuilder test = new BillBuilder();
        test.setWallet(testWallet);
        test.setAmount(testAmount);
        Bill testBill = test.build();
        Bill expectedBill = new Bill(null, null, null, testAmount, null, testWallet);
        assertThat(testBill.getCoinAmount(), is(expectedBill.getCoinAmount()));
    }

    @Test
    public void BuilderSetGBPAmount() {
        BillBuilder test = new BillBuilder();
        test.setWallet(testWallet);
        test.setGBPAmount(testGbpAmount);
        Bill testBill = test.build();
        Bill expectedBill = new Bill(null, null, null, null, testGbpAmount, testWallet);
        assertThat(testBill.getGbpAmount(), is(expectedBill.getGbpAmount()));
    }

}
