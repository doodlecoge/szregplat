package szregplat.dbcore.dao;

import szregplat.omc.Ws320RuntimeException;
import szregplat.model.be.BlockHistory;
import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by huaiwang on 7/15/14.
 */

@Repository
public class BlockHistoryDao extends BaseDao {
    public List<BlockHistory> getBlockHistories(String patientId) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(BlockHistory.class);
        criteria.add(Restrictions.eq("patientId", patientId));
        List list = criteria.list();
        session.close();
        return list;
    }

    public void addBlock(String patientId, String vendorId,
                         String operator, String reason) {
        BlockHistory his = new BlockHistory();
        // fixme: check if patientId and vendorId exists
        his.setPatientId(patientId);
        his.setOperation(BlockHistory.OP.block);
        his.setVendorId(vendorId);
        his.setOperator(operator);
        his.setReason(reason);
        his.setWhen(new Date());


        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.save(his);
        } catch (Exception e) {
            throw new Ws320RuntimeException(e);
        } finally {
            if (session != null) session.close();
        }
    }

    public void addUnblock(String patientId, String vendorId,
                           String operator, String reason) {
        BlockHistory his = new BlockHistory();
        // fixme: check if patientId and vendorId exists
        his.setPatientId(patientId);
        his.setOperation(BlockHistory.OP.unblock);
        his.setVendorId(vendorId);
        his.setOperator(operator);
        his.setReason(reason);
        his.setWhen(new Date());

        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.save(his);
        } catch (Exception e) {
            throw new Ws320RuntimeException(e);
        } finally {
            if (session != null) session.close();
        }
    }

}
