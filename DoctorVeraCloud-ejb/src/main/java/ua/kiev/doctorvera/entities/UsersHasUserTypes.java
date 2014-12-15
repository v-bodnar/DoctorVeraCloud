/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Bodun
 */
@Entity
@Table(name = "UsersHasUserTypes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsersHasUserTypes.findAll", query = "SELECT u FROM UsersHasUserTypes u")
    //@NamedQuery(name = "UsersHasUserTypes.findByUsersHasUserTypes", query = "SELECT u FROM UsersHasUserTypes u WHERE u.usersHasUserTypes = :usersHasUserTypes")
    })
public class UsersHasUserTypes implements Serializable,Identified<Integer> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "UsersHasUserTypes")
    private Integer usersHasUserTypes;
    @JoinColumn(name = "UserId", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users userId;
    @JoinColumn(name = "UserTypeId", referencedColumnName = "UserTypeId")
    @ManyToOne(optional = false)
    private UserTypes userTypeId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DateCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @JoinColumn(name = "UserCreated", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users userCreated;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Deleted")
    private boolean deleted;

    public UsersHasUserTypes() {
    }

    public UsersHasUserTypes(Integer usersHasUserTypes) {
        this.usersHasUserTypes = usersHasUserTypes;
    }

    public Integer getUsersHasUserTypesId() {
        return usersHasUserTypes;
    }

    public void setUsersHasUserTypesId(Integer usersHasUserTypes) {
        this.usersHasUserTypes = usersHasUserTypes;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    public UserTypes getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(UserTypes userTypeId) {
        this.userTypeId = userTypeId;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result
				+ ((userTypeId == null) ? 0 : userTypeId.hashCode());
		result = prime
				* result
				+ ((usersHasUserTypes == null) ? 0 : usersHasUserTypes
						.hashCode());
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
		UsersHasUserTypes other = (UsersHasUserTypes) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (userTypeId == null) {
			if (other.userTypeId != null)
				return false;
		} else if (!userTypeId.equals(other.userTypeId))
			return false;
		if (usersHasUserTypes == null) {
			if (other.usersHasUserTypes != null)
				return false;
		} else if (!usersHasUserTypes.equals(other.usersHasUserTypes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UsersHasUserTypes [usersHasUserTypes=" + usersHasUserTypes
				+ ", userId=" + userId + ", userTypeId=" + userTypeId + "]";
	}

    @Override
    public Integer getId() {
       return getUsersHasUserTypesId();
    }

    @Override
    public void setId(Integer id) {
        setUsersHasUserTypesId(id);
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
    public Date getDateCreated() {
        return dateCreated;
    }
    @Override
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }   
}
