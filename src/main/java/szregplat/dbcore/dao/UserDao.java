package szregplat.dbcore.dao;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szregplat.model.Patient;

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

}
