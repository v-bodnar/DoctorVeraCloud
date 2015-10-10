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
@Table(name = "PolicyHasUserGroups")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PolicyHasUserTypes.findAll", query = "SELECT p FROM PolicyHasUserGroups p"),
    @NamedQuery(name = "PolicyHasUserTypes.findByPolicyHasUserTypesId", query = "SELECT p FROM PolicyHasUserGroups p WHERE p.policyHasUserGroupId = :policyHasUserGroupId"),
    @NamedQuery(name = "PolicyHasUserTypes.findByDateCreated", query = "SELECT p FROM PolicyHasUserGroups p WHERE p.dateCreated = :dateCreated"),
    @NamedQuery(name = "PolicyHasUserTypes.findByDeleted", query = "SELECT p FROM PolicyHasUserGroups p WHERE p.deleted = :deleted")})
public class PolicyHasUserGroups implements Serializable,Identified<Integer> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PolicyHasUserGroupId")
    private Integer policyHasUserGroupId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DateCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Deleted")
    private boolean deleted;
    @JoinColumn(name = "UserGroup", referencedColumnName = "UserGroupId")
    @ManyToOne(optional = false)
    private UserGroups userGroup;
    @JoinColumn(name = "Policy", referencedColumnName = "PolicyId")
    @ManyToOne(optional = false)
    private Policy policy;
    @JoinColumn(name = "UserCreated", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users userCreated;

    public PolicyHasUserGroups() {
    }

    public PolicyHasUserGroups(Integer policyHasUserGroupId) {
        this.policyHasUserGroupId = policyHasUserGroupId;
    }

    public PolicyHasUserGroups(Integer policyHasUserGroupId, Date dateCreated, boolean deleted) {
        this.policyHasUserGroupId = policyHasUserGroupId;
        this.dateCreated = dateCreated;
        this.deleted = deleted;
    }

    public Integer getPolicyHasUserGroupId() {
        return policyHasUserGroupId;
    }

    public void setPolicyHasUserGroupId(Integer policyHasUserTypesId) {
        this.policyHasUserGroupId = policyHasUserTypesId;
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

    public UserGroups getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroups userType) {
        this.userGroup = userType;
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
				+ ((policyHasUserGroupId == null) ? 0 : policyHasUserGroupId
						.hashCode());
		result = prime * result
				+ ((userCreated == null) ? 0 : userCreated.hashCode());
		result = prime * result
				+ ((userGroup == null) ? 0 : userGroup.hashCode());
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
		PolicyHasUserGroups other = (PolicyHasUserGroups) obj;
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
		if (policyHasUserGroupId == null) {
			if (other.policyHasUserGroupId != null)
				return false;
		} else if (!policyHasUserGroupId.equals(other.policyHasUserGroupId))
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
		return "PolicyHasUserGroups [policyHasUserTypesId="
				+ policyHasUserGroupId + ", dateCreated=" + dateCreated
				+ ", deleted=" + deleted + ", userType=" + userGroup
				+ ", policy=" + policy + ", userCreated=" + userCreated + "]";
	}

    @Override
    public Integer getId() {
        return getPolicyHasUserGroupId();
    }

    @Override
    public void setId(Integer id) {
        setPolicyHasUserGroupId(id);
    }

    
    
}
