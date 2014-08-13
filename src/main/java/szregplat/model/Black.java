package szregplat.model;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-29
 * Time: 上午9:33
 * 黑名单信息。
 */
public class Black {

    /**身份证号*/
    public String idCard ;
    /**进入原因*/
    public String enterReason ;
    /**创建时间*/
    public Date createTime ;

    /***********SET/GET**********/
    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getEnterReason() {
        return enterReason;
    }

    public void setEnterReason(String enterReason) {
        this.enterReason = enterReason;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
