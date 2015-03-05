package btill.terminal.bitcoin;

import org.bitcoinj.core.PeerAddress;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;

import javax.annotation.Nullable;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by dlmiddlecote on 20/02/15.
 * Adapted by Adam Kent on 28/02/2015
 */
public class WalletKitThread extends Thread {

    private WalletAppKit _walletAppKit;
    private Boolean _setupComplete = false;
    private Boolean _isRunning = false;

    private String _filePrefix = "test_btc";
    private final TestNet3Params _netParams = TestNet3Params.get();

    public WalletKitThread(/*Context context, BTillController bTillController, File file,*/ @Nullable String filePrefix) {
        if (filePrefix != null)
            _filePrefix = filePrefix;
    }

    public Boolean setupComplete() {
        return _setupComplete;
    }

    public Boolean isRunning() {
        return _isRunning;
    }

    public WalletAppKit getWalletAppKit() {
        return _walletAppKit;
    }

    @Override
    public void run() {

        //Log.d(TAG, "WalletKitThread has started"); // TODO: RESTORE LOGGING

        /* TODO:    CURRENTLY THE WALLET IS FIXED AS THE ONE IN ./bitcoin_files/backup-till.wallet
                        WORK OUT A WAY TO MAKE THIS DYNAMIC - DAN MID PROBABLY HAS */

        _walletAppKit = new WalletAppKit(_netParams, new File("./bitcoin_files/"), _filePrefix) {
            @Override
            protected void onSetupCompleted() {
                // Allow spending unconfirmed transactions
                //Log.d(TAG, "Inside WalletKit OnSetupComplete"); // TODO: RESTORE LOGGING
                _walletAppKit.wallet().allowSpendingUnconfirmedTransactions();
                _walletAppKit.peerGroup().setBloomFilterFalsePositiveRate(0.00000000000000001);
                //_walletAppKit.peerGroup().setMaxConnections(11);
                _walletAppKit.peerGroup().setFastCatchupTimeSecs(_walletAppKit.wallet().getEarliestKeyCreationTime());
                System.out.println(_walletAppKit.wallet().toString());
                System.out.println(peerGroup().getConnectedPeers().toString());
                System.out.println(filePrefix + ": Wallet App Kit Setup Complete");
                _setupComplete = true;
            }
        };

        _walletAppKit.startAsync();
        System.out.println(_filePrefix + ": Wallet App Kit Started");
        //Log.d(TAG, "WalletAppKit has started"); // TODO: RESTORE LOGGING

        _walletAppKit.awaitRunning();
        System.out.println(_filePrefix + ": Wallet App Running");
        try {
            _walletAppKit.peerGroup().addAddress(new PeerAddress(InetAddress.getByName("93.93.135.12"), 18333));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        //Log.d(TAG, "WalletAppKit is running"); // TODO: RESTORE LOGGING
        _isRunning = true;
    }

}