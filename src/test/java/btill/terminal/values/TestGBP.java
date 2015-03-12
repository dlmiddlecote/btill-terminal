package btill.terminal.values;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TestGBP {
    private GBP gbp;

    @Before
    public void makeGBP() {
        this.gbp = new GBP(100);
    }

    @Test
    public void testCreation() {
        assert(gbp.getPence() == 100);
    }

    @Test
    public void testToString() {
        assertThat(gbp.toString(), is("£1.00"));
    }

    @Test
    public void testZeroGBP() {
        assertThat(new GBP().getPence(), is(0));
    }

}
