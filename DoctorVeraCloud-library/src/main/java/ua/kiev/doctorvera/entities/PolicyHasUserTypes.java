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
 * Entity class Describes User
 * @author Volodymyr Bodnar
 */
@Entity
@Table(name = "PolicyHasUserTypes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PolicyHasUserTypes.findAll", query = "SELECT p FROM PolicyHasUserTypes p"),
    @NamedQuery(name = "PolicyHasUserTypes.findByPolicyHasUserTypesId", query = "SELECT p FROM PolicyHasUserTypes p WHERE p.policyHasUserTypesId = :policyHasUserTypesId"),
    @NamedQuery(name = "PolicyHasUserTypes.findByDateCreated", query = "SELECT p FROM PolicyHasUserTypes p WHERE p.dateCreated = :dateCreated"),
    @NamedQuery(name = "PolicyHasUserTypes.findByDeleted", query = "SELECT p FROM PolicyHasUserTypes p WHERE p.deleted = :deleted")})
public class PolicyHasUserTypes implements Serializable,Identified<Integer> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PolicyHasUserTypesId")
    private Integer policyHasUserTypesId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DateCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Deleted")
    private boolean deleted;
    @JoinColumn(name = "UserType", referencedColumnName = "UserTypeId")
    @ManyToOne(optional = false)
    private UserTypes userType;
    @JoinColumn(name = "Policy", referencedColumnName = "PolicyId")
    @ManyToOne(optional = false)
    private Policy policy;
    @JoinColumn(name = "UserCreated", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users userCreated;

    public PolicyHasUserTypes() {
    }

    public PolicyHasUserTypes(Integer policyHasUserTypesId) {
        this.policyHasUserTypesId = policyHasUserTypesId;
    }

    public PolicyHasUserTypes(Integer policyHasUserTypesId, Date dateCreated, boolean deleted) {
        this.policyHasUserTypesId = policyHasUserTypesId;
        this.dateCreated = dateCreated;
        this.deleted = deleted;
    }

    public Integer getPolicyHasUserTypesId() {
        return policyHasUserTypesId;
    }

    public void setPolicyHasUserTypesId(Integer policyHasUserTypesId) {
        this.policyHasUserTypesId = policyHasUserTypesId;
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

    public UserTypes getUserType() {
        return userType;
    }

    public void setUserType(UserTypes userType) {
        this.userType = userType;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
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
		result = prime * result + ((policy == null) ? 0 : policy.hashCode());
		result = prime
				* result
				+ ((policyHasUserTypesId == null) ? 0 : policyHasUserTypesId
						.hashCode());
		result = prime * result
				+ ((userCreated == null) ? 0 : userCreated.hashCode());
		result = prime * result
				+ ((userType == null) ? 0 : userType.hashCode());
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
		PolicyHasUserTypes other = (PolicyHasUserTypes) obj;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (deleted != other.deleted)
			return false;
		if (policy == null) {
			if (other.policy != null)
				return false;
		} else if (!policy.equals(other.policy))
			return false;
		if (policyHasUserTypesId == null) {
			if (other.policyHasUserTypesId != null)
				return false;
		} else if (!policyHasUserTypesId.equals(other.policyHasUserTypesId))
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
		return "PolicyHasUserTypes [policyHasUserTypesId="
				+ policyHasUserTypesId + ", dateCreated=" + dateCreated
				+ ", deleted=" + deleted + ", userType=" + userType
				+ ", policy=" + policy + ", userCreated=" + userCreated + "]";
	}

    @Override
    public Integer getId() {
        return getPolicyHasUserTypesId();
    }

    @Override
    public void setId(Integer id) {
        setPolicyHasUserTypesId(id);
    }

    
    
}
