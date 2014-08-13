package szregplat.dbcore.dao;

import szregplat.omc.Ws320RuntimeException;
import szregplat.model.be.Patient;
import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

/**
 * Created by huaiwang on 7/16/14.
 */
public class PatientDao extends BaseDao {
    public List<Patient> getPatients() {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Patient.class);
        try {
            List list = criteria.list();
            return list;
        } catch (Exception e) {
            throw new Ws320RuntimeException(e);
        } finally {
            if (session != null) session.close();
        }
    }


    public Patient getPatientById(String id) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Patient.class)
                .add(Restrictions.eq("patientId", id));
        try {
            return (Patient) criteria.uniqueResult();
        } catch (Exception e) {
            throw new Ws320RuntimeException(e);
        } finally {
            if (session != null) session.close();
        }
    }

    public void modifyPatientName(String id, String name) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Patient.class)
                .add(Restrictions.eq("patientId", id));
        try {
            Object p = criteria.uniqueResult();
            if (p == null) return;
            Patient patient = (Patient) p;
            patient.setName(name);
            session.save(patient);
        } catch (Exception e) {
            throw new Ws320RuntimeException(e);
        } finally {
            if (session != null) session.close();
        }
    }

    public void addPatient(String id, String name) {
        Session session = sessionFactory.openSession();
        Patient patient = new Patient();
        patient.setName(name);
        patient.setPatientId(id);
        try {
            session.save(patient);
        } catch (Exception e) {
            throw new Ws320RuntimeException(e);
        } finally {
            if (session != null) session.close();
        }
    }

    public void blockPatient(String id) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Patient.class);
        try {
            Object p = criteria.uniqueResult();
            if (p == null) return;

            Patient patient = (Patient) p;
            patient.setIsBlocked(Patient.YesNo.YES);
            patient.setBlockedAt(new Date());

            session.save(patient);
        } catch (Exception e) {
            throw new Ws320RuntimeException(e);
        } finally {
            if (session != null) session.close();
        }
    }

    public void unblockPatient(String id) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Patient.class);
        try {
            Object p = criteria.uniqueResult();
            if (p == null) return;

            Patient patient = (Patient) p;
            patient.setIsBlocked(Patient.YesNo.NO);
            patient.setUnblockAt(new Date());

            session.save(patient);
        } catch (Exception e) {
            throw new Ws320RuntimeException(e);
        } finally {
            if (session != null) session.close();
        }
    }
}
