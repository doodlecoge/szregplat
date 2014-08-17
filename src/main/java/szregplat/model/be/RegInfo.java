package szregplat.model.be;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-29
 * Time: 上午9:31
 * 预约信息。保存患者每次预约的相关信息
 */
public class RegInfo {

    /**预约流水号*/
    public Long SN ;
    /**身份证号*/
    public String idCard ;
    /**医院名字*/
    public String hospName ;
    /**科室名字*/
    public String departName ;
    /**专家名字*/
    public String docName ;
    /**医保类型*/
    public String insureType ;
    /**医保费用*/
    public Double insurePay ;
    /**自付费用*/
    public Double selfPay ;
    /**挂号时间*/
    public Date regTime ;
    /**就诊日期*/
    public Date clinicDate ;
    /**排版类型：上午、下午*/
    public String workType ;
    /**就诊开始时间*/
    public Date pipeBeginTime ;
    /**就诊结束时间*/
    public Date pipeEndTime ;
    /**就诊序号*/
    public String clinicSN ;
    /**是否取号 0:未取号。1：已取号。*/
    public Integer isFetched ;
    /**取号时间*/
    public Date fetchTime ;
    /**是否付费 0：未付费。1：已付费。*/
    public Integer isPayed ;
    /**未付费用*/
    public Double unPayedFee ;
    /**支付类型*/
    public String payType ;
    /**支付账号*/
    public String payAccount ;
    /**预约途径*/
    public String regVia ;
    /**预约前端服务提供商标志*/
    public Vendor vendor ;
    /**操作员标识*/
    public String operator ;
    /**是否退号*/
    public Integer isQuit ;
    /**退号时间*/
    public Date quitTime ;
    /**退号途径*/
    public String quitVia ;


    /*******************SET/GET***********************/
    public Long getSN() {
        return SN;
    }

    public void setSN(Long SN) {
        this.SN = SN;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getHospName() {
        return hospName;
    }

    public void setHospName(String hospName) {
        this.hospName = hospName;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getInsureType() {
        return insureType;
    }

    public void setInsureType(String insureType) {
        this.insureType = insureType;
    }

    public Double getInsurePay() {
        return insurePay;
    }

    public void setInsurePay(Double insurePay) {
        this.insurePay = insurePay;
    }

    public Double getSelfPay() {
        return selfPay;
    }

    public void setSelfPay(Double selfPay) {
        this.selfPay = selfPay;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public Date getClinicDate() {
        return clinicDate;
    }

    public void setClinicDate(Date clinicDate) {
        this.clinicDate = clinicDate;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public Date getPipeBeginTime() {
        return pipeBeginTime;
    }

    public void setPipeBeginTime(Date pipeBeginTime) {
        this.pipeBeginTime = pipeBeginTime;
    }

    public Date getPipeEndTime() {
        return pipeEndTime;
    }

    public void setPipeEndTime(Date pipeEndTime) {
        this.pipeEndTime = pipeEndTime;
    }

    public String getClinicSN() {
        return clinicSN;
    }

    public void setClinicSN(String clinicSN) {
        this.clinicSN = clinicSN;
    }

    public Integer getFetched() {
        return isFetched;
    }

    public void setFetched(Integer fetched) {
        isFetched = fetched;
    }

    public Date getFetchTime() {
        return fetchTime;
    }

    public void setFetchTime(Date fetchTime) {
        this.fetchTime = fetchTime;
    }

    public Integer getPayed() {
        return isPayed;
    }

    public void setPayed(Integer payed) {
        isPayed = payed;
    }

    public Double getUnPayedFee() {
        return unPayedFee;
    }

    public void setUnPayedFee(Double unPayedFee) {
        this.unPayedFee = unPayedFee;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getRegVia() {
        return regVia;
    }

    public void setRegVia(String regVia) {
        this.regVia = regVia;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getQuit() {
        return isQuit;
    }

    public void setQuit(Integer quit) {
        isQuit = quit;
    }

    public Date getQuitTime() {
        return quitTime;
    }

    public void setQuitTime(Date quitTime) {
        this.quitTime = quitTime;
    }

    public String getQuitVia() {
        return quitVia;
    }

    public void setQuitVia(String quitVia) {
        this.quitVia = quitVia;
    }
}
