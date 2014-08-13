package szregplat.model;

import szregplat.omc.Ws320RuntimeException;

/**
 * Created by hch on 2014/5/18.
 */
public class Schedule extends DoctorWork implements Cloneable {
    public String hospitalName;
    public String departmentName;
    public String doctorName;
    public Schedule original;
    public Schedule replaced;

    public String id() {
        return hospitalName + "_"
                + departmentName + "_"
                + doctorName + "_"
                + WorkDate + "_"
                + WorkType;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Ws320RuntimeException(e);
        }
    }
}
