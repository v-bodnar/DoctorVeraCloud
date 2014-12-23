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
@Table(name = "DoctorsHasMethod")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DoctorsHasMethod.findAll", query = "SELECT d FROM DoctorsHasMethod d"),
    @NamedQuery(name = "DoctorsHasMethod.findByDoctorsHasMethodId", query = "SELECT d FROM DoctorsHasMethod d WHERE d.doctorsHasMethodId = :doctorsHasMethodId"),
    @NamedQuery(name = "DoctorsHasMethod.findByDateCreated", query = "SELECT d FROM DoctorsHasMethod d WHERE d.dateCreated = :dateCreated"),
    @NamedQuery(name = "DoctorsHasMethod.findByDeleted", query = "SELECT d FROM DoctorsHasMethod d WHERE d.deleted = :deleted")})
public class DoctorsHasMethod implements Serializable,Identified<Integer> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DoctorsHasMethodId")
    private Integer doctorsHasMethodId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DateCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Deleted")
    private boolean deleted;
    @JoinColumn(name = "Doctor", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users doctor;
    @JoinColumn(name = "UserCreated", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users userCreated;
    @JoinColumn(name = "Method", referencedColumnName = "MethodId")
    @ManyToOne(optional = false)
    private Methods method;

    public DoctorsHasMethod() {
    }

    public DoctorsHasMethod(Integer doctorsHasMethodId) {
        this.doctorsHasMethodId = doctorsHasMethodId;
    }

    public DoctorsHasMethod(Integer doctorsHasMethodId, Date dateCreated, boolean deleted) {
        this.doctorsHasMethodId = doctorsHasMethodId;
        this.dateCreated = dateCreated;
        this.deleted = deleted;
    }

    public Integer getDoctorsHasMethodId() {
        return doctorsHasMethodId;
    }

    public void setDoctorsHasMethodId(Integer doctorsHasMethodId) {
        this.doctorsHasMethodId = doctorsHasMethodId;
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

    public Users getDoctor() {
        return doctor;
    }

    public void setDoctor(Users doctor) {
        this.doctor = doctor;
    }
    @Override
    public Users getUserCreated() {
        return userCreated;
    }
    @Override
    public void setUserCreated(Users userCreated) {
        this.userCreated = userCreated;
    }

    public Methods getMethod() {
        return method;
    }

    public void setMethod(Methods method) {
        this.method = method;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + ((doctor == null) ? 0 : doctor.hashCode());
		result = prime
				* result
				+ ((doctorsHasMethodId == null) ? 0 : doctorsHasMethodId
						.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
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
		DoctorsHasMethod other = (DoctorsHasMethod) obj;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (deleted != other.deleted)
			return false;
		if (doctor == null) {
			if (other.doctor != null)
				return false;
		} else if (!doctor.equals(other.doctor))
			return false;
		if (doctorsHasMethodId == null) {
			if (other.doctorsHasMethodId != null)
				return false;
		} else if (!doctorsHasMethodId.equals(other.doctorsHasMethodId))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
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
		return "DoctorsHasMethod [doctorsHasMethodId=" + doctorsHasMethodId
				+ ", dateCreated=" + dateCreated + ", deleted=" + deleted
				+ ", doctor=" + doctor + ", userCreated=" + userCreated
				+ ", method=" + method + "]";
	}

    @Override
    public Integer getId() {
        return getDoctorsHasMethodId();
    }

    @Override
    public void setId(Integer id) {
        setDoctorsHasMethodId(id);
    }
}
