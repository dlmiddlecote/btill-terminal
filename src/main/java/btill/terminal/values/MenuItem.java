package btill.terminal.values;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class MenuItem {
    private final String name;
    private final GBP price;
    private int quantity;

    public MenuItem(String name, GBP price) {
        this.name = name;
        this.price = price;
        this.quantity = 0;
    }

    public MenuItem(String name, GBP price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public GBP cost() {
        return price;
    }

    public int quantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "MenuItem{" + name + ", " + price + '}';
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
