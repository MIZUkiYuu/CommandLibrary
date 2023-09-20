package me.mizukiyuu.customsolidsky.utils;

public class EnumUtil {
    public static Enum<?> valueOf(Class<? extends Enum<?>> clazz, String str) {
        if (clazz.isEnum()) {
            Enum<?>[] enums = clazz.getEnumConstants();
            for (Enum<?> anEnum : enums) {
                if (anEnum.name().equals(str)) {
                    return anEnum;
                }
            }
        }
        return null;
    }
}
