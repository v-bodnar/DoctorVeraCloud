package ua.kiev.doctorvera.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

/**
 * Entity class Describes Users Group for message delivery
 * @author Volodymyr Bodnar
 * @date 22.11.2015
 */
@Entity
@Table(name = "DeliveryGroup")
@XmlRootElement
public class DeliveryGroup implements Serializable, Identified<Integer>{
    @Id
    @Column(name = "DeliveryGroupId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer deliveryGroupId;

    @Basic(optional = false)
    @Column(name = "Name")
    private String name;

    @Basic
    @Column(name = "Description")
    private String description;

    @OneToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="DeliveryGroupHasUsers",
            joinColumns={@JoinColumn(name="User", referencedColumnName="UserId")},
            inverseJoinColumns={@JoinColumn(name="DeliveryGroup", referencedColumnName="DeliveryGroupId")})
    private Collection<Users> users;

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


    public Integer getDeliveryGroupId() {
        return deliveryGroupId;
    }
    public void setDeliveryGroupId(Integer deliveryGroupId) {
        this.deliveryGroupId = deliveryGroupId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Collection<Users> getUsers() {
        return users;
    }

    public void setUsers(Collection<Users> users) {
        this.users = users;
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

        DeliveryGroup that = (DeliveryGroup) o;

        if (deleted != that.deleted) return false;
        if (!deliveryGroupId.equals(that.deliveryGroupId)) return false;
        if (!name.equals(that.name)) return false;
        if (!description.equals(that.description)) return false;
        if (!dateCreated.equals(that.dateCreated)) return false;
        return userCreated.equals(that.userCreated);

    }

    @Override
    public int hashCode() {
        int result = deliveryGroupId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + dateCreated.hashCode();
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + userCreated.hashCode();
        return result;
    }
}
