package btill.terminal.values;

import org.bitcoinj.core.Coin;

import java.util.Date;

public class Receipt {
    public static Receipt receipt(GBP gbp, Coin bitcoins) {
        return new Receipt(new Date(), gbp, bitcoins);
    }

    private final Date date;
    private final GBP gbp;
    private final Coin bitcoins;

    public Receipt(Date date, GBP gbp, Coin bitcoins) {
        this.date = date;
        this.gbp = gbp;
        this.bitcoins = bitcoins;
    }
}
