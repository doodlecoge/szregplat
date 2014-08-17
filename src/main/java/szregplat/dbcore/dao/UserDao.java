package szregplat.dbcore.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szregplat.model.Patient;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-30
 * Time: 下午4:06
 * To change this template use File | Settings | File Templates.
 */
public class UserDao extends SimpleBaseDao<Patient> {
    private static final Logger log = LoggerFactory.getLogger(UserDao.class);

    /**
     * 向患者信息表中插入一条记录
     * @param patient  需要插入的一条患者记录
     */
    public void insert(Patient patient){

        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.save(patient);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("数据库中添加患者信息异常！传入的信息-->患者姓名："+patient.patientname+"。患者身份证号："+patient.idcard);
        } finally {
            if (session != null) session.close();
        }

    }

    /**
     * 更新患者的信息。更新依据，patient.idcard
     * @param patient 需要更新的患者
     */
    public void update(Patient patient){
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.update(patient);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("更新数据库中患者信息异常！传入的信息-->患者姓名："+patient.patientname+"。患者身份证号："+patient.idcard);
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 根据用户的身份证号，获得用户的相关信息
     * @param idCard  用户的身份证号
     * @return        如果用户存在，返回用户的信息，否则返回null
     */
    public Patient getPatientByIdCard(String idCard){

        Patient patient = null;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Patient.class).add(Restrictions.eq("idCard", idCard));
            patient = (Patient) criteria.uniqueResult();
        } catch (Exception e) {
            log.error("查询数据库中患者信息异常！查询身份证号："+idCard);
        } finally {
            if (session != null) session.close();
        }
        return patient ;

    }

    /**
     * 查询数据库中所有的患者列表
     * @return        如果数据库中患者存在，返回患者列表。否则返回null
     */
    public List<Patient> getAllPatients(){
        List<Patient> list = null ;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Patient.class);
            list = criteria.list();
        } catch (Exception e) {
            log.error("查询数据库中患者列表异常！！！");
        } finally {
            if (session != null) session.close();
        }
        return list ;
    }

}
