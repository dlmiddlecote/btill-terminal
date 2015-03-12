package btill.terminal.values;

import java.util.Iterator;

public class Order {
    private Menu order;
    private GBP total;

    public Order(Menu order) {
        this.order = order;
    }

    public GBP total() {
        int runningTotal = 0;
        Iterator iterator = this.order.iterator();
        while (iterator.hasNext()) {
            MenuItem item = (MenuItem) iterator.next();
            runningTotal += item.getPrice().getPence() * item.getQuantity();
        }
        this.total = new GBP(runningTotal);
        return total;
    }

    public Menu getOrder() {
        return order;
    }

}