package btill.terminal.values;

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

    @Override
    public String toString() {
        float f = (float) (pence / 100.0);
        return String.format("£%.2f", f);
    }
}
