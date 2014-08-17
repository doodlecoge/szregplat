package szregplat.dbcore.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szregplat.dbcore.dao.tablemodel.HospTable;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-30
 * Time: 下午3:56
 * To change this template use File | Settings | File Templates.
 */
public class HospitalDao extends SimpleBaseDao<HospTable>{
    private static final Logger log = LoggerFactory.getLogger(HospitalDao.class);

    /**
     * 向医院表插入一条数据，，医院的编号hospid唯一非空
     * @param hospTable    需要插入的医院数据
     */
    public void insert(HospTable hospTable){

        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.save(hospTable);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("数据库中插入医院记录异常！医院编号："+hospTable.getHospid()+" 。 医院名称："+hospTable.getHospname());
        } finally {
            if (session != null) session.close();
        }

    }

    /**
     * 更新数据库中的一条医院记录，根据hospTable.hospid进行更新
     * @param hospTable  需要更新的医院记录
     */
    public void update(HospTable hospTable){
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.update(hospTable);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("更新数据库中医院信息异常！传入的数据-->>医院编号："+hospTable.getHospid()+" 。医院名称："+hospTable.getHospname());
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 删除医院表的一条记录，根据HospTable.hospid进行删除
     * @param hospid    需要删除的医院记录的医院编号
     */
    public void delete(String hospid){
        HospTable hospTable = new HospTable();
        hospTable.setHospid(hospid);
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(hospTable);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("删除数据库中医院异常！传入的数据-->>医院编号："+hospid);
        } finally {
            if (session != null) session.close();
        }

    }

    /**
     * 根据医院的编号查询医院的信息
     * @param hospid  医院的编号
     * @return         返回一条医院的信息。如果该记录不存在返回null
     */
    public HospTable getByHospId(String hospid){

        HospTable hospTable = null;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(HospTable.class).add(Restrictions.eq("hospid", hospid));
            hospTable = (HospTable) criteria.uniqueResult();
        } catch (Exception e) {
            log.error("查询数据库中医院信息异常！查询医院编号："+hospid);
        } finally {
            if (session != null) session.close();
        }
        return hospTable ;
    }


}
