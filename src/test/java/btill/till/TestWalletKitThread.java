package btill.till;

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
    public void AAAConstructor() {
        WalletKitThread test = new WalletKitThread("./bitcoin_test_files","testWalletKitThreadConstructor");
        assertThat(test, notNullValue());
        assertTrue(!test.setupComplete());
        assertTrue(!test.isRunning());
    }

    @Test
    public void WalletStarts() {
        WalletKitThread test = new WalletKitThread("./bitcoin_test_files","testWalletKitThreadRuns");
        test.run();
        assertTrue(test.setupComplete());
        assertTrue(test.isRunning());
        try {
            test.terminate();
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void WalletStops() {
        WalletKitThread test = new WalletKitThread("./bitcoin_test_files","testWalletKitThreadStops");
        test.run();
        try {
            test.terminate();
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }
        assertTrue(!test.isRunning());
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
