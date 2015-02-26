package btill.terminal.values;

import java.util.Iterator;

import static java.util.Arrays.asList;
import static java.util.Arrays.sort;

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
            runningTotal += item.cost().getPence() * item.quantity();
        }
        System.out.println("Total = " + runningTotal);
        return new GBP(runningTotal);
    }

    public static void main(String[] args) {
        Menu menu = new Menu(asList(new MenuItem("lager", new GBP(300), 3)));
        Order order = new Order(menu);
        GBP total = order.total();
        System.out.println("Total = " + total);
    }
}