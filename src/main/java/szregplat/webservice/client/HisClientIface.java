package szregplat.webservice.client;

/**
 * Created by Administrator on 14-4-13.
 */
public interface HisClientIface {
    public String getDepartInfo(String hospitalId);

    public String getDoctorInfo(String hospitalId);

    public String getDepartWorkSchedule(String hospitalId);

    public String getDoctorWorkSchedule(String hospitalId);
}
