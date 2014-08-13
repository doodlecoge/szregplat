package szregplat.dbcore.trigger;

import javax.persistence.*;

/**
 * Created by hch on 2014/5/14.
 */

@Entity
@Table(name = "triggers", schema = "ws320")
public class TriggerInfo {
    @Column
    private String selector;
    @Column
    private String attribute;
    @Column
    private String oldValue;
    @Column
    private String newValue;

    // action is used by TriggerFactory
    // to create trigger instance.
    @Id
    @Enumerated(EnumType.STRING)
    private TriggerAction action;
    @Column
    @Enumerated(EnumType.STRING)
    private TriggerStage stage;


    public String toString() {
        return selector + ","
                + attribute + ","
                + oldValue + ","
                + newValue + ","
                + action + ","
                + stage;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public TriggerAction getAction() {
        return action;
    }

    public void setAction(TriggerAction action) {
        this.action = action;
    }

    public TriggerStage getStage() {
        return stage;
    }

    public void setStage(TriggerStage stage) {
        this.stage = stage;
    }
}
