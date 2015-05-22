package ua.kiev.doctorvera.managedbeans;

import org.primefaces.context.RequestContext;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import ua.kiev.doctorvera.entities.Plan;
import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.PlanFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.RoomsFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.validators.PlanValidator;
import ua.kiev.doctorvera.web.resources.Mapping;
import ua.kiev.doctorvera.web.resources.Message;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@ManagedBean(name="planView")
@ViewScoped
public class PlanView {
	
	private final static Logger LOG = Logger.getLogger(PlanView.class.getName());
	private final Integer DOCTORS_TYPE_ID = Integer.parseInt(Mapping.getInstance().getProperty(Mapping.UserTypes.DOCTORS_TYPE_ID));
	@EJB
	private RoomsFacadeLocal roomsFacade;
	
	@EJB
	private UsersFacadeLocal usersFacade;
	
	@EJB
	private PlanFacadeLocal planFacade;
	
    @ManagedProperty(value="#{userLoginView.authorizedUser}")
	private Users authorizedUser;
    
    @ManagedProperty(value="#{planValidator}")
	private PlanValidator planValidator;

    @ManagedProperty(value="#{sessionParams.planRoom}")
    private Rooms currentRoom;
	
	private List<Rooms> allRooms;
	
	private List<Users> allDoctors;
	
	private List<Plan> allPlan;
	
	private Plan plan;

	private ScheduleModel eventModel;
	
	private DefaultScheduleEvent event = new DefaultScheduleEvent();
	
	public PlanView(){}

    private String cssStyle;
	
	@PostConstruct
	public void init(){
		eventModel = new LazyScheduleModel() {
			private static final long serialVersionUID = 8535371059490008142L;

			@Override
            public void loadEvents(Date start, Date end) {
				allPlan = planFacade.findByRoomAndStartDate(currentRoom, start, end);
				for(Plan plan : allPlan){
					eventModel.addEvent(eventFromPlan(plan));
				}

			}  
		};
        generateCss();
		allRooms = roomsFacade.findAll();
		allDoctors = usersFacade.findByType(DOCTORS_TYPE_ID);
		plan = new Plan();
		//colorize();
	}
	
	
	public Rooms getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(Rooms currentRoom) {
		this.currentRoom = currentRoom;
	}

	public PlanValidator getPlanValidator() {
		return planValidator;
	}

	public void setPlanValidator(PlanValidator planValidator) {
		this.planValidator = planValidator;
	}


	public Users getAuthorizedUser() {
		return authorizedUser;
	}

	public void setAuthorizedUser(Users authorizedUser) {
		this.authorizedUser = authorizedUser;
	}

	public List<Rooms> getAllRooms() {
		return allRooms;
	}

	public void setAllRooms(List<Rooms> allRooms) {
		this.allRooms = allRooms;
	}
	
	public void initNewPlan() {
		this.plan = new Plan();
	}

	public List<Users> getAllDoctors() {
		return allDoctors;
	}

	public void setAllDoctors(List<Users> allDoctors) {
		this.allDoctors = allDoctors;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}
	
    public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = (DefaultScheduleEvent)event;
	}

	public ScheduleModel getEventModel() {
        return eventModel;
    }

    public String getCssStyle() {
        return cssStyle;
    }

    public void setCssStyle(String cssStyle) {
        this.cssStyle = cssStyle;
    }

    public void onEventSelect(SelectEvent selectEvent) {
		event = (DefaultScheduleEvent) selectEvent.getObject();
        plan = planFacade.find(((Plan)event.getData()).getId());
    }
     
    public void onDateSelect(SelectEvent selectEvent) {
    	event = new DefaultScheduleEvent();
    	plan = new Plan();
    	plan.setDateTimeStart(((Date)selectEvent.getObject()));
    	plan.setDateTimeEnd(((Date)selectEvent.getObject()));
    }
     
    public void onEventMove(ScheduleEntryMoveEvent selectEvent) {
    	event = (DefaultScheduleEvent)selectEvent.getScheduleEvent();
        plan = planFacade.find(((Plan)event.getData()).getId());
        
		//Validation for crossing other Plan records
		//Check if changes are legal (if there are no scheduled records in time intervals changed)
		if(planValidator.resizePlanValidate(plan.getDateTimeStart(), plan.getDateTimeEnd(), 
				event.getStartDate(), event.getEndDate(), currentRoom, plan)) return;
    	
    	plan.setDateCreated(new Date());
    	plan.setUserCreated(authorizedUser);
    	plan.setDateTimeStart(event.getStartDate());
    	plan.setDateTimeEnd(event.getEndDate());
    	
    	planFacade.edit(plan);
    	LOG.info("Plan id: " + plan.getId() + " updated");
    	
    	eventModel.updateEvent(event);
    	LOG.info("Event id: " + event.getId() + " updated");
    	
    	
		final String successMessage = Message.getInstance().getMessage(Message.Plan.PLAN_EDITED);
		final String successTitle = Message.getInstance().getMessage(Message.Plan.PLAN_ADD_DIALOG_TITLE);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
    }
     
    public void onEventResize(ScheduleEntryResizeEvent selectEvent) {
    	event = (DefaultScheduleEvent)selectEvent.getScheduleEvent();
        plan = planFacade.find(((Plan)event.getData()).getId());
        
		//Validation for crossing other Plan records
		//Check if changes are legal (if there are no scheduled records in time intervals changed)
		if(planValidator.resizePlanValidate(plan.getDateTimeStart(), plan.getDateTimeEnd(), 
				event.getStartDate(), event.getEndDate(), currentRoom, plan)) return;
    	
    	plan.setDateCreated(new Date());
    	plan.setUserCreated(authorizedUser);
    	plan.setDateTimeStart(event.getStartDate());
    	plan.setDateTimeEnd(event.getEndDate());
    	
    	planFacade.edit(plan);
    	LOG.info("Plan id: " + plan.getId() + " updated");
    	
    	eventModel.updateEvent(event);
    	LOG.info("Event id: " + event.getId() + " updated");
    	
		final String successMessage = Message.getInstance().getMessage(Message.Plan.PLAN_EDITED);
		final String successTitle = Message.getInstance().getMessage(Message.Plan.PLAN_ADD_DIALOG_TITLE);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
    }

    // This method is add Plan form listener form plan.xhtml
    // It contains creating and persisting Plan record, creating schedule event and updating schedule
    // and notifying user about success or error
    public void addPlan(ActionEvent actionEvent) {
    	
    	//Setting current timestamp and user created plan record
    	plan.setDateCreated(new Date());
    	plan.setUserCreated(authorizedUser);
    	
    	//When we create new Plan record Id of Plan and event are null
    	if(plan.getId() == null || event.getId() == null){
    		//Validation for crossing other Plan records
    		if(planValidator.addPlanValidate(plan.getDateTimeStart(), 
    				plan.getDateTimeEnd(), currentRoom, null)) return;
    		
    		planFacade.create(plan);
    		LOG.info("new plan id: " + plan.getId() + " persisted");
    		
    		event = eventFromPlan(plan);
            eventModel.addEvent(event);
            LOG.info("new event id: " + event.getId() + " scheduled");
            
    		final String successMessage = Message.getInstance().getMessage(Message.Plan.PLAN_SAVED);
    		final String successTitle = Message.getInstance().getMessage(Message.Plan.PLAN_ADD_DIALOG_TITLE);
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
    		RequestContext context = RequestContext.getCurrentInstance();
    		context.execute("PF('addPlanDialog').hide();");
    	}else
    		LOG.info("Something goes wrong event Id:" + event.getId() + " Plan entity: " + plan);
    }
    
    // This method is edit Plan form listener form plan.xhtml
    // It contains updating Plan record, updating schedule event and updating schedule
    // and notifying user about success or error
    public void editPlan(ActionEvent actionEvent) {
    	//Setting current time stamp and user updated plan record
    	plan.setDateCreated(new Date());
    	plan.setUserCreated(authorizedUser);
    	
    	//When we update Plan record Id of the Plan and event are already set
    	if(plan.getId() != null || event.getId() != null){
    		
    		//Validation for crossing other Plan records
    		//Check if changes are legal (if there are no scheduled records in time intervals changed)
    		if(planValidator.updatePlanValidate(plan.getDateTimeStart(), plan.getDateTimeEnd(), 
    				event.getStartDate(), event.getEndDate(), currentRoom, plan)) return;
    		
    		planFacade.edit(plan);
    		LOG.info("Plan id: " + plan.getId() + " updated");
    		event = eventFromPlan(plan);
            eventModel.updateEvent(event);
            LOG.info("Event id: " + event.getId() + " updated");
            
    		final String successMessage = Message.getInstance().getMessage(Message.Plan.PLAN_EDITED);
    		final String successTitle = Message.getInstance().getMessage(Message.Plan.PLAN_ADD_DIALOG_TITLE);
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
    	}

		//resetting values
        event = new DefaultScheduleEvent();
        plan = new Plan();
    }
    
    public void deletePlan(ActionEvent actionEvent) {
    	if (plan != null && event != null){

    		planFacade.remove(plan);
    		eventModel.deleteEvent(event);
    		
    		LOG.info("Plan id: " + plan.getId() + " deleted");
    		LOG.info("Event id: " + event.getId() + " deleted");
    		
    		final String successMessage = Message.getInstance().getMessage(Message.Plan.PLAN_DELETED);
    		final String successTitle = Message.getInstance().getMessage(Message.Plan.PLAN_ADD_DIALOG_TITLE);
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
    	}
    }
    private DefaultScheduleEvent eventFromPlan(Plan plan){
    	DefaultScheduleEvent newEvent = new DefaultScheduleEvent(
			plan.getDoctor().getFirstName() + 
			plan.getDoctor().getLastName() + " / " +  
			plan.getDescription(), 
			plan.getDateTimeStart(), 
			plan.getDateTimeEnd(),
			plan);
    	newEvent.setDescription(
    		plan.getDoctor().getFirstName() +
    		plan.getDoctor().getLastName() + " / " +
    		plan.getRoom().getName() + " / " +
    		plan.getDescription()
    		);
    	newEvent.setStyleClass("doc"+plan.getDoctor().getId());
    	if(event != null && event.getId() != null)
    		newEvent.setId(event.getId());
    	return newEvent;
    }

    private void generateCss(){
        cssStyle = "<style>";
        for(Users doctor : usersFacade.findByType(3))
            cssStyle += ".doc" + doctor.getId() + "{background-color: #" + doctor.getColor() + "}";
        cssStyle += "</style>";
    }

    /*
    public void colorize(){
    	for(int key : colors.keySet())
             RequestContext.getCurrentInstance().execute("colorize('doc"+key+"','"+colors.get(key)+"');alert('This onload script is added from backing bean.')");
    }
    */
    
/*
	public void deleteSelectedRoom(){
		roomsFacade.remove(selectedRoom);
		allRooms.remove(selectedRoom);
		final String successMessage = Message.getInstance().getMessage(Message.Rooms.ROOMS_DELETED);
		final String successTitle = Message.getInstance().getMessage(Message.Validator.VALIDATOR_SUCCESS_TITLE);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}	
	
    public void saveSelectedRoom() {
		selectedRoom.setDateCreated(new Date());
		selectedRoom.setUserCreated(authorizedUser);
		roomsFacade.edit(selectedRoom);
		final String successMessage = Message.getInstance().getMessage(Message.Rooms.ROOMS_EDITED);
		final String successTitle = Message.getInstance().getMessage(Message.Validator.VALIDATOR_SUCCESS_TITLE);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
    }
    
	public void saveNewRoom(){
		newRoom.setDateCreated(new Date());
		newRoom.setUserCreated(authorizedUser);
		roomsFacade.create(newRoom);
		allRooms.add(newRoom);
		final String successMessage = Message.getInstance().getMessage(Message.Messages.APPLICATION_SAVED);
		final String successTitle = Message.getInstance().getMessage(Message.Rooms.ROOMS_SAVED);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}
*/
    
}
