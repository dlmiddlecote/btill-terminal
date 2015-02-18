package btill.terminal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bitcoinj.core.Coin;

public class GBP {
    private static int pounds;
    private static long pence;
    private static final double CONVERSION_RATE = 6534.589;

    public GBP(int pounds, int pence) {
        this.pounds = pounds;
        this.pence = pence;
    }

    public GBP() {
        this.pence = 0;
    }

    public GBP(long pence) {
        this.pence = pence;
    }

    public GBP(long pounds, int pence) {
        this.pence = pounds * 100 + pence;
    }

    public GBP(long pounds, long pence) {
        this.pence = pounds * 100 + pence;
    }

    public GBP(double pounds) {
        this.pence = (long) (pounds * 100);
    }

    public long getPounds() {
        return pence / 100;
    }

    public long getPence() {
        return pence;
    }

    public Coin getSatoshis() {
        return Coin.valueOf((long) (pence * CONVERSION_RATE));
    }

    public static Coin getSatoshis(GBP amount) {
        return Coin.valueOf((long) (amount.getPence() * CONVERSION_RATE));
    }

    @Override
    public String toString() {
        float f = (float) (pence / 100.0);
        return String.format("£%.2f", f);
    }

    public void plus(GBP that) {
        pence += that.pence;
    }

    public void minus(GBP that) {
        pence -= that.pence;
    }

    public GBP add(GBP that) {
        return new GBP(this.pence + that.pence);
    }

    public GBP subtract(GBP that) {
        return new GBP(this.pence - that.pence);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
