package btill.bitcoin;

import btill.terminal.bitcoin.WalletKitThread;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by Adam Kent on 07/03/2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestWalletKitThread {

    @Test
    public void kitAAAConstructor() {
        WalletKitThread test = new WalletKitThread("resources/bitcoin_test_files", "testWalletKitThreadConstructor");
        assertThat(test, notNullValue());
        assertTrue(!test.setupComplete());
        assertTrue(!test.isRunning());
    }

    @Test
    public void kitWalletStarts() {
        WalletKitThread test = new WalletKitThread("resources/bitcoin_test_files", "testWalletKitThreadRuns");
        test.run();
        assertTrue(test.setupComplete());
        assertTrue(test.isRunning());
        try {
            while (test.isRunning())
                test.terminate();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void kitWalletStops() {
        WalletKitThread test = new WalletKitThread("resources/bitcoin_test_files", "testWalletKitThreadStops");
        test.run();
        try {
            while (test.isRunning())
                test.terminate();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        assertTrue(!test.isRunning());
    }

    @Test
    public void kitZZZNotReallyATestDeleteTmpFiles() {
        final File dir = new File("resources/bitcoin_test_files");
        final String[] allFiles = dir.list();
        for (final String file : allFiles) {
            if (file.endsWith(".tmp")) {
                new File("resources/bitcoin_test_files/" + file).delete();
            }
        }
    }

}
