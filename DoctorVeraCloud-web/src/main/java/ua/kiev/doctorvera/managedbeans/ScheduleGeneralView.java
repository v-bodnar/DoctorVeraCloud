package ua.kiev.doctorvera.managedbeans;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleModel;
import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.entities.Schedule;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.ScheduleFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.web.resources.Mapping;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@ManagedBean(name="scheduleGeneralView")
@ViewScoped
public class ScheduleGeneralView {
	private final static Logger LOG = Logger.getLogger(ScheduleView.class.getName());

	@EJB
	private UsersFacadeLocal usersFacade;

	@EJB
	private ScheduleFacadeLocal scheduleFacade;

	@ManagedProperty(value="#{userLoginView.authorizedUser}")
	private Users authorizedUser;

	@ManagedProperty(value="#{sessionParams}")
	private SessionParams sessionParams;

	//All Schedule Items
	private List<Schedule> allSchedule;

	private ScheduleModel eventModel;

	private String cssStyle;

	//Constructor)
	@PostConstruct
	public void init(){
		//Schedule populating
		eventModel = new LazyScheduleModel() {
			private static final long serialVersionUID = 8535371059490008142L;

			@Override
			public void loadEvents(Date start, Date end) {

				allSchedule = scheduleFacade.findByStartDateBetween(start, end);
				for(Schedule schedule : allSchedule){
					eventModel.addEvent(eventFromSchedule(schedule));
				}

			}
		};
		generateCss();
	}


	//When event is clicked method prepares data for addSchedule dialog
	public void onEventSelect(SelectEvent selectEvent) throws IOException {
		DefaultScheduleEvent event = (DefaultScheduleEvent) selectEvent.getObject();
		Rooms room = ((Schedule)event.getData()).getRoom();
		sessionParams.setScheduleRoom(room);

		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		context.redirect(context.getRequestContextPath() + Mapping.getInstance().getProperty(Mapping.Page.SCHEDULE_PAGE));
	}

	//Creates Schedule event from Schedule record
	private DefaultScheduleEvent eventFromSchedule(Schedule schedule){
		DefaultScheduleEvent newEvent;
		if(schedule.getPatient() != null){
			newEvent = new DefaultScheduleEvent(
					schedule.getMethod().getShortName() + " / " +
							schedule.getPatient().getFirstName() +
							schedule.getPatient().getLastName() + " / " +
							schedule.getDescription(),
					schedule.getDateTimeStart(),
					schedule.getDateTimeEnd(),
					schedule
			);
			newEvent.setDescription(
					schedule.getMethod().getShortName() + " / " +
							schedule.getPatient().getFirstName() +
							schedule.getPatient().getLastName() + " / " +
							schedule.getDescription()
			);
		}else{
			newEvent = new DefaultScheduleEvent(
					schedule.getMethod().getShortName() + " / " +
							schedule.getDescription(),
					schedule.getDateTimeStart(),
					schedule.getDateTimeEnd(),
					schedule
			);
			newEvent.setDescription(
					schedule.getMethod().getShortName() + " / " +
							schedule.getDescription()
			);
		}
		newEvent.setStyleClass("doc" + schedule.getDoctor().getId());
		return newEvent;
	}

	private void generateCss(){
		cssStyle = "<style>";
		for(Users doctor : usersFacade.findByType(3))
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
}
