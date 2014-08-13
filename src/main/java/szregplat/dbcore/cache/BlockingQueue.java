package szregplat.dbcore.cache;
import java.util.concurrent.ArrayBlockingQueue;
/**
 *
 * User: WeiSW
 * Date: 14-8-6
 * Time: 下午12:42
 * 阻塞的队列，保证程序中只有一个队列，使用单例模式
 */
public class BlockingQueue extends ArrayBlockingQueue<QueueEntry>{

    private static BlockingQueue blockingQueue ;

    private static int capacity ;

    private BlockingQueue(int capacity) {
        super(capacity);
        BlockingQueue.capacity=capacity;
    }

    /**
     * 向外界程序提供的唯一的方法，访问阻塞队列。
     * @return      返回唯一的阻塞队列对象
     */
    public static BlockingQueue getInstance(){
        if(blockingQueue==null)
            return new BlockingQueue(capacity);
        else
            return blockingQueue ;
    }


}
