/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.entities;

import org.hibernate.annotations.IndexColumn;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entity class Describes User
 * @author Volodymyr Bodnar
 */
@Entity
@Table(name = "UserGroups")
@XmlRootElement
public class UserGroups implements Serializable,Identified<Integer> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserGroupId")
    private Integer userGroupId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Name")
    private String name;
    @Size(max = 200)
    @Column(name = "Description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DateCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Deleted")
    private boolean deleted;

    @OrderColumn(name="user")
    @ManyToMany(mappedBy="userGroups", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Collection<Users> users;

    @JoinColumn(name = "UserCreated", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users userCreated;

    @OrderColumn(name="DeliveryGroup")
    @ManyToMany(mappedBy="userGroups")
    private Collection<DeliveryGroup> deliveryGroups;


    @OrderColumn(name="Policy")
    @ManyToMany(mappedBy="userGroups", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Collection<Policy> policies;

    public UserGroups() {
    }

    public UserGroups(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public UserGroups(Integer userGroupId, String name, Date dateCreated, boolean deleted) {
        this.userGroupId = userGroupId;
        this.name = name;
        this.dateCreated = dateCreated;
        this.deleted = deleted;
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userTypeId) {
        this.userGroupId = userTypeId;
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

    @XmlTransient
    public Collection<Users> getUsers() {
        return users;
    }

    public void setUsers(Collection<Users> users) {
        this.users = users;
    }

    public Collection<DeliveryGroup> getDeliveryGroups() {
        return deliveryGroups;
    }

    public void setDeliveryGroups(Collection<DeliveryGroup> deliveryGroups) {
        this.deliveryGroups = deliveryGroups;
    }

    @Override
    public Users getUserCreated() {
        return userCreated;
    }
    @Override
    public void setUserCreated(Users userCreated) {
        this.userCreated = userCreated;
    }

    public Collection<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(Collection<Policy> policies) {
        this.policies = policies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserGroups that = (UserGroups) o;

        if (deleted != that.deleted) return false;
        if (userGroupId != null ? !userGroupId.equals(that.userGroupId) : that.userGroupId != null) return false;
        if (!name.equals(that.name)) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (!dateCreated.equals(that.dateCreated)) return false;
        return userCreated.equals(that.userCreated);

    }

    @Override
    public int hashCode() {
        int result = userGroupId != null ? userGroupId.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + dateCreated.hashCode();
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + userCreated.hashCode();
        return result;
    }

    @Override
	public String toString() {
		return "UserGroups [userTypeId=" + userGroupId + ", name=" + name
				+ ", description=" + description + ", dateCreated="
				+ dateCreated + ", deleted=" + deleted + ", userCreated="
				+ userCreated + "]";
	}

    @Override
    public Integer getId() {
        return getUserGroupId();
    }

    @Override
    public void setId(Integer id) {
        setUserGroupId(id);
    }


    
}
