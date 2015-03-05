package ua.kiev.doctorvera.utils;

import java.util.Date;

import javax.faces.bean.ManagedProperty;

import ua.kiev.doctorvera.entities.Methods;
import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.entities.Schedule;
import ua.kiev.doctorvera.entities.Users;

public class ScheduleBuilder{
	
	private Schedule schedule;
	
	public ScheduleBuilder(Users authorizedUser){
		schedule = new Schedule();
		schedule.setDateCreated(new Date());
		schedule.setUserCreated(authorizedUser);
	}
	
	public ScheduleBuilder setDoctor(Users doctor){
		schedule.setDoctor(doctor);
		return this;
	}
	public ScheduleBuilder setPatient(Users patient){
		schedule.setPatient(patient);
		return this;
	}
	public ScheduleBuilder setAssistent(Users assistent){
		schedule.setAssistent(assistent);
		return this;
	}
	public ScheduleBuilder setDoctorDirected(Users doctorDirected){
		schedule.setDoctorDirected(doctorDirected);
		return this;
	}
	public ScheduleBuilder setRoom(Rooms room){
		schedule.setRoom(room);
		return this;
	}
	public ScheduleBuilder setMethod(Methods method){
		schedule.setMethod(method);
		return this;
	}
	public ScheduleBuilder setDateTimeStart(Date startTime){
		schedule.setDateTimeStart(startTime);
		return this;
	}
	public ScheduleBuilder setDateTimeEnd(Date endTime){
		schedule.setDateTimeEnd(endTime);
		return this;
	}
	
	public Schedule build(){
		return schedule;
	}

}