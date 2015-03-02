package btill.terminal.values;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Menu implements Iterable<MenuItem> {
    private List<MenuItem> items = new ArrayList<MenuItem>();
    private ArrayList<String> categories = new ArrayList<String>();
    private int mOrderId;

    // TODO: static order IDs

    public int getOrderId() {
        return mOrderId;
    }

    public void setOrderId(int orderId) {
        mOrderId = orderId;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public Menu(List<MenuItem> items) {
        this.items = items;
        for (MenuItem item : items) {
            if (!categories.contains(item.getCategory()))
                categories.add(item.getCategory());
        }
    }

    public void sortCategories() {
        for (MenuItem item : items) {
            if (!categories.contains(item.getCategory()))
                categories.add(item.getCategory());
        }
    }

    public Menu() {
    }

    @Override
    public Iterator<MenuItem> iterator() {
        return items.iterator();
    }


    @Override
    public String toString() {
        return "Menu{" + items + '}';
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public void add(MenuItem item) {
        items.add(item);
    }

    public MenuItem get(int position) {
        return items.get(position);
    }

    public int size() {
        return items.size();
    }

    public ArrayList<MenuItem> getCategoryItems(String category) {
        ArrayList<MenuItem> sortedItems = new ArrayList<MenuItem>();
        for (MenuItem item : items) {
            if (item.getCategory().equals(category))
                sortedItems.add(item);
        }
        return sortedItems;
    }

    public static Menu removeNonZero(Menu menu) {
        Menu retMenu = new Menu();
        for (MenuItem item : menu) {
            if (item.getQuantity() != 0)
                retMenu.add(item);
        }

        return retMenu;
    }
}
