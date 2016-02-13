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
@Table(name = "Policy")
@XmlRootElement
public class Policy implements Serializable,Identified<Integer> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PolicyId")
    private Integer policyId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "StringId")
    private String stringId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PolicyGroup")
    private String policyGroup;
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
    @JoinColumn(name = "UserCreated", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users userCreated;

    @OrderColumn(name="UserGroup")
    @ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(
            name="PolicyHasUserGroups",
            joinColumns={@JoinColumn(name="Policy", referencedColumnName="PolicyId")},
            inverseJoinColumns={@JoinColumn(name="UserGroup", referencedColumnName="UserGroupId")})
    private Collection<UserGroups> userGroups;

    public Policy() {
    }

    public Policy(Integer policyId) {
        this.policyId = policyId;
    }

    public Policy(Integer policyId, String name, Date dateCreated, boolean deleted) {
        this.policyId = policyId;
        this.dateCreated = dateCreated;
        this.deleted = deleted;
    }

    public Integer getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Integer policyId) {
        this.policyId = policyId;
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
    @Override
    public Users getUserCreated() {
        return userCreated;
    }
    @Override
    public void setUserCreated(Users userCreated) {
        this.userCreated = userCreated;
    }

    public String getPolicyGroup() {
        return policyGroup;
    }

    public void setPolicyGroup(String policyGroup) {
        this.policyGroup = policyGroup;
    }

    public String getStringId() {
        return stringId;
    }

    public void setStringId(String stringId) {
        this.stringId = stringId;
    }

    @XmlTransient
    public Collection<UserGroups> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(Collection<UserGroups> userGroups) {
        this.userGroups = userGroups;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Policy policy = (Policy) o;

        if (deleted != policy.deleted) return false;
        if (policyId != null ? !policyId.equals(policy.policyId) : policy.policyId != null) return false;
        if (!stringId.equals(policy.stringId)) return false;
        if (!policyGroup.equals(policy.policyGroup)) return false;
        if (description != null ? !description.equals(policy.description) : policy.description != null) return false;
        return dateCreated != null ? dateCreated.equals(policy.dateCreated) : policy.dateCreated == null && !(userCreated != null ? !userCreated.equals(policy.userCreated) : policy.userCreated != null);

    }

    @Override
    public int hashCode() {
        int result = policyId != null ? policyId.hashCode() : 0;
        result = 31 * result + stringId.hashCode();
        result = 31 * result + policyGroup.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (dateCreated != null ? dateCreated.hashCode() : 0);
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + (userCreated != null ? userCreated.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Policy{" +
                "dateCreated=" + dateCreated +
                ", policyId=" + policyId +
                ", stringId='" + stringId + '\'' +
                ", policyGroup='" + policyGroup + '\'' +
                ", description='" + description + '\'' +
                ", deleted=" + deleted +
                ", userCreated=" + userCreated +
                '}';
    }

    @Override
    public Integer getId() {
        return getPolicyId();
    }

    @Override
    public void setId(Integer id) {
        setPolicyId(id);
    }
}
