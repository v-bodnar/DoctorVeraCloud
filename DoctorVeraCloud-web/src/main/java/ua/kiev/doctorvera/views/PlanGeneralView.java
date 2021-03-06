package ua.kiev.doctorvera.views;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleModel;
import ua.kiev.doctorvera.entities.Plan;
import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.PlanFacadeLocal;
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
import java.util.List;

@Named(value="planGeneralView")
@ViewScoped
public class PlanGeneralView implements Serializable {
	
	private final Integer DOCTORS_TYPE_ID = Integer.parseInt(Config.getInstance().getString("DOCTORS_USER_GROUP_ID"));
	
	@EJB
	private PlanFacadeLocal planFacade;
	
	@EJB
	private UsersFacadeLocal usersFacade;

    @Inject
    private SessionParams sessionParams;
	
	private List<Plan> allPlan;

	private ScheduleModel eventModel;
	
    private String cssStyle;

	private List<Users> allDoctors;

	private Users selectedDoctor;
	
	public PlanGeneralView(){}
	
	@PostConstruct
	public void init(){
		loadEvents();
		allDoctors = usersFacade.findByGroup(DOCTORS_TYPE_ID);
        generateCss();
	}
	
    //Creates Plan event from Plan record
    private DefaultScheduleEvent eventFromPlan(Plan plan){
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
    	DefaultScheduleEvent newEvent = new DefaultScheduleEvent(
    		plan.getRoom().getName() + " | " + 
    		plan.getDoctor().getFirstName() + 
			plan.getDoctor().getLastName() + " | " +  
			plan.getDescription(), 
			plan.getDateTimeStart(), 
			plan.getDateTimeEnd(),
			plan);
    	newEvent.setDescription(
			formatter.format(plan.getDateTimeStart()) + " - " +
			formatter.format(plan.getDateTimeEnd()) + " | " +
    		plan.getRoom().getName() + " | " + 
    		plan.getDoctor().getFirstName() +
    		plan.getDoctor().getLastName() + " | " +
    		plan.getRoom().getName() + " | " +
    		plan.getDescription()
    		);
    	newEvent.setStyleClass("doc" + plan.getDoctor().getId());
		newEvent.setEditable(false);
    	return newEvent;
    }

    public void filter(){
    	if(selectedDoctor != null) {
			eventModel = new LazyScheduleModel() {
				private static final long serialVersionUID = 8535371059490008142L;

				@Override
				public void loadEvents(Date start, Date end) {
					allPlan = planFacade.findByStartDateBetweenAndDoctor(start, end, selectedDoctor);
					for (Plan plan : allPlan) {
						eventModel.addEvent(eventFromPlan(plan));
					}

				}
			};
		}else{
			loadEvents();
		}
	}

	private void loadEvents(){
		eventModel = new LazyScheduleModel() {
			private static final long serialVersionUID = 8535371059490008142L;

			@Override
			public void loadEvents(Date start, Date end) {
				allPlan = planFacade.findByStartDateBetween(start, end);
				for(Plan plan : allPlan){
					eventModel.addEvent(eventFromPlan(plan));
				}

			}
		};
	}
	
    private void generateCss(){
        cssStyle = "<style>";
        for(Users doctor : usersFacade.findByGroup(DOCTORS_TYPE_ID))
            cssStyle += ".doc" + doctor.getId() + "{background-color: #" + doctor.getColor() + "}";
        cssStyle += "</style>";
    }

	public void onEventSelect(SelectEvent selectEvent) throws IOException {
    	DefaultScheduleEvent event = (DefaultScheduleEvent) selectEvent.getObject();
        Rooms room = ((Plan)event.getData()).getRoom();
        sessionParams.setScheduleRoom(room);
        
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect(context.getRequestContextPath() + Mapping.getInstance().getString("SCHEDULE_PAGE"));
    }
     
    /* --------------------------------------------*/
    /* --------- Setters && Getters ---------------*/
    /* --------------------------------------------*/
	
	public ScheduleModel getEventModel() {
        return eventModel;
    }

    public String getCssStyle() {
        return cssStyle;
    }

    public void setCssStyle(String cssStyle) {
        this.cssStyle = cssStyle;
    }
    
    public void setSessionParams(SessionParams sessionParams) {
		this.sessionParams = sessionParams;
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