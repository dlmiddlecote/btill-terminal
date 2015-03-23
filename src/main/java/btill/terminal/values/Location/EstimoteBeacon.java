package btill.terminal.values.Location;

/**
 * Created by dlmiddlecote on 23/03/15.
 */
public class EstimoteBeacon {

    private String address;
    private int id;
    private Position coordinates;

    public EstimoteBeacon(int id, String address, Position coordinates) {
        this.id = id;
        this.address = address;
        this.coordinates = coordinates;
    }

    public String getAddress() {
        return address;
    }

    public int getId() {
        return id;
    }

    public double getXCoord() {
        return coordinates.getX();
    }

    public double getYCoord() {
        return coordinates.getY();
    }
}
