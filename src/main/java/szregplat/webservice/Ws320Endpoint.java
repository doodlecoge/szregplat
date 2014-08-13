package szregplat.webservice;

import szregplat.model.MemoryCache;
import szregplat.model.NODE;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * Created by hch on 2014/5/9.
 */

@Endpoint
public class Ws320Endpoint {
    private static final String NS = "http://service.hch.me";
    private static final MemoryCache cache = MemoryCache.getInstance();
    private Ws320Service service;

    @Autowired
    public Ws320Endpoint(Ws320Service service) {
        this.service = service;
    }

    @ResponsePayload
    @PayloadRoot(localPart = "GetHospInfoRequest", namespace = NS)
    public Element handleGetHospInfoRequest(@RequestPayload Element xml) {
        String hospitalName = getHospitalName(xml);
        Document document = DocumentHelper.createDocument();
        Element resp = document.addElement(NODE.GET_HOSPITAL_RESPONSE, NS);
        return service.getHospitalResponse(hospitalName);
    }

    private String getHospitalName(Element req) {
        Element elFilter = req.element(NODE.FILTER);
        Element elHospName = elFilter.element(NODE.HOSPITAL_NAME);
        if (null == elHospName || "".equals(elHospName.getText())) {
            return null;
        } else {
            return elHospName.getText();
        }
    }


    @ResponsePayload
    @PayloadRoot(localPart = "GetRegInfoRequest", namespace = NS)
    public Element handleGetRegInfoRequest(@RequestPayload Element xml) {
        Document document = DocumentHelper.createDocument();
        Element ele = document.addElement("GetRegInfoResponse", NS);
        ele.setText("GetRegInfoResponse");
        return ele;

    }


    @ResponsePayload
    @PayloadRoot(localPart = "GetRegPoolRequest", namespace = NS)
    public Element handleGetRegPoolRequest(@RequestPayload Element xml) {
        Document document = DocumentHelper.createDocument();
        Element ele = document.addElement("GetRegPoolResponse", NS);
        ele.setText("GetRegPoolResponse");
        return ele;

    }


    @ResponsePayload
    @PayloadRoot(localPart = "NotifyHospInfoRequest", namespace = NS)
    public Element handleNotifyHospInfoRequest(@RequestPayload Element xml) {
        Document document = DocumentHelper.createDocument();
        Element ele = document.addElement("NotifyHospInfoResponse", NS);
        ele.setText("NotifyHospInfoResponse");
        return ele;
    }


    @ResponsePayload
    @PayloadRoot(localPart = "RegisterRequest", namespace = NS)
    public Element handleRegisterRequest(@RequestPayload Element xml) {
        Document document = DocumentHelper.createDocument();
        Element ele = document.addElement("RegisterResponse", NS);
        ele.setText("RegisterResponse");
        return ele;
    }

}
