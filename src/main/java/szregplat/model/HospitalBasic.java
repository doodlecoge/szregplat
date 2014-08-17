package szregplat.model;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-29
 * Time: 上午11:45
 * To change this template use File | Settings | File Templates.
 */
public class HospitalBasic {

    /** 医院等级（三级甲等） */
    public String grade ;
    /** 医院性质（国营、民营等） */
    public String kind ;
    /** 地址*/
    public String address ;
    /** 联系方式 */
    public String phone ;
    /** 简介 */
    public String intro ;
    /** 路线 */
    public String way ;
    /** 医院图片url */
    public String hospimg ;
    /** 医院网址 */
    public String hospwebaddr ;
    /** 接口类型（实时、中间库、准实时） */
    public String translateType ;
    /** 通讯接口地址 */
    public String translateAddr ;
    /** 端口号 */
    public int port ;
    /** 排序代码 */
    public int sortcode ;
    /** 地区（沧浪区、高新区等） */
    public String zone ;
    /** 卫生机构类别（综合医院、护理院等） */
    public String classes ;


    /********************SET/GET**********************/
    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getHospimg() {
        return hospimg;
    }

    public void setHospimg(String hospimg) {
        this.hospimg = hospimg;
    }

    public String getHospwebaddr() {
        return hospwebaddr;
    }

    public void setHospwebaddr(String hospwebaddr) {
        this.hospwebaddr = hospwebaddr;
    }

    public String getTranslateType() {
        return translateType;
    }

    public void setTranslateType(String translateType) {
        this.translateType = translateType;
    }

    public String getTranslateAddr() {
        return translateAddr;
    }

    public void setTranslateAddr(String translateAddr) {
        this.translateAddr = translateAddr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getSortcode() {
        return sortcode;
    }

    public void setSortcode(int sortcode) {
        this.sortcode = sortcode;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }
}
