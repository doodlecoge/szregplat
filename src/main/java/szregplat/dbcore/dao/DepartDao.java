package szregplat.dbcore.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szregplat.dbcore.dao.tablemodel.DepartTable;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-30
 * Time: 下午4:10
 * To change this template use File | Settings | File Templates.
 */
public class DepartDao extends SimpleBaseDao<DepartTable>{

    private static final Logger log = LoggerFactory.getLogger(DepartDao.class);

    /**
     * 向科室表插入一条数据，，科室的编号非空唯一
     * @param departTable    需要插入的科室数据
     */
    public void insert(DepartTable departTable){

        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.save(departTable);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("数据库中插入科室记录异常！科室编号："+departTable.getDepartid()+" . 医院名称："+departTable.getHospname()+" . 科室名称："+departTable.getDepartname());
        } finally {
            if (session != null) session.close();
        }

    }

    /**
     * 更新数据库中的一条科室记录，根据科室的编号进行更新
     * @param departTable  需要更新的科室记录
     */
    public void update(DepartTable departTable){
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.update(departTable);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("更新数据库中科室信息异常！传入的数据-->>科室编号："+departTable.getDepartid()+" 。医院名称："+departTable.getHospname()+" 。科室名称："+departTable.getDepartname());
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 删除科室表的一条记录，根据科室编号进行删除
     * @param departid    需要删除的科室记录的科室编号
     */
    public void delete(String departid){
        DepartTable departTable = new DepartTable();
        departTable.setDepartid(departid);
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(departTable);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("删除数据库中科室异常！传入的数据-->>科室编号："+departid);
        } finally {
            if (session != null) session.close();
        }

    }

    /**
     * 根据科室的编号查询科室的信息
     * @param departid  科室的编号
     * @return         返回一条科室的信息。如果该记录不存在返回null
     */
    public DepartTable getByDepartId(String departid){

        DepartTable departTable = null;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(DepartTable.class).add(Restrictions.eq("departid", departid));
            departTable = (DepartTable) criteria.uniqueResult();
        } catch (Exception e) {
            log.error("查询数据库中科室信息异常！查询科室编号："+departid);
        } finally {
            if (session != null) session.close();
        }
        return departTable ;
    }


}
