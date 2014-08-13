package szregplat.util;

import szregplat.omc.Ws320RuntimeException;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by hch on 2014/5/1.
 */
public class TimeUtils {
    public static long HourMs = 60 * 60 * 1000;
    public static long DayMs = 24 * HourMs;

    private static DateFormat defaultFormatter
            = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static String getTimeStamp() {
        Calendar now = Calendar.getInstance();
        return defaultFormatter.format(now.getTime());
    }

    public static String getTimeStamp(String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        Calendar now = Calendar.getInstance();
        return df.format(now.getTime());
    }

    public static String getTimeStamp(Calendar cal, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(cal.getTime());
    }

    public static String getTimeStamp(long ms, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ms);
        return df.format(cal.getTime());
    }


    public static XMLGregorianCalendar stringToXMLGregorianCalendar(String dateStr, String format) {
        DateFormat df = new SimpleDateFormat(format);
        Date d = null;
        try {
            d = df.parse(dateStr);
        } catch (ParseException e) {
            throw new Ws320RuntimeException(e);
        }

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(d);
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (DatatypeConfigurationException e) {
            throw new Ws320RuntimeException(e);
        }
    }
}
