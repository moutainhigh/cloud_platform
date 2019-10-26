package cn.sensordb2.stcloud.otherTools.markdown;

/**
 * Created by sensordb on 16/12/21.
 */
public class Column {
    String name;
    int length;

    public Column(String name, int length) {
        this.name = name;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
