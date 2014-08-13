package szregplat.model;

/**
 * Created with IntelliJ IDEA.
 * User: WeiSW
 * Date: 14-7-29
 * Time: 上午10:11
 * 转诊信息
 */
public class Referral {

    /**挂号流水号*/
    public Long SN ;
    /**转出医院名称*/
    public String outHospName ;
    /**转出科室名称*/
    public String outDepartName ;
    /**转出医生名称*/
    public String outDoctorName ;
    /**转诊原因*/
    public String reason ;
    /**病史摘要*/
    public String medicalRecord ;
    /**初步诊断*/
    public String preDiagnosis ;


    /************SET/GET**************/
    public Long getSN() {
        return SN;
    }

    public void setSN(Long SN) {
        this.SN = SN;
    }

    public String getOutHospName() {
        return outHospName;
    }

    public void setOutHospName(String outHospName) {
        this.outHospName = outHospName;
    }

    public String getOutDepartName() {
        return outDepartName;
    }

    public void setOutDepartName(String outDepartName) {
        this.outDepartName = outDepartName;
    }

    public String getOutDoctorName() {
        return outDoctorName;
    }

    public void setOutDoctorName(String outDoctorName) {
        this.outDoctorName = outDoctorName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(String medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public String getPreDiagnosis() {
        return preDiagnosis;
    }

    public void setPreDiagnosis(String preDiagnosis) {
        this.preDiagnosis = preDiagnosis;
    }
}
