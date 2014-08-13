package szregplat.dbcore.cache;

import szregplat.model.Cache;
import szregplat.model.Hospital;
import szregplat.model.Patient;
import szregplat.model.be.Config;

import java.util.HashMap;

/**
 * User: WeiSW
 * Date: 14-8-4
 * Time: 下午2:25
 * Cache负责缓存的声明、加载
 */
public class DBCache {

    /**一个最大的缓存对象*/
    public static Cache cache = Cache.getInstance() ;
    /**
     * 成员变量hospitals、patients、configs对应的是缓存中的三大缓存对象
     */
    public static HashMap<String,Hospital> hospitals ;
    public static HashMap<String,Patient> patients ;
    public static HashMap<String,Config> configs ;
    /**用于DB函数内部的变量*/
    private static HashMap<String,Hospital> hospPing ;
    private static HashMap<String,Hospital> hospPang ;

    /**静态代码块，让三大缓存对象引用Cache对象的缓存对象*/
    static{
        hospitals = cache.hospitals ;
        patients = cache.patients ;
        configs = cache.configs ;
    }

    /**
     * 上电加载缓存
     * object Patient 更新Patient缓存
     * object Hospital 更新Hospital缓存
     * object Config 更新Config缓存
     * object others 更新所有缓存
     * @param
     */
    public static void poweron(Object obect){}

    /*updatePatient();//不一致如何处理，告警
    forceUpdatePatientByCache 不一致以缓存为准，多的写入少的。

    {foreach updatePatient(idcard)}
    updatePatient(idcard);*/

    /**
     * 更新身份证号为idcard的缓存
     * @param idCard 需要更新用户缓存，用户身份证号
     */
    public static void updatePatient(String idCard){}

    /**
     * 更新医院的缓存，医院的名称为hospName
     * @param hospName 需要更新医院缓存，医院的名称
     */
    public static void updateHospital(String hospName){}


    /**********************内部函数************************/
    /**
     * 实现Hospital 缓存的乒乓切换，保证医院传送和程序使用的是不同的Hospital 缓存
     * @return
     */
    public static HashMap<String,Hospital> db_PingPangHospital(){
        return null ;
    }

}
