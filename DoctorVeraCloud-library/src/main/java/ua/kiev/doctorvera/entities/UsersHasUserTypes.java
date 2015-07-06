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
@IdClass(value=UsersHasUserTypes.UsersHasUserTypesId.class)
@Table(name = "UsersHasUserTypes")
@XmlRootElement
public class UsersHasUserTypes implements Serializable,Identified<Integer> {
	private static final long serialVersionUID = -7352790399385415680L;
	@Id
    @JoinColumn(name = "User", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users user;
    @Id
    @JoinColumn(name = "UserType", referencedColumnName = "UserTypeId")
    @ManyToOne(optional = false)
    private UserTypes userType;
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

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public UserTypes getUserType() {
        return userType;
    }

    public void setUserType(UserTypes userType) {
        this.userType = userType;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + ((userCreated == null) ? 0 : userCreated.hashCode());
		result = prime * result + ((userType == null) ? 0 : userType.hashCode());
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
		if (userType == null) {
			if (other.userType != null)
				return false;
		} else if (!userType.equals(other.userType))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "UsersHasUserTypes [user=" + user + ", userType=" + userType + ", dateCreated=" + dateCreated + ", userCreated=" + userCreated + ", deleted="
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
    public static class UsersHasUserTypesId implements Serializable { 
    	private static final long serialVersionUID = 6772711638360754952L;
    	
        private Users user;
    	
        private UserTypes userType;
        
    	public Users getUser() {
    		return user;
    	}	
    	
    	public UserTypes getUserType() {
    		return userType;
    	}
    	
    	@Override
    	public int hashCode() {
    		final int prime = 31;
    		int result = 1;
    		result = prime * result + ((user == null) ? 0 : user.hashCode());
    		result = prime * result + ((userType == null) ? 0 : userType.hashCode());
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
    		UsersHasUserTypesId other = (UsersHasUserTypesId) obj;
    		if (user == null) {
    			if (other.user != null)
    				return false;
    		} else if (!user.equals(other.user))
    			return false;
    		if (userType == null) {
    			if (other.userType != null)
    				return false;
    		} else if (!userType.equals(other.userType))
    			return false;
    		return true;
    	}
    }
}


