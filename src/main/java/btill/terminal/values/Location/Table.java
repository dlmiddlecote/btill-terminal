package btill.terminal.values.Location;

/**
 * Created by dlmiddlecote on 21/03/15.
 */
public class Table {

    private int tableNumber;
    private Position centre;
    private double radius;

    public Table(int number, Position centre, double radius) {
        tableNumber = number;
        this.centre = centre;
        this.radius = radius;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public double getCentreX() {
        return centre.getX();
    }

    public double getCentreY() {
        return centre.getY();
    }

    public double getRadius() {
        return radius;
    }
}
