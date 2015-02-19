package btill.terminal.values;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class MenuItem {
    private final String name;
    private final GBP price;

    public MenuItem(String name, GBP price) {
        this.name = name;
        this.price = price;
    }

    public GBP cost() {
        return price;
    }

    @Override public String toString() {
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
