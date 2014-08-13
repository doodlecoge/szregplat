package szregplat.dbcore.dao;

import szregplat.model.be.HospitalEntity;
import szregplat.model.be.User;
import szregplat.dbcore.trigger.TriggerInfo;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hch on 2014/5/17.
 */
@Repository
public class TheDao {
    @Autowired
    @Qualifier(value = "h2Sf")
    private SessionFactory sessionFactory;

    public List<HospitalEntity> getHospitals() {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(HospitalEntity.class);
        List list = criteria.list();
        session.close();
        return list;
    }

    public void addHospital() {
        HospitalEntity hos = new HospitalEntity();
        hos.setId("id");
        hos.setName("name");
        hos.setAddress("address");
        hos.setPhone("phone");
        hos.setWsdl("http://localhost:8080");

        Session session = sessionFactory.openSession();

        Object id = session.get(HospitalEntity.class, "id");
        if (id != null) return;
        session.beginTransaction();
        session.save(hos);
        session.getTransaction().commit();
    }


    public List<TriggerInfo> getTriggerInfos() {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(TriggerInfo.class);
        List list = criteria.list();
        session.close();
        return list;
    }


    public List<User> getUsers() {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(User.class);
        List list = criteria.list();
        session.close();
        return list;
    }
}
