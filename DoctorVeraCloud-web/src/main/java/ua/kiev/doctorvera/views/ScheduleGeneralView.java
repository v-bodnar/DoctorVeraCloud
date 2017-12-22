package ua.kiev.doctorvera.views;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleModel;
import ua.kiev.doctorvera.entities.Plan;
import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.entities.Schedule;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.PlanFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.ScheduleFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.resources.Config;
import ua.kiev.doctorvera.resources.Mapping;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

@Named(value="scheduleGeneralView")
@ViewScoped
public class ScheduleGeneralView implements Serializable {
	private final static Logger LOG = Logger.getLogger(ScheduleView.class.getName());
	private final Integer DOCTORS_USER_GROUP_ID = Integer.parseInt(Config.getInstance().getString("DOCTORS_USER_GROUP_ID"));

	@EJB
	private UsersFacadeLocal usersFacade;

	@EJB
	private PlanFacadeLocal planFacade;

	@EJB
	private ScheduleFacadeLocal scheduleFacade;

	private Users authorizedUser;

	@Inject
	private SessionParams sessionParams;

	//All Schedule Items
	private List<Schedule> allSchedule;

	//All Plan Items
	private List<Plan> allPlan;

	private ScheduleModel eventModel;

	private String cssStyle;

	private List<Users> allDoctors;

	private Users selectedDoctor;

	//Constructor)
	@PostConstruct
	public void init(){
		authorizedUser = sessionParams.getAuthorizedUser();
		allDoctors = usersFacade.findByGroup(DOCTORS_USER_GROUP_ID);
		loadEvents();
		generateCss();
	}


	//When event is clicked method prepares data for addSchedule dialog
	public void onEventSelect(SelectEvent selectEvent) throws IOException {
		DefaultScheduleEvent event = (DefaultScheduleEvent) selectEvent.getObject();
		Rooms room = ((Schedule)event.getData()).getRoom();
		sessionParams.setScheduleRoom(room);

		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		context.redirect(context.getRequestContextPath() + Mapping.getInstance().getString("SCHEDULE_PAGE"));
	}

	//Creates Schedule event from Schedule record
	private DefaultScheduleEvent eventFromSchedule(Schedule schedule){
		DefaultScheduleEvent newEvent;
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		if(schedule.getPatient() != null){
			newEvent = new DefaultScheduleEvent(
					schedule.getMethod().getShortName() + " | " +
							schedule.getPatient().getFirstName() + " " +
							schedule.getPatient().getLastName() + " | " +
							schedule.getDescription(),
					schedule.getDateTimeStart(),
					schedule.getDateTimeEnd(),
					schedule
			);
			newEvent.setDescription(
					formatter.format(schedule.getDateTimeStart()) + " - " +
							formatter.format(schedule.getDateTimeEnd()) + " | " +
					schedule.getMethod().getShortName() + " | " +
							schedule.getPatient().getFirstName() + " " +
							schedule.getPatient().getLastName() + " | " +
							schedule.getDescription()
			);
		}else{
			newEvent = new DefaultScheduleEvent(
					schedule.getMethod().getShortName() + " | " +
							schedule.getDescription(),
					schedule.getDateTimeStart(),
					schedule.getDateTimeEnd(),
					schedule
			);
			newEvent.setDescription(
					formatter.format(schedule.getDateTimeStart()) + " - " +
							formatter.format(schedule.getDateTimeEnd()) + " | " +
					schedule.getMethod().getShortName() + " | " +
							schedule.getDescription()
			);
		}
		newEvent.setStyleClass("doc" + schedule.getDoctor().getId());
		newEvent.setEditable(false);
		return newEvent;
	}

	//Creates Plan event from Plan record
	private DefaultScheduleEvent eventFromPlan(Plan plan) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		DefaultScheduleEvent newEvent = new DefaultScheduleEvent(
				plan.getDoctor().getFirstName() + " " +
						plan.getDoctor().getLastName() + " | " +
						plan.getDescription(),
				plan.getDateTimeStart(),
				plan.getDateTimeEnd(),
				plan);
		newEvent.setDescription(
				formatter.format(plan.getDateTimeStart()) + " - " +
						formatter.format(plan.getDateTimeEnd()) + " | " +
						plan.getDoctor().getFirstName() + " " +

						plan.getDoctor().getLastName() + " | " +
						plan.getRoom().getName() + " | " +
						plan.getDescription()
		);
		newEvent.setStyleClass("fc-plan doc" + plan.getDoctor().getId());
		return newEvent;
	}

	public void filter(){
		if(selectedDoctor == null){
			loadEvents();
		}else {
			//Schedule populating
			eventModel = new LazyScheduleModel() {
				private static final long serialVersionUID = 8535371059490008142L;

				@Override
				public void loadEvents(Date start, Date end) {

					allPlan = planFacade.findByStartDateBetweenAndDoctor(start, end, selectedDoctor);
					for (Plan plan : allPlan) {
						eventModel.addEvent(eventFromPlan(plan));
					}

					allSchedule = scheduleFacade.findByEmployeeAndEndDateBetweenExclusiveFrom(selectedDoctor,start, end);
					//remove unnecessary breaks
					Iterator<Schedule> iter = allSchedule.iterator();
					while(iter.hasNext()){
						Schedule nextSchedule = iter.next();
						if(nextSchedule.getParentSchedule() != null && !allSchedule.contains(nextSchedule.getParentSchedule())){
							iter.remove();
						}
					}
					for(Schedule schedule : allSchedule){
						eventModel.addEvent(eventFromSchedule(schedule));
					}

				}
			};
		}
	}

	private void loadEvents(){
		eventModel = new LazyScheduleModel() {
			private static final long serialVersionUID = 8535371059490008142L;

			@Override
			public void loadEvents(Date start, Date end) {

				allPlan = planFacade.findByStartDateBetween(start, end);
				for (Plan plan : allPlan) {
					eventModel.addEvent(eventFromPlan(plan));
				}

				allSchedule = scheduleFacade.findByStartDateBetween(start, end);
				for(Schedule schedule : allSchedule){
					eventModel.addEvent(eventFromSchedule(schedule));
				}

			}
		};
	}

	private void generateCss(){
		cssStyle = "<style>";
		for(Users doctor : usersFacade.findByGroup(DOCTORS_USER_GROUP_ID))
			cssStyle += ".doc" + doctor.getId() + "{background-color: #" + doctor.getColor() + "}";
		cssStyle += "</style>";
	}

    /* --------------------------------------------*/
    /* --------- Setters && Getters ---------------*/
    /* --------------------------------------------*/


	public Users getAuthorizedUser() {
		return authorizedUser;
	}

	public void setAuthorizedUser(Users authorizedUser) {
		this.authorizedUser = authorizedUser;
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public String getCssStyle() {
		return cssStyle;
	}

	public void setSessionParams(SessionParams sessionParams) {
		this.sessionParams = sessionParams;
	}

	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}

	public List<Users> getAllDoctors() {
		return allDoctors;
	}

	public void setAllDoctors(List<Users> allDoctors) {
		this.allDoctors = allDoctors;
	}

	public Users getSelectedDoctor() {
		return selectedDoctor;
	}

	public void setSelectedDoctor(Users selectedDoctor) {
		this.selectedDoctor = selectedDoctor;
	}
}
