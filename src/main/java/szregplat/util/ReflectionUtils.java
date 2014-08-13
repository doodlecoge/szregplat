package szregplat.util;

import szregplat.omc.Ws320RuntimeException;

import java.lang.reflect.Field;

/**
 * Created by hch on 2014/5/2.
 */
public class ReflectionUtils {
    public static <T> T clone(Class<T> klass, Object obj) {
        T t = null;

        try {
            t = klass.newInstance();
        } catch (Exception e) {
            throw new Ws320RuntimeException(e);
        }

        Field[] declaredFields = klass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            try {
                Object val = declaredField.get(obj);
                declaredField.set(t, val);
            } catch (IllegalAccessException e) {
                throw new Ws320RuntimeException(e);
            }
        }

        return t;
    }
}
