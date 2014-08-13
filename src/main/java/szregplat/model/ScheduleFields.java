package szregplat.model;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zq on 2014/5/25.
 */
public class ScheduleFields {
    private static Map<String, Field> map = new HashMap<String, Field>();

    static {
        Field[] fields = Schedule.class.getFields();
        for (Field field : fields) {
            map.put(field.getName(), field);
        }
    }

    public static Field get(String name) {
        return map.get(name);
    }
}
