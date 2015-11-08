/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.entities;

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
@NamedQueries({
    @NamedQuery(name = "UserGroups.findAll", query = "SELECT u FROM UserGroups u"),
    @NamedQuery(name = "UserGroups.UserGroupId", query = "SELECT u FROM UserGroups u WHERE u.userGroupId = :userGroupId"),
    @NamedQuery(name = "UserGroups.findByName", query = "SELECT u FROM UserGroups u WHERE u.name = :name"),
    @NamedQuery(name = "UserGroups.findByDescription", query = "SELECT u FROM UserGroups u WHERE u.description = :description"),
    @NamedQuery(name = "UserGroups.findByDateCreated", query = "SELECT u FROM UserGroups u WHERE u.dateCreated = :dateCreated"),
    @NamedQuery(name = "UserGroups.findByDeleted", query = "SELECT u FROM UserGroups u WHERE u.deleted = :deleted")})
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
    @ManyToMany(mappedBy="userGroups")
    private Collection<Users> users;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userGroup")
    private Collection<PolicyHasUserGroups> policyHasUserGroupsCollection;
    
    @JoinColumn(name = "UserCreated", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users userCreated;

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

    //    @XmlTransient
//    public Collection<UsersHasUserGroups> getUsersHasUserGroupsCollection() {
//        return usersHasUserGroupsCollection;
//    }
//
//    public void setUsersHasUserGroupsCollection(Collection<UsersHasUserGroups> usersHasUserGroupsCollection) {
//        this.usersHasUserGroupsCollection = usersHasUserGroupsCollection;
//    }

    @XmlTransient
    public Collection<PolicyHasUserGroups> getPolicyHasUserGroupsCollection() {
        return policyHasUserGroupsCollection;
    }

    public void setPolicyHasUserGroupsCollection(Collection<PolicyHasUserGroups> policyHasUserGroupsCollection) {
        this.policyHasUserGroupsCollection = policyHasUserGroupsCollection;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((userCreated == null) ? 0 : userCreated.hashCode());
		result = prime * result
				+ ((userGroupId == null) ? 0 : userGroupId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserGroups other = (UserGroups) obj;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (deleted != other.deleted)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (userCreated == null) {
			if (other.userCreated != null)
				return false;
		} else if (!userCreated.equals(other.userCreated))
			return false;
		if (userGroupId == null) {
			if (other.userGroupId != null)
				return false;
		} else if (!userGroupId.equals(other.userGroupId))
			return false;
		return true;
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
