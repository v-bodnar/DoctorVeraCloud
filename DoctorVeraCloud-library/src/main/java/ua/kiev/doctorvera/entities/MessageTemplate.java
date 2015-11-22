package ua.kiev.doctorvera.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Entity class Describes Message content
 * @author Volodymyr Bodnar
 * @date 22.11.2015
 */
@Entity
@Table(name = "MessageTemplate")
@XmlRootElement
public class MessageTemplate  implements Serializable, Identified<Integer>{
    @Id
    @Column(name = "MessageTemplateId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageTemplateId;

    @Basic(optional = false)
    @NotNull
    @Column(name = "Name")
    private String name;

    @Basic(optional = false)
    @NotNull
    @Column(name = "Content")
    private String content;

    @Basic
    @Column(name = "Description")
    private String description;

    @Basic(optional = false)
    @NotNull
    @Column(name = "Type")
    @Enumerated(EnumType.ORDINAL)
    private Type type;

    @Basic(optional = false)
    @NotNull
    @Column(name = "System")
    private boolean system;

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

    public static enum Type {
        SMS,
        EMAIL;
    }

    public Integer getMessageTemplateId() {
        return messageTemplateId;
    }

    public void setMessageTemplateId(Integer messageTemplateId) {
        this.messageTemplateId = messageTemplateId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
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

        MessageTemplate that = (MessageTemplate) o;

        if (system != that.system) return false;
        if (deleted != that.deleted) return false;
        if (!messageTemplateId.equals(that.messageTemplateId)) return false;
        if (!name.equals(that.name)) return false;
        if (!content.equals(that.content)) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (type != that.type) return false;
        if (!dateCreated.equals(that.dateCreated)) return false;
        return userCreated.equals(that.userCreated);

    }

    @Override
    public int hashCode() {
        int result = messageTemplateId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + content.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + type.hashCode();
        result = 31 * result + (system ? 1 : 0);
        result = 31 * result + dateCreated.hashCode();
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + userCreated.hashCode();
        return result;
    }
}
