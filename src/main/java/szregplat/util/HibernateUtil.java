package szregplat.util;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hch on 2014/5/2.
 */
public class HibernateUtil {

    /**
     * ThreadLocal Session Map
     */
    public static final ThreadLocal MAP = new ThreadLocal();

    private static final Logger LOG = LoggerFactory.getLogger(HibernateUtil.class);

    private static final SessionFactory SESSION_FACTORY;

    /**
     * Make default construct private
     */
    private HibernateUtil() {
    }

    /** Loads Hibernate config. */
    static {
        try {
            LOG.debug("HibernateUtil.static - loading config");
            SESSION_FACTORY = new Configuration().configure().buildSessionFactory();
            LOG.debug("HibernateUtil.static - end");
        } catch (HibernateException ex) {
            throw new RuntimeException("Exception building SessionFactory: " + ex.getMessage(), ex);
        }
    }

    /**
     * Gets Hibernate Session for current thread.  When finished, users
     * must return session using {@link #closeSession() closeSession()} method.
     *
     * @return Hibernate Session for current thread.
     * @throws HibernateException if there is an error opening a new session.
     */
    public static Session currentSession() throws HibernateException {
        Session s = (Session) MAP.get();
        // Open a new Session, if this Thread has none yet
        if (s == null) {
            s = SESSION_FACTORY.openSession();
            MAP.set(s);
        }
        return s;
    }

    /**
     * Closes the Hibernate Session.  Users must call this method after calling
     * {@link #currentSession() currentSession()}.
     *
     * @throws HibernateException if session has problem closing.
     */
    public static void closeSession() throws HibernateException {
        Session s = (Session) MAP.get();
        MAP.set(null);
        if (s != null) {
            s.close();
        }
    }
}
