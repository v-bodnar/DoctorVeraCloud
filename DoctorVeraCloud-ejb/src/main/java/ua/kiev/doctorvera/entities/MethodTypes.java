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
 *
 * @author Bodun
 */
@Entity
@Table(name = "MethodTypes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MethodTypes.findAll", query = "SELECT m FROM MethodTypes m"),
    @NamedQuery(name = "MethodTypes.findByMethodTypeId", query = "SELECT m FROM MethodTypes m WHERE m.methodTypeId = :methodTypeId"),
    @NamedQuery(name = "MethodTypes.findByShortName", query = "SELECT m FROM MethodTypes m WHERE m.shortName = :shortName"),
    @NamedQuery(name = "MethodTypes.findByFullName", query = "SELECT m FROM MethodTypes m WHERE m.fullName = :fullName"),
    @NamedQuery(name = "MethodTypes.findByDateCreated", query = "SELECT m FROM MethodTypes m WHERE m.dateCreated = :dateCreated"),
    @NamedQuery(name = "MethodTypes.findByDeleted", query = "SELECT m FROM MethodTypes m WHERE m.deleted = :deleted")})
public class MethodTypes implements Serializable,Identified<Integer> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "MethodTypeId")
    private Integer methodTypeId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "ShortName")
    private String shortName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FullName")
    private String fullName;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "methodType")
    private Collection<Methods> methodsCollection;

    public MethodTypes() {
    }

    public MethodTypes(Integer methodTypeId) {
        this.methodTypeId = methodTypeId;
    }

    public MethodTypes(Integer methodTypeId, String shortName, String fullName, Date dateCreated, boolean deleted) {
        this.methodTypeId = methodTypeId;
        this.shortName = shortName;
        this.fullName = fullName;
        this.dateCreated = dateCreated;
        this.deleted = deleted;
    }

    public Integer getMethodTypeId() {
        return methodTypeId;
    }

    public void setMethodTypeId(Integer methodTypeId) {
        this.methodTypeId = methodTypeId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    @XmlTransient
    public Collection<Methods> getMethodsCollection() {
        return methodsCollection;
    }

    public void setMethodsCollection(Collection<Methods> methodsCollection) {
        this.methodsCollection = methodsCollection;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result
				+ ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result
				+ ((methodTypeId == null) ? 0 : methodTypeId.hashCode());
		result = prime * result
				+ ((shortName == null) ? 0 : shortName.hashCode());
		result = prime * result
				+ ((userCreated == null) ? 0 : userCreated.hashCode());
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
		MethodTypes other = (MethodTypes) obj;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (deleted != other.deleted)
			return false;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (methodTypeId == null) {
			if (other.methodTypeId != null)
				return false;
		} else if (!methodTypeId.equals(other.methodTypeId))
			return false;
		if (shortName == null) {
			if (other.shortName != null)
				return false;
		} else if (!shortName.equals(other.shortName))
			return false;
		if (userCreated == null) {
			if (other.userCreated != null)
				return false;
		} else if (!userCreated.equals(other.userCreated))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MethodTypes [methodTypeId=" + methodTypeId + ", shortName="
				+ shortName + ", fullName=" + fullName + ", dateCreated="
				+ dateCreated + ", deleted=" + deleted + ", userCreated="
				+ userCreated + "]";
	}

    @Override
    public Integer getId() {
        return getMethodTypeId();
    }

    @Override
    public void setId(Integer id) {
        setMethodTypeId(id);
    }
}
