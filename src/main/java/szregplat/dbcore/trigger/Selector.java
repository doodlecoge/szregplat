package szregplat.dbcore.trigger;

import szregplat.omc.Ws320Exception;
import szregplat.omc.Ws320RuntimeException;
import szregplat.model.Schedule;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zq on 2014/5/25.
 */
public class Selector {
    private static final Pattern ptn = Pattern.compile("\\(([a-zA-Z0-9]+)='([^']+)'\\)");

    private final Map<Field, String> kvs = new HashMap<Field, String>();

    private Selector() {

    }

    public boolean match(Schedule schedule) {
        for (Field field : kvs.keySet()) {
            Object val = null;
            try {
                val = field.get(schedule);
            } catch (IllegalAccessException e) {
                throw new Ws320RuntimeException(e);
            }
            if (!kvs.get(field).equals(val)) return false;
        }
        return true;
    }

    public static Selector getInstance(String query) throws Ws320Exception {
        if (query == null) return null;

        Matcher matcher = ptn.matcher(query);
        Selector selector = new Selector();
        selector.kvs.clear();
        while (matcher.find()) {
            String k = matcher.group(1);
            String v = matcher.group(2);
            ScheduleAttributeMap attr;

            try {
                attr = ScheduleAttributeMap.valueOf(k);
            } catch (Exception e) {
                throw new Ws320Exception("select attribute [" + k + "] does not exists", e);
            }

            selector.kvs.put(attr.field, v);
        }
        return selector;
    }

    public static void main(String[] args) {
        String str = "(a='100') and (b='20')";
        Matcher matcher = ptn.matcher(str);

        while (matcher.find()) {
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
        }
    }
}
