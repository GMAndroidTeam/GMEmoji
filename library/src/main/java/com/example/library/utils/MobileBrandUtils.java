package com.example.library.utils;

import android.os.Build;
import android.text.TextUtils;

/**
 * @Description
 * @Author mashihong
 * @CreatedDate 2019-06-27
 */
public class MobileBrandUtils {
    private static final String PREFIX_HUAWEI = "HUAWEI";
    private static final String PREFIX_XIAOMI = "XIAOMI";
    private static final String PREFIX_OTHERS = "OTHERS";

    //MIUI标识
    public static final String BRAND_MIUI = "xiaomi";

    //EMUI标识
    public static final String BRAND_EMUI1 = "huawei";
    public static final String BRAND_EMUI2 = "honor";

    public enum ROM_TYPE {
        MIUI(PREFIX_XIAOMI),
        EMUI(PREFIX_HUAWEI),
        OTHER(PREFIX_OTHERS);

        private String prefix;

        ROM_TYPE(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    /**
     * @param
     * @return ROM_TYPE ROM类型的枚举
     * @description获取ROM类型: MIUI_ROM, EMUI_ROM, OTHER_ROM
     */

    public static ROM_TYPE getRomType() {
        ROM_TYPE rom_type = ROM_TYPE.OTHER;

        String brand = Build.BRAND;
        if (!TextUtils.isEmpty(brand)) {
            if (brand.toLowerCase().contains(BRAND_EMUI1) || brand.toLowerCase().contains(BRAND_EMUI2)) {
                return ROM_TYPE.EMUI;
            }
            if (brand.toLowerCase().contains(BRAND_MIUI)) {
                return ROM_TYPE.MIUI;
            }
        }
        return rom_type;
    }

}
