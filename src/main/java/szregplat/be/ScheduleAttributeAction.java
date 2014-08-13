package szregplat.be;

import szregplat.dbcore.trigger.ScheduleAttributeMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zq on 2014/5/25.
 */
@Controller
@RequestMapping("/attr")
public class ScheduleAttributeAction {
    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap mm) {
        Map<String, String> attrMap = new HashMap<String, String>();

        ScheduleAttributeMap[] values = ScheduleAttributeMap.values();
        for (ScheduleAttributeMap value : values) {
            attrMap.put(value.name(), value.realAttr);
        }

        mm.put("attrMap", attrMap);
        return "attrs";
    }
}
