package cn.sensordb2.stcloud.otherTools.markdown;

import java.util.Vector;

/**
 * Created by sensordb on 16/12/21.
 */
public class TableSchema {
    Vector<Column> columns = new Vector<>();

    public TableSchema(Vector<Column> columns) {
        this.columns = columns;
    }

    public Vector<Column> getColumns() {
        return columns;
    }

    public void setColumns(Vector<Column> columns) {
        this.columns = columns;
    }
}
