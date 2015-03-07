package btill.terminal;

import btill.terminal.bitcoin.GBP2SatoshisExchangeRate;
import btill.terminal.values.GBP;
import org.bitcoinj.core.Coin;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by Adam Kent on 07/03/2015.
 */
public class TestGBP2SatoshisExchangeRate {

    @Test
    public void Constructor() {
        GBP2SatoshisExchangeRate test = new GBP2SatoshisExchangeRate(10.0);
        assertThat(test, notNullValue());
    }

    //TODO consider negative or zero values?

    @Test
    public void ConversionGBPtoSatoshis() {
        GBP2SatoshisExchangeRate test = new GBP2SatoshisExchangeRate(10.0);
        assertThat(test.getSatoshis(new GBP(1)), is(Coin.valueOf(10)));
    }

    @Test(expected = NullPointerException.class)
    public void ConversionGBPtoSatoshisNull() {
        GBP2SatoshisExchangeRate test = new GBP2SatoshisExchangeRate(10.0);
        test.getSatoshis(null);
    }

    @Test
    public void ConversionSatoshistoGBP() {
        GBP2SatoshisExchangeRate test = new GBP2SatoshisExchangeRate(10.0);
        assertThat(test.getGBP(Coin.valueOf(10)), is(new GBP(1)));
    }

    @Test(expected = NullPointerException.class)
    public void ConversionSatoshistoGBPNull() {
        GBP2SatoshisExchangeRate test = new GBP2SatoshisExchangeRate(10.0);
        test.getGBP(null);
    }

}
