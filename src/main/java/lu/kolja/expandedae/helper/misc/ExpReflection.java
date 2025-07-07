package lu.kolja.expandedae.helper.misc;

import java.lang.reflect.Field;

public class ExpReflection {
    public static Field getField(Class<?> clazz, String field) {
        Field f;

        try {
            f = clazz.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Failed to find field: " + field, e);
        }

        f.setAccessible(true);
        return f;
    }

    public static void setField(Object obj, Field field, Object value) {
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to set field: " + field, e);
        }
    }
}
