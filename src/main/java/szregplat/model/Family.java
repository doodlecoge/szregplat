package szregplat.model;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-29
 * Time: 上午10:02
 * 家庭信息。保存机顶盒户主和成员的身份信息
 */
public class Family {

    /**家庭编号*/
    public int id ;
    /**户主身份证号*/
    public String headIdCard ;
    /**成员身份证号*/
    public String membIdCard ;
    /**创建时间*/
    public Date createTime ;


    /**************SET/GET****************/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeadIdCard() {
        return headIdCard;
    }

    public void setHeadIdCard(String headIdCard) {
        this.headIdCard = headIdCard;
    }

    public String getMembIdCard() {
        return membIdCard;
    }

    public void setMembIdCard(String membIdCard) {
        this.membIdCard = membIdCard;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
