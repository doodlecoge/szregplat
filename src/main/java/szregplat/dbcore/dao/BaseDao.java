package szregplat.dbcore.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by huaiwang on 7/15/14.
 */
@Component
public class BaseDao {
    @Autowired
    @Qualifier(value = "h2Sf")
    protected SessionFactory sessionFactory;
}
