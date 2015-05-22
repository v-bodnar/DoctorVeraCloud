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
@Table(name = "Share")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Share.findAll", query = "SELECT s FROM Share s"),
    @NamedQuery(name = "Share.findByShareId", query = "SELECT s FROM Share s WHERE s.shareId = :shareId"),
    @NamedQuery(name = "Share.findBySalaryDoctor", query = "SELECT s FROM Share s WHERE s.salaryDoctor = :salaryDoctor"),
    @NamedQuery(name = "Share.findBySalaryAssistant", query = "SELECT s FROM Share s WHERE s.salaryAssistant = :salaryAssistant"),
    @NamedQuery(name = "Share.findByPersentageDoctor", query = "SELECT s FROM Share s WHERE s.persentageDoctor = :persentageDoctor"),
    @NamedQuery(name = "Share.findByPercentageAssistant", query = "SELECT s FROM Share s WHERE s.percentageAssistant = :percentageAssistant"),
    @NamedQuery(name = "Share.findByDataTime", query = "SELECT s FROM Share s WHERE s.dataTime = :dataTime"),
    @NamedQuery(name = "Share.findByDateCreated", query = "SELECT s FROM Share s WHERE s.dateCreated = :dateCreated"),
    @NamedQuery(name = "Share.findByDeleted", query = "SELECT s FROM Share s WHERE s.deleted = :deleted")})
public class Share implements Serializable,Identified<Integer> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ShareId")
    private Integer shareId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "SalaryDoctor")
    private Float salaryDoctor;
    @Column(name = "SalaryAssistant")
    private Float salaryAssistant;
    @Column(name = "PersentageDoctor")
    private Float persentageDoctor;
    @Column(name = "PercentageAssistant")
    private Float percentageAssistant;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DataTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DateCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Deleted")
    private boolean deleted;
    @JoinColumn(name = "Method", referencedColumnName = "MethodId")
    @ManyToOne(optional = false)
    private Methods method;
    @JoinColumn(name = "Doctor", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users doctor;
    @JoinColumn(name = "Assistant", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users assistant;
    @JoinColumn(name = "UserCreated", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users userCreated;

    public Share() {
    }

    public Share(Integer shareId) {
        this.shareId = shareId;
    }

    public Share(Integer shareId, Date dataTime, Date dateCreated, boolean deleted) {
        this.shareId = shareId;
        this.dataTime = dataTime;
        this.dateCreated = dateCreated;
        this.deleted = deleted;
    }

    public Integer getShareId() {
        return shareId;
    }

    public void setShareId(Integer shareId) {
        this.shareId = shareId;
    }

    public Float getSalaryDoctor() {
        return salaryDoctor;
    }

    public void setSalaryDoctor(Float salaryDoctor) {
        this.salaryDoctor = salaryDoctor;
    }

    public Float getSalaryAssistant() {
        return salaryAssistant;
    }

    public void setSalaryAssistant(Float salaryAssistant) {
        this.salaryAssistant = salaryAssistant;
    }

    public Float getPersentageDoctor() {
        return persentageDoctor;
    }

    public void setPersentageDoctor(Float persentageDoctor) {
        this.persentageDoctor = persentageDoctor;
    }

    public Float getPercentageAssistant() {
        return percentageAssistant;
    }

    public void setPercentageAssistant(Float percentageAssistant) {
        this.percentageAssistant = percentageAssistant;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
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

    public Methods getMethod() {
        return method;
    }

    public void setMethod(Methods method) {
        this.method = method;
    }

    public Users getDoctor() {
        return doctor;
    }

    public void setDoctor(Users doctor) {
        this.doctor = doctor;
    }

    public Users getAssistant() {
        return assistant;
    }

    public void setAssistant(Users assistant) {
        this.assistant = assistant;
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
				+ ((assistant == null) ? 0 : assistant.hashCode());
		result = prime * result
				+ ((dataTime == null) ? 0 : dataTime.hashCode());
		result = prime * result
				+ ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + ((doctor == null) ? 0 : doctor.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime
				* result
				+ ((percentageAssistant == null) ? 0 : percentageAssistant
						.hashCode());
		result = prime
				* result
				+ ((persentageDoctor == null) ? 0 : persentageDoctor.hashCode());
		result = prime * result
				+ ((salaryAssistant == null) ? 0 : salaryAssistant.hashCode());
		result = prime * result
				+ ((salaryDoctor == null) ? 0 : salaryDoctor.hashCode());
		result = prime * result + ((shareId == null) ? 0 : shareId.hashCode());
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
		Share other = (Share) obj;
		if (assistant == null) {
			if (other.assistant != null)
				return false;
		} else if (!assistant.equals(other.assistant))
			return false;
		if (dataTime == null) {
			if (other.dataTime != null)
				return false;
		} else if (!dataTime.equals(other.dataTime))
			return false;
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
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		if (percentageAssistant == null) {
			if (other.percentageAssistant != null)
				return false;
		} else if (!percentageAssistant.equals(other.percentageAssistant))
			return false;
		if (persentageDoctor == null) {
			if (other.persentageDoctor != null)
				return false;
		} else if (!persentageDoctor.equals(other.persentageDoctor))
			return false;
		if (salaryAssistant == null) {
			if (other.salaryAssistant != null)
				return false;
		} else if (!salaryAssistant.equals(other.salaryAssistant))
			return false;
		if (salaryDoctor == null) {
			if (other.salaryDoctor != null)
				return false;
		} else if (!salaryDoctor.equals(other.salaryDoctor))
			return false;
		if (shareId == null) {
			if (other.shareId != null)
				return false;
		} else if (!shareId.equals(other.shareId))
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
		return "Share [shareId=" + shareId + ", salaryDoctor=" + salaryDoctor
				+ ", salaryAssistant=" + salaryAssistant
				+ ", persentageDoctor=" + persentageDoctor
				+ ", percentageAssistant=" + percentageAssistant
				+ ", dataTime=" + dataTime + ", dateCreated=" + dateCreated
				+ ", deleted=" + deleted + ", method=" + method + ", doctor="
				+ doctor + ", assistant=" + assistant + ", userCreated="
				+ userCreated + "]";
	}

    @Override
    public Integer getId() {
        return getShareId();
    }

    @Override
    public void setId(Integer id) {
        setShareId(id);
    }


    
}
