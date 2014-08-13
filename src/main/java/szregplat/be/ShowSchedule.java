package szregplat.be;

import szregplat.model.MemoryCache;
import szregplat.model.ScheduleCache;
import szregplat.model.be.HospitalEntity;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huaiwang on 5/27/14.
 */
@Controller
@RequestMapping("/sche")
public class ShowSchedule {
    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap map) {
        MemoryCache cache = MemoryCache.getInstance();
        Map<String, HospitalEntity> hospitals = cache.getHospitals();
        Map<String, String> hosMap = new HashMap<String, String>();
        for (String key : hospitals.keySet()) {
            hosMap.put(key, hospitals.get(key).getName());
        }
        map.put("hospitals", hosMap);
        return "schedule";
    }

    @RequestMapping(
            value = "/{hospitalId}",
            method = RequestMethod.GET,
            produces = "text/plain;charset=UTF-8"
    )
    @ResponseBody
    public String show(@PathVariable("hospitalId") String hospitalId, ModelMap map) {
        ScheduleCache sc = MemoryCache.getInstance().getSchedules(hospitalId);
        Document doc = sc.getData();
//        return doc.asXML();

        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        StringWriter sw = new StringWriter();
        XMLWriter writer = new XMLWriter(sw, format);
        try {
            writer.write(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }
}
