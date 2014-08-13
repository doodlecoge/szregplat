package szregplat.model;

import szregplat.model.be.Config;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-29
 * Time: 下午1:17
 * 最大的缓存对象类Cache，存放Hospital、Patient、config三大缓存
 * Cache采用单例模式。保证只有一个大的缓存对象
 */
public class Cache {

    private static Cache cache ;

    public HashMap<String,Hospital> hospitals ;
    public HashMap<String,Patient> patients ;
    public HashMap<String,Config> configs ;
    private Cache(){
        hospitals = new HashMap<String,Hospital>();
        patients = new HashMap<String,Patient>();
        configs = new HashMap<String, Config>();
    }

    public static Cache getInstance(){
        if(cache==null)
            cache = new Cache();
        return cache ;
    }

}
