package szregplat.model.be;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-29
 * Time: 上午8:46
 * 与预约平台相关的配置信息
 */
public class Config {

    /**配置项关键字*/
    public String key ;
    /**配置项的值*/
    public String value ;
    /**备注*/
    public String annotation ;

    /***********SET/GET*************/
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
}
