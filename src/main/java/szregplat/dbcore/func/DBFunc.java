package szregplat.dbcore.func;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szregplat.dbcore.cache.DBCache;
import szregplat.dbcore.cache.DBCore;
import szregplat.dbcore.cache.ModContent;
import szregplat.dbcore.cache.QueueEntry;
import szregplat.dbcore.dao.ConfigDao;
import szregplat.dbcore.dao.VendorDao;
import szregplat.model.Hospital;
import szregplat.model.Patient;
import szregplat.model.be.Config;
import szregplat.model.be.RegInfo;
import szregplat.model.be.Result;
import szregplat.model.be.Vendor;
import szregplat.model.enumerate.ErrorCode;

/**
 *
 * User: WeiSW
 * Date: 14-8-4
 * Time: 下午2:22
 * 负责处理业务接口
 */
public class DBFunc {
    private static final Logger log = LoggerFactory.getLogger(DBFunc.class);

    /**
     * 向缓存和数据库中插入一条患者记录。缓存中插入成功，再向数据库插入(阻塞队列)
     * @param patient 需要插入缓存和数据库中的用户
     * @return        如果插入到缓存失败，直接返回错误，不操作数据库
     */
    public static Result addUser(Patient patient){
        Result result = new Result();
        if(patient == null){
            /**患者的数据为空*/
            result.set(ErrorCode.ParaVerifyFail,"患者信息为空");
            return result ;
        }
        if(patient.idcard == null){
            /**患者身份证为空*/
            result.set(ErrorCode.DataIncomplete,"患者的身份证号为空");
            return  result ;
        }
        /**向缓存中添加用户*/
        Result temp = DBCore.db_addPatientToCache(patient);
        if(temp == null || temp.ResultCode != 0){
            /**向缓存中添加用户失败*/
            result = temp ;
            return result;
        }

        try{
            QueueEntry entry = new QueueEntry();
            entry.object = patient;
            entry.operate = 1 ;
            /**向阻塞的队列中添加Entry。Entry中包括操作的对象和操作类型*/
            DBCore.db_addEntryToQueue(entry);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 获得缓存中的用户信息。
     * @param idCard       患者的身份证号
     * @return             缓存中的患者信息，如果缓存中没有患者的信息，返回null
     */
    public static Patient getUser(String idCard){
        Patient patient = null ;
        if(idCard != null){
            patient = DBCore.db_getPatientByIdCardInCache(idCard);
        }
        return patient;
    }

    /**
     * 根据患者的身份证号修改患者的其它信息,,先修改缓存中的，将修改数据库中数据的操作放到队列中(阻塞队列)
     * @param patient      患者的信息。其中包含需要更新的信息
     * @return             操作缓存或数据库返回的结果
     */
    public static Result modifyUser(Patient patient) {
        Result result = new Result();
        if (patient == null) {
            /**患者信息为空*/
            result.set(ErrorCode.ParaVerifyFail,"患者信息为空");
            return result;
        }
        if (patient.idcard == null) {
            /**患者的身份证号为空*/
            result.set(ErrorCode.DataIncomplete,"患者的身份证号为空");
            return result;
        }
        /**修改缓存中的患者信息*/
        Result temp = DBCore.db_modifyPatientInCache(patient);
        if (temp == null || temp.ResultCode != 0) {
            result = temp ;
            return result;
        }

        try {
            QueueEntry entry = new QueueEntry();
            entry.object = patient;
            entry.operate = 2;
            /**向阻塞队列中添加Entry，*/
            DBCore.db_addEntryToQueue(entry);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;

    }

    /**
     * 根据配置项关键字key，获得缓存中的配置信息。
     * 如果缓存中的配置信息不存在，获得数据库中的配置信息
     * @param key           配置项的关键字
     * @return              如果缓存中的配置信息存在，返回缓存中的配置信息。
     *                      如果缓存中的不存在，数据库中的存在，返回数据库中的配置信息
     *                      如果两个都不存在，返回null。
     */
    public static Config getConfig(String key){
        Config config = null ;
        if(key!=null){
            /**获得缓存中的配置信息*/
            config = DBCore.db_getConfigByKeyInCache(key);
            if(config == null){
                ConfigDao configDao = new ConfigDao();
                /**获得数据库中的配置信息*/
                config = configDao.getByKeyDB(key);
            }
        }
        return config ;
    }

    /**
     * 根据医院的名字，获得医院缓存
     * @param hospName      医院的名字
     * @param out_hospital  出参。缓存中医院的信息
     * @return              返回缓存结构中的医院缓存，如果医院缓存没有建立返回null
     */
    public static Result getHospital(String hospName ,Hospital out_hospital){
        Result result = new Result();
        if(hospName == null){
            /**医院名称为空*/
            result.set(ErrorCode.AbsentPara,"缺少必选参数医院名称");
            return result;
        }
        if(out_hospital == null){
            /**传进来的出参，out_hospital为空*/
            result.set(ErrorCode.ParaVerifyFail,"参数验证没通过");
            return result;
        }
        /**获得缓存中的医院*/
        out_hospital = DBCache.hospitals.get(hospName);

        if(out_hospital.hospid == null || out_hospital.hospBasic == null){
            /**从缓存中获得医院失败*/
            result.set(ErrorCode.HandleCacheFail,"获得缓存中的医院信息失败");
            return result ;
        }

        return result;

    }

    /**
     * 向预约缓存中添加一条预约记录。修改成功后，将修改数据库的操作放到队列中(阻塞队列)
     * @param regInfo            需要添加的预约记录
     * @return                   返回操作缓存或数据库的结果
     */
    public static Result addReg(RegInfo regInfo){
        Result result = new Result();
        if(regInfo == null){
            /**预约信息为空*/
            result.set(ErrorCode.ParaVerifyFail,"预约信息为空");
            return result;
        }
        if(regInfo.idCard == null){
            /**身份证号为空*/
            result.set(ErrorCode.DataIncomplete,"患者的身份证号为空");
            return result;
        }
        /**向缓存中添加预约信息*/
        Result temp = DBCore.db_addRegInfoToCache(regInfo);
        if(temp == null || temp.ResultCode != 0){
            /**缓存中添加预约纪录失败*/
            result = temp ;
            return result;
        }

        try{
            QueueEntry entry = new QueueEntry();
            entry.object = regInfo;
            entry.operate = 1;
            /**向阻塞队列中添加Entry*/
            DBCore.db_addEntryToQueue(entry);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        return result ;
    }

    /**
     * 删除预约缓存中的预约记录，删除成功后，将修改数据库的操作放到队列中（阻塞队列）
     * @param idCard      预约患者的身份证号
     * @param SN          预约流水号
     * @return            返回操作缓存或数据库的结果
     */
    public static Result delReg(String idCard,Long SN){
        Result result = new Result();
        if(idCard == null || SN == null){
            /**参数idCard或SN为空*/
            result.set(ErrorCode.ParaVerifyFail,"参数验证没通过");
            return result;
        }
        /**获得缓存中的该用户*/
        Patient patient = DBCache.patients.get(idCard);
        if(patient == null){
            /**缓存中没有该用户*/
            result.set(ErrorCode.MismatchDataInCache,"缓存中没有指定用户");
            return result;
        }

        /**获得缓存中的预约记录*/
        RegInfo regInfo = new RegInfo();
        /**删除缓存中的预约记录,同时临时保存删除的预约记录*/
        Result temp = DBCore.db_deleteRegInfoInCache(patient,SN,regInfo);
        if(result.ResultCode != 0){
            /**操作缓存中的预约记录异常*/
            result = temp ;
            return result;
        }

        QueueEntry queueEntry = new QueueEntry();
        queueEntry.object = regInfo;
        queueEntry.operate = 3;
        try{
            /**操作数据库，放到阻塞队列中*/
            DBCore.db_addEntryToQueue(queueEntry);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 根据用户名和密码查询数据库。获取厂商信息
     * @param username      厂商用户名
     * @param password      密码
     * @param out_vendor    出参。厂商的信息
     * @return              返回操作数据库的结果
     */
    public static Result getVendor(String username,String password, Vendor out_vendor){
        Result result = new Result();
        if(username == null || password == null){
            /**缺少参数username+password*/
            result.set(ErrorCode.ParaVerifyFail,"参数验证没通过");
            return result ;
        }

        Vendor temp = new Vendor();
        /**通过DAO层访问数据库的厂商信息表*/
        VendorDao vendorDao = new VendorDao();
        temp = vendorDao.getVendorByUserNameAndPass(username,password);
        if(temp == null){
            result.set(ErrorCode.MismatchDataInCache,"用户名或密码与数据库中保存的不一致");
            return result ;
        }
        if(out_vendor!=null){
            /**给出参赋值*/
            out_vendor = temp ;
        }

        return result;
    }

    /**
     * 修改预约记录，先修改缓存中的预约记录，再将修改数据库中的预约记录操作放到队列中(阻塞队列)
     * @param idCard            患者身份证号
     * @param regInfo           预约记录的信息
     * @param sn                预约流水号
     * @param modContent        需要修改的预约字段组合成的类
     * @return
     */
    public static Result modifyReg(String idCard,RegInfo regInfo , Long sn , ModContent modContent){
        Result result = new Result();
        if(idCard == null){
            result.set(ErrorCode.AbsentPara,"缺少必选参数身份证号");
            return result;
        }
        if(regInfo == null && sn == null){
            result.set(ErrorCode.ParaVerifyFail,"预约信息和挂号流水号必须存在一个");
            return result;
        }
        if(modContent == null){
            result.set(ErrorCode.ParaVerifyFail,"需要修改的字段为空");
            return result;
        }

        if(DBCache.patients == null){
            /**用户缓存列表没有建立*/
            result.set(ErrorCode.CacheListNotFound,"用户缓存列表没有建立");
            log.error("用户缓存列表没有建立");
            return result;
        }
        if(DBCache.patients.get(idCard) == null){
            /**用户缓存中没有指定的用户*/
            result.set(ErrorCode.HandleCacheFail,"缓存中没有指定的用户");
            return result;
        }
        if(DBCache.patients.get(idCard).reginfos == null){
            /**该用户没有预约记录*/
            result.set(ErrorCode.HandleCacheFail,"缓存中指定用户没有预约记录");
            return result;
        }

        if(sn == null){
            /**SN不存在，从RegInfo中获得SN*/
            result = DBCore.db_updateRegInfoInDBAndCache(idCard,regInfo,modContent);
        }else{
            /**SN存在，忽略RegInfo.*/
            RegInfo regInfo1 = new RegInfo();
            regInfo1.SN = sn ;
            result = DBCore.db_updateRegInfoInDBAndCache(idCard, regInfo1, modContent);
        }

        return result;
    }


    /**
     * 性能统计，
     * @param itemName 性能统计项的名称
     */
    public static void measure(String itemName){

    }

    /**
     * 告警通知
     * @param message 告警的消息
     * @param level   告警的级别。共5级（1-5），1级最低，5级最高
     */
    public static void alert(String message,int level){

    }

    /**
     * 通知业务接口
     * 仅用于通知业务缓存已更新，由FE通知第三方数据改变
     */
    public static void notifyFE(){

    }




}
