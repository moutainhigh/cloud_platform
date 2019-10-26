package cn.sensordb2.stcloud.server.common;

public class AccountType {
    public static final int USER = 1;
    public static final int ROUTER = 2;
    public static final int RING = 3;
    public static final int LOCK = 4;
    public static final int GATEWAY = 5;
    public static final int GUARD = 6;
    public static final int CLOUDADMIN = 7;
    public static final int WIFIGUARD = 8;
    public static final int OUTER = 9;
    public static final int TEMP = 10;
    public static final int HLOCK = 11;
    public static final int THIRDPARTYDEVICE = 12;
    public static final int WORK_MANAGER = 13;
    public static final int FLOCK = 14;

    public static final String USER_STRING = "USER";
    public static final String ROUTER_STRING = "ROUTER";
    public static final String RING_STRING = "RING";
    public static final String LOCK_STRING = "LOCK";
    public static final String GATEWAY_STRING = "GATEWAY";
    public static final String GUARD_STRING = "GUARD";
    public static final String CLOUDADMIN_STRING = "CLOUDADMIN";
    public static final String WIFIGUARD_STRING = "WIFIGUARD";
    public static final String OUTER_STRING = "OUTER";
    public static final String TEMP_STRING = "TEMP";
    public static final String HLOCK_STRING = "HLOCK";
    public static final String UNKNOWN_STRING = "UNKNOWN";
    public static final String WORKMANAGER = "WORKMANAGER";
    public static final String FLOCK_STRING = "FLOCK";

    public static final int UNKNOWN = -1;

    public static int getAccountType(String type) {
        if ("USER".equals(type)) {
            return USER;
        } else if ("ROUTER".equals(type)) {
            return ROUTER;
        } else if ("RING".equals(type)) {
            return RING;
        } else if ("LOCK".equals(type)) {
            return LOCK;
        } else if ("GATEWAY".equals(type)) {
            return GATEWAY;
        } else if ("GUARD".equals(type)) {
            return GUARD;
        } else if ("CLOUDADMIN".equals(type)) {
            return CLOUDADMIN;
        } else if ("WIFIGUARD".equals(type)) {
            return WIFIGUARD;
        } else if ("OUTER".equals(type)) {
            return OUTER;
        } else if ("TEMP".equals(type)) {
            return TEMP;
        } else if ("HLOCK".equals(type)) {
            return HLOCK;
        } else if ("THIRDPARTYDEVICE".equals(type)) {
            return THIRDPARTYDEVICE;
        } else if ("WORKMANAGER".equals(type)) {
            return WORK_MANAGER;
        } else if ("FLOCK".equals(type)) {
            return FLOCK;
        } else return UNKNOWN;
    }

    public static boolean isAppUser(int type) {
        return type == AccountType.USER
                || type == AccountType.OUTER
                || type == AccountType.TEMP
                || type == AccountType.CLOUDADMIN
                || type == AccountType.WORK_MANAGER;
    }

    public static boolean isOuterOrTempAppUser(int type) {
        return type == AccountType.OUTER
                || type == AccountType.TEMP;
    }

    public static boolean isLOCK(int type) {
        return type == AccountType.LOCK;
    }

    public static boolean isLOCKAlike(int type) {
        return type == AccountType.LOCK || type == AccountType.HLOCK || type == AccountType.FLOCK;
    }

    public static boolean isCloudAdmin(int type) {
        return type == AccountType.CLOUDADMIN;
    }

    public static boolean isGuard(int type) {
        return type == AccountType.GUARD;
    }

    public static boolean isWifiGuard(int type) {
        return type == AccountType.WIFIGUARD;
    }

    public static boolean isHLock(int type) {
        return type == AccountType.HLOCK;
    }

    public static boolean isRing(int type) {
        return type == AccountType.RING;
    }

    public static boolean isGate(int type) {
        return type == AccountType.GATEWAY;
    }

    public static boolean isFLock(int type) {
        return type == AccountType.FLOCK;
    }

    public static String getAccountTypeString(int type) {
        if (USER == type) {
            return "USER";
        } else if (ROUTER == type) {
            return "ROUTER";
        } else if (RING == type) {
            return "RING";
        } else if (LOCK == type) {
            return "LOCK";
        } else if (GATEWAY == type) {
            return "GATEWAY";
        } else if (GUARD == type) {
            return "GUARD";
        } else if (CLOUDADMIN == type) {
            return "CLOUDADMIN";
        } else if (WIFIGUARD == type) {
            return "WIFIGUARD";
        } else if (OUTER == type) {
            return "OUTER";
        } else if (TEMP == type) {
            return "TEMP";
        } else if (HLOCK == type) {
            return "HLOCK";
        } else if (THIRDPARTYDEVICE == type) {
            return "THIRDPARTYDEVICE";
        } else if (WORK_MANAGER == type) {
            return "WORKMANAGER";
        } else if (FLOCK == type) {
            return FLOCK_STRING;
        } else return "UNKNOWN";
    }

    public static String getAccountTypeUNKNOWN() {
        return "UNKNOWN";
    }

    public static boolean isWorkManger(int type) {
        return type == WORK_MANAGER;
    }

}
