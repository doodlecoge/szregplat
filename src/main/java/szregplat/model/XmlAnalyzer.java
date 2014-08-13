package szregplat.model;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 14-4-20.
 */
public class XmlAnalyzer {
    private static Field[] departInfoFields = DepartInfo.class.getDeclaredFields();
    private static String[] departInfoTags = "HospitalCode,DepartName,DepartId,DepartIntro,DepartType,RegistryFee,ClinicFee,MZKS,Departsex,Babyflag,BabyAge".split(",");


    public static List<DepartInfo> getDeparts(String xml) throws DocumentException, NoSuchFieldException, IllegalAccessException, InstantiationException {
        return get(xml, "/Message/DepartInfo", DepartInfo.class);
    }

    public static List<DoctorInfo> getDoctors(String xml) throws DocumentException, NoSuchFieldException, IllegalAccessException, InstantiationException {
        return get(xml, "/Message/DoctorInfo", DoctorInfo.class);
    }

    public static List<DepartWork> getDepartWorks(String xml) throws DocumentException, NoSuchFieldException, IllegalAccessException, InstantiationException {
        return get(xml, "/Message/DepartCalendar", DepartWork.class);
    }

    public static List<DoctorWork> getDoctorWorks(String xml) throws DocumentException, NoSuchFieldException, IllegalAccessException, InstantiationException {
        return get(xml, "/Message/DoctorCalendar", DoctorWork.class);
    }


    private static <T> List<T> get(String xml, String path, Class<T> c) throws DocumentException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        SAXReader reader = new SAXReader();
        Document doc = reader.read(new ByteArrayInputStream(xml.getBytes()));
        List list = doc.selectNodes(path);
        List<T> lst = new ArrayList<T>();

        for (Object info : list) {
            Node infoNode = (Node) info;
            T ins = (T) c.newInstance();
            List children = infoNode.selectNodes("./*");

            for (Object child : children) {
                Node childNode = (Node) child;
                String text = childNode.getText();
                String tag = childNode.getName();
                Field field = c.getDeclaredField(tag);
                field.setAccessible(true);
                field.set(ins, text);
            }

            lst.add(ins);
        }

        return lst;
    }
}
