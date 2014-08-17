package szregplat.dbcore.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szregplat.dbcore.dao.ConfigDao;
import szregplat.dbcore.dao.RegDao;
import szregplat.dbcore.dao.UserDao;
import szregplat.model.Cache;
import szregplat.model.Hospital;
import szregplat.model.Patient;
import szregplat.model.be.Config;
import szregplat.model.be.RegInfo;

import java.util.*;

/**
 * User: WeiSW
 * Date: 14-8-4
 * Time: 下午2:25
 * Cache负责缓存的声明、加载
 */
public class DBCache {
    private static final Logger log = LoggerFactory.getLogger(DBCache.class);
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

    /**
     * 更新医院缓存列表
     * */
    public static void updateHospital(){

    }

    /**
     * 更新用户缓存列表
     */
    public static void updatePatient(){
        /**1、拷贝一份用户缓存*/
        HashMap<String,Patient> copyPatients = DBCache.patients ;
        /**2、修改拷贝的缓存*/
        /****①、数据库中的患者信息列表*/
        UserDao userDao = new UserDao();
        List<Patient> patientList_DB = userDao.getAllPatients() ;
        /****②、数据库中的患者信息与缓存中的患者信息整合*/

        if(copyPatients.size() > patientList_DB.size())
            forceUpDatePatientsByCache(patientList_DB, copyPatients);
        else
            forceUpDatePatientsByDB(patientList_DB,copyPatients);
        /**3、切换用户缓存*/
        DBCache.patients = copyPatients ;
    }

    /**
     * 更新配置缓存列表...因为配置项新加的配置项直接写库，
     * 只会有这种情况：
     *       新的配置项-->写库-->完成
     * 不会出现这种情况：
     *       新的配置项-->写缓存-->同步数据库-->完成
     *
     * 所以更新配置缓存列表，，只要从数据库中获得所有的配置项，并加载到缓存即可。
     */
    public static void updateConfig(){
        ConfigDao configDao = new ConfigDao();
        /**1、获得数据库中的配置项列表*/
        ArrayList<Config> configList = configDao.getAllConfigs();
        /**2、组装数据库中的配置项列表*/
        HashMap<String,Config> configHashMap = new HashMap<String, Config>() ;
        if(configList == null){
            /******数据库中的配置项为空 --> 缓存中的配置列表不会变为null，，还是用原来的配置项缓存列表**/
            log.error("数据库中的配置项为空");
            return;
        }
        for(int i = 0 ; i < configList.size() ; i++){
            Config config = configList.get(i) ;
            configHashMap.put(config.key,config);
        }
        /**3、切换缓存中的配置项列表*/
        DBCache.configs = configHashMap ;

    }

    /**
     * 更新医院的缓存，医院的名称为hospName
     * @param hospName 需要更新医院缓存，医院的名称
     */
    public static void updateHospital(String hospName){}

    /**
     * 更新身份证号为idcard的缓存
     * @param idCard 需要更新用户缓存...用户身份证号
     */
    public static void updatePatient(String idCard){
        UserDao userDao = new UserDao();
        /**数据库中的用户信息*/
        Patient patient_DB = userDao.getPatientByIdCard(idCard);
        /**缓存中的用户信息*/
        Patient patient_Cache = DBCache.patients.get(idCard);
        /**让缓存中的用户，保持和数据库中的用户信息相同
         * 存在风险：如果后台修改数据库中的患者信息成功，
         */
        //patient_Cache = patient_DB ;
        /**将数据库中的用户封装成list*/
        List<Patient> patientList_DB = new ArrayList<Patient>();
        patientList_DB.add(patient_DB);
        /**将缓存中的用户封装成Map*/
        HashMap<String,Patient> patientMap_Cache = new HashMap<String, Patient>();
        patientMap_Cache.put(patient_Cache.idcard,patient_Cache);
        /**强制更新用户缓存，保持和数据库中的一致*/
        forceUpDatePatientsByDB(patientList_DB,patientMap_Cache);

    }

    /************************************内部函数*******************************/
    /**
     * 实现Hospital 缓存的乒乓切换，保证医院传送和程序使用的是不同的Hospital 缓存
     * @return
     */
    private static HashMap<String,Hospital> db_PingPangHospital(){
        return null ;
    }

    /**
     * 更新用户缓存列表。
     * 缓存中的数据比较多，向数据库中添加多余的用户数据
     * 这个函数调用的机会应该不多：
     * 只有一种情况可能会调用：
     *      1、新添加的用户,缓存中添加成功，写数据库操作还没有开始
     *      (但是这种情况，是会弥补上来的。。因为队列写库一直都在运行)
     * @param patientList_DB 数据库中的患者信息列表
     * @param patients_Cache copy的用户缓存Map
     */
    private static void forceUpDatePatientsByCache(List<Patient> patientList_DB, HashMap<String, Patient> patients_Cache){
        UserDao userDao = new UserDao();
        /**数据库中的用户名称集合>>>>>
         * 方便下面的比较，将用户名字放到集合中，这样下面就避免出现双层循环
         * */
        HashSet<String> userNameSet_DB = new HashSet<String>();
        for(int i = 0 ; i < patientList_DB.size() ; i++){
            userNameSet_DB.add(patientList_DB.get(i).idcard) ;
        }
        /**缓存中的用户集合*/
        Set<Map.Entry<String,Patient>> patientsEntryCache = patients_Cache.entrySet();
        /**遍历缓存中的用户集合*/
        Iterator<Map.Entry<String,Patient>> iter = patientsEntryCache.iterator() ;
        for( ; iter.hasNext() ; ){
            Map.Entry<String,Patient> patientEntry = iter.next() ;
            if(!userNameSet_DB.contains(patientEntry.getKey())){
                /**数据库中没有该用户。。则将该用户添加到数据库中*/
                userDao.insert(patientEntry.getValue());
            }
        }

    }

    /**
     * 更新用户缓存列表。
     * 数据库中的数据比较多，向缓存中添加多余的用户数据
     * @param patientList_DB  数据库中的患者信息列表
     * @param patients_Cache  copy的用户缓存Map
     */
    private static void forceUpDatePatientsByDB(List<Patient> patientList_DB , HashMap<String,Patient> patients_Cache){

        for(int i = 0 ;i < patientList_DB.size() ; i++){
            /**数据库中的一条患者信息*/
            Patient patient_DB = patientList_DB.get(i) ;
            String idCard_DB = patient_DB.idcard ;
            if(patients_Cache.containsKey(idCard_DB)){
                /**缓存中存在该用户*/
                Patient patient_Cache = patients_Cache.get(idCard_DB) ;
                /****更新该用户缓存的字段*/
                deepCopyPatient(patient_Cache,patient_DB);
            }else{
                /**缓存中不存在该用户--->向缓存中追加该用户>>>>>>>预约记录列表是否追加到缓存中
                 * 预约记录应该追加到缓存中，应为DBFunc中modiReg接口，当缓存中没有对应的预约记录时，是不会继续查询数据库的
                 * */
                patients_Cache.put(idCard_DB,patient_DB);
                /**缓存中的用户追加预约记录*/
                Patient patient_Cache = patients_Cache.get(idCard_DB);
                /****数据库中该用户的预约记录列表...按照SN升序排列**/
                List<RegInfo> patient_RegInfoList_DB = new RegDao().getRegInfoListByIdCard(idCard_DB);
                /****处理两个预约记录列表**/
                List<RegInfo> regInfos = handleRegInfoList(patient_Cache.reginfos, patient_RegInfoList_DB);
                /****处理后的结果链，添加到对应的用户缓存下*/
                patient_Cache.reginfos = regInfos;

            }

        }

    }

    /**
     * 深度复制患者。
     * 将oldPatient中的字段的值赋给newPatient中的字段
     * 存在问题：
     * 情况1、oldPatient = DB中的Patient ; newPatient = Cache中的Patient
     *      如果用户修改了医保类型等，缓存中的修改成功，修改操作放到队列中，数据库中的还没有修改。
     * 情况2、oldPatient = Cache中的Patient ; newPatient = DB中的Patient
     *      对于这个copyPatientList可能不是最新的
     * @param newPatient    最终形成的新的患者
     * @param oldPatient    旧的患者,复制其中的字段
     */
    private static void deepCopyPatient(Patient newPatient , Patient oldPatient){
        //这样处理？？？？？
        newPatient.idcard = oldPatient.idcard ;
        newPatient.patientname = oldPatient.patientname ;
        newPatient.password = oldPatient.password ;
        newPatient.birthday = oldPatient.birthday ;
        newPatient.sex = oldPatient.sex ;
        newPatient.insureType = oldPatient.insureType ;
        newPatient.phone = oldPatient.phone ;
        newPatient.isBlack = oldPatient.isBlack ;
        newPatient.payAccount = oldPatient.payAccount ;
        newPatient.familyid = oldPatient.familyid ;

        /**对预约记录链表进行处理*/
        newPatient.reginfos = handleRegInfoList(newPatient.reginfos,oldPatient.reginfos);

    }

    /**
     * 处理预约记录链表。。将两个链表进行整合，得到最终的预约记录链表
     * 这里链表的处理只判断预约流水号SN是否相同，
     * 1、如果SN相同，最终形成的链表，以缓存中的预约记录为准。（因为可能数据库没有及时更新）
     * 2、如果数据存在SN而缓存没有，则将数据库中的预约记录添加到最终链表中
     * @param regInfoList_Cache  缓存中的预约记录链表
     * @param regInfoList_DB     数据库中预约记录链表
     * @return          两个链表处理后。最终得到的链表
     */
    private static List<RegInfo> handleRegInfoList(List<RegInfo> regInfoList_Cache , List<RegInfo> regInfoList_DB){
        List<RegInfo> list = new ArrayList<RegInfo>();

        if(regInfoList_Cache == null || regInfoList_Cache.size() == 0 ){
            list = regInfoList_DB ;
            return list ;
        }
        if(regInfoList_DB == null || regInfoList_DB.size() == 0 ){
            list = regInfoList_Cache ;
            return list ;
        }

        /**
         * 两个链表都不为空....
         * 两条链表是指定时间内的个人预约记录链表，数据不会太多。
         */
        /**1、将数据库中的预约记录，放到HashMap中。。为了避免下面的双层循环*/
         HashMap<Long,RegInfo> regInfoMap_DB = new HashMap<Long,RegInfo>();
         for(int i = 0 ; i < regInfoList_DB.size() ; i++){
            RegInfo regInfo = regInfoList_DB.get(i) ;
            regInfoMap_DB.put(regInfo.SN,regInfo) ;
         }
        /**2、将缓存中的预约链表，全部添加到最终链表*/
        list.addAll(regInfoList_Cache) ;
        /**3、遍历最终链，将数据库中独有的预约记录添加到最终链中*/
        for(int i = 0 ; i < list.size() ; i++ ){
            RegInfo regInfo = list.get(i) ;
            if(!regInfoMap_DB.containsKey(regInfo.SN)){
                list.add(regInfoMap_DB.get(regInfo.SN)) ;
            }
        }

        /**4、返回处理后的最终链表*/
        return list ;
    }


}
