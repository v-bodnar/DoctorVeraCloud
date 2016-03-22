package ua.kiev.doctorvera.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Entity class Describes log record for send messages transaction, when messages are sent to group of users
 * @author Volodymyr Bodnar
 * @date 22.11.2015
 */
@Entity
@Table(name = "TransactionLog")
@XmlRootElement
public class TransactionLog  implements Serializable, Identified<Integer>{
    @Id
    @Column(name = "TransactionLogId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionLogId;

    @Basic
    @Column(name = "UId")
    private String uId;

    @Basic(optional = false)
    @NotNull
    @Column(name = "Status")
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @JoinColumn(name = "MessageTemplate", referencedColumnName = "MessageTemplateId")
    @ManyToOne(optional = false)
    private MessageTemplate messageTemplate;

    @Size(max = 255)
    @Column(name = "Details")
    private String details;

    @Column(name = "RecipientsCount")
    private Integer recipientsCount;

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

    public enum Status {
        NEW,
        PROGRESS,
        SENT,
        DELIVERED,
        DELIVERY_ERROR,
        SEND_ERROR
    }


    public Integer getTransactionLogId() {
        return transactionLogId;
    }

    public void setTransactionLogId(Integer transactionLogId) {
        this.transactionLogId = transactionLogId;
    }

    public MessageTemplate getMessageTemplate() {
        return messageTemplate;
    }

    public void setMessageTemplate(MessageTemplate messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getRecipientsCount() {
        return recipientsCount;
    }

    public void setRecipientsCount(Integer recipientsCount) {
        this.recipientsCount = recipientsCount;
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
        return transactionLogId;
    }
    @Override
    public void setId(Integer id) {
        transactionLogId = id;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionLog that = (TransactionLog) o;

        if (deleted != that.deleted) return false;
        if (transactionLogId != null ? !transactionLogId.equals(that.transactionLogId) : that.transactionLogId != null)
            return false;
        if (status != that.status) return false;
        if (!messageTemplate.equals(that.messageTemplate)) return false;
        if (details != null ? !details.equals(that.details) : that.details != null) return false;
        if (uId != null ? !uId.equals(that.uId) : that.uId != null) return false;
        if (recipientsCount != null ? !recipientsCount.equals(that.recipientsCount) : that.recipientsCount != null)
            return false;
        return dateCreated.equals(that.dateCreated) && userCreated.equals(that.userCreated);

    }

    @Override
    public int hashCode() {
        int result = transactionLogId != null ? transactionLogId.hashCode() : 0;
        result = 31 * result + status.hashCode();
        result = 31 * result + messageTemplate.hashCode();
        result = 31 * result + (details != null ? details.hashCode() : 0);
        result = 31 * result + (uId != null ? uId.hashCode() : 0);
        result = 31 * result + (recipientsCount != null ? recipientsCount.hashCode() : 0);
        result = 31 * result + dateCreated.hashCode();
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + userCreated.hashCode();
        return result;
    }
}
