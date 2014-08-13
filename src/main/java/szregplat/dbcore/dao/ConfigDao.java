package szregplat.dbcore.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szregplat.model.be.Config;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-30
 * Time: 下午4:09
 * To change this template use File | Settings | File Templates.
 */
public class ConfigDao extends SimpleBaseDao<Config> {
    private static final Logger log = LoggerFactory.getLogger(ConfigDao.class);
    /**
     * 向配置表中插入一条数据 .保证config.key非空唯一
     *
     * @param config 需要插入的配置项
     */
    public void insert(Config config) {
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.save(config);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("数据库中插入配置项异常！配置项为："+config.key);
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 更新配置表中的一条数据。根据config.key更新配置项
     *
     * @param config 需要更新的配置项
     */
    public void update(Config config) {
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.update(config);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("更新数据库中配置项异常！配置项为："+config.key);
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 删除配置表的一条记录，根据config.key进行删除
     *
     * @param key 需要删除的配置记录的关键字
     */
    public void delete(String key) {
        Config config = new Config();
        config.key = key;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(config);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("删除数据库中配置项异常！配置项关键字："+config.key);
        } finally {
            if (session != null) session.close();
        }

    }

    /**
     * 根据配置项关键字，查询配置项，查询依据config.key
     *
     * @param key 配置项关键字
     * @return 返回一条配置记录。如果该记录不存在返回null
     */
    public Config getByKeyDB(String key) {
        Config config = null;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Config.class).add(Restrictions.eq("key", key));
            config = (Config) criteria.uniqueResult();
        } catch (Exception e) {
            log.error("查询数据库中配置项异常！查询关键字："+key);
        } finally {
            if (session != null) session.close();
        }
        return config;
    }

}
