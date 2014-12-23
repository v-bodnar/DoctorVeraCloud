/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entity class Describes User
 * @author Volodymyr Bodnar
 */
@Entity
@Table(name = "UserTypes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserTypes.findAll", query = "SELECT u FROM UserTypes u"),
    @NamedQuery(name = "UserTypes.findByUserTypeId", query = "SELECT u FROM UserTypes u WHERE u.userTypeId = :userTypeId"),
    @NamedQuery(name = "UserTypes.findByName", query = "SELECT u FROM UserTypes u WHERE u.name = :name"),
    @NamedQuery(name = "UserTypes.findByDescription", query = "SELECT u FROM UserTypes u WHERE u.description = :description"),
    @NamedQuery(name = "UserTypes.findByDateCreated", query = "SELECT u FROM UserTypes u WHERE u.dateCreated = :dateCreated"),
    @NamedQuery(name = "UserTypes.findByDeleted", query = "SELECT u FROM UserTypes u WHERE u.deleted = :deleted")})
public class UserTypes implements Serializable,Identified<Integer> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "UserTypeId")
    private Integer userTypeId;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userTypeId")
    private Collection<UsersHasUserTypes> usersHasUserTypesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userType")
    private Collection<PolicyHasUserTypes> policyHasUserTypesCollection;
    @JoinColumn(name = "UserCreated", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users userCreated;

    public UserTypes() {
    }

    public UserTypes(Integer userTypeId) {
        this.userTypeId = userTypeId;
    }

    public UserTypes(Integer userTypeId, String name, Date dateCreated, boolean deleted) {
        this.userTypeId = userTypeId;
        this.name = name;
        this.dateCreated = dateCreated;
        this.deleted = deleted;
    }

    public Integer getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Integer userTypeId) {
        this.userTypeId = userTypeId;
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
    public Collection<UsersHasUserTypes> getUsersHasUserTypesCollection() {
        return usersHasUserTypesCollection;
    }

    public void setUsersHasUserTypesCollection(Collection<UsersHasUserTypes> usersHasUserTypesCollection) {
        this.usersHasUserTypesCollection = usersHasUserTypesCollection;
    }

    @XmlTransient
    public Collection<PolicyHasUserTypes> getPolicyHasUserTypesCollection() {
        return policyHasUserTypesCollection;
    }

    public void setPolicyHasUserTypesCollection(Collection<PolicyHasUserTypes> policyHasUserTypesCollection) {
        this.policyHasUserTypesCollection = policyHasUserTypesCollection;
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
				+ ((userTypeId == null) ? 0 : userTypeId.hashCode());
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
		UserTypes other = (UserTypes) obj;
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
		if (userTypeId == null) {
			if (other.userTypeId != null)
				return false;
		} else if (!userTypeId.equals(other.userTypeId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserTypes [userTypeId=" + userTypeId + ", name=" + name
				+ ", description=" + description + ", dateCreated="
				+ dateCreated + ", deleted=" + deleted + ", userCreated="
				+ userCreated + "]";
	}

    @Override
    public Integer getId() {
        return getUserTypeId();
    }

    @Override
    public void setId(Integer id) {
        setUserTypeId(id);
    }


    
}
