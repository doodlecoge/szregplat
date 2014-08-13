package szregplat.model.be;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by hch on 2014/7/12.
 */
public class OrderHistory {
//    id         INT AUTO_INCREMENT,
//    order_id   INT         NOT NULL,
//    patient_id VARCHAR(18) NOT NULL, -- redundant, for query cancel times quickly
//    vendor_id  VARCHAR(20) NOT NULL,
//    operator   VARCHAR(20) NOT NULL,
//    operation  VARCHAR(2)  NOT NULL, -- 挂号,退号,修改
//    what       VARCHAR(20) NOT NULL,
//    old        VARCHAR(20) NULL,
//    new        VARCHAR(20) NULL,
//    when       DATETIME    NOT NULL,
//    CONSTRAINT IF NOT EXISTS pk_order_his_id PRIMARY KEY (id)


    @Id
    private String id;
    @Column
    private String order_id;
    @Column
    private String patient_id;
    @Column
    private String vendor_id;
    @Column
    private String operator;
    @Column
    private String operation;
    @Column
    private String what;
    @Column
    private String old_value;
    @Column
    private String new_value;
    @Column
    private Date when;


    public static enum  OP {
        ADD
    }
}
