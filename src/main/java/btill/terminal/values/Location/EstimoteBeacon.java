package btill.terminal.values.Location;

/**
 * Created by dlmiddlecote on 23/03/15.
 */
public class EstimoteBeacon {

    private String address;
    private int id;
    private int tableNumber;
    //private Position coordinates;

    public EstimoteBeacon(int id, String address, int tableNumber) {
        this.id = id;
        this.address = address;
        this.tableNumber = tableNumber;
    }

    public String getAddress() {
        return address;
    }

    public int getId() {
        return id;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    /*public double getXCoord() {
        return coordinates.getX();
    }

    public double getYCoord() {
        return coordinates.getY();
    }*/

}
