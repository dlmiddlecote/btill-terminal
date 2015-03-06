package btill.terminal.bitcoin;

import btill.terminal.values.GBP;
import org.bitcoinj.core.Coin;

public class GBP2SatoshisExchangeRate {
    private double rate;

    public GBP2SatoshisExchangeRate(double rate) {
        this.rate = rate;
    }

    public Coin getSatoshis(GBP amount) {
        return Coin.valueOf((long) (amount.getPence() * rate));
    }

    public GBP getGBP(Coin amount) {
        return new GBP((int) (amount.getValue() / rate));
    }

}
