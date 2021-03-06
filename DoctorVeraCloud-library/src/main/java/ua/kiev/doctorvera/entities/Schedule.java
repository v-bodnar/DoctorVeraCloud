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

/**
 * Entity class Describes User
 * @author Volodymyr Bodnar
 */
@Entity
@Table(name = "Schedule")
@XmlRootElement
public class Schedule implements Serializable,Identified<Integer> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ScheduleId")
    private Integer scheduleId;
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
    @JoinColumn(name = "Method", referencedColumnName = "MethodId")
    @ManyToOne(optional = false)
    private Methods method;
    @JoinColumn(name = "Room", referencedColumnName = "RoomId")
    @ManyToOne(optional = false)
    private Rooms room;
    @JoinColumn(name = "Patient", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users patient;
    @JoinColumn(name = "Assistant", referencedColumnName = "UserId")
    @ManyToOne
    private Users assistant;
    @JoinColumn(name = "Doctor", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users doctor;
    @JoinColumn(name = "DoctorDirected", referencedColumnName = "UserId")
    @ManyToOne
    private Users doctorDirected;
    @JoinColumn(name = "UserCreated", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users userCreated;
    @JoinColumn(name = "ParentSchedule", referencedColumnName = "ScheduleId")
    @ManyToOne
    private Schedule parentSchedule;

    @OneToMany(mappedBy="schedule")
    private Collection<Payments> payments;

    public Schedule() {
    }

    public Schedule(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Schedule(Integer scheduleId, Date dateTimeStart, Date dateTimeEnd, Date dateCreated, boolean deleted) {
        this.scheduleId = scheduleId;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
        this.dateCreated = dateCreated;
        this.deleted = deleted;
    }

    public Schedule(Schedule schedule, Methods method, Date startTime, Users patient, Rooms room, Users authorizedUser ){
        this.doctor = schedule.getDoctor();
        this.patient = patient;
        this.assistant = schedule.getAssistant();
        this.doctorDirected = schedule.getDoctorDirected();
        this.room = room;
        this.method = method;
        this.dateTimeStart = startTime;
        this.dateTimeEnd = new Date(startTime.getTime() + (method.getTimeInMinutes() * 60L * 1000L));
        this.dateCreated = new Date();
        this.userCreated = authorizedUser;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
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

    public Schedule getParentSchedule() {
        return parentSchedule;
    }

    public void setParentSchedule(Schedule parentSchedule) {
        this.parentSchedule = parentSchedule;
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

    public Methods getMethod() {
        return method;
    }

    public void setMethod(Methods method) {
        this.method = method;
    }

    public Rooms getRoom() {
        return room;
    }

    public void setRoom(Rooms room) {
        this.room = room;
    }

    public Users getPatient() {
        return patient;
    }

    public void setPatient(Users patient) {
        this.patient = patient;
    }

    public Users getAssistant() {
        return assistant;
    }

    public void setAssistant(Users assistent) {
        this.assistant = assistent;
    }

    public Users getDoctor() {
        return doctor;
    }

    public void setDoctor(Users doctor) {
        this.doctor = doctor;
    }

    public Users getDoctorDirected() {
        return doctorDirected;
    }

    public Collection<Payments> getPayments() {
        return payments;
    }

    public void setPayments(Collection<Payments> payments) {
        this.payments = payments;
    }

    public void setDoctorDirected(Users doctorDirected) {
        this.doctorDirected = doctorDirected;
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
				+ ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result
				+ ((dateTimeEnd == null) ? 0 : dateTimeEnd.hashCode());
		result = prime * result
				+ ((dateTimeStart == null) ? 0 : dateTimeStart.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((doctor == null) ? 0 : doctor.hashCode());
		result = prime * result
				+ ((doctorDirected == null) ? 0 : doctorDirected.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((patient == null) ? 0 : patient.hashCode());
		result = prime * result + ((room == null) ? 0 : room.hashCode());
		result = prime * result
				+ ((scheduleId == null) ? 0 : scheduleId.hashCode());
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
		Schedule other = (Schedule) obj;
		if (assistant == null) {
			if (other.assistant != null)
				return false;
		} else if (!assistant.equals(other.assistant))
			return false;
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
		if (doctorDirected == null) {
			if (other.doctorDirected != null)
				return false;
		} else if (!doctorDirected.equals(other.doctorDirected))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		if (patient == null) {
			if (other.patient != null)
				return false;
		} else if (!patient.equals(other.patient))
			return false;
		if (room == null) {
			if (other.room != null)
				return false;
		} else if (!room.equals(other.room))
			return false;
		if (scheduleId == null) {
			if (other.scheduleId != null)
				return false;
		} else if (!scheduleId.equals(other.scheduleId))
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
		return "Schedule [scheduleId=" + scheduleId + ", dateTimeStart="
				+ dateTimeStart + ", dateTimeEnd=" + dateTimeEnd
				+ ", description=" + description + ", dateCreated="
				+ dateCreated + ", deleted=" + deleted + ", method=" + method
				+ ", room=" + room + ", patient=" + patient + ", assistent="
				+ assistant + ", doctor=" + doctor + ", doctorDirected="
				+ doctorDirected + ", userCreated=" + userCreated + "]";
	}

    @Override
    public Integer getId() {
        return getScheduleId();
    }

    @Override
    public void setId(Integer id) {
        setScheduleId(id);
    }

    
    
}
