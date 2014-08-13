package szregplat.webservice;

import szregplat.model.MemoryCache;
import szregplat.model.NODE;
import szregplat.model.ScheduleCache;
import szregplat.model.be.HospitalEntity;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by huaiwang on 6/3/14.
 */

@Service
public class Ws320Service {
    private static final String NS = "http://service.hch.me";
    private static final MemoryCache cache = MemoryCache.getInstance();


    public Element getHospitalResponse(String hospitalName) {
        if (hospitalName == null || hospitalName.equals("")) {
            return getHospitals();
        }

        Document document = DocumentHelper.createDocument();
        Element resp = document.addElement(NODE.GET_HOSPITAL_RESPONSE, NS);
        resp.addElement(NODE.RESULT, NS);
        // todo: add result code and message

        Element elHospital = getHospitalElement(hospitalName);

        if (elHospital != null) {
            resp.add(elHospital);
        }

        return resp;
    }

    private Element getHospitals() {
        Document document = DocumentHelper.createDocument();
        Element resp = document.addElement(NODE.GET_HOSPITAL_RESPONSE, NS);
        resp.addElement(NODE.RESULT, NS);
        // todo: add result code and message

        Map<String, HospitalEntity> hospitals = cache.getHospitals();
        for (HospitalEntity hospital : hospitals.values()) {
            Element elHospital = getHospitalElement(hospital.getName());
            if (elHospital != null) resp.add(elHospital);
        }

        return resp;
    }


    private Element getHospitalElement(String hospitalName) {
        HospitalEntity hosEntiry = cache.getHospitalByName(hospitalName);

        if (hosEntiry == null) {
            // todo: response with not found error
            return null;
        }

        ScheduleCache schedules = cache.getSchedules(hosEntiry.getId());
        if (schedules == null) {
            // todo: response with no schedules
            return null;
        }

        Element elHospital = DocumentHelper.createElement(NODE.HOSPITAL);
        elHospital.addAttribute(NODE.ATTR_NAME, hosEntiry.getName());
        Element elBasic = elHospital.addElement(NODE.BASIC, NS);
        Element elPhone = elBasic.addElement(NODE.PHONE, NS);
        elPhone.setText(hosEntiry.getPhone());
        // todo: add other hospital basic infos

        elHospital.add(schedules.getData().getRootElement());

        return elHospital;
    }
}
