package szregplat.model;

import szregplat.model.be.HospitalEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hch on 2014/5/18.
 */
public class MemoryCache {
    // singleton
    private static MemoryCache ins = new MemoryCache();

    private MemoryCache() {
    }

    ;

    public static MemoryCache getInstance() {
        return ins;
    }

    private Map<String, HospitalEntity> hospitals = new HashMap<String, HospitalEntity>();

    private Map<String, ScheduleCache> schedules =
            new HashMap<String, ScheduleCache>();

    public void addHospital(HospitalEntity hos) {
        hospitals.put(hos.getId(), hos);
    }

    public HospitalEntity getHospital(String hosId) {
        return hospitals.get(hosId);
    }

    public HospitalEntity getHospitalByName(String hosName) {
        for (HospitalEntity hos : hospitals.values()) {
            if (hos.getName().equals(hosName)) return hos;
        }
        return null;
    }

    public Map<String, HospitalEntity> getHospitals() {
        return hospitals;
    }

    public void setSchedules(String hospitalId, ScheduleCache schedules) {
        this.schedules.put(hospitalId, schedules);
    }

    public ScheduleCache getSchedules(String hospitalId) {
        return this.schedules.get(hospitalId);
    }
}
