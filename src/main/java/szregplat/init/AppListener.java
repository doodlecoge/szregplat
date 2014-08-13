package szregplat.init;

import szregplat.timejob.CacheUpdatingJob;
import szregplat.model.MemoryCache;
import szregplat.dbcore.dao.TheDao;
import szregplat.model.be.HospitalEntity;
import szregplat.dbcore.trigger.TriggerInterface;
import szregplat.dbcore.trigger.TriggerFactory;
import szregplat.dbcore.trigger.TriggerInfo;
import szregplat.util.Timer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.WebUtils;

import java.util.List;

/**
 * Created by zq on 2014/5/18.
 */
public class AppListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext ctx = event.getApplicationContext();

        // set system property "webapp.root" to the absolute path
        // the .war file is exploded. You can get it by calling
        // System.getProperty("webapp.root") in any class.
        if (ctx instanceof WebApplicationContext) {
            WebApplicationContext wctx = (WebApplicationContext) ctx;
            WebUtils.setWebAppRootSystemProperty(wctx.getServletContext());
        }

        TheDao theDao = ctx.getBean(TheDao.class);

        // get triggers
        List<TriggerInfo> triggerInfos = theDao.getTriggerInfos();
        for (TriggerInfo triggerInfo : triggerInfos) {
            TriggerInterface trigger = TriggerFactory.getTrigger(triggerInfo);
            TriggerFactory.addTriggerInstance(trigger);
        }



        // populate hospitals from database
        List<HospitalEntity> hospitals = theDao.getHospitals();
        MemoryCache cache = MemoryCache.getInstance();
        for (HospitalEntity hospital : hospitals) {
            cache.addHospital(hospital);

            String id = hospital.getId();
            Timer timer = Timer.getInstance("load " + id);
            timer.start();
            CacheUpdatingJob.getInstance().update(id);
            timer.stop();
        }

        System.out.println("end");

    }
}
