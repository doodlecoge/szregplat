package szregplat.model;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-29
 * Time: 上午8:35
 * 科室的详细信息
 */
public class Depart {

    /**科室编号*/
    public String departId ;
    /**科室名称*/
    public String departName ;
    /**科室基本信息*/
    public DepartBasic departBasic ;

    /**科室排班列表*/
    public List<Schedual> departScheduals ;
    /**本科室的医生*/
    public HashMap<String,Doctor> doctors ;


}
