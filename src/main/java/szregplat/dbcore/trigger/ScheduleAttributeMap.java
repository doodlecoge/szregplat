package szregplat.dbcore.trigger;

import szregplat.omc.Ws320RuntimeException;
import szregplat.model.Schedule;

import java.lang.reflect.Field;

/**
 * Created by zq on 2014/5/25.
 */
public enum ScheduleAttributeMap {
    hospital("hospitalName"),
    department("departmentName"),
    doctor("doctorName"),
    date("WorkDate"),
    amp("WorkType"),
    status("WorkStatus"),
    limited("Limited"),
    registry_fee("Registryfee"),
    clinic_fee("Chinicfee"),
    begin_no("BeginNo"),
    space_no("SpaceNo");

    public String realAttr;
    public Field field;

    ScheduleAttributeMap(String attr) {
        realAttr = attr;
        try {
            field = Schedule.class.getField(attr);
        } catch (NoSuchFieldException e) {
            throw new Ws320RuntimeException(e);
        }
    }

}
