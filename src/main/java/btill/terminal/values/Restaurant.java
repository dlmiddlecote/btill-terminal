package btill.terminal.values;

import btill.terminal.values.Location.*;

import java.util.ArrayList;

/**
 * Created by dlmiddlecote on 23/03/15.
 */
public class Restaurant {

    private String name;
    private BeaconData beacons;
    private TableData tables;

    public Restaurant(String name, BeaconData beacons, ArrayList<Table> tables) {
        this.name = name;
        this.beacons = beacons;
        this.tables = new TableData(tables);
    }

    public String getName() {
        return name;
    }

    public BeaconData getBeacons() {
        return beacons;
    }

    public TableData getTables() {
        return tables;
    }

    public Integer tableNumberFromData(LocationData locationData) {

        if (locationData.size() >= 4) {
            EstimoteBeacon estimote1 = beacons.get(1);
            EstimoteBeacon estimote2 = beacons.get(2);
            EstimoteBeacon estimote3 = beacons.get(3);
            EstimoteBeacon estimote4 = beacons.get(4);


            double w = locationData.getDistance(estimote1.getAddress());
            double x = locationData.getDistance(estimote2.getAddress());
            double y = locationData.getDistance(estimote3.getAddress());
            double z = locationData.getDistance(estimote4.getAddress());

            double ABSquared = (estimote2.getXCoord() - estimote1.getXCoord()) * (estimote2.getXCoord() - estimote1.getXCoord())
                    + (estimote2.getYCoord() - estimote1.getYCoord()) * (estimote2.getYCoord() - estimote1.getYCoord());
            double ADSquared = (estimote1.getXCoord() - estimote4.getXCoord()) * (estimote1.getXCoord() - estimote4.getXCoord())
                    + (estimote1.getYCoord() - estimote4.getYCoord()) * (estimote1.getYCoord() - estimote4.getYCoord());
            double BCSquared = (estimote2.getXCoord() - estimote3.getXCoord()) * (estimote2.getXCoord() - estimote3.getXCoord())
                    + (estimote2.getYCoord() - estimote3.getYCoord()) * (estimote2.getYCoord() - estimote3.getYCoord());
            double DCSquared = (estimote3.getXCoord() - estimote4.getXCoord()) * (estimote3.getXCoord() - estimote4.getXCoord())
                    + (estimote3.getYCoord() - estimote4.getYCoord()) * (estimote3.getYCoord() - estimote4.getYCoord());

            double xCoord1 = ((w * w) - (x * x) + ABSquared) / (2 * Math.sqrt(ABSquared));
            double xCoord2 = ((y * y) - (z * z) + DCSquared) / (2 * Math.sqrt(DCSquared));

            double yCoord1 = ((w * w) - (z * z) + ADSquared) / (2 * Math.sqrt(ADSquared));
            double yCoord2 = ((y * y) - (x * x) + BCSquared) / (2 * Math.sqrt(BCSquared));

            double xpos = 0.5 * (xCoord1 - xCoord2 + Math.sqrt(ABSquared));
            double ypos = 0.5 * (yCoord1 - yCoord2 + Math.sqrt(ADSquared));

            System.out.println("Position is: (" + xpos + ", " + ypos + ")");

            for (Table table : tables.getTables()) {
                double distance = Math.sqrt((table.getCentreX() - xpos) * (table.getCentreX() - xpos)
                        + (table.getCentreY() - ypos) * (table.getCentreY() - ypos));
                if (distance <= table.getRadius()) {
                    System.out.println("Table is: " + table.getTableNumber());
                    return table.getTableNumber();
                }
            }
        }

        return -1;
    }
}
