package szregplat.model;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by huaiwang on 5/26/14.
 */
public class ScheduleCache {
    private static final Logger log = LoggerFactory.getLogger(ScheduleCache.class);
    private final String hospitalId;
    private final Map<String, DepartInfo> departs;
    private final Map<String, DoctorInfo> doctors;
    private final Map<String, Schedule> schedules;
    private final Map<String, Schedule> repScheds;

    public ScheduleCache(String hospitalId) {
        this.hospitalId = hospitalId;
        departs = new ConcurrentHashMap<String, DepartInfo>();
        doctors = new ConcurrentHashMap<String, DoctorInfo>();
        schedules = new ConcurrentHashMap<String, Schedule>();
        repScheds = new ConcurrentHashMap<String, Schedule>();
    }


    /*------------------------------------------------------------------------*/

    public void addDeparts(List<DepartInfo> departInfos) {
        for (DepartInfo departInfo : departInfos) {
            departs.put(departInfo.DepartId, departInfo);
        }
    }

    public Map<String, DepartInfo> getDeparts() {
        return departs;
    }

    public void addDoctors(List<DoctorInfo> doctorInfos) {
        for (DoctorInfo doctorInfo : doctorInfos) {
            doctors.put(doctorInfo.DoctorId, doctorInfo);
        }
    }

    public Map<String, DoctorInfo> getDoctors() {
        return doctors;
    }

    /*------------------------------------------------------------------------*/

    public Map<String, Schedule> getSchedules() {
        return schedules;
    }

    public Map<String, Schedule> getReplacedSchedules() {
        return repScheds;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    /*------------------------------------------------------------------------*/

    public Document getData() {
        final String SEP = "::";

        // depart name => depart object
        Map<String, DepartInfo> departMap = new HashMap<String, DepartInfo>();
        for (DepartInfo dptInfo : departs.values()) {
            departMap.put(dptInfo.DepartName, dptInfo);
        }

        // doctor name => doctor object
        Map<String, DoctorInfo> doctorMap = new HashMap<String, DoctorInfo>();
        for (DoctorInfo doctorInfo : doctors.values()) {
            doctorMap.put(doctorInfo.DoctorName, doctorInfo);
        }

        // depart name => schedule list
        // depart name SEP doctor name => schedule list
        Map<String, List<Schedule>> scheduleMap =
                new HashMap<String, List<Schedule>>();

        for (Schedule schedule : schedules.values()) {
            if (schedule.replaced != null) {
                schedule = schedule.replaced;
            }

            String key = schedule.departmentName;
            if (schedule.doctorName != null) key += SEP + schedule.doctorName;

            log.debug("~~~>" + key);

            if (!scheduleMap.containsKey(key)) {
                scheduleMap.put(key, new ArrayList<Schedule>());
            }

            scheduleMap.get(key).add(schedule);
        }


        Map<String, Set<String>> dptDocSet = new HashMap<String, Set<String>>();
        for (String key : scheduleMap.keySet()) {
            // fixme: should check that dpt and doc name do not contains SEP.
            String[] split = key.split(SEP);
            if (split.length < 2) continue;
            String dptName = split[0];
            String docName = split[1];
            if (!dptDocSet.containsKey(dptName)) {
                dptDocSet.put(dptName, new HashSet<String>());
            }
            dptDocSet.get(dptName).add(docName);
        }


        final String NS = "http://service.hch.me";
        Document document = DocumentHelper.createDocument();
        Element elDpts = document.addElement("Departments", NS);
        Set<String> dptNames = new HashSet<String>();

        for (String key : scheduleMap.keySet()) {
            // fixme: should check that dpt and doc name do not contains SEP.
            String dptName = key.split(SEP)[0];

            if (dptNames.contains(dptName)) continue;
            else dptNames.add(dptName);

            // add a department
            DepartInfo dptInfo = departMap.get(dptName);
            Element elDpt = elDpts.addElement("Department", NS);
            elDpt.addAttribute("name", dptInfo.DepartName);


            // add doctors
            Set<String> docNames = dptDocSet.get(dptName);
            if (docNames != null && docNames.size() > 0) {
                Element elDocs = elDpt.addElement("Doctors", NS);

                for (String docName : docNames) {
                    DoctorInfo docInfo = doctorMap.get(docName);
                    Element elDoc = elDocs.addElement("Doctor", NS);
                    elDoc.addAttribute("name", docInfo.DoctorName);


                    // add doctor schedules
                    String skey = dptInfo.DepartName + SEP + docInfo.DoctorName;
                    List<Schedule> docScheList = scheduleMap.get(skey);

                    log.debug("===>" + skey);

                    if (docScheList != null && docScheList.size() > 0) {
                        Element elSches = elDoc.addElement("Schedules", NS);

                        for (Schedule schedule : docScheList) {
                            Element elSche = elSches.addElement("Schedule", NS);
                            Element elWorkDate = elSche.addElement("WorkDate", NS);
                            elWorkDate.addText(schedule.WorkDate);
                            Element elWorkType = elSche.addElement("WorkType", NS);
                            elWorkType.addText(schedule.WorkType);
                            // todo: add other attributes for schedule element
                        }
                    }
                }
            }


            // add department schedules
            List<Schedule> dptSches = scheduleMap.get(key);

            if (dptSches != null && dptSches.size() > 0) {
                Element elSches = elDpt.addElement("Schedules", NS);

                for (Schedule schedule : dptSches) {
                    Element elSche = elSches.addElement("Schedule", NS);
                    Element elWorkDate = elSche.addElement("WorkDate", NS);
                    elWorkDate.addText(schedule.WorkDate);
                    Element elWorkType = elSche.addElement("WorkType", NS);
                    elWorkType.addText(schedule.WorkType);
                    // todo: add other attributes for schedule element
                }
            }
        }


        return document;
    }

}
