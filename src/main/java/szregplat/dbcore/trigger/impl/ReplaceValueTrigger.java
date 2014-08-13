package szregplat.dbcore.trigger.impl;

import szregplat.omc.Ws320RuntimeException;
import szregplat.model.Schedule;
import szregplat.model.ScheduleCache;
import szregplat.dbcore.trigger.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by zq on 2014/5/22.
 */
public class ReplaceValueTrigger implements TriggerInterface {
    private static final Logger log = LoggerFactory.getLogger(ReplaceValueTrigger.class);
    private TriggerInfo triggerInfo;
    private Selector selector;

    @Override
    public boolean hasInterest(TriggerInfo triggerInfo) {
        if (TriggerAction.replace_val == triggerInfo.getAction()) {
            this.triggerInfo = triggerInfo;
            return true;
        } else return false;
    }

    @Override
    public void handle(ScheduleCache sc) {
        Map<String, Schedule> schedules = sc.getSchedules();
        Map<String, Schedule> replaceds = sc.getReplacedSchedules();

        for (String sid : schedules.keySet()) {
            Schedule schedule = schedules.get(sid);

            if (selector != null && !selector.match(schedule)) continue;

            String attr = triggerInfo.getAttribute();
            Field field = ScheduleAttributeMap.valueOf(attr).field;
            String old = null;
            try {
                old = field.get(schedule).toString();
            } catch (IllegalAccessException e) {
                log.error(field.getName(), e);
                continue;
            }
            if (!old.equals(triggerInfo.getOldValue())) {
                continue;
            }


            if (schedule.replaced == null) {
                Schedule newSchedule = (Schedule) schedule.clone();
                newSchedule.original = schedule;
                schedule.replaced = newSchedule;
            }

            String oid = schedule.replaced.id();

            try {
                field.set(schedule.replaced, triggerInfo.getNewValue());
            } catch (Exception e) {
                throw new Ws320RuntimeException(e);
            }

            replaceds.remove(oid);
            replaceds.put(schedule.replaced.id(), schedule.replaced);
        }


    }

    @Override
    public TriggerStage getTriggerStage() {
        return triggerInfo.getStage();
    }

    @Override
    public void setSelector(Selector selector) {
        this.selector = selector;
    }
}
