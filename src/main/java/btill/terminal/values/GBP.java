package btill.terminal.values;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * GBP is our representation of real, British money. The kind of money you can depend on, that has stood up to the
 * Blitz and the Communists, to the bankers and the unchecked immigration. The kind of money you can sink your teeth
 * into and be assured of good, old-fashioned, British germs, the kind of germs that apologise (with an 's', no less!)
 * before ravaging your organs for the precious, colonial resources upon which the Empire was built. The money of
 * Shakespeare, Churchill, the Beatles, Sean Connery, Harry Potter. David Beckham's right foot. David Beckham's left
 * foot, come to that! Rule Britannia, Britannia rules the waves! Send her victorious, happy and glorious, long to reign
 * over us, God save the GBP!
 */
public class GBP {
    private final int pence;

    public GBP(int pence) {
        this.pence = pence;
    }

    public GBP() {
        this.pence = 0;
    }

    public int getPence() {
        return pence;
    }

    public GBP plus(GBP that) {
        return new GBP(pence + that.pence);
    }

    public GBP minus(GBP that) {
        return new GBP(pence - that.pence);
    }

    public GBP times(int mult) {
        return new GBP(pence * mult);
    }

    @Override
    public String toString() {
        float f = (float) (pence / 100.0);
        return String.format("£%.2f", f);
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
