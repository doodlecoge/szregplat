package szregplat.model;

import szregplat.model.be.RegInfo;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-29
 * Time: 上午8:37
 * 患者的基本信息
 */
public class Patient {

    /** 身份证号 */
    public String idcard  ;
    /** 姓名 */
    public String patientname ;
    /** 密码，便于以后扩展 */
    public String password ;
    /** 出生日期 */
    public Date birthday ;
    /** 性别。男、女 */
    public String sex ;
    /** 社保类型。(自费、市民卡、园区医保) */
    public String insureType ;
    /** 联系方式 */
    public String phone ;
    /**是否黑名单*/
    public Boolean isBlack ;
    /**支付账号*/
    public String payAccount ;
    /**家庭编号*/
    public int familyid ;
    /**预约信息列表*/
    public List<RegInfo> reginfos ;


    /**********SET/GET***********/
    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getPatientname() {
        return patientname;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getInsureType() {
        return insureType;
    }

    public void setInsureType(String insureType) {
        this.insureType = insureType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getBlack() {
        return isBlack;
    }

    public void setBlack(Boolean black) {
        isBlack = black;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public int getFamilyid() {
        return familyid;
    }

    public void setFamilyid(int familyid) {
        this.familyid = familyid;
    }


    public List<RegInfo> getReginfos() {
        return reginfos;
    }

    public void setReginfos(List<RegInfo> reginfos) {
        this.reginfos = reginfos;
    }


}
