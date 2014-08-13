package szregplat.model.be;

import szregplat.model.enumerate.ErrorCode;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-29
 * Time: 上午10:35
 * 返回的结果
 */
public class Result {

    /**结果的代码*/
    public int ResultCode ;
    /**结果的描述*/
    public String ResultMsg ;

    public Result(){
        ResultCode = 0;
        ResultMsg = "成功";
    }

    public void set(ErrorCode errorCode,String resultMsg){
        this.ResultCode = errorCode.code;
        this.ResultMsg = resultMsg;
    }

}
