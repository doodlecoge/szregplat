package szregplat.dbcore.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szregplat.model.Family;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * User: WeiSW
 * Date: 14-7-30
 * Time: 下午4:07
 *
 */
public class FamilyDao extends SimpleBaseDao<Family> {
    private static final Logger log = LoggerFactory.getLogger(FamilyDao.class);
    /**
     * 向家庭组表插入一条记录
     * @param family    需要插入的一条家庭组记录
     */
    public void insert(Family family){

        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.save(family);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("数据库中插入家庭组异常！插入的信息-->户主身份证号："+family.headIdCard+" 。成员身份证号："+family.membIdCard+" 。创建时间: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(family.createTime));
        } finally {
            if (session != null) session.close();
        }

    }

    /**
     * 更新家庭组信息的一条记录。更新依据family.id
     * @param family     需要更新的家庭组信息
     */
    public void update(Family family){
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.update(family);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("更新数据库中家庭组异常！传入的信息-->户主身份证号："+family.headIdCard+" 。成员身份证号："+family.membIdCard+" 。创建时间: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(family.createTime));
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 删除家庭组表的一条记录。删除依据family.id
     * @param id      家庭组的编号
     */
    public void delete(int id){
        Family family = new Family();
        family.id=id;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(family);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("删除数据库中家庭组异常！删除的主键："+family.id);
        } finally {
            if (session != null) session.close();
        }

    }

    /**
     * 根据家庭组的编号，查询家庭组记录
     * @param id  家庭组的编号
     * @return         返回一条家庭组记录。如果该记录不存在返回null
     */
    public Family getById(String id){
        Family family = null ;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Family.class).add(Restrictions.eq("id", id));
            family = (Family)criteria.uniqueResult();
        } catch (Exception e) {
            log.error("查询数据库中家庭组异常！查询的主键："+family.id);
        } finally {
            if (session != null) session.close();
        }

        return family ;
    }

    /**
     * 根据户主身份证号，家庭成员身份证号，查询家庭组记录
     * @param headIdcard       户主的身份证号
     * @param membIdcard       家庭成员的身份证号
     * @return                 返回一条家庭组记录。如果该记录不存在返回null
     */
    public Family getByHeadCardAndMembCard(String headIdcard,String membIdcard){

        Family family = null ;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Family.class).add(Restrictions.eq("headIdCard", headIdcard)).add(Restrictions.eq("membIdCard",membIdcard));
            family = (Family)criteria.uniqueResult();
        } catch (Exception e) {
            log.debug("查询数据库中家庭组异常！查询的户主身份证号：" + family.headIdCard + " 。成员身份证号：" + family.membIdCard);
        } finally {
            if (session != null) session.close();
        }

        return family ;

    }

    /**
     * 根据户主的身份证号，查询所有家庭组的记录
     * @param headIdcard 户主的身份证号
     * @return           返回该户主的所有家庭组记录。如果不存在，否则返回的结果长度为0
     */
    public List<Family> getByHeadIdCard(String headIdcard){

        ArrayList<Family> list = null ;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            list = (ArrayList<Family>)session.createCriteria(Family.class).add(Restrictions.eq("headIdCard", headIdcard)).list();
        } catch (Exception e) {
            log.debug("查询户主对应的家庭组列表异常！户主身份证号："+headIdcard);
        } finally {
            if (session != null) session.close();
        }

        return list ;
    }

}
