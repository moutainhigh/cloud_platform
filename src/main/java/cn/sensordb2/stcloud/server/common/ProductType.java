package cn.sensordb2.stcloud.server.common;

/**
 * Created by sensordb on 16/10/18.
 */
public class ProductType {
    public static final String SMARTLIFE = "SMARTLIFE";
    public static final String COMPANY_GUARD = "COMPANY_GUARD";
    public static final String COMMUNITY_GUARD = "COMMUNITY_GUARD";

    public static boolean isValidProductType(String productType) {
        return SMARTLIFE.equals(productType) ||
                COMPANY_GUARD.equals(productType) ||
                COMMUNITY_GUARD.equals(productType);
    }
}
