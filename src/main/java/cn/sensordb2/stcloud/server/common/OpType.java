package cn.sensordb2.stcloud.server.common;

/**
 * Created by sensordb on 16/8/18.
 */
public class OpType {
    public static String INSERT = "I";
    public static String DELETE = "D";
    public static String UPDATE = "A";
    public static String CREATE = "CREATE";

    public static boolean isInsert(String opType) {
        return OpType.INSERT.equals(opType);
    }

    public static boolean isDelete(String opType) {
        return OpType.DELETE.equals(opType);
    }

    public static boolean isUpdate(String opType) {
        return OpType.UPDATE.equals(opType);
    }

    public static boolean isCreate(String opType) {
        return OpType.CREATE.equals(opType);
    }
}
