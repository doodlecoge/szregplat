package szregplat.model;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-29
 * Time: 下午12:28
 * To change this template use File | Settings | File Templates.
 */
public class DoctorBasic {

    /**医生性别*/
    public String docSex ;
    /**医生职称*/
    public String docRank ;
    /**医生总评分*/
    public int totleScore ;
    /**评价总次数*/
    public int evaluateCount ;
    /**医生简介*/
    public String docIntro ;
    /**医生特长*/
    public String docMajor ;
    /**是否专家 false:否。true：是。*/
    public boolean isExpert ;
    /**挂号费*/
    public double registryFee ;
    /**门诊费*/
    public double clinicFee ;


}
