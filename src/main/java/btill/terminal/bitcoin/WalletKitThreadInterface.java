package btill.terminal.bitcoin;


import org.bitcoinj.core.Wallet;

public interface WalletKitThreadInterface {
    public void setUpWalletThread();
    public void startWalletThread();
    public Wallet getWallet();
}
