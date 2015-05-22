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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity class Describes User
 * @author Volodymyr Bodnar
 */
@Entity
@Table(name = "Methods")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Methods.findAll", query = "SELECT m FROM Methods m"),
    @NamedQuery(name = "Methods.findByMethodId", query = "SELECT m FROM Methods m WHERE m.methodId = :methodId"),
    @NamedQuery(name = "Methods.findByShortName", query = "SELECT m FROM Methods m WHERE m.shortName = :shortName"),
    @NamedQuery(name = "Methods.findByFullName", query = "SELECT m FROM Methods m WHERE m.fullName = :fullName"),
    @NamedQuery(name = "Methods.findByShortDescription", query = "SELECT m FROM Methods m WHERE m.shortDescription = :shortDescription"),
    @NamedQuery(name = "Methods.findByFullDescription", query = "SELECT m FROM Methods m WHERE m.fullDescription = :fullDescription"),
    @NamedQuery(name = "Methods.findByTimeInMinutes", query = "SELECT m FROM Methods m WHERE m.timeInMinutes = :timeInMinutes"),
    @NamedQuery(name = "Methods.findByDateCreated", query = "SELECT m FROM Methods m WHERE m.dateCreated = :dateCreated"),
    @NamedQuery(name = "Methods.findByDeleted", query = "SELECT m FROM Methods m WHERE m.deleted = :deleted")})
public class Methods implements Serializable,Identified<Integer> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "MethodId")
    private Integer methodId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ShortName")
    private String shortName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FullName")
    private String fullName;
    @Size(max = 100)
    @Column(name = "ShortDescription")
    private String shortDescription;
    @Size(max = 300)
    @Column(name = "FullDescription")
    private String fullDescription;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TimeInMinutes")
    private int timeInMinutes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DateCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Deleted")
    private boolean deleted;
    /*
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "method")
    private Collection<DoctorsHasMethod> doctorsHasMethodCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "method")
    private Collection<Prices> pricesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "method")
    private Collection<Schedule> scheduleCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "method")
    private Collection<Share> shareCollection;
    */
    @JoinColumn(name = "MethodType", referencedColumnName = "MethodTypeId")
    @ManyToOne(optional = false)
    private MethodTypes methodType;
    @JoinColumn(name = "UserCreated", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users userCreated;

    public Methods() {
    }

    public Methods(Integer methodId) {
        this.methodId = methodId;
    }

    public Methods(Integer methodId, String shortName, String fullName, int timeInMinutes, Date dateCreated, boolean deleted) {
        this.methodId = methodId;
        this.shortName = shortName;
        this.fullName = fullName;
        this.timeInMinutes = timeInMinutes;
        this.dateCreated = dateCreated;
        this.deleted = deleted;
    }

    public Integer getMethodId() {
        return methodId;
    }

    public void setMethodId(Integer methodId) {
        this.methodId = methodId;
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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public int getTimeInMinutes() {
        return timeInMinutes;
    }

    public void setTimeInMinutes(int timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
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
/*
    @XmlTransient
    public Collection<DoctorsHasMethod> getDoctorsHasMethodCollection() {
        return doctorsHasMethodCollection;
    }

    public void setDoctorsHasMethodCollection(Collection<DoctorsHasMethod> doctorsHasMethodCollection) {
        this.doctorsHasMethodCollection = doctorsHasMethodCollection;
    }

    @XmlTransient
    public Collection<Prices> getPricesCollection() {
        return pricesCollection;
    }

    public void setPricesCollection(Collection<Prices> pricesCollection) {
        this.pricesCollection = pricesCollection;
    }

    @XmlTransient
    public Collection<Schedule> getScheduleCollection() {
        return scheduleCollection;
    }

    public void setScheduleCollection(Collection<Schedule> scheduleCollection) {
        this.scheduleCollection = scheduleCollection;
    }

    @XmlTransient
    public Collection<Share> getShareCollection() {
        return shareCollection;
    }

    public void setShareCollection(Collection<Share> shareCollection) {
        this.shareCollection = shareCollection;
    }
*/
    public MethodTypes getMethodType() {
        return methodType;
    }

    public void setMethodType(MethodTypes methodType) {
        this.methodType = methodType;
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
				+ ((fullDescription == null) ? 0 : fullDescription.hashCode());
		result = prime * result
				+ ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result
				+ ((methodId == null) ? 0 : methodId.hashCode());
		result = prime * result
				+ ((methodType == null) ? 0 : methodType.hashCode());
		result = prime
				* result
				+ ((shortDescription == null) ? 0 : shortDescription.hashCode());
		result = prime * result
				+ ((shortName == null) ? 0 : shortName.hashCode());
		result = prime * result + timeInMinutes;
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
		Methods other = (Methods) obj;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (deleted != other.deleted)
			return false;
		if (fullDescription == null) {
			if (other.fullDescription != null)
				return false;
		} else if (!fullDescription.equals(other.fullDescription))
			return false;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (methodId == null) {
			if (other.methodId != null)
				return false;
		} else if (!methodId.equals(other.methodId))
			return false;
		if (methodType == null) {
			if (other.methodType != null)
				return false;
		} else if (!methodType.equals(other.methodType))
			return false;
		if (shortDescription == null) {
			if (other.shortDescription != null)
				return false;
		} else if (!shortDescription.equals(other.shortDescription))
			return false;
		if (shortName == null) {
			if (other.shortName != null)
				return false;
		} else if (!shortName.equals(other.shortName))
			return false;
		if (timeInMinutes != other.timeInMinutes)
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
		return "Methods [methodId=" + methodId + ", shortName=" + shortName
				+ ", fullName=" + fullName + ", shortDescription="
				+ shortDescription + ", fullDescription=" + fullDescription
				+ ", timeInMinutes=" + timeInMinutes + ", dateCreated="
				+ dateCreated + ", deleted=" + deleted + ", methodType="
				+ methodType + ", userCreated=" + userCreated + "]";
	}

    @Override
    public Integer getId() {
        return getMethodId();
    }

    @Override
    public void setId(Integer id) {
        setMethodId(id);
    }
}
