package szregplat.dbcore.cache;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szregplat.dbcore.dao.SimpleBaseDao;

/**
 *
 * User: WeiSW
 * Date: 14-8-4
 * Time: 下午4:10
 * 从阻塞队列中提取QueueEntry，并执行对应的数据库操作
 */
public class ExecuteThread extends SimpleBaseDao {
    private static final Logger log = LoggerFactory.getLogger(ExecuteThread.class);
    private BlockingQueue blockingQueue ;

    /**
     * 任务执行的方法
     */
    public void run() {
        while(true){
            QueueEntry queueEntry = null;
            try {
                queueEntry = (QueueEntry)blockingQueue.take();
                System.out.println("获取队头的信息"+queueEntry.object+"->>"+queueEntry.operate);
                this.executeSQL(queueEntry);
                System.out.println("执行完数据库操作:" +queueEntry.object+"->>"+queueEntry.operate);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从QueueEntry中提取信息。
     * QueueEntry.object 操作的对象
     * QueueEntry.operate 指定的操作。。1、Insert。2、update。3、delete。4、query。
     * @param queueEntry 队列中的对象
     */
    private void executeSQL(QueueEntry queueEntry){
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            switch(queueEntry.operate){
                case 1 :{
                    session.save(queueEntry.object);
                }break;
                case 2 :{
                    session.update(queueEntry.object);
                }break;
                case 3 :{
                    session.delete(queueEntry.object);
                }break;
                case 4 :{
                    //查询操作目前不写进队列

                }break;
                default:{
                    log.error("从阻塞的队列中提取队头的信息。指定的操作类型: "+queueEntry.operate+" 不存在!!!!");
                }
            }
            session.getTransaction().commit();

        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            log.error("从队列中获得队头的信息。操作数据库产生异常！！操作类型："+queueEntry.operate+" 。操作对象："+queueEntry.object);
        } finally {
            if (session != null) session.close();
        }

    }

    /********************BlockingQueue属性提供SET/GET*********************/
    public BlockingQueue getBlockingQueue() {
        return blockingQueue;
    }

    public void setBlockingQueue(BlockingQueue blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

}
