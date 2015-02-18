package btill.terminal;

import org.bitcoinj.core.Coin;

import java.util.Date;

public class Receipt {

    private Date date;
    private GBP gbp;
    private Coin bitcoins;

    public Receipt() {
        this.date = new Date();
    }

    public void amount(GBP gbp, Coin bitcoins) {
        this.gbp = gbp;
        this.bitcoins = bitcoins;
    }
}
