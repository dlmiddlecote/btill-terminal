package btill.terminal.values;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TestMenuItem {

    private MenuItem fakeItem = new MenuItem("fake", new GBP(100), "category");
    private MenuItem fakeItem2 = new MenuItem("fake2", new GBP(200), "nothing");

    @Test
    public void returnCorrectCategory() {
        assertThat(fakeItem.getCategory(), is("category"));
    }

    @Test
    public void setCorrectCategory() {
        fakeItem2.setCategory("new category");
        assertThat(fakeItem2.getCategory(), is("new category"));
    }

    @Test
    public void returnCorrectName() {
        assertThat(fakeItem.getName(), is("fake"));
    }

    @Test
    public void returnCorrectPrice() {
        assertEquals(fakeItem.getPrice().getPence(), new GBP(100).getPence());
    }

    @Test
    public void itemWithNoQuantityReturnsZero() {
        assertThat(fakeItem.getQuantity(), is(0));
    }

    @Before
    @Test
    public void itemReturnsCorrectSetQuantity() {
        fakeItem2.setQuantity(2);
        assertThat(fakeItem2.getQuantity(), is(2));
    }

    @Test
    public void canIncrementQuantityOnItem() {
        fakeItem.incrementQuantity();
        assertThat(fakeItem.getQuantity(), is(1));
    }

    @Test
    public void canDecrementQuantityOnItem() {
        fakeItem2.decrementQuantity();
        assertThat(fakeItem2.getQuantity(), is(1));
    }

    @Test
    public void cantDecrementQuantityBelowZero() {
        fakeItem.decrementQuantity();
        fakeItem.decrementQuantity();
        assertThat(fakeItem.getQuantity(), is(0));
    }

    @Test
    public void cantIncrementQuantityAboveTen() {
        for (int i = 0; i < 8; i++) {
            fakeItem2.incrementQuantity();
        }
        assertThat(fakeItem2.getQuantity(), is(10));
    }

    @Test
    public void testToString() {
        assertThat(fakeItem.toString(), is("MenuItem{fake, £1.00}"));
    }
}