package btill.till;

import btill.terminal.bitcoin.BitcoinTill;
import btill.terminal.values.Bill;
import btill.terminal.values.GBP;
import btill.terminal.values.Receipt;
import btill.terminal.values.SignedBill;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.protocols.payments.PaymentProtocolException;
import org.bitcoinj.protocols.payments.PaymentSession;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by Adam Kent on 07/03/2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBitcoinTill {

    private Coin testAmount = Coin.valueOf(17);
    private GBP testGBPAmount = new GBP(29);
    private String testMemo = "testMemo";
    private String testPaymentURL = "testPaymentURL";
    private byte[] testMerchantData = new String("testMerchantData").getBytes();

    @Test
    public void AAAConstructor() {
        BitcoinTill test = new BitcoinTill("./bitcoin_test_files","testConstructor");
        assertThat(test, notNullValue());
        assertTrue(test.isRunning());
        try {
            test.stopWalletThread();
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tillWalletStops() {
        BitcoinTill test = new BitcoinTill("./bitcoin_test_files","testConstructor");
        try {
            test.stopWalletThread();
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }
        assertTrue(!test.isRunning());
    }

    @Test
    public void tillSetsExchangeRate() {
        assertThat(BitcoinTill.getConversionRate(), greaterThanOrEqualTo(0.0));
    }

    @Test
    public void tillSetsMemo() {
        BitcoinTill test = new BitcoinTill("./bitcoin_test_files","testSetsMemo");
        test.setMemo(testMemo);
        assertThat(test.getMemo(), equalTo(testMemo));
        try {
            test.stopWalletThread();
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tillSetsPaymentURL() {
        BitcoinTill test = new BitcoinTill("./bitcoin_test_files","testSetsPaymentURL");
        test.setPaymentURL(testPaymentURL);
        assertThat(test.getPaymentURL(), equalTo(testPaymentURL));
        try {
            test.stopWalletThread();
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tillSetsMerchantData() {
        BitcoinTill test = new BitcoinTill("./bitcoin_test_files","testSetsMerchantData");
        test.setMerchantData(testMerchantData);
        assertThat(test.getMerchantData(), equalTo(testMerchantData));
        try {
            test.stopWalletThread();
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tillCreatesBillForAmount() {
        BitcoinTill test = new BitcoinTill("./bitcoin_test_files","testCreatesBillForAmount");
        Bill testBill = test.createBillForAmount(testAmount);
        Bill expectedBill = new Bill(null, null, null, testAmount, null, test.getWallet(), false);
        assertThat(testBill.getCoinAmount(), equalTo(expectedBill.getCoinAmount()));
        try {
            test.stopWalletThread();
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tillCreatesBillForAmountGBP() {
        BitcoinTill test = new BitcoinTill("./bitcoin_test_files","testCreatesBillForAmountGBP");
        Bill testBill = test.createBillForAmount(testGBPAmount);
        Bill expectedBill = new Bill(null, null, null, null, testGBPAmount, test.getWallet(), false);
        assertThat(testBill.getGbpAmount(), equalTo(expectedBill.getGbpAmount()));
        try {
            test.stopWalletThread();
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tillCreatesBillForAmountGBPMemo() {
        BitcoinTill test = new BitcoinTill("./bitcoin_test_files","testCreatesBillForAmountGBPMemo");
        Bill testBill = test.createBillForAmount(testGBPAmount,testMemo);
        Bill expectedBill = new Bill(testMemo, null, null, null, testGBPAmount, test.getWallet(), false);
        assertThat(testBill.getGbpAmount(), equalTo(expectedBill.getGbpAmount()));
        try {
            test.stopWalletThread();
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tillCreatesBillForAmountGBPMemoSetter() {
        BitcoinTill test = new BitcoinTill("./bitcoin_test_files","testCreatesBillForAmountMemoSetter");
        test.setMemo(testMemo);
        Bill testBill = test.createBillForAmount(testGBPAmount);
        Bill expectedBill = new Bill(testMemo, null, null, null, testGBPAmount, test.getWallet(), false);
        assertThat(testBill.getGbpAmount(), equalTo(expectedBill.getGbpAmount()));
        try {
            test.stopWalletThread();
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tillSettleBill(){
        Receipt testReceipt = null;

        BitcoinTill test = new BitcoinTill("./bitcoin_test_files","testSettleBillUsing");
        Bill testBill = test.createBillForAmount(testAmount);
        try {
            PaymentSession testPaymentSession = new PaymentSession(testBill.getRequest(),false);

            Wallet.SendRequest testSendRequest = testPaymentSession.getSendRequest();

            testSendRequest.ensureMinRequiredFee = false;
            testSendRequest.fee = Coin.ZERO;
            testSendRequest.feePerKb = Coin.ZERO;
            testSendRequest.signInputs = false;

            test.getWallet().completeTx(testSendRequest);
            test.getWallet().signTransaction(testSendRequest);
            List<Transaction> txil = new ArrayList<Transaction>();
            txil.add(testSendRequest.tx);

            SignedBill testSignedBill = new SignedBill(testPaymentSession.getPayment(txil,test.getWallet().currentReceiveAddress(),testPaymentSession.getMemo()), null, null);

            testReceipt = test.settleBillUsing(testSignedBill);

        } catch (PaymentProtocolException e) {
            e.printStackTrace();
        } catch (InsufficientMoneyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertThat(testReceipt,notNullValue());
        try {
            test.stopWalletThread();
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ZZZNotReallyATestDeleteTmpFiles(){
        final File dir = new File("./bitcoin_test_files");
        final String[] allFiles = dir.list();
        for (final String file : allFiles) {
            if (file.endsWith(".tmp")) {
                new File("./bitcoin_test_files/" + file).delete();
            }
        }
    }
}
