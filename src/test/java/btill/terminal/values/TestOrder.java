package btill.terminal.values;

import java.util.Iterator;

import static java.util.Arrays.asList;

public class TestOrder {




}




//
//public class Order {
//    private Menu order;
//    private GBP total;
//
//    public Order(Menu order) {
//        this.order = order;
//    }
//
//    public GBP total() {
//        int runningTotal = 0;
//        Iterator iterator = this.order.iterator();
//        while (iterator.hasNext()) {
//            MenuItem item = (MenuItem) iterator.next();
//            runningTotal += item.getPrice().getPence() * item.getQuantity();
//        }
//        return new GBP(runningTotal);
//    }
//
//    public Menu getOrder() {
//        return order;
//    }
//
//    public static void main(String[] args) {
//        MenuItem item = new MenuItem("lager", new GBP(300), "drink");
//        item.setQuantity(3);
//        Menu menu = new Menu(asList(item));
//        Order order = new Order(menu);
//        GBP total = order.total();
//        System.out.println("Total = " + total);
//    }
//}