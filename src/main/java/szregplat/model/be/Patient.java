package szregplat.model.be;

import szregplat.util.EnumIface;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by huaiwang on 14-4-3.
 */
@Entity
@Table(name = "patients", schema = "ws320")
public class Patient {
    @Id
    @Column(name = "patient_id")
    private String patientId;

    @Column(name = "name")
    private String name;

    @Column(name = "is_blocked")
    @Type(
            type = "szregplat.util.EnumType",
            parameters = {
                    @Parameter(
                            name = "enumClass",
                            value = "szregplat.mvc.model.Patient$YesNo"
                    )
            }
    )
    private YesNo isBlocked;

    @Column(name = "blocked_at")
    private Date blockedAt;

    @Column(name = "unblock_at")
    private Date unblockAt;


    @Column(name = "created_at")
    private Date createdAt;


    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public YesNo getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(YesNo isBlocked) {
        this.isBlocked = isBlocked;
    }

    public Date getBlockedAt() {
        return blockedAt;
    }

    public void setBlockedAt(Date blockedAt) {
        this.blockedAt = blockedAt;
    }

    public Date getUnblockAt() {
        return unblockAt;
    }

    public void setUnblockAt(Date unblockAt) {
        this.unblockAt = unblockAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public static enum YesNo implements EnumIface<YesNo> {
        YES("Y"),
        NO("N");

        public String value;

        YesNo(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public YesNo getEnum(String value) {
            if (value.equals("N")) return NO;
            else return YES;
        }
    }
}
