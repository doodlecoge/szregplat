package szregplat.model.be;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-29
 * Time: 上午9:30
 * 第三方厂商的信息
 */
public class Vendor {

    /**厂商名*/
    public String vendorName ;
    /**厂商用户名*/
    public String userName ;
    /**密码*/
    public String password ;
    /**通讯接口地址*/
    public String translateAddr ;

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTranslateAddr() {
        return translateAddr;
    }

    public void setTranslateAddr(String translateAddr) {
        this.translateAddr = translateAddr;
    }

}
