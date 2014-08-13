package szregplat.webservice.client;

import szregplat.util.Config;

/**
 * Created by Administrator on 14-4-13.
 */
public class HisClientFactory {
    private static Config sysConfig = Config.getInstance("system.properties");

    // todo: add client for each hospital
    public static HisClientIface getHisClient(String hospitalId) {
        if ("1".equals(sysConfig.getString("debug"))) {
            return new TestHisClient();
        }

        if ("".equals(hospitalId)) {
            return null;
        } else {
            return new DefaultHisClient();
        }
    }
}
