package ua.kiev.doctorvera.entities;

import org.joda.time.DateTime;
import ua.kiev.doctorvera.resources.Message;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(
            name = "MessageSchedulerHasDeliveryGroup",
            joinColumns = {@JoinColumn(name = "MessageSchedulerId", referencedColumnName = "MessageSchedulerId")},
            inverseJoinColumns = {@JoinColumn(name = "DeliveryGroupId", referencedColumnName = "DeliveryGroupId")})
    private List<DeliveryGroup> deliveryGroups;

    @JoinColumn(name = "User", referencedColumnName = "UserId")
    @ManyToOne
    private Users user;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DateStart")
    @Temporal(TemporalType.DATE)
    private Date dateStart;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DateEnd")
    @Temporal(TemporalType.DATE)
    private Date dateEnd;

    @Basic(optional = false)
    @NotNull
    @Column(name = "Time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @NotNull
    @Column(name = "DayOfWeekId", nullable = false)
    @ElementCollection(targetClass = DayOfWeek.class)
    @CollectionTable(name = "SchedulerHasDaysOfWeek",
            joinColumns = @JoinColumn(name = "SchedulerId"))
    @Enumerated(EnumType.ORDINAL)
    private List<DayOfWeek> daysOfWeek;

    @Basic(optional = false)
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

    public enum DayOfWeek implements Identified<Integer> {
        MONDAY(Message.getInstance().getString("CALENDAR_MONDAY")),
        TUESDAY(Message.getInstance().getString("CALENDAR_TUESDAY")),
        WEDNESDAY(Message.getInstance().getString("CALENDAR_WEDNESDAY")),
        THURSDAY(Message.getInstance().getString("CALENDAR_THURSDAY")),
        FRIDAY(Message.getInstance().getString("CALENDAR_FRIDAY")),
        SATURDAY(Message.getInstance().getString("CALENDAR_SATURDAY")),
        SUNDAY(Message.getInstance().getString("CALENDAR_SUNDAY"));

        DayOfWeek(String localName){
            this.localName=localName;
        }
        String localName;

        public String getLocalName() {
            return localName;
        }

        public void setLocalName(String localName) {
            this.localName = localName;
        }

        @Override
        public Integer getId() {
            return ordinal();
        }

        @Override
        public void setId(Integer id) {

        }

        @Override
        public Users getUserCreated() {
            return null;
        }

        @Override
        public void setUserCreated(Users userCreated) {

        }

        @Override
        public Date getDateCreated() {
            return null;
        }

        @Override
        public void setDateCreated(Date datereated) {

        }

        @Override
        public boolean getDeleted() {
            return false;
        }

        @Override
        public void setDeleted(boolean deleted) {

        }
    }

    public Integer getMessageSchedulerId() {
        return messageSchedulerId;
    }

    public void setMessageSchedulerId(Integer messageSchedulerId) {
        this.messageSchedulerId = messageSchedulerId;
    }

    public List<DeliveryGroup> getDeliveryGroups() {
        return deliveryGroups;
    }

    public void setDeliveryGroups(List<DeliveryGroup> deliveryGroups) {
        this.deliveryGroups = deliveryGroups;
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

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = new DateTime(dateEnd).withTime(23, 59, 59, 999).toDate();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
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
        return messageSchedulerId;
    }
    @Override
    public void setId(Integer id) {
        messageSchedulerId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageScheduler that = (MessageScheduler) o;

        if (deleted != that.deleted) return false;
        if (messageSchedulerId != null ? !messageSchedulerId.equals(that.messageSchedulerId) : that.messageSchedulerId != null)
            return false;
        if (!messageTemplate.equals(that.messageTemplate)) return false;
        if (!dateStart.equals(that.dateStart)) return false;
        if (!dateEnd.equals(that.dateEnd)) return false;
        if (!time.equals(that.time)) return false;
        if (!dateCreated.equals(that.dateCreated)) return false;
        return userCreated.equals(that.userCreated);

    }

    @Override
    public int hashCode() {
        int result = messageSchedulerId != null ? messageSchedulerId.hashCode() : 0;
        result = 31 * result + messageTemplate.hashCode();
        result = 31 * result + dateStart.hashCode();
        result = 31 * result + dateEnd.hashCode();
        result = 31 * result + time.hashCode();
        result = 31 * result + dateCreated.hashCode();
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + userCreated.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MessageScheduler{" +
                "messageSchedulerId=" + messageSchedulerId +
                ", messageTemplate=" + messageTemplate.getName() +
                '}';
    }
}
