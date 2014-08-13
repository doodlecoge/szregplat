package szregplat.model.be;


import javax.persistence.*;
import java.util.Date;

/**
 * Created by huaiwang on 7/15/14.
 */
@Entity
@Table(name = "block_histories", schema = "ws320")
public class BlockHistory {
    @Id
    private int id;

    @Column(name = "patient_id")
    private String patientId;

    @Column
    @Enumerated(EnumType.STRING)
    private OP operation;

    @Column(name = "vendor_id")
    private String vendorId;

    @Column
    private String operator;

    @Column
    private String reason;

    @Column
    private Date when;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public OP getOperation() {
        return operation;
    }

    public void setOperation(OP operation) {
        this.operation = operation;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public static enum OP {
        block, unblock;
    }
}
