package btill.terminal.bitcoin;

import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;

import javax.annotation.Nullable;
import java.io.File;

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

    public Boolean setupComplete(){
    	return _setupComplete;
    }
    
    public Boolean isRunning(){
    	return _isRunning;
    }
    
    public WalletAppKit getWalletAppKit() {
        return _walletAppKit;
    }

    @Override
    public void run() {

        //Log.d(TAG, "WalletKitThread has started");

    	_walletAppKit = new WalletAppKit(_netParams, new File("./files/"), _filePrefix) {
            @Override
            protected void onSetupCompleted() {
                // Allow spending unconfirmed transactions
                //Log.d(TAG, "Inside WalletKit OnSetupComplete");
                _walletAppKit.wallet().allowSpendingUnconfirmedTransactions();
                _walletAppKit.peerGroup().setBloomFilterFalsePositiveRate(0.0001);
                _walletAppKit.peerGroup().setMaxConnections(11);
                _walletAppKit.peerGroup().setFastCatchupTimeSecs(_walletAppKit.wallet().getEarliestKeyCreationTime());
                //mWalletAppKit.wallet().autosaveToFile(mFile, 1, TimeUnit.MINUTES, null);
                
                //System.out.println(filePrefix + ": Wallet App Kit Setup Complete");
                _setupComplete = true;
            }
        };

        _walletAppKit.startAsync();
        System.out.println(_filePrefix + ": Wallet App Kit Started");
        //Log.d(TAG, "WalletAppKit has started");
        _walletAppKit.awaitRunning();
        System.out.println(_filePrefix + ": Wallet App Running");        
        //Log.d(TAG, "WalletAppKit is running");
        _isRunning = true;
    }

}