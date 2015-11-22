package ua.kiev.doctorvera.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @Basic
    @Column(name = "Status")
    private Byte status;

    @JoinColumn(name = "Transaction", referencedColumnName = "TransactionLogId")
    @ManyToOne(optional = false)
    private TransactionLog transaction;

    @JoinColumn(name = "Recipient", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users recipient;

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
    public Byte getStatus() {
        return status;
    }
    public void setStatus(Byte status) {
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

        MessageLog that = (MessageLog) o;

        if (deleted != that.deleted) return false;
        if (!messageLogId.equals(that.messageLogId)) return false;
        if (!uId.equals(that.uId)) return false;
        if (!status.equals(that.status)) return false;
        if (!transaction.equals(that.transaction)) return false;
        if (!recipient.equals(that.recipient)) return false;
        if (!dateCreated.equals(that.dateCreated)) return false;
        return userCreated.equals(that.userCreated);

    }

    @Override
    public int hashCode() {
        int result = messageLogId.hashCode();
        result = 31 * result + uId.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + transaction.hashCode();
        result = 31 * result + recipient.hashCode();
        result = 31 * result + dateCreated.hashCode();
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + userCreated.hashCode();
        return result;
    }
}
