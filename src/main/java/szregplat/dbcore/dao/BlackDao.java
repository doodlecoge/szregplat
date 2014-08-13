package szregplat.dbcore.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szregplat.model.Black;

import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-30
 * Time: 下午4:08
 * To change this template use File | Settings | File Templates.
 */
public class BlackDao extends SimpleBaseDao<Black> {
    private static final Logger log = LoggerFactory.getLogger(BlackDao.class);
    /**
     * 向黑名单表插入一条记录.保证black.idcard非空唯一
     * @param black    需要插入的黑名单记录
     */
    public void insert(Black black){

        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.save(black);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("数据库中插入黑名单异常！身份证号："+black.idCard+" 。创建时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(black.createTime)+" 。进入原因："+black.enterReason);
        } finally {
            if (session != null) session.close();
        }

    }

    /**
     * 更新数据库中的一条黑名单记录，根据black.idcard进行更新
     * @param black  需要更新的黑名单记录
     */
    public void update(Black black){
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.update(black);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("更新数据库中黑名单异常！传入的数据-->>身份证号："+black.idCard+" 。创建时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(black.createTime)+" 。进入原因："+black.enterReason);
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 删除黑名单表的一条记录，根据black.idcard进行删除
     * @param idCard    需要删除的黑名单记录的身份证号
     */
    public void delete(String idCard){
        Black black = new Black();
        black.idCard=idCard;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(black);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("删除数据库中黑名单异常！传入的数据-->>身份证号："+black.idCard);
        } finally {
            if (session != null) session.close();
        }

    }

    /**
     * 根据身份证号查询黑名单记录，
     * @param idCard  身份证号
     * @return         返回一条黑名单记录。如果该记录不存在返回null
     */
    public Black getByIdCard(String idCard){

        Black black = null;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Black.class).add(Restrictions.eq("idCard", idCard));
            black = (Black) criteria.uniqueResult();
        } catch (Exception e) {
            log.error("查询数据库中黑名单异常！查询身份证号："+idCard);
        } finally {
            if (session != null) session.close();
        }
        return black ;
    }

}
