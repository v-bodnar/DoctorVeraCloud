package ua.kiev.doctorvera.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Entity class Describes message in particular locale and type
 * @author Volodymyr Bodnar
 * @date 23.01.2016
 */

@Entity
@Table(name = "MessageBundle")
@XmlRootElement
public class MessageBundle implements Serializable, Identified<Integer>, Comparable<MessageBundle> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MessageBundleId")
    private Integer messageBundleId;

    @Basic(optional = false)
    @NotNull
    @Column(name = "MessageKey")
    private String messageKey;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Value")
    private String value;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Type")
    @Enumerated(EnumType.ORDINAL)
    private Type type;
    @ManyToOne
    @JoinColumn (name = "Locale")
    private Locale locale;

    @Basic(optional = false)
    @Column(name = "DateCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Basic(optional = false)
    @Column(name = "Deleted")
    private boolean deleted;
    @JoinColumn(name = "UserCreated", referencedColumnName = "UserId")
    @ManyToOne(optional = false,fetch= FetchType.LAZY)
    private Users userCreated;

    public enum Type {
        MESSAGE,
        CONFIG,
        MAPPING
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageBundle that = (MessageBundle) o;

        if (deleted != that.deleted) return false;
        if (messageBundleId != null ? !messageBundleId.equals(that.messageBundleId) : that.messageBundleId != null)
            return false;
        if (!messageKey.equals(that.messageKey)) return false;
        if (!value.equals(that.value)) return false;
        if (type != that.type) return false;
        if (locale != null ? !locale.equals(that.locale) : that.locale != null) return false;
        return dateCreated.equals(that.dateCreated) && userCreated.equals(that.userCreated);

    }

    @Override
    public int hashCode() {
        int result = messageBundleId != null ? messageBundleId.hashCode() : 0;
        result = 31 * result + messageKey.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + dateCreated.hashCode();
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + userCreated.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MessageBundle{" +
                "key='" + messageKey + '\'' +
                ", value='" + value + '\'' +
                ", locale=" + locale +
                ", type=" + type +
                '}';
    }

    public Integer getMessageBundleId() {
        return messageBundleId;
    }

    public void setMessageBundleId(Integer messageBundleId) {
        this.messageBundleId = messageBundleId;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String key) {
        this.messageKey = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public Date getDateCreated() {
        return dateCreated;
    }

    @Override
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isDeleted() {
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
        return getMessageBundleId();
    }

    @Override
    public void setId(Integer id) {
        setMessageBundleId(id);
    }

    @Override
    public boolean getDeleted() {
        return deleted;
    }

    @Override
    public int compareTo(MessageBundle bundle) {
        if(bundle == null) return -1;
        return messageKey.compareTo(bundle.getMessageKey());
    }
}
