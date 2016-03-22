package ua.kiev.doctorvera.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Entity class Describes Log record for sent Message
 * @author Volodymyr Bodnar
 * @date 22.11.2015
 */
@Entity
@Table(name = "MessageLog")
@XmlRootElement
public class MessageLog implements Serializable, Identified<Integer>{
    @Id
    @Column(name = "MessageLogId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageLogId;

    @Basic
    @Column(name = "UId")
    private String uId;

    @Basic(optional = false)
    @NotNull
    @Column(name = "Status")
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @JoinColumn(name = "Transaction", referencedColumnName = "TransactionLogId")
    @ManyToOne(optional = false)
    private TransactionLog transaction;

    @JoinColumn(name = "Recipient", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users recipient;

    @Size(max = 200)
    @Column(name = "Details")
    private String details;

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
        WAITING,
        SENDING,
        PAUSED,
        CANCELED,
        NEW,
        SENT,
        DELIVERED,
        DELIVERY_ERROR,
        SEND_ERROR
    }


    public Integer getMessageLogId() {
        return messageLogId;
    }
    public void setMessageLogId(Integer messageLogId) {
        this.messageLogId = messageLogId;
    }
    public String getuId() {
        return uId;
    }
    public void setuId(String uId) {
        this.uId = uId;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public Users getRecipient() {
        return recipient;
    }

    public void setRecipient(Users recipient) {
        this.recipient = recipient;
    }

    public TransactionLog getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionLog transaction) {
        this.transaction = transaction;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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
        return messageLogId;
    }
    @Override
    public void setId(Integer id) {
        this.messageLogId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageLog that = (MessageLog) o;

        if (deleted != that.deleted) return false;
        if (messageLogId != null ? !messageLogId.equals(that.messageLogId) : that.messageLogId != null) return false;
        if (uId != null ? !uId.equals(that.uId) : that.uId != null) return false;
        if (status != that.status) return false;
        if (!transaction.equals(that.transaction)) return false;
        if (!recipient.equals(that.recipient)) return false;
        if (details != null ? !details.equals(that.details) : that.details != null) return false;
        if (!dateCreated.equals(that.dateCreated)) return false;
        return userCreated.equals(that.userCreated);

    }

    @Override
    public int hashCode() {
        int result = messageLogId != null ? messageLogId.hashCode() : 0;
        result = 31 * result + (uId != null ? uId.hashCode() : 0);
        result = 31 * result + status.hashCode();
        result = 31 * result + transaction.hashCode();
        result = 31 * result + recipient.hashCode();
        result = 31 * result + (details != null ? details.hashCode() : 0);
        result = 31 * result + dateCreated.hashCode();
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + userCreated.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MessageLog{" +
                "messageLogId=" + messageLogId +
                ", status=" + status +
                ", details='" + details + '\'' +
                ", recipient=" + recipient +
                '}';
    }
}
