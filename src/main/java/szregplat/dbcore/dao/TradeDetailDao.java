package szregplat.dbcore.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szregplat.model.be.TradeDetail;

import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-30
 * Time: 下午4:07
 * To change this template use File | Settings | File Templates.
 */
public class TradeDetailDao extends SimpleBaseDao<TradeDetail> {
    private static final Logger log = LoggerFactory.getLogger(TradeDetailDao.class);

    /**
     * 向交易明细表插入一条记录.保证TradeDetail.id非空唯一
     * @param tradeDetail    需要插入的交易明细记录
     */
    public void insert(TradeDetail tradeDetail){

        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.save(tradeDetail);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("数据库中添加交易明细异常！插入的信息-->事件类型: "+tradeDetail.getEventType()+".发起人: "+tradeDetail.getSponsor()+"。交易对象："+tradeDetail.getTradeObj()+"。报告人："+tradeDetail.getReporter()+"。创建时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tradeDetail.getCreateTime()));
        } finally {
            if (session != null) session.close();
        }

    }

    /**
     * 更新数据库中的一条交易明细记录，根据TradeDetail.id进行更新
     * @param tradeDetail  需要更新的交易记录
     */
    public void update(TradeDetail tradeDetail){
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.update(tradeDetail);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("更新数据库中交易明细异常！传入的信息-->事件类型: "+tradeDetail.getEventType()+".发起人: "+tradeDetail.getSponsor()+"。交易对象："+tradeDetail.getTradeObj()+"。报告人："+tradeDetail.getReporter()+"。创建时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tradeDetail.getCreateTime()));
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 删除交易明细表的一条记录，根据TradeDetail.id进行删除
     * @param id    需要删除的交易记录的id
     */
    public void delete(int id){
        TradeDetail tradeDetail = new TradeDetail();
        tradeDetail.setId(id);

        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(tradeDetail);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("删除数据库中交易明细异常！传入的信息-->交易编号: "+id);
        } finally {
            if (session != null) session.close();
        }

    }

    /**
     * 根据交易编号查询交易明细记录，
     * @param id  交易编号
     * @return         返回一条交易记录。如果该记录不存在返回null
     */
    public TradeDetail getById(int id){
        TradeDetail tradeDetail = null ;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(TradeDetail.class).add(Restrictions.eq("id",id));
            tradeDetail = (TradeDetail)criteria.uniqueResult();
        } catch (Exception e) {
            log.error("查询数据库中交易明细异常！传入的信息-->交易编号: "+id);
        } finally {
            if (session != null) session.close();
        }

        return tradeDetail ;
    }

}
