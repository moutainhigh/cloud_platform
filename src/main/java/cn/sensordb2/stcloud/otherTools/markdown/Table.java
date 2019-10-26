package cn.sensordb2.stcloud.otherTools.markdown;

import cn.sensordb2.stcloud.util.Tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by sensordb on 16/12/21.
 */
public class Table {
    TableSchema tableSchema;
    TableData tableData;

    public Table(TableSchema tableSchema, TableData tableData) {
        this.tableSchema = tableSchema;
        this.tableData = tableData;
    }

    public void saveToMarddownFile(File file, String title) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        Vector<Column> columns = tableSchema.getColumns();

        bw.write("##"+title);
        bw.write("\n");

        int columnTitleLength;
        for (Column column : columns) {
            columnTitleLength = column.getName().length();
            bw.write("| ");
            bw.write(column.getName());
            bw.write(Tools.sameCharWithLength(' ', column.getLength() - columnTitleLength));
            bw.write(" ");
        }
        bw.write("|");
        bw.write("\n");

        for (Column column : columns) {
            columnTitleLength = column.getName().length();
            bw.write("| ");
            bw.write(Tools.sameCharWithLength('-', column.getLength()));
            bw.write(" ");
        }
        bw.write("|");
        bw.write("\n");

        int i=0;
        for (Vector<String> record : this.tableData.getData()) {
            i = 0;
            for (String fieldData : record) {
                columnTitleLength = fieldData.length();
                bw.write("| ");
                bw.write(fieldData);
                bw.write(Tools.sameCharWithLength(' ', columns.get(i).getLength() - columnTitleLength));
                bw.write(" ");
                i++;
            }
            bw.write("|");
            bw.write("\n");
        }
        bw.flush();
        bw.close();

    }
}
