package ua.kiev.doctorvera.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Refferrance entity for manyToMany relationship between users and deliveryGroups
 * @author Volodymyr Bodnar
 * @date 22.11.2015
 */
@Entity
@Table(name = "DeliveryGroupHasUsers")
@XmlRootElement
public class DeliveryGroupHasUsers  implements Serializable, Identified<Integer>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DeliveryGroupHasUsersId")
    private Integer deliveryGroupHasUsersId;

    @JoinColumn(name = "User", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users user;

    @JoinColumn(name = "DeliveryGroup", referencedColumnName = "DeliveryGroupId")
    @ManyToOne(optional = false)
    private DeliveryGroup deliveryGroup;

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


    public Integer getDeliveryGroupHasUsersId() {
        return deliveryGroupHasUsersId;
    }
    public void setDeliveryGroupHasUsersId(Integer deliveryGroupHasUsersId) {
        this.deliveryGroupHasUsersId = deliveryGroupHasUsersId;
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

        DeliveryGroupHasUsers that = (DeliveryGroupHasUsers) o;

        if (deleted != that.deleted) return false;
        if (!deliveryGroupHasUsersId.equals(that.deliveryGroupHasUsersId)) return false;
        if (!user.equals(that.user)) return false;
        if (!deliveryGroup.equals(that.deliveryGroup)) return false;
        if (!dateCreated.equals(that.dateCreated)) return false;
        return userCreated.equals(that.userCreated);

    }

    @Override
    public int hashCode() {
        int result = deliveryGroupHasUsersId.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + deliveryGroup.hashCode();
        result = 31 * result + dateCreated.hashCode();
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + userCreated.hashCode();
        return result;
    }
}
