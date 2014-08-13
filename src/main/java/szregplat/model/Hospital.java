package szregplat.model;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-29
 * Time: 上午8:33
 * 医院的详细信息
 */
public class Hospital {

    /** 医院编号 */
    public String hospid ;
    /** 医院名称 */
    public String hospname ;
    /**医院基本信息*/
    public HospitalBasic hospBasic ;
    /**科室信息*/
    public HashMap<String,Depart> departs ;


    /*******************SET/GET********************/

    public String getHospid() {
        return hospid;
    }

    public void setHospid(String hospid) {
        this.hospid = hospid;
    }

    public String getHospname() {
        return hospname;
    }

    public void setHospname(String hospname) {
        this.hospname = hospname;
    }

    public HospitalBasic getHospBasic() {
        return hospBasic;
    }

    public void setHospBasic(HospitalBasic hospBasic) {
        this.hospBasic = hospBasic;
    }
}
