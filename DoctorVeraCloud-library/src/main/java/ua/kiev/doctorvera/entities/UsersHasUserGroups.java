package ua.kiev.doctorvera.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity class Describes User
 * @author Volodymyr Bodnar
 */
@Entity
@IdClass(value=UsersHasUserGroups.UsersHasUserGroupsId.class)
@Table(name = "UsersHasUserGroups")
@XmlRootElement
public class UsersHasUserGroups implements Serializable,Identified<Integer> {
	private static final long serialVersionUID = -7352790399385415680L;
	@Id
    @JoinColumn(name = "User", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users user;
    @Id
    @JoinColumn(name = "UserGroup", referencedColumnName = "UserGroupId")
    @ManyToOne(optional = false)
    private UserGroups userGroup;
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

    public UsersHasUserGroups() {
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public UserGroups getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroups userType) {
        this.userGroup = userType;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + ((userCreated == null) ? 0 : userCreated.hashCode());
		result = prime * result + ((userGroup == null) ? 0 : userGroup.hashCode());
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
		UsersHasUserGroups other = (UsersHasUserGroups) obj;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (deleted != other.deleted)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (userCreated == null) {
			if (other.userCreated != null)
				return false;
		} else if (!userCreated.equals(other.userCreated))
			return false;
		if (userGroup == null) {
			if (other.userGroup != null)
				return false;
		} else if (!userGroup.equals(other.userGroup))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "UsersHasUserGroups [user=" + user + ", userGroup=" + userGroup + ", dateCreated=" + dateCreated + ", userCreated=" + userCreated + ", deleted="
				+ deleted + "]";
	}

	@Override
	public Integer getId() {
		return null;
	}

	@Override
	public void setId(Integer id) {
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
    
    @Embeddable
    public static class UsersHasUserGroupsId implements Serializable {
    	private static final long serialVersionUID = 6772711638360754952L;
    	
        private Users user;
    	
        private UserGroups userGroup;
        
    	public Users getUser() {
    		return user;
    	}	
    	
    	public UserGroups getUserGroup() {
    		return userGroup;
    	}
    	
    	@Override
    	public int hashCode() {
    		final int prime = 31;
    		int result = 1;
    		result = prime * result + ((user == null) ? 0 : user.hashCode());
    		result = prime * result + ((userGroup == null) ? 0 : userGroup.hashCode());
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
    		UsersHasUserGroupsId other = (UsersHasUserGroupsId) obj;
    		if (user == null) {
    			if (other.user != null)
    				return false;
    		} else if (!user.equals(other.user))
    			return false;
    		if (userGroup == null) {
    			if (other.userGroup != null)
    				return false;
    		} else if (!userGroup.equals(other.userGroup))
    			return false;
    		return true;
    	}
    }
}


