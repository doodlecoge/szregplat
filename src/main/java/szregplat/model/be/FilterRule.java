package szregplat.model.be;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-29
 * Time: 上午10:12
 * 过滤规则信息
 */
public class FilterRule {

    /**Xpath*/
    private String xPath ;
    /**触发器类型*/
    private String trigType ;
    /**替换前的值*/
    private String oldValue ;
    /**替换的新值*/
    private String newValue ;
    /**方法名*/
    private String methodName ;

    public String getxPath() {
        return xPath;
    }

    public void setxPath(String xPath) {
        this.xPath = xPath;
    }

    public String getTrigType() {
        return trigType;
    }

    public void setTrigType(String trigType) {
        this.trigType = trigType;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
