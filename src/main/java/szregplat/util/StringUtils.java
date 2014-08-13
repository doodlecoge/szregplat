package szregplat.util;

/**
 * Created by Administrator on 14-4-13.
 */
public class StringUtils {
    //todo:
    public String replaceAll(String targetString, String[] searchs, String replaces) {
        if (searchs == null || replaces == null || searchs.length != replaces.length()) {

        }
        return targetString;
    }


    public static String encode(String value) {
        if (value == null) {
            return "";
        } else {
            return (value.replaceAll("&", "&amp;").
                    replaceAll("/", "&#47;").
                    replaceAll("<", "&lt;").
                    replaceAll(">", "&gt;").
                    replaceAll("\"", "&quot;").
                    replaceAll("!", "&#33;").
                    replaceAll("\\?", "&#63;").
                    replaceAll("=", "&#61;").
                    replaceAll("%", "&#37;").
                    replaceAll("'", "&apos;"));
        }
    }
}
