package btill.terminal.values;

import org.bitcoinj.core.Coin;

import java.util.Date;

public class Receipt {

    public static Receipt receipt(GBP gbp) {
        return new Receipt(new Date(), gbp);
    }

    private final Date date;
    private final GBP gbp;
    // private final Coin bitcoins = null;
    // do we want to have bitcoins included in the receipt?

    public Receipt(Date date, GBP gbp, Coin bitcoins) {
        this.date = date;
        this.gbp = gbp;
//        this.bitcoins = bitcoins;
    }


    public Receipt(Date date, GBP gbp) {
        this.date = date;
        this.gbp = gbp;
    }
}
