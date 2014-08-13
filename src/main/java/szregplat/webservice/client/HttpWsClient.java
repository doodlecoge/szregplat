package szregplat.webservice.client;

import szregplat.omc.Ws320RuntimeException;
import szregplat.webservice.HttpEngine;
import szregplat.util.Config;
import szregplat.util.FileUtils;
import org.dom4j.*;

import java.util.Set;

/**
 * Created by hch on 2014/6/27.
 */
public class HttpWsClient implements HisClientIface {
    private static final String Envelope = FileUtils.load("/ds/envelope.xml");
    private static final String InfoReq = FileUtils.load("/ds/info-req.xml");
    private static final String Ns = "http://ws.apache.org/axis2";
    private static final HttpEngine Eng = new HttpEngine();
    private static final Config WsdlConfig = Config.getInstance("/confidential/ws_loc.properties");


    public static void main(String[] args) throws DocumentException {
        HttpWsClient client = new HttpWsClient();

        Set<String> keys = WsdlConfig.getKeys();
        for (String key : keys) {
            try {
                System.out.println(key + "=" + WsdlConfig.getString(key));
                String departInfo = client.getDepartInfo(key);
                System.out.println(departInfo);
            } catch (Exception e) {
                System.out.println("error: " + key);
            }
            break;
        }


    }

    @Override
    public String getDepartInfo(String hospitalId) {
        String reqxml = FileUtils.load("/ds/info-req.xml");
        String payload = reqxml.replace("{hospital-id}", hospitalId);
        String envelope = buildEnvelope("getDepartInfo", payload);

        try {
            Eng.post(WsdlConfig.getString(hospitalId), envelope);
            String resp = Eng.getResponse();
            System.out.println(resp);

            return extractResponse(resp, "Body/getDepartInfoResponse/return");
        } catch (Exception e) {
            throw new Ws320RuntimeException(e);
        }
    }

    @Override
    public String getDoctorInfo(String hospitalId) {
        return null;
    }

    @Override
    public String getDepartWorkSchedule(String hospitalId) {
        return null;
    }

    @Override
    public String getDoctorWorkSchedule(String hospitalId) {
        return null;
    }


    private String buildEnvelope(String method, String payload) {
        try {
            Document document = DocumentHelper.parseText(Envelope);
            Element rootElement = document.getRootElement();
            Element envelope = rootElement.element("Body");
            Element el = envelope.addElement(method, Ns);
            Element arg0 = el.addElement("arg0", Ns);
            arg0.addText(payload);
            return document.asXML();
        } catch (DocumentException e) {
            throw new Ws320RuntimeException(e);
        }
    }

    private String extractResponse(String resp, String path)
            throws DocumentException {
        String[] split = path.split("/");
        Document document = DocumentHelper.parseText(resp);
        Element rootElement = document.getRootElement();

        Element el = rootElement;
        for (String p : split) {
            el = el.element(p);
        }

        return el.getText();
    }

}
