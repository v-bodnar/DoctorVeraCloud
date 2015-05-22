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
@Table(name = "Plan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Plan.findAll", query = "SELECT p FROM Plan p"),
    @NamedQuery(name = "Plan.findByPlanId", query = "SELECT p FROM Plan p WHERE p.planId = :planId"),
    @NamedQuery(name = "Plan.findByDateTimeStart", query = "SELECT p FROM Plan p WHERE p.dateTimeStart = :dateTimeStart"),
    @NamedQuery(name = "Plan.findByDateTimeEnd", query = "SELECT p FROM Plan p WHERE p.dateTimeEnd = :dateTimeEnd"),
    @NamedQuery(name = "Plan.findByDescription", query = "SELECT p FROM Plan p WHERE p.description = :description"),
    @NamedQuery(name = "Plan.findByDateCreated", query = "SELECT p FROM Plan p WHERE p.dateCreated = :dateCreated"),
    @NamedQuery(name = "Plan.findByDeleted", query = "SELECT p FROM Plan p WHERE p.deleted = :deleted")})
public class Plan implements Serializable,Identified<Integer> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PlanId")
    private Integer planId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DateTimeStart")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeStart;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DateTimeEnd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeEnd;
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
    @JoinColumn(name = "Room", referencedColumnName = "RoomId")
    @ManyToOne(optional = false)
    private Rooms room;
    @JoinColumn(name = "Doctor", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users doctor;
    @JoinColumn(name = "UserCreated", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users userCreated;

    public Plan() {
    }

    public Plan(Integer planId) {
        this.planId = planId;
    }

    public Plan(Integer planId, Date dateTimeStart, Date dateTimeEnd, Date dateCreated, boolean deleted) {
        this.planId = planId;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
        this.dateCreated = dateCreated;
        this.deleted = deleted;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Date getDateTimeStart() {
        return dateTimeStart;
    }

    public void setDateTimeStart(Date dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public Date getDateTimeEnd() {
        return dateTimeEnd;
    }

    public void setDateTimeEnd(Date dateTimeEnd) {
        this.dateTimeEnd = dateTimeEnd;
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

    public Rooms getRoom() {
        return room;
    }

    public void setRoom(Rooms room) {
        this.room = room;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result
				+ ((dateTimeEnd == null) ? 0 : dateTimeEnd.hashCode());
		result = prime * result
				+ ((dateTimeStart == null) ? 0 : dateTimeStart.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((doctor == null) ? 0 : doctor.hashCode());
		result = prime * result + ((planId == null) ? 0 : planId.hashCode());
		result = prime * result + ((room == null) ? 0 : room.hashCode());
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
		Plan other = (Plan) obj;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (dateTimeEnd == null) {
			if (other.dateTimeEnd != null)
				return false;
		} else if (!dateTimeEnd.equals(other.dateTimeEnd))
			return false;
		if (dateTimeStart == null) {
			if (other.dateTimeStart != null)
				return false;
		} else if (!dateTimeStart.equals(other.dateTimeStart))
			return false;
		if (deleted != other.deleted)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (doctor == null) {
			if (other.doctor != null)
				return false;
		} else if (!doctor.equals(other.doctor))
			return false;
		if (planId == null) {
			if (other.planId != null)
				return false;
		} else if (!planId.equals(other.planId))
			return false;
		if (room == null) {
			if (other.room != null)
				return false;
		} else if (!room.equals(other.room))
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
		return "Plan [planId=" + planId + ", dateTimeStart=" + dateTimeStart
				+ ", dateTimeEnd=" + dateTimeEnd + ", description="
				+ description + ", dateCreated=" + dateCreated + ", deleted="
				+ deleted + ", room=" + room + ", doctor=" + doctor
				+ ", userCreated=" + userCreated + "]";
	}

    @Override
    public Integer getId() {
        return getPlanId();
    }

    @Override
    public void setId(Integer id) {
        setPlanId(id);
    }
}
