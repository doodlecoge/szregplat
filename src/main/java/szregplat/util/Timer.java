package szregplat.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * Created by hch on 2014/5/18.
 */
public class Timer {
    private static final Logger log = LoggerFactory.getLogger(Timer.class);
    private long start;
    private String name;

    private Timer(String name) {
        this.name = name;
    }

    public static Timer getInstance(String name) {
        return new Timer(name);
    }

    public void start() {
        start = Calendar.getInstance().getTimeInMillis();
    }

    public void stop() {
        long diff = Calendar.getInstance().getTimeInMillis() - start;
        log.debug(name + ": " + diff + "ms");
    }
}
