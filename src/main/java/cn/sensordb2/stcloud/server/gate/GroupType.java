package cn.sensordb2.stcloud.server.gate;

/**
 */
public class GroupType {
    public static final int COMPANY = 101;
    public static final int DEPARTMENT = 102;
    public static final int PROJECT_GROUP = 103;

    public static final int COMMUNITY = 201;
    public static final int BUIDING = 202;
    public static final int UNIT = 203;
    public static final int HOUSE = 204;

    public static final int HOTEL_GROUP = 301;
    public static final int HOTEL = 302;
    public static final int HOTEL_BUILDING = 303;
    public static final int HOTEL_FLOOR = 304;
    public static final int HOTEL_ROOM = 305;



    public static boolean isValidGroupType(int groupType) {
        boolean result = groupType==COMPANY ||
                groupType==DEPARTMENT ||
                groupType==PROJECT_GROUP ||
                groupType==COMMUNITY ||
                groupType==BUIDING ||
                groupType==UNIT ||
                groupType==HOUSE ||
                groupType==HOTEL_GROUP ||
                groupType==HOTEL ||
                groupType==HOTEL_BUILDING ||
                groupType==HOTEL_FLOOR ||
                groupType==HOTEL_ROOM;
        return result;
    }

    public static boolean isValidTopGroupType(int groupType) {
        boolean result = groupType==COMPANY ||
                groupType==COMMUNITY ||
                groupType==HOTEL_GROUP ||
                groupType==HOTEL;
        return result;
    }

    public static boolean isCompanyGroup(int type) {
        return type==COMPANY;
    }
}
