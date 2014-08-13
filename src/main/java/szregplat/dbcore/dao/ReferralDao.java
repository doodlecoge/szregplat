package szregplat.dbcore.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szregplat.model.Referral;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-30
 * Time: 下午4:09
 * To change this template use File | Settings | File Templates.
 */
public class ReferralDao extends SimpleBaseDao<Referral> {
    private static final Logger log = LoggerFactory.getLogger(ReferralDao.class);

    /**
     * 向转诊表插入一条记录
     * @param referral    需要插入的一条转诊记录
     */
    public void insert(Referral referral){

        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.save(referral);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("数据库中插入转诊异常！插入的信息-->挂号流水号："+referral.getSN()+"。转出医院名称: "+referral.outHospName);
        } finally {
            if (session != null) session.close();
        }

    }

    /**
     * 更新转诊表的一条记录。更新依据referral.sn
     * @param referral     需要更新的转诊信息
     */
    public void update(Referral referral){
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.update(referral);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("更新数据库中转诊异常！传入的信息-->挂号流水号："+referral.getSN()+"。转出医院名称: "+referral.outHospName);
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 删除转诊表的一条记录。删除依据referral.sn
     * @param sn      挂号流水号
     */
    public void delete(Long sn){
        Referral referral = new Referral();
        referral.setSN(sn);

        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(referral);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("删除数据库中转诊异常！传入的信息-->挂号流水号："+referral.getSN()+"。转出医院名称: "+referral.outHospName);
        } finally {
            if (session != null) session.close();
        }

    }

    /**
     * 根据挂号流水号，查询转诊表的记录
     * @param sn  挂号流水号
     * @return         返回一条转诊记录。如果该记录不存在返回null
     */
    public Referral getBySN(String sn){
        Referral referral = null ;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Referral.class).add(Restrictions.eq("sn", sn));
            referral = (Referral)criteria.uniqueResult();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("查询数据库中转诊异常！传入的信息-->挂号流水号："+referral.getSN()+"。转出医院名称: "+referral.outHospName);
        } finally {
            if (session != null) session.close();
        }

        return referral ;
    }

}
