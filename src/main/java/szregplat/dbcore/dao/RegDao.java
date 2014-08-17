package szregplat.dbcore.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szregplat.model.be.RegInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-30
 * Time: 下午4:07
 * To change this template use File | Settings | File Templates.
 */
public class RegDao extends SimpleBaseDao<RegInfo> {
    private static final Logger log = LoggerFactory.getLogger(RegDao.class);

    /**
     * 向预约表插入一条记录.保证RegInfo.SN非空唯一
     * @param reginfo    需要插入的预约记录
     */
    public void insert(RegInfo reginfo){
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.save(reginfo);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("数据库中插入预约记录异常！插入的信息-->挂号流水号："+reginfo.SN+" 。 身份证号："+reginfo.idCard+"。预约时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(reginfo.regTime));
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 更新数据库中的一条预约记录，根据RegInfo.SN进行更新
     * @param reginfo  需要更新的预约记录
     */
    public void update(RegInfo reginfo){
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.update(reginfo);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("更新数据库中预约记录异常！传入的信息-->挂号流水号："+reginfo.SN+" 。 身份证号："+reginfo.idCard+"。预约时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(reginfo.regTime));
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 删除预约表的一条记录，根据RegInfo.SN进行删除
     * @param SN    需要删除的预约记录的SN
     */
    public void delete(Long SN){
        RegInfo regInfo = new RegInfo();
        regInfo.SN=SN;

        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(regInfo);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("删除数据库中预约记录异常！传入的信息-->挂号流水号："+regInfo.SN+" 。 身份证号："+regInfo.idCard+"。预约时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(regInfo.regTime));
        } finally {
            if (session != null) session.close();
        }

    }

    /**
     * 根据SN查询预约记录，
     * @param SN  预约流水号
     * @return         返回一条预约记录。如果该记录不存在返回null
     */
    public RegInfo getBySN(Long SN){
        RegInfo regInfo = null ;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(RegInfo.class).add(Restrictions.eq("SN", SN));
            regInfo = (RegInfo)criteria.uniqueResult();
        } catch (Exception e) {
            log.error("查询数据库中预约记录异常！传入的信息-->挂号流水号："+SN);
        } finally {
            if (session != null) session.close();
        }
        return regInfo ;
    }

    /**
     * 根据患者的身份证号，查询所有的预约记录
     * @param idCard  患者的身份证号
     * @return        返回预约记录列表，如果预约记录不存在，返回null
     */
    public List<RegInfo> getRegInfoListByIdCard(String idCard){
        List<RegInfo> list = null ;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(RegInfo.class).add(Restrictions.eq("idcard", idCard)).addOrder(Order.asc("SN"))  ;
            list = criteria.list();
        } catch (Exception e) {
            log.error("查询数据库中患者预约记录列表异常！！！");
        } finally {
            if (session != null) session.close();
        }
        return list ;
    }

    /**
     * 获得从当前时间开始向前，指定时间段之内的预约记录列表
     * @param idCard   患者的身份证号
     * @param period   指定的时间段。如：3、表示从现在起倒推三个月的预约记录
     * @return         返回预约记录列表，如果预约记录为空，则列表的长度为0
     */
    public List<RegInfo> getRegInfoListByIdCardAndPeriod(String idCard , int period){
        /**当前时间*/
        Date to = new Date();
        /**时间段之前的时间*/
        Calendar calendar = Calendar.getInstance() ;
        calendar.add(Calendar.MONTH,-period);
        Date from = calendar.getTime();
        /**查询数据库获得预约信息列表*/
        List<RegInfo> list = new ArrayList<RegInfo>();
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(RegInfo.class)
                    .add(Restrictions.eq("idcard", idCard))
                    .add(Restrictions.between("date", from, to))
                    .addOrder(Order.asc("SN"));
            list = criteria.list();
        } catch (Exception e) {
            log.error("查询数据库中患者指定时间段的预约记录列表异常！！！指定的时间段："+period);
        } finally {
            if (session != null) session.close();
        }
        return list ;

    }

}
