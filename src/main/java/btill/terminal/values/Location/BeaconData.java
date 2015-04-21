package btill.terminal.values.Location;

import java.util.ArrayList;

/**
 * Created by dlmiddlecote on 23/03/15.
 */
public class BeaconData {

    private ArrayList<EstimoteBeacon> beacons;

    public BeaconData(ArrayList<EstimoteBeacon> beacons) {
        this.beacons = beacons;
    }

    public EstimoteBeacon get(int id) {
        for (EstimoteBeacon beacon : beacons) {
            if (beacon.getId() == id) {
                return beacon;
            }
        }
        return beacons.get(id);
    }

    public EstimoteBeacon get(String address) {
        for (EstimoteBeacon beacon : beacons) {
            if (beacon.getAddress().equals(address))  {
                return beacon;
            }
        }
        return null;
    }
}
