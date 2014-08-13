package szregplat.dbcore.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szregplat.model.be.Vendor;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-30
 * Time: 下午4:08
 * To change this template use File | Settings | File Templates.
 */
public class VendorDao extends SimpleBaseDao<Vendor> {

    private static final Logger log = LoggerFactory.getLogger(VendorDao.class);

    /**
     * 向厂商表插入厂商信息
     * @param vendor 厂商的信息
     */
    public void insert(Vendor vendor){

        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.save(vendor);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("数据库中插入厂商信息异常！插入的信息-->厂商名："+vendor.vendorName+"。厂商用户名："+ vendor.userName+"。厂商密码："+vendor.password+"。通讯地址："+vendor.translateAddr);
        } finally {
            if (session != null) session.close();
        }

    }

    /**
     * 查询厂商信息表，获得厂商的相关信息，根据传入的厂商名进行查询
     * @param userName   厂商用户名
     * @param  password  用户密码
     * @return           如果厂商存在，返回厂商信息，否则返回null
     */
    public Vendor getVendorByUserNameAndPass(String userName,String password){

        Vendor vendor = null;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Vendor.class).add(Restrictions.eq("username", userName))
                    .add(Restrictions.eq("password", password));
            vendor = (Vendor) criteria.uniqueResult();
        } catch (Exception e) {
            log.error("查询数据库中厂商信息异常！查询厂商用户名："+userName+" 。密码："+password);
        } finally {
            if (session != null) session.close();
        }
        return vendor ;

    }

}
