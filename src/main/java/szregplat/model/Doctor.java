package szregplat.model;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-29
 * Time: 上午9:15
 * 医生的详细信息
 */
public class Doctor {

    /**医生编号*/
    public String docId ;
    /**医生姓名*/
    public String docName ;
    /**医生基本信息*/
    public DoctorBasic doctorBasic ;

    /**专家排班列表*/
    public List<Schedual> doctorScheduals ;

}
