package szregplat.model.be;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-29
 * Time: 上午10:10
 * 交易明细。
 */
public class TradeDetail {

    /**交易编号*/
    private int id ;
    /**创建时间*/
    private Date createTime ;
    /**事件类型*/
    private String eventType ;
    /**事件描述*/
    private String eventDescription ;
    /**发起人*/
    private String sponsor ;
    /**交易对象*/
    private String tradeObj ;
    /**报告人*/
    private String reporter ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getTradeObj() {
        return tradeObj;
    }

    public void setTradeObj(String tradeObj) {
        this.tradeObj = tradeObj;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

}
