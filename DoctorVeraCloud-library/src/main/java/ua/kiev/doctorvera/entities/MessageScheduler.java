package ua.kiev.doctorvera.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
/**
 * Entity class Describes Scheduler for sending messages
 * @author Volodymyr Bodnar
 * @date 22.11.2015
 */
@Entity
@Table(name = "MessageScheduler")
@XmlRootElement
public class MessageScheduler  implements Serializable, Identified<Integer>{

    @Id
    @Column(name = "MessageSchedulerId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageSchedulerId;

    @JoinColumn(name = "MessageTemplate", referencedColumnName = "MessageTemplateId")
    @ManyToOne(optional = false)
    @NotNull
    private MessageTemplate messageTemplate;

    @JoinColumn(name = "DeliveryGroup", referencedColumnName = "DeliveryGroupId")
    @ManyToOne(optional = false)
    private DeliveryGroup deliveryGroup;

    @JoinColumn(name = "User", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users user;

    @Basic
    @Column(name = "CronPattern")
    private String cronPattern;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DateCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Basic(optional = false)
    @NotNull
    @Column(name = "Deleted")
    private boolean deleted;

    @JoinColumn(name = "UserCreated", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users userCreated;

    public Integer getMessageSchedulerId() {
        return messageSchedulerId;
    }
    public void setMessageSchedulerId(Integer messageSchedulerId) {
        this.messageSchedulerId = messageSchedulerId;
    }
    public String getCronPattern() {
        return cronPattern;
    }
    public void setCronPattern(String cronPattern) {
        this.cronPattern = cronPattern;
    }

    public DeliveryGroup getDeliveryGroup() {
        return deliveryGroup;
    }

    public void setDeliveryGroup(DeliveryGroup deliveryGroup) {
        this.deliveryGroup = deliveryGroup;
    }

    public MessageTemplate getMessageTemplate() {
        return messageTemplate;
    }

    public void setMessageTemplate(MessageTemplate messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Override
    public Date getDateCreated() {
        return dateCreated;
    }
    @Override
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    @Override
    public boolean getDeleted() {
        return deleted;
    }
    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    @Override
    public Users getUserCreated() {
        return userCreated;
    }
    @Override
    public void setUserCreated(Users userCreated) {
        this.userCreated = userCreated;
    }
    @Override
    public Integer getId() {
        return null;
    }
    @Override
    public void setId(Integer id) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageScheduler that = (MessageScheduler) o;

        if (deleted != that.deleted) return false;
        if (!messageSchedulerId.equals(that.messageSchedulerId)) return false;
        if (!messageTemplate.equals(that.messageTemplate)) return false;
        if (deliveryGroup != null ? !deliveryGroup.equals(that.deliveryGroup) : that.deliveryGroup != null)
            return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (!cronPattern.equals(that.cronPattern)) return false;
        if (!dateCreated.equals(that.dateCreated)) return false;
        return userCreated.equals(that.userCreated);

    }

    @Override
    public int hashCode() {
        int result = messageSchedulerId.hashCode();
        result = 31 * result + messageTemplate.hashCode();
        result = 31 * result + (deliveryGroup != null ? deliveryGroup.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + cronPattern.hashCode();
        result = 31 * result + dateCreated.hashCode();
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + userCreated.hashCode();
        return result;
    }
}
