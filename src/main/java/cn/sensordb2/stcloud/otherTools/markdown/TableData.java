package cn.sensordb2.stcloud.otherTools.markdown;

import java.util.Vector;

/**
 * Created by sensordb on 16/12/21.
 */
public class TableData {
    int columnCount;
    Vector<Vector<String>> data = new Vector<>();

    public TableData(int columnCount) {
        this.columnCount = columnCount;
    }

    public void addRecord(Vector record) {
        if(columnCount!=record.size())
            return;
        this.data.add(record);
    }

    public Vector<Vector<String>> getData() {
        return data;
    }
}
