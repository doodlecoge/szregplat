package szregplat.dbcore.cache;

/**
 *
 * User: WeiSW
 * Date: 14-8-8
 * Time: 上午9:11
 * 队列中存放的数据的Entry条目，，
 */
public class QueueEntry {

    /**
     * 被操作的对象。
     */
    public Object object ;
    /**
     * 操作类型，1、增加。2、更新。3、删除。4、查询(应该用不到)
     */
    public int operate ;




    /*******************SET/GET********************/

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }
}

