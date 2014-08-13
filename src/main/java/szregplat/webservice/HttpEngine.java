package szregplat.webservice;

import szregplat.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hch on 2014/6/14.
 */
public class HttpEngine {
    private static final Logger log = LoggerFactory.getLogger(HttpEngine.class);
    public static final String GET = "GET";
    public static final String POST = "POST";

    private static final Map<String, String> ReqProps =
            new HashMap<String, String>();

    static {
        ReqProps.put("Content-Type", "text/xml; charset=UTF-8");
    }

    private int readTimeout = 30000;
    private int connTimeout = 10000;
    private CookieManager cm;
    private HttpURLConnection httpConn;

    {
        cm = new CookieManager();
        cm.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cm);
    }

    public static void main(String[] args) throws Exception {
        String base = "http://172.25.61.4:8080";

        String wsdl = base + "/axis2/services/WS4SZ?wsdl";

        HttpEngine eng = new HttpEngine();

        String data = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "    <S:Body>" +
                "        <getDepartInfo xmlns=\"http://ws.apache.org/axis2\">" +
                "           <arg0>{payload}</arg0>" +
                "        </getDepartInfo>" +
                "    </S:Body>" +
                "</S:Envelope>";

        String msg = "<Message><RequestInfo><HospitalId>aa</HospitalId></RequestInfo></Message>";
        msg = StringUtils.encode(msg);
        data = data.replace("{payload}", msg);

        data = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "    <soapenv:Body>\n" +
                "        <getDepartInfo xmlns=\"http://ws.apache.org/axis2\">\n" +
                "            <arg0 xmlns=\"\">&lt;Message>&lt;RequestInfo>&lt;HospitalId>aa&lt;/HospitalId>&lt;/RequestInfo>&lt;/Message></arg0>\n" +
                "        </getDepartInfo>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";


        System.out.println(wsdl);

        eng.post(wsdl, data);
        String response = eng.getResponse();
        System.out.println(response);
    }

    public HttpEngine get(String url) throws Exception {
        return get(url, "UTF-8");
    }

    public HttpEngine get(String url, String encoding) throws Exception {
        return exec(METHOD.GET, url, null);
    }


    public HttpEngine post(String url, Map<String, String> data) throws Exception {
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for (String key : data.keySet()) {
            sb.append(sep).append(key).append("=").append(data.get(key));
            sep = "&";
        }
        return post(url, sb.toString());
    }

    public HttpEngine post(String url, String data) throws Exception {
        return exec(METHOD.POST, url, data);
    }

    private HttpEngine exec(METHOD method, String url, String data)
            throws Exception {
        CookieStore cs = cm.getCookieStore();
        List<HttpCookie> cookies = cs.getCookies();
        for (HttpCookie cookie : cookies) {
            System.out.println("> " + cookie.getName() + ":" + cookie.getValue());
        }


        URL res = new URL(url);
        URLConnection urlConn = res.openConnection();

        if (!(urlConn instanceof HttpURLConnection)) {
            throw new Exception("not http");
        }

        httpConn = (HttpURLConnection) urlConn;

        if (httpConn instanceof HttpsURLConnection) {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[]{trustAll}, null);
            HttpsURLConnection httpsConn = (HttpsURLConnection) httpConn;
            httpsConn.setSSLSocketFactory(sslcontext.getSocketFactory());
        }

        httpConn.setReadTimeout(readTimeout);
        httpConn.setConnectTimeout(connTimeout);
        for (String key : ReqProps.keySet()) {
            httpConn.setRequestProperty(key, ReqProps.get(key));
        }

        // send request data
        if (method == METHOD.POST && data != null) {
            httpConn.setDoOutput(true);
            OutputStream os = httpConn.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            bos.write(data.getBytes());
            bos.close();
        }


        List<String> strings = httpConn.getHeaderFields().get("Set-Cookie");
        if (strings != null) {
            for (String string : strings) {
                log.debug("Set-Cookie: " + string);
            }
        }

        return this;
    }

    public int getCode() throws IOException {
        return httpConn.getResponseCode();
    }

    public String getResponse() throws IOException {
        return getResponse("UTF-8");
    }

    public String getResponse(String encoding) throws IOException {
        InputStream is = httpConn.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b;
        while ((b = bis.read()) != -1) baos.write(b);
        bis.close();
        return baos.toString();
    }

    public String getHeaderField(String name) {
        return httpConn.getHeaderField(name);
    }

    public Map<String, List<String>> getHeaderFields() {
        return httpConn.getHeaderFields();
    }

    private static enum METHOD {
        GET, POST
    }

    private static TrustManager trustAll = new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
    };
}
//https://passport.9wee.com/login
//http://s8.hero.9wee.com/passport.php?act=login&referer=/
//https://passport.9wee.com/api/verify.php?appId=103&p=BzAGbQZnAmxaJFcmAWsBZFQ%2BVHRVc1w%2BAjMBa1EjUjgHI1EiUzsFIgE%2BV2JRNQJqVSdWPAQkUSIHIFN5VGcHSQd0BmUGEwJzWm1XEwEiAWtUKlQ%2BVWRcKQI6ASBRaFIqBzRRZVMuBTIBa1c4USQCYlVDViQEMVElByNTLFQ7B3oHJQZ5BiUCPlovV3ABYgEVVGVUNVV1XH4CZgFKUT1SMgc2UWlTbgV0ATZXY1FzAjVVY1YxBCJRMwciU3lUZwdMB3QGZQYTAnRaZFcmAWsBZ1Q%2BVHRVcVwNAjQBfFFzUmYHIlE6UzEFawEmVyVRIwJrVXg%3D
//http://s8.hero.9wee.com/passport.php?act=login&referer=/&p=BDNTOFA6XDIHeVIjUzkBYlg4U2sFc1ElV2VSLlIhVDIGPwQ7V1tSb1U0UXEDaFt6WDIAMVA2Um9QJQZvAGRVawRqU2FQMFxuB2FSZFNiATZYalNkBTJRZ1c1Um1SMFRpBjYENFcxUmNVMVFgAzFbPlgxADRQMlIxUDQGKwA7VSkEaFMzUDJcMgcgUjFTcwEjWFdTJAUiUTNXclICUjtUPwZyBG5Xd1I8VWBRaQNxWytYMwBxUD5SbVA9BisAdVUpBDdTcFBtXGkHb1I1UyEBaFh7U2sFZFFsVyJSPlIxVDoGJgRgVyZSPVUjUWkDa1szWCoAbFBtUjZQbAZnAGFVNwQ3UyBQOFx7BzhSYlMxAWlYKlN0BRRRb1clUhxSY1R%2BBhIEbFd3Um5VMVE8A3ZbTFgxACdQRlIQUCIGMAA5VXgEaVNxUDlcMAc4UnJTcwEyWHtTIgUmUTlXclI5UnBUYAYjBG9XN1I0VWpRcQMwWzpYOQBmUDNSNlA0BjAAZFVtBGZTMVBiXDwHZFJoU2UBNVhqU2MFMlFkVzBSbVJhVGIGaQQxV2ZSZVViUTEDcVsyWHsAOFAxUm9QJQZsAG1VOwQ7U25QIVwzB3FSalMzAWlYKlNzBWpRJVc6UmxSZ1RhBnIEIFd3UmNVIlEMAyFbbFhpAG5QW1I3UG4GewB0VTIEcFM5UHBcMgcyUmpTIQFxWDNTIgVrUWdXMVJnUnBULQY1BCdXbVJgVSlRDAMnW2BYZQBnUCZSblB0BjMAMVVuBGhTIFAxXDgHM1JkUzMBZVg6U2MFYFFgVzBSbFJiVGIGcgRuV3dSPFVhUWIDaVsrWH4AZ1B2UjxQYQZwAF9VOQQ9U2ZQZlwqBzlSI1M5AWBYOlNrBXNRYFdkUmhSYFQ%2BBmUEN1c0UmRVNlFkA2pbalhrADZQZVI2UGYGPwA4VT8EMVMzUGJcPwc3UmdTMQE3WG1TZwVmUXRXO1Ig
//