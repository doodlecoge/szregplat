package szregplat.dbcore.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szregplat.model.be.FilterRule;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-30
 * Time: 下午4:09
 * To change this template use File | Settings | File Templates.
 */
public class FilterRuleDao extends SimpleBaseDao<FilterRule> {
    private static final Logger log = LoggerFactory.getLogger(FilterRuleDao.class);
    /**
     * 向过滤规则表插入一条记录.保证filterrule.xpath非空唯一
     * @param filterRule    需要插入的过滤规则
     */
    public void insert(FilterRule filterRule){

        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.save(filterRule);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("数据库中插入过滤规则异常！插入的信息-->XPath："+filterRule.getxPath());
        } finally {
            if (session != null) session.close();
        }

    }

    /**
     * 更新数据库中的一条过滤规则，根据filterrule.xpath进行更新
     * @param filterRule  需要更新的过滤规则记录
     */
    public void update(FilterRule filterRule){
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.update(filterRule);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("更新数据库中过滤规则异常！更新的信息-->XPath："+filterRule.getxPath());
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 删除过滤规则表的一条记录，根据filterrule.xpath进行删除
     * @param xpath    需要删除的过滤规则记录的XPath
     */
    public void delete(String xpath){
        FilterRule filterRule = new FilterRule();
        filterRule.setxPath(xpath);

        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(filterRule);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("删除数据库中过滤规则异常！传入的信息-->XPath："+filterRule.getxPath());
        } finally {
            if (session != null) session.close();
        }

    }

    /**
     * 根据xpath查询过滤规则表的记录，
     * @param xpath  XPath
     * @return         返回一条过滤规则表的记录。如果该记录不存在返回null
     */
    public FilterRule getByXpath(String xpath){
        FilterRule filterRule = null ;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(FilterRule.class).add(Restrictions.eq("xpath", xpath));
            filterRule = (FilterRule)criteria.uniqueResult();
        } catch (Exception e) {
            log.error("查询数据库中过滤规则异常！传入的信息-->XPath："+filterRule.getxPath());
        } finally {
            if (session != null) session.close();
        }
        return filterRule ;
    }


}
