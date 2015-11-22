package ua.kiev.doctorvera.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @Basic(optional = false)
    @NotNull
    @Column(name = "Status")
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @JoinColumn(name = "MessageScheduler", referencedColumnName = "MessageSchedulerId")
    @ManyToOne(optional = false)
    private MessageScheduler messageScheduler;

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

    public static enum Status {
        NEW,
        SENT,
        DELIVERED,
        DELIVERY_ERROR,
        SEND_ERROR;
    }


    public Integer getTransactionLogId() {
        return transactionLogId;
    }

    public void setTransactionLogId(Integer transactionLogId) {
        this.transactionLogId = transactionLogId;
    }

    public MessageScheduler getMessageScheduler() {
        return messageScheduler;
    }

    public void setMessageScheduler(MessageScheduler messageScheduler) {
        this.messageScheduler = messageScheduler;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

        TransactionLog that = (TransactionLog) o;

        if (deleted != that.deleted) return false;
        if (!transactionLogId.equals(that.transactionLogId)) return false;
        if (status != that.status) return false;
        if (!messageScheduler.equals(that.messageScheduler)) return false;
        if (!dateCreated.equals(that.dateCreated)) return false;
        return userCreated.equals(that.userCreated);

    }

    @Override
    public int hashCode() {
        int result = transactionLogId.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + messageScheduler.hashCode();
        result = 31 * result + dateCreated.hashCode();
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + userCreated.hashCode();
        return result;
    }
}
