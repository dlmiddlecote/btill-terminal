package btill.terminal.bitcoin;

import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;

/**
 * WalletKitThread abstracts the functionality of the {@link org.bitcoinj.core.Wallet} into a separate thread which
 * deals with the grimier sections of the bitcoinj library. The idea is to set it going once and forget about it,
 * accessing the wallet hidden within through the {@link btill.terminal.bitcoin.BitcoinTill} getter.
 */
public class WalletKitThread extends Thread {

    protected static final Logger Log = LoggerFactory.getLogger(WalletKitThread.class);
    private final TestNet3Params _netParams = TestNet3Params.get();
    private WalletAppKit _walletAppKit;
    private Boolean _setupComplete = false;
    private Boolean _isRunning = false;
    private String _filePrefix = "test_btc";
    private String _folder = "./bitcoin_files";

    public WalletKitThread(@Nullable String folder, @Nullable String filePrefix) {
        if (folder != null)
            _folder = folder;
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

        Log.debug("WalletKitThread has started");

        _walletAppKit = new WalletAppKit(_netParams, new File(_folder), _filePrefix) {
            @Override
            protected void onSetupCompleted() {

                // Allow spending unconfirmed transactions
                Log.debug("Inside WalletKit OnSetupComplete");
                _walletAppKit.wallet().allowSpendingUnconfirmedTransactions();
                _walletAppKit.peerGroup().setBloomFilterFalsePositiveRate(0.00000000000000001);
                //_walletAppKit.peerGroup().setMaxConnections(11);
                _walletAppKit.peerGroup().setFastCatchupTimeSecs(_walletAppKit.wallet().getEarliestKeyCreationTime());
                _walletAppKit.setAutoStop(false);
                Log.info(_walletAppKit.wallet().toString(false, false, false, null));
                Log.info(_walletAppKit.wallet().getRecentTransactions(3,true).toString());
                //System.out.println(peerGroup().getConnectedPeers().toString());
                Log.debug(filePrefix + ": Wallet App Kit Setup Complete");
                _setupComplete = true;

            }
        };

        _walletAppKit.startAsync();
        Log.debug("WalletAppKit has started");

        _walletAppKit.awaitRunning();
        Log.info(_filePrefix + ": Wallet App Running");

        _isRunning = true;
    }

    public void terminate() {
        _walletAppKit.stopAsync();
        _walletAppKit.awaitTerminated();
        Log.info(_filePrefix + ": Wallet App Terminated");
        try {
            join(1000);
        } catch (InterruptedException e) {
            Log.error(_filePrefix + ": Wallet App Termination Failure");
            e.printStackTrace();
        }
        _isRunning = false;
    }

}