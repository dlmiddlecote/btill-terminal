package btill.terminal.values.Location;

import java.util.ArrayList;

/**
 * Created by dlmiddlecote on 21/03/15.
 */
public class TableData {

    private ArrayList<Table> tables;

    public TableData(ArrayList<Table> tables) {
        this.tables = tables;
    }

    public void add(Table table) {
        tables.add(table);
    }

    public ArrayList<Table> getTables() {
        return tables;
    }
}
