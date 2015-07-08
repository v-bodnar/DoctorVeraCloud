package ua.kiev.doctorvera.managedbeans;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleModel;
import ua.kiev.doctorvera.entities.Plan;
import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.PlanFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.resources.Mapping;

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

@ManagedBean(name="planGeneralView")
@ViewScoped
public class PlanGeneralView {
	
	private final Integer DOCTORS_TYPE_ID = Integer.parseInt(Mapping.getInstance().getString("DOCTORS_TYPE_ID"));
	
	@EJB
	private PlanFacadeLocal planFacade;
	
	@EJB
	private UsersFacadeLocal usersFacade;
	
    @ManagedProperty(value="#{sessionParams}")
    private SessionParams sessionParams;
	
	private List<Plan> allPlan;

	private ScheduleModel eventModel;
	
    private String cssStyle;
	
	public PlanGeneralView(){}
	
	@PostConstruct
	public void init(){
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
        generateCss();
	}
	
    //Creates Plan event from Plan record
    private DefaultScheduleEvent eventFromPlan(Plan plan){
    	DefaultScheduleEvent newEvent = new DefaultScheduleEvent(
    		plan.getRoom().getName() + " / " + 
    		plan.getDoctor().getFirstName() + 
			plan.getDoctor().getLastName() + " / " +  
			plan.getDescription(), 
			plan.getDateTimeStart(), 
			plan.getDateTimeEnd(),
			plan);
    	newEvent.setDescription(
    		plan.getRoom().getName() + " / " + 
    		plan.getDoctor().getFirstName() +
    		plan.getDoctor().getLastName() + " / " +
    		plan.getRoom().getName() + " / " +
    		plan.getDescription()
    		);
    	newEvent.setStyleClass("doc" + plan.getDoctor().getId());
    	return newEvent;
    }
	
    private void generateCss(){
        cssStyle = "<style>";
        for(Users doctor : usersFacade.findByType(DOCTORS_TYPE_ID))
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
}