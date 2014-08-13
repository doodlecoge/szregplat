package szregplat.dbcore.cache;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-30
 * Time: 下午4:22
 * To change this template use File | Settings | File Templates.
 */
public class ModContent {

    /**医保类型。自费、医保卡*/
    public String insureType;
    /**是否取号。0：未取号。1：已取号*/
    public Integer isFetched;
    /**取号时间*/
    public Date fetchtime;
    /**是否付费。0：未付费。1：已付费*/
    public Integer isPayed;
    /**未付费用*/
    public Double unPayedFee;
    /**支付类型。支付宝、预充值账户、微信、银联*/
    public String payType;
    /**支付账号*/
    public String payAccount;

}
