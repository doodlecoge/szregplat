package szregplat.model.enumerate;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-8-11
 * Time: 下午4:19
 * To change this template use File | Settings | File Templates.
 */
public enum ErrorCode {

    /**通用返回码*/
    //0 成功
    Success(0),
    /**协议处理层返回码：*/
    //101 请求参数格式非法
    IllegalPara(101),
    //102 缺少必选参数
    AbsentPara(102),
    //103 不认识的请求类型
    UnknownReq(103),
    //104 不认识的请求参数
    UnknownPara(104),
    //105 缺少满足条件的条件参数
    AbsentCondPara(105),
    //106 参数不允许
    ProhibitPara(106),
    /**基本校验层返回码：*/
    //201 参数验证未通过
    ParaVerifyFail(201),
    //202 数据不完整
    DataIncomplete(202),
    //203 数据与保存的不匹配
    MismatchData(203),
    //204 数据项关系不合法
    IllegalDataPair(204),
    //205 缓存列表没有建立
    CacheListNotFound(205),
    //206 与缓存中保存的数据不一致
    MismatchDataInCache(206),
    //207 操作缓存失败
    HandleCacheFail(207),
    //208 与数据库中保存的数据不一致
    MismatchDataInDB(208),
    //209 操作数据库失败
    DBOperFail(209),
    /**业务处理层返回码：*/
    //301 系统资源低，无法完成请求
    SysResExhausted(301),
    //302 系统错误
    SysFail(302),
    //303 未知异常
    UnknownException(303),
    //304 没有查找到记录
    RecordNotFound(304),
    //305 记录已存在
    RecordExist(305),
    //306 用户未授权
    UserNotAuthorised(306),
    //307 数据库错误
    DBError(307),
    /**业务应用层返回码：*/
    //601 没有此用户
    AbsentUser(601),
    //602 没有预约记录
    AbsentRegRecord(602),
    //603 没有找到请求的医院
    HospNotFound(603),
    //604 没有找到请求的科室
    DepartNotFound(604),
    //605 没有找到请求的医生
    DoctorNotFound(605),
    //606 没有找到请求的排班
    SchedualNotFound(606),
    //607 存在多条满足要求的记录，无法继续处理
    MultiRecExist(607),
    //701 号源较上次减少
    LessCanRegNum(701),
    //702 预约已满
    FullReg(702),
    //705 费用信息不正确
    FeeIncorrect(705),
    //706 用户预约信息过期
    RegOutOfDate(706),
    //707 用户已取号
    FectchedRecord(707),
    //708 用户已预约，不能重复预约
    RepeatReg(708),
    //709 此排班已停诊
    SchedualStopped(709),
    //710 此排班已停挂
    SchedualStopReg(710),
    //720 不在工作时间内
    NotInWorkTime(720),
    //721 申请预约的时间段不满足预约规则
    TimeNotSatisfyRule(721),
    //725 因年龄限制，预约失败
    AgeLimit(725),
    //726 因性别限制，预约失败
    SexLimit(726),
    //730 因用户黑名单限制，预约失败
    BlackListLimit(730);

    public int code ;
    private ErrorCode(int code){
        this.code = code ;
    }

}
