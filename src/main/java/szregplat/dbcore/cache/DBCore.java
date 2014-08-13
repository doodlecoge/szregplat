package szregplat.dbcore.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szregplat.model.Patient;
import szregplat.model.be.Config;
import szregplat.model.be.RegInfo;
import szregplat.model.be.Result;
import szregplat.model.enumerate.ErrorCode;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * User: WeiSW
 * Date: 14-8-4
 * Time: 下午2:23
 * 负责和数据库打交道，负责处理并发、Cache变更
 * 负责并发调用，以及负责缓存和物理库的同步
 */
public class DBCore {

    private static final Logger log = LoggerFactory.getLogger(DBCore.class);
    private static BlockingQueue blockingQueue ;

    /**
     * 向DBFunc提供的方法，在DBFunc内部使用。将QueueEntry添加到队尾...
     * 方法被加锁，，添加Entry到队列
     * @param queueEntry          添加到队尾的对象
     * @throws InterruptedException
     */
    public static synchronized void db_addEntryToQueue(QueueEntry queueEntry) throws InterruptedException {
        blockingQueue.put(queueEntry);
    }


    /*********************Cache 变更*********************/

    /**
     * 向患者缓存中添加新的患者.判断患者是否已经存在.
     * @param patient 患者的信息
     * @return        返回操作缓存的结果
     */
    public static Result db_addPatientToCache(Patient patient){
        Result result = new Result();
        if(patient == null){
            /**患者信息为空*/
            result.set(ErrorCode.ParaVerifyFail,"患者信息为空");
            return result;
        }
        if(patient.idcard == null){
            /**患者身份证号为空*/
            result.set(ErrorCode.DataIncomplete,"患者的身份证号为空");
            return result;
        }

        if (DBCache.patients == null) {
            /**用户缓存列表没有建立*/
            result.set(ErrorCode.CacheListNotFound,"用户缓存列表没有建立");
            log.error("用户缓存列表没有建立");
            return result ;
        }
        /**向用户缓存添加用户*/
        DBCache.patients.put(patient.idcard, patient);
        if (!DBCache.patients.containsKey(patient.idcard)) {
            /**向用户缓存中添加用户失败*/
            result.set(ErrorCode.HandleCacheFail,"缓存中添加用户失败");
            log.error("缓存中添加用户失败!!待添加的用户 身份证号："+patient.idcard+" 。姓名："+patient.patientname);
            return result ;
        }

        return result;
    }

    /**
     * 修改缓存中的用户信息，根据Patient.idcard进行修改
     * @param patient 需要修改的患者
     * @return        修改缓存操作返回的结果
     */
    public static Result db_modifyPatientInCache(Patient patient){
        Result result = new Result();
        if(patient == null){
            /**患者信息为空*/
            result.set(ErrorCode.ParaVerifyFail,"患者信息为空");
            return result;
        }
        if(patient.idcard == null){
            /**患者的身份证号为空*/
            result.set(ErrorCode.DataIncomplete,"患者的身份证号为空");
            return result;
        }
        if(DBCache.patients == null){
            /**用户缓存列表没有建立*/
            result.set(ErrorCode.CacheListNotFound,"用户缓存列表没有建立");
            log.error("用户缓存列表没有建立");
            return result;
        }
        if(!DBCache.patients.containsKey(patient.idcard)){
            /**缓存中没有该用户*/
            result.set(ErrorCode.MismatchDataInCache,"缓存中没有指定用户数据");
            return result;
        }
        /**修改缓存中的用户*/
        DBCache.patients.put(patient.idcard,patient);
        if(!DBCache.patients.containsKey(patient.idcard)){
            /**修改缓存中的该用户失败*/
            result.set(ErrorCode.HandleCacheFail,"缓存中的数据操作失败");
            log.error("操作缓存中数据失败！！修改用户信息。待修改的用户身份证号："+patient.idcard+" 。姓名："+patient.patientname);
            return result;
        }

        return  result ;
    }

    /**
     * 根据患者的身份证号，获得缓存中的患者信息
     * @param idCard  患者的身份证号
     * @return        如果缓存中患者存在，返回该患者，否则返回null
     */
    public static Patient db_getPatientByIdCardInCache(String idCard){
        Patient patient = null ;
        if(DBCache.patients!=null){
            patient = DBCache.patients.get(idCard);
        }else{
            /**用户缓存列表没有建立*/
            log.error("用户缓存列表没有建立");
        }
        return patient;
    }

    /**
     * 根据配置项的关键字，获得配置项
     * @param key 配置项的关键字
     * @return    如果该配置项存在，返回配置项，否则返回null
     */
    public static Config db_getConfigByKeyInCache(String key){
        Config config = null ;
        if(DBCache.configs!=null){
            config = DBCache.configs.get(key);
        }else{
            /**配置缓存列表没有建立*/
            log.error("配置缓存列表没有建立");
        }
        return config;
    }

    /**
     * 将预约记录添加到相应的用户缓存下面。
     * @param regInfo 需要添加的预约记录
     * @return        返回缓存操作的结果
     */
    public static Result db_addRegInfoToCache(RegInfo regInfo){
        Result result = new Result();
        if(regInfo == null){
            /**预约信息为空*/
            result.set(ErrorCode.ParaVerifyFail,"预约信息为空");
            return result;
        }
        if(regInfo.idCard == null){
            /**身份证号为空*/
            result.set(ErrorCode.DataIncomplete,"预约人身份证号为空");
            return result;
        }
        if(DBCache.patients == null){
            /**用户缓存列表没有建立*/
            result.set(ErrorCode.CacheListNotFound,"用户缓存列表没有建立");
            log.error("用户缓存列表没有建立");
            return result;
        }

        Patient patient = DBCache.patients.get(regInfo.idCard);
        if(patient != null && patient.idcard == regInfo.idCard){
            /**获得缓存中的用户，建立用户的预约记录，或向该用户下的预约记录缓存追加预约记录*/
            ArrayList<RegInfo> temp = (ArrayList<RegInfo>)patient.reginfos;
            if(temp==null){
                temp = new ArrayList<RegInfo>();
            }
            temp.add(regInfo);
        }else{
            /**缓存中没有该用户*/
            result.set(ErrorCode.MismatchDataInCache,"缓存中用户不存在");
        }

        return result;
    }

    /**
     * 删除用户缓存下的指定的预约记录，根据预约的流水号SN
     * @param patient      指定的用户
     * @param SN           预约流水号
     * @param out_regInfo  出参，预约记录的详细信息
     * @return             返回操作缓存中预约记录的结果
     */
    public static Result db_deleteRegInfoInCache(Patient patient,Long SN,RegInfo out_regInfo){
        Result result = new Result();
        /**用户的所有的预约记录*/
        ArrayList<RegInfo> reginfos = (ArrayList<RegInfo>)DBCache.patients.get(patient.idcard).reginfos;
        if(reginfos != null){
            for(int i = 0 ; i < reginfos.size() ; i++){
                RegInfo regInfo = reginfos.get(i) ;
                if(regInfo.SN == SN){
                    /**给出参赋值*/
                    out_regInfo = reginfos.get(i);
                    /**缓存中移除指定的预约记录*/
                    reginfos.remove(i);
                    break;
                }else{
                    /**没有该预约流水号的预约记录*/
                    result.set(ErrorCode.MismatchDataInCache,"没有指定流水号的预约记录");
                }
            }
            /**添加一步判断，预约记录是否移除*//*
            ArrayList<RegInfo> temp = (ArrayList<RegInfo>)DBCache.patients.get(patient.idcard).reginfos;
            if(temp.contains(out_regInfo)){
                *//**移除缓存中的预约记录失败*//*
            }*/
        }else{
            /**该用户没有预约记录*/
            result.set(ErrorCode.HandleCacheFail,"指定用户没有预约记录");
        }

        return result ;
    }

    /**
     * 修改缓存中的预约记录。将RegInfo和modContent进行组装，形成新的RegInfo
     * @param regInfo    需要更新的预约信息
     * @param modContent 应该更新的预约字段信息
     * @return           返回更新后的预约记录
     */
    private static RegInfo db_updateRegInfo(RegInfo regInfo , ModContent modContent){
        if(modContent.fetchtime != null)
            regInfo.fetchTime = modContent.fetchtime ;
        if(modContent.insureType != null)
            regInfo.insureType = modContent.insureType ;
        if(modContent.isFetched != null)
            regInfo.isFetched = modContent.isFetched ;
        if(modContent.isPayed != null)
            regInfo.isPayed = modContent.isPayed ;
        if(modContent.payAccount != null)
            regInfo.payAccount = modContent.payAccount ;
        if(modContent.unPayedFee != null)
            regInfo.unPayedFee = modContent.unPayedFee ;
        if(modContent.payType != null)
            regInfo.payType = modContent.payType ;

        return regInfo ;
    }

    /**
     * 判断缓存中的预约记录是否更新成功,
     * @param regInfo    需要判断的预约记录信息
     * @param modContent 应该更新的预约字段信息
     * @return           返回操作缓存的结果
     */
    private static Result db_isRegInfoUpdated(RegInfo regInfo , ModContent modContent){
        Result result = new Result();
        /**失败原因（满足两者之一，都没有修改成功）
         * 1：预约记录中的字段为空，并且ModContent中对应的字段不为空
         *      如：(regInfo.fetchTime == null && modContent.fetchtime != null)
         * 2：预约记录中的字段不为空，并且预约记录中的字段和ModContent中对应的字段不相等
         *      如：(regInfo.fetchTime != null && modContent.fetchtime != regInfo.fetchTime)
         * */
        boolean boolean1 = (regInfo.fetchTime == null && modContent.fetchtime != null)
                || (regInfo.fetchTime != null && modContent.fetchtime != regInfo.fetchTime);
        if(boolean1){
            result.set(ErrorCode.HandleCacheFail,"修改缓存预约记录中的取号时间失败");
            return result;
        }

        boolean boolean2 = (regInfo.insureType == null && modContent.insureType != null)
                || (regInfo.insureType != null && modContent.insureType != regInfo.insureType);
        if(boolean2){
            result.set(ErrorCode.HandleCacheFail,"修改缓存预约记录中的医保类型失败");
            return result;
        }

        boolean boolean3 = (regInfo.isFetched == null && modContent.isFetched != null)
                || (regInfo.isFetched != null && modContent.isFetched != regInfo.isFetched);
        if(boolean3){
            result.set(ErrorCode.HandleCacheFail,"修改缓存预约记录中的是否取号失败");
            return result;
        }

        boolean boolean4 = (regInfo.isPayed == null && modContent.isPayed != null)
                || (regInfo.isPayed != null && modContent.isPayed != regInfo.isPayed);
        if(boolean4){
            result.set(ErrorCode.HandleCacheFail,"修改缓存预约记录中的是否付费失败");
            return result;
        }

        boolean boolean5 = (regInfo.payAccount == null && modContent.payAccount != null)
                || (regInfo.payAccount != null && modContent.payAccount != regInfo.payAccount);
        if(boolean5){
            result.set(ErrorCode.HandleCacheFail,"修改缓存预约记录中的支付账号失败");
            return result;
        }

        boolean boolean6 = (regInfo.unPayedFee == null && modContent.unPayedFee != null)
                || (regInfo.unPayedFee != null && modContent.unPayedFee != regInfo.unPayedFee);
        if(boolean6){
            result.set(ErrorCode.HandleCacheFail,"修改缓存预约记录中的未付费用失败");
            return result;
        }

        boolean boolean7 = (regInfo.payType == null && modContent.payType != null)
                || (regInfo.payType != null && modContent.payType != regInfo.payType);
        if(boolean7){
            result.set(ErrorCode.HandleCacheFail,"修改缓存预约记录中的支付类型失败");
            return result;
        }

        return result ;
    }

    /**
     * 更新缓存和数据库中的预约记录，
     * @param idCard      用户的身份证号
     * @param regInfo     需要更新的预约记录
     * @param modContent  需要更新的几个字段
     * @return            返回操作缓存的结果
     */
    public static Result db_updateRegInfoInDBAndCache(String idCard ,RegInfo regInfo, ModContent modContent){
        Result result = new Result() ;
        /**获得缓存中的预约记录列表*/
        List<RegInfo> reginfoList = DBCache.patients.get(idCard).reginfos;
        RegInfo regInfo_Cache = null ;
        for(int i = 0 ; i < reginfoList.size() ; i++){
            RegInfo temp = reginfoList.get(i);
            if(temp.SN == regInfo.SN){
                regInfo_Cache = temp ;
                break ;
            }
        }
        if(regInfo_Cache == null){
            /**缓存中没有对应的预约记录*/
            result.set(ErrorCode.HandleCacheFail,"缓存中没有对应的预约记录");
            return result;
        }

        /**修改缓存中的预约记录*/
        RegInfo newRegInfo = DBCore.db_updateRegInfo(regInfo_Cache,modContent);
        /**检查缓存更新是否成功*/
        Result result1 = DBCore.db_isRegInfoUpdated(newRegInfo,modContent);
        if(result1.ResultCode != 0){
            /**更新缓存失败，返回错误的结果...不再更新数据库*/
            result = result1 ;
            return result ;
        }

        try{
            QueueEntry queueEntry = new QueueEntry();
            queueEntry.object = newRegInfo ;
            queueEntry.operate = 2 ;
            /**将修改数据库中的预约记录，添加到阻塞队列中*/
            DBCore.db_addEntryToQueue(queueEntry);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        return result;
    }




    /*********************阻塞队列的SET/GET**********************/
    public BlockingQueue getBlockingQueue() {
        return blockingQueue;
    }

    public void setBlockingQueue(BlockingQueue blockingQueue) {
        this.blockingQueue = blockingQueue;
    }


}