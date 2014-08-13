package szregplat.timejob;

import szregplat.omc.Ws320RuntimeException;
import szregplat.model.*;
import szregplat.webservice.client.HisClientFactory;
import szregplat.webservice.client.HisClientIface;
import szregplat.dbcore.trigger.TriggerFactory;
import szregplat.dbcore.trigger.TriggerInterface;
import szregplat.dbcore.trigger.TriggerStage;
import szregplat.util.Config;
import szregplat.util.FileUtils;
import szregplat.util.TimeUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Field;
//import java.nio.file.Files;
//import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huaiwang on 14-4-30.
 */
public class CacheUpdatingJob extends Thread {
    private static final Logger log = LoggerFactory.getLogger(CacheUpdatingJob.class);
    public static Config sysConfig = Config.getInstance("system.properties");
    private final MemoryCache cache = MemoryCache.getInstance();

    // singleton
    private static final CacheUpdatingJob ins = new CacheUpdatingJob();

    private CacheUpdatingJob() {
    }

    public static CacheUpdatingJob getInstance() {
        return ins;
    }


    private static final String dpt_info = "-dpt-info-";
    private static final String doc_info = "-doc-info-";
    private static final String dpt_work = "-dpt-work-";
    private static final String doc_work = "-doc-work-";


    private static final String dptInfoTagName = "DepartInfo";
    private static final String docInfoTagName = "DoctorInfo";
    private static final String dptWorkTagName = "DepartCalendar";
    private static final String docWorkTagName = "DoctorCalendar";


    private Map<String, Field> DepartInfoFields;
    private Map<String, Field> DoctorInfoFields;
    private Map<String, Field> DepartWorkFields;
    private Map<String, Field> DoctorWorkFields;

    private Map<String, Field> ScheduleFieldMap;
    private Map<String, Field> DptWorkFieldMap;

    private String cacheFolder = null;
    private String backupFolder = null;


    {
        /* cache & backup folder */
        String cache_folder = sysConfig.getString("cache_folder");
        this.cacheFolder = prepareFolder(cache_folder).getAbsolutePath();

        String backup_folder = sysConfig.getString("backup_folder");
        this.backupFolder = prepareFolder(backup_folder).getAbsolutePath();


        /* fields mapping */
        DepartInfoFields = new HashMap<String, Field>();
        Field[] fields = DepartInfo.class.getFields();
        for (Field field : fields) {
            DepartInfoFields.put(field.getName(), field);
        }


        DoctorInfoFields = new HashMap<String, Field>();
        fields = DoctorInfo.class.getFields();
        for (Field field : fields) {
            DoctorInfoFields.put(field.getName(), field);
        }


        DepartWorkFields = new HashMap<String, Field>();
        fields = DepartWork.class.getFields();
        for (Field field : fields) {
            DepartWorkFields.put(field.getName(), field);
        }

        DoctorWorkFields = new HashMap<String, Field>();
        fields = DoctorWork.class.getFields();
        for (Field field : fields) {
            DoctorWorkFields.put(field.getName(), field);
        }


        ScheduleFieldMap = new HashMap<String, Field>();
        fields = Schedule.class.getFields();
        for (Field field : fields) {
            ScheduleFieldMap.put(field.getName(), field);
        }

        DptWorkFieldMap = new HashMap<String, Field>();
        fields = DepartWork.class.getFields();
        for (Field field : fields) {
            DptWorkFieldMap.put(field.getName(), field);
        }
    }

    private File prepareFolder(String baseFolder) {
        File file = new File(baseFolder);

        if (file.isAbsolute()) {
            if (!FileUtils.ancestorExists(file)) {
                throw new Ws320RuntimeException("path not exists:" + baseFolder);
            }

            if (!file.exists()) {
                file.mkdirs();
            }
        }
        // if the cache_folder is not an absolute path,
        // we assume it is a direct child folder under web root.
        else {
            Pattern ptn = Pattern.compile("[0-9a-zA-Z_\\-]+");
            Matcher matcher = ptn.matcher(baseFolder);
            if (!matcher.matches()) {
                throw new Ws320RuntimeException("name of cache_folder incorrect");
            }
            String webroot = System.getProperty("webapp.root");
            if (webroot == null) {
                throw new Ws320RuntimeException("system property webapp.root not specified");
            }
            file = new File(webroot + "/" + baseFolder);
            file.mkdir();
        }

        return file;
    }

    /*-------------------------------------------------*/
    /* worker                                        */
    /*-------------------------------------------------*/

    public void update(String hospitalId) {
        // 1. download data to disk
        downloadHisData(hospitalId);

        // 2. unmarshal from xml
        ScheduleCache sc = loadCacheFromFile(hospitalId);

        // 3. error check,
        // this step is merged in to setp 2.

        // 4. invoke `cmp` stage triggers if any event
        cmpTrigger(sc);

        // 5. replace cache
        repTrigger(sc);

        cache.setSchedules(hospitalId, sc);
    }


    /*-------------------------------------------------*/
    /* download                                        */
    /*-------------------------------------------------*/


    private void downloadHisData(String hospitalId) {
        // use date string as sub folder name
        String folderName = TimeUtils.getTimeStamp("yyyy-MM-dd");

        // make sure the sub folder exists
        prepareCacheSubFolder(folderName);

        // backup any existent file
        backupCache(folderName, hospitalId);

        // download files
        download(hospitalId, folderName);
    }

    private void prepareCacheSubFolder(String ts) {
        File subFolder = new File(cacheFolder + File.separator + ts);

        if (!subFolder.exists()) {
            subFolder.mkdir();
        }
    }

    private void backupCache(String subFolderName, String hospitalId) {
        String folderStr = cacheFolder + File.separator + subFolderName;
        File folder = new File(folderStr);


        File[] files = folder.listFiles(new FileNameStartsWithFilter(hospitalId));

        String subFolderStr = backupFolder + File.separator + subFolderName;
        File subFolder = new File(subFolderStr);
        if (!subFolder.exists()) {
            subFolder.mkdirs();
        }

        for (File file : files) {
            String newPath = subFolderStr + File.separator + file.getName();
            try {
                FileUtils.move(file, new File(newPath));
            } catch (IOException e) {
                throw new Ws320RuntimeException(e);
            }
        }
    }

    private void download(String hospitalId, String saveTo) {
        HisClientIface hisClient = HisClientFactory.getHisClient(hospitalId);

        FileUtils fu = FileUtils.getInstance(cacheFolder + File.separator + saveTo);
        String rst = null;
        String sss = null;


        sss = TimeUtils.getTimeStamp("yyyy-MM-dd-hh-mm-ss");
        rst = hisClient.getDepartInfo(hospitalId);
        fu.saveAs(hospitalId + dpt_info + sss + ".xml", rst);


        sss = TimeUtils.getTimeStamp("yyyy-MM-dd-hh-mm-ss");
        rst = hisClient.getDoctorInfo(hospitalId);
        fu.saveAs(hospitalId + doc_info + sss + ".xml", rst);

        sss = TimeUtils.getTimeStamp("yyyy-MM-dd-hh-mm-ss");
        rst = hisClient.getDepartWorkSchedule(hospitalId);
        fu.saveAs(hospitalId + dpt_work + sss + ".xml", rst);

        sss = TimeUtils.getTimeStamp("yyyy-MM-dd-hh-mm-ss");
        rst = hisClient.getDoctorWorkSchedule(hospitalId);
        fu.saveAs(hospitalId + doc_work + sss + ".xml", rst);
    }


    /*-------------------------------------------------*/
    /* unmarshal                                       */
    /*-------------------------------------------------*/

    private ScheduleCache loadCacheFromFile(String hospitalId) {
        String ts = TimeUtils.getTimeStamp("yyyy-MM-dd");

        List<DepartInfo> departInfos = loadHisData(dpt_info, dptInfoTagName, ts, hospitalId, DepartInfo.class, DepartInfoFields);
        List<DoctorInfo> doctorInfos = loadHisData(doc_info, docInfoTagName, ts, hospitalId, DoctorInfo.class, DoctorInfoFields);
        List<DepartWork> departWorks = loadHisData(dpt_work, dptWorkTagName, ts, hospitalId, DepartWork.class, DepartWorkFields);
        List<DoctorWork> doctorWorks = loadHisData(doc_work, docWorkTagName, ts, hospitalId, DoctorWork.class, DoctorWorkFields);

        return populateCache(hospitalId, departInfos, doctorInfos, departWorks, doctorWorks);
    }

    private <T> List<T> loadHisData(String which, String tagName, String ts, String hospitalId, Class<T> klass, Map<String, Field> fields) {
        String prefix = hospitalId + which;
        File folder = new File(cacheFolder + File.separator + ts);

        File[] files = folder.listFiles(new FileNameStartsWithFilter(prefix));

        if (files == null) {
            throw new Ws320RuntimeException("file with prefix [" + prefix + "] not found");
        }

        if (files.length != 1) {
            throw new Ws320RuntimeException("expect 1 file start with [" + prefix + "], actual " + files.length);
        }

        try {
            return unmarshalHisXml(tagName, files[0], klass, fields);
        } catch (Exception e) {
            throw new Ws320RuntimeException(e);
        }
    }

    private <T> List<T> unmarshalHisXml(String tagName, File file, Class<T> klass, Map<String, Field> fields) throws DocumentException, NoSuchFieldException, IllegalAccessException, InstantiationException {
        SAXReader reader = new SAXReader();
        reader.setEncoding("UTF-8");
        Document document = reader.read(file);
        Element root = document.getRootElement();
        List<Element> dptInfos = root.elements();


        List<T> items = new ArrayList<T>();
        for (int i = 0; i < dptInfos.size(); i++) {
            Element item = dptInfos.get(i);

            if (!item.getName().equalsIgnoreCase(tagName)) {
                log.warn(file.getAbsolutePath() + ": tag #" + i + ", name [" +
                        item.getName() + "] not " + dptInfoTagName);
                continue;
            }

            List<Element> props = item.elements();

            if (props.size() != fields.size()) {
                throw new Ws320RuntimeException(file.getAbsolutePath() +
                        ": tag #" + i +
                        ", number of children != size of DepartInfo fields");
            }

            T di = klass.newInstance();

            for (Element prop : props) {
                String name = prop.getName();
                Field field = fields.get(name);
                if (name.equals("WorkDate")) {
                    field.set(di, extractDateNotTime(prop.getText()));
                } else {
                    field.set(di, prop.getText());
                }
            }

            items.add(di);
        }
        return items;
    }

    private String extractDateNotTime(String str) {
        Pattern ptn = Pattern.compile("([0-9]{4}-[0-9]{2}-[0-9]{2})");
        Matcher matcher = ptn.matcher(str);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return str;
        }
    }


    private ScheduleCache populateCache(String hospitalId,
                                        List<DepartInfo> departInfos,
                                        List<DoctorInfo> doctorInfos,
                                        List<DepartWork> departWorks,
                                        List<DoctorWork> doctorWorks) {

        ScheduleCache sc = new ScheduleCache(hospitalId);
        sc.addDeparts(departInfos);
        sc.addDoctors(doctorInfos);
        Map<String, DepartInfo> departMap = sc.getDeparts();
        Map<String, DoctorInfo> doctorMap = sc.getDoctors();


        Map<String, Schedule> scheduleMap = sc.getSchedules();


        for (DepartWork departWork : departWorks) {
            Schedule schedule = new Schedule();

            try {
                copyFields(schedule, departWork);
            } catch (Exception e) {
                log.error("this should never happened", e);
                continue;
            }

            schedule.hospitalName = MemoryCache.getInstance().getHospital(schedule.Hospitalcode).getName();
            DepartInfo dptInfo = departMap.get(schedule.DepartId);
            if (dptInfo == null) {
                log.error("department with id [" + schedule.DepartId + "] not found, " + schedule.Hospitalcode);
                continue;
            } else {
                schedule.departmentName = dptInfo.DepartName;
            }

            scheduleMap.put(schedule.id(), schedule);
        }


        for (DoctorWork doctorWork : doctorWorks) {
            Schedule schedule = new Schedule();

            try {
                copyFields(schedule, doctorWork);
            } catch (Exception e) {
                log.error("this should never happened", e);
                continue;
            }

            schedule.hospitalName = MemoryCache.getInstance().getHospital(schedule.Hospitalcode).getName();

            DepartInfo dptinfo = departMap.get(schedule.DepartId);
            if (dptinfo == null) {
                log.error("doctor department with id [" + schedule.DepartId + "] not found, " + schedule.Hospitalcode);
                continue;
            } else {
                schedule.departmentName = dptinfo.DepartName;
            }

            DoctorInfo docInfo = doctorMap.get(schedule.DoctorId);
            if (docInfo == null) {
                log.error("doctor with id [" + schedule.DoctorId + "] not found, " + schedule.Hospitalcode);
                continue;
            } else {
                schedule.doctorName = docInfo.DoctorName;
            }

            scheduleMap.put(schedule.id(), schedule);
        }

        return sc;
    }


    private void copyFields(Schedule schedule, DepartWork work)
            throws IllegalAccessException, NoSuchFieldException {
        for (String fieldName : DptWorkFieldMap.keySet()) {
            Object val = DptWorkFieldMap.get(fieldName).get(work);
            ScheduleFieldMap.get(fieldName).set(schedule, val);
        }

        if (work instanceof DoctorWork) {
            schedule.DoctorId = ((DoctorWork) work).DoctorId;
            schedule.Expertsfee = ((DoctorWork) work).Expertsfee;
        }
    }


    /*-------------------------------------------------*/
    /* change detection                                */
    /*-------------------------------------------------*/
    private void cmpTrigger(ScheduleCache newScheduleCache) {
        List<TriggerInterface> triggers = TriggerFactory.getTriggerInstances();

        for (TriggerInterface trigger : triggers) {
            if (TriggerStage.cmp != trigger.getTriggerStage()) {
                continue;
            }
            trigger.handle(newScheduleCache);
        }
    }


    /*-------------------------------------------------*/
    /* replace schedule attribute as needed            */
    /*-------------------------------------------------*/
    private void repTrigger(ScheduleCache sc) {
        List<TriggerInterface> triggers = TriggerFactory.getTriggerInstances();

        for (TriggerInterface trigger : triggers) {
            if (TriggerStage.rep != trigger.getTriggerStage()) {
                continue;
            }
            trigger.handle(sc);
        }
    }

    /////////
    static class FileNameStartsWithFilter implements FilenameFilter {
        private String prefix;

        public FileNameStartsWithFilter(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public boolean accept(File dir, String name) {
            return name.startsWith(prefix);
        }
    }


    @Override
    public void run() {
        while (true) {

        }
    }
}
