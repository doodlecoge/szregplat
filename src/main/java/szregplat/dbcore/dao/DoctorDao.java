package szregplat.dbcore.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szregplat.dbcore.dao.tablemodel.DoctorTable;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-30
 * Time: 下午4:10
 * To change this template use File | Settings | File Templates.
 */
public class DoctorDao extends SimpleBaseDao<DoctorTable>{

    private static final Logger log = LoggerFactory.getLogger(DoctorDao.class);

    /**
     * 向医生表插入一条数据，，医生的编号非空唯一
     * @param doctorTable    需要插入的医生的数据
     */
    public void insert(DoctorTable doctorTable){

        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.save(doctorTable);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("数据库中插入医生记录异常！医生编号："+doctorTable.getDoctorid()+" . 医生名称："+doctorTable.getDoctorname());
        } finally {
            if (session != null) session.close();
        }

    }

    /**
     * 更新数据库中的一条医生记录，根据医生的编号进行更新
     * @param doctorTable  需要更新的医生记录
     */
    public void update(DoctorTable doctorTable){
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.update(doctorTable);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("更新数据库中医生信息异常！传入的数据-->>医生编号："+doctorTable.getDoctorid()+" 。医生名称："+doctorTable.getDoctorname());
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 删除医生表的一条记录，根据医生编号进行删除
     * @param doctorid    需要删除的医生记录的医生编号
     */
    public void delete(String doctorid){
        DoctorTable doctorTable = new DoctorTable();
        doctorTable.doctorid = doctorid ;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(doctorTable);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("删除数据库中医生信息异常！传入的数据-->>医生编号："+doctorid);
        } finally {
            if (session != null) session.close();
        }

    }

    /**
     * 根据医生的编号查询医生的信息
     * @param doctorid 医生的编号
     * @return         返回一条医生的信息。如果该记录不存在返回null
     */
    public DoctorTable getByDoctorId(String doctorid){

        DoctorTable doctorTable = null;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(DoctorTable.class).add(Restrictions.eq("doctorid", doctorid));
            doctorTable = (DoctorTable) criteria.uniqueResult();
        } catch (Exception e) {
            log.error("查询数据库中医生信息异常！查询医生编号："+doctorid);
        } finally {
            if (session != null) session.close();
        }
        return doctorTable ;
    }


}
