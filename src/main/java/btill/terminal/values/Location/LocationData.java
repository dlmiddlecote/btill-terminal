package btill.terminal.values.Location;

import com.google.gson.Gson;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by dlmiddlecote on 20/03/15.
 */
public class LocationData {

    private Map<String, Double> locations;
    private Date time;

    public LocationData(TreeMap<String, Double> treeMap) {
        locations = treeMap;
    }

    public void add(String address, double distance) {
        locations.put(address, distance);
    }

    public void setTime(long currentMilliseconds) {
        time = new Date(currentMilliseconds);
    }

    public double getDistance(String address) {
        return locations.get(address);
    }

    public int size() {
        return locations.size();
    }

    public EstimoteBeacon getNearestBeacon(BeaconData beacons) {
        double distance = 100000.0;
        String address = null;
        for (Map.Entry<String, Double> entry : locations.entrySet()) {
            if (entry.getValue() < distance) {
                distance = entry.getValue();
                address = entry.getKey();
            }
        }
        if (address != null) {
            System.out.println("Nearest Beacon is: " + address + ", at time " + time.toString());
            return beacons.get(address);
        }
        else {
            return null;
        }
    }

    public boolean isOld() {
        return time.before(new Date(System.currentTimeMillis() - 600000));
    }

    @Override
    public String toString() {
        return new Gson().toJson(this, LocationData.class);
    }


}
