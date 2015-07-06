package ua.kiev.doctorvera.managedbeans;

import org.primefaces.context.RequestContext;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.*;
import ua.kiev.doctorvera.entities.*;
import ua.kiev.doctorvera.facadeLocal.*;
import ua.kiev.doctorvera.validators.PlanValidator;
import ua.kiev.doctorvera.validators.ScheduleValidator;
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
import java.util.*;
import java.util.logging.Logger;

@ManagedBean(name="scheduleView")
@ViewScoped
public class ScheduleView {
	
	private final static Logger LOG = Logger.getLogger(ScheduleView.class.getName());
	private final static long FIVE_MINUTES_IN_MILLIS=360000;//millisecs
	private final Integer PATIENTS_TYPE_ID = Integer.parseInt(Mapping.getInstance().getProperty(Mapping.UserTypes.PATIENTS_TYPE_ID));
	private final Integer ASSISTENTS_TYPE_ID = Integer.parseInt(Mapping.getInstance().getProperty(Mapping.UserTypes.ASSISTENTS_TYPE_ID));
	private final Integer METHOD_BREAK_ID = Integer.parseInt(Mapping.getInstance().getProperty(Mapping.Methods.METHOD_BREAK_ID));
	private final Integer USERS_BREAK_ID = Integer.parseInt(Mapping.getInstance().getProperty(Mapping.Users.USERS_BREAK_ID));
	@EJB
	private RoomsFacadeLocal roomsFacade;
	
	@EJB
	private UsersFacadeLocal usersFacade;
	
	@EJB
	private UserTypesFacadeLocal userTypesFacade;
	
	@EJB
	private PlanFacadeLocal planFacade;
	
	@EJB
	private ScheduleFacadeLocal scheduleFacade;
	
	@EJB
	private MethodsFacadeLocal methodsFacade;

	@EJB
	private PricesFacadeLocal pricesFacade;

    @EJB
    private MethodTypesFacadeLocal methodTypesFacade;
	
    @ManagedProperty(value="#{userLoginView.authorizedUser}")
	private Users authorizedUser;
    
    @ManagedProperty(value="#{planValidator}")
	private PlanValidator planValidator;
    
    @ManagedProperty(value="#{scheduleValidator}")
	private ScheduleValidator scheduleValidator;

    @ManagedProperty(value="#{sessionParams.scheduleRoom}")
    private Rooms currentRoom;

    //Data for Schedule Form Autocomplete inputs
    private final Set<String> allFirstNames = new HashSet<String>() ;
    private final Set<String> allMiddleNames = new HashSet<String>();
    private final Set<String> allLastNames = new HashSet<String>();
	
    //New User
    private Users newUser;
    
    private Integer breakTime;
    
    //All Patients
    private List<Users> allPatients;
    
    //All Assistents
    private List<Users> allAssistents;
    
    private List<Users> allPartners;
    
    //All Plan Items
	private List<Plan> allPlan;
	
    //All Schedule Items
	private List<Schedule> allSchedule;
	
	//Schedule Entity to manipulate
	private Schedule schedule;

    private MethodTypes selectedMethodType;

    private List<Methods> selectedMethods;

    private List<MethodTypes> allMethodTypes;

    //Model for picklist PrimeFaces widget
    private DualListModel<Methods> methodsDualListModel;

	private ScheduleModel eventModel;
	
	private DefaultScheduleEvent event = new DefaultScheduleEvent();

    private String cssStyle;
    
    //Constructor)
	@PostConstruct
	public void init(){
		allPatients = usersFacade.findByType(PATIENTS_TYPE_ID);
		allAssistents = usersFacade.findByType(ASSISTENTS_TYPE_ID);
		schedule = new Schedule();
		newUser = new Users();
        selectedMethods = new ArrayList<Methods>();
        allMethodTypes = methodTypesFacade.findAll();
        selectedMethodType = findFirstMethodType();
		breakTime = 5;

        constructPickList();

		//We will need these names for form autocomplete
		for (Users user : usersFacade.findAll()){
			allFirstNames.add(user.getFirstName());
			allMiddleNames.add(user.getMiddleName());
			allLastNames.add(user.getLastName());
		}

        //Schedule populating
		eventModel = new LazyScheduleModel() {
			private static final long serialVersionUID = 8535371059490008142L;

			@Override
            public void loadEvents(Date start, Date end) {
				allPlan = planFacade.findByRoomAndStartDateBetween(currentRoom, start, end);
				for(Plan plan : allPlan){
					eventModel.addEvent(eventFromPlan(plan));
				}
				
				allSchedule = scheduleFacade.findByRoomAndStartDateBetween(currentRoom, start, end);
				for(Schedule schedule : allSchedule){
					eventModel.addEvent(eventFromSchedule(schedule));
				}
				
			}  
		};
        generateCss();
	}

	
	public MethodTypes findFirstMethodType(){
    	try{
    		return methodTypesFacade.findAll().get(0);
    	}	catch(IndexOutOfBoundsException e){
    		return null;
    	}
    	 
    }
    
    public Float getTotalPrice(){
    	Float totalPrice = new Float(0);
    	for(Methods method : selectedMethods){
    		totalPrice += pricesFacade.findLastPrice(method).getTotal();
    	}
    	return totalPrice;
    }
    
    public Integer getTotalTime(){
    	Integer totalTime = 0;
    	for(Methods method : selectedMethods){
    		totalTime += method.getTimeInMinutes();
    	}
    	return totalTime;
    }
    
    //When event is clicked method prepares data for addSchedule dialog
    public void onEventSelect(SelectEvent selectEvent) {
		event = (DefaultScheduleEvent) selectEvent.getObject();
		Object data = event.getData();
		selectedMethods.clear();
		
		//If Plan Event was clicked we create new Schedule Event
		if (data instanceof Plan){
			Plan plan = planFacade.find(((Plan)event.getData()).getId());
			schedule = new Schedule();
			newUser = new Users();
			selectedMethodType = null;
			schedule.setDoctor(plan.getDoctor());
			schedule.setDateTimeStart(getEarliestTime(plan));
			event = new DefaultScheduleEvent();
		// else if Schedule event was clicked we update existing Schedule event
		} else if (data instanceof Schedule){
			schedule = scheduleFacade.find(((Schedule)event.getData()).getId());
			newUser = schedule.getPatient();
			selectedMethodType = schedule.getMethod().getMethodType();
			selectedMethods.add(schedule.getMethod());
			constructPickList();
		}
    }
    
    //This method determines the earliest available time in the given Plan Record
    private Date getEarliestTime(Plan plan){
    	List<Schedule> scheduledRecords = scheduleFacade.findByRoomAndDatesOutsideScheduleOrEqual(currentRoom, plan.getDateTimeStart(),plan.getDateTimeEnd());
    	//Sorting by Date Start
    	Collections.sort(scheduledRecords, new Comparator<Schedule>() {
	            @Override
	            public int compare(Schedule  schedule1, Schedule  schedule2){

	                return  schedule1.getDateTimeStart().compareTo(schedule2.getDateTimeStart());
	            }
	    });
    	
    	if(scheduledRecords.size() == 0)
    		return plan.getDateTimeStart();
    	else{
    		Date earliestTime = plan.getDateTimeStart();
    		for(Schedule record : scheduledRecords){
    			if(earliestTime.before(record.getDateTimeStart()))
    				return earliestTime;
    			else
    				earliestTime = record.getDateTimeEnd();
    		}
    		return earliestTime;
    	}
    }

    //Constructs Methods Picklist 
    public void constructPickList(){
        if (selectedMethodType != null && selectedMethodType.getId() != null){
            List<Methods> allMethods = methodsFacade.findByType(selectedMethodType);

            for(Methods method : selectedMethods){
                allMethods.remove(method);
            }
            methodsDualListModel = new DualListModel<Methods>(allMethods, selectedMethods);
        } else
            methodsDualListModel = new DualListModel<Methods>(new ArrayList<Methods>(), new ArrayList<Methods>());
    }
    
    //When any empty date in the schedule is clicked 
    //this method have to control data from addPlanDialog
    public void onDateSelect(SelectEvent selectEvent) {
    	event = new DefaultScheduleEvent();
    	//plan = new Plan();
    	//plan.setDateTimeStart(((Date)selectEvent.getObject()));
    	//plan.setDateTimeEnd(((Date)selectEvent.getObject()));
    }
    
    //Fills users data if phone number fits existing one
    public void fillUserData(){
    	List<Users> usersByPhone = usersFacade.findByPhoneNumberMobile(newUser.getPhoneNumberMobile());
    	
    	if(usersByPhone.size() == 1)
    		newUser = usersByPhone.get(0);
    }

    //Checks dates of moved Schedule event and updates persistent state of this record
    public void onEventMove(ScheduleEntryMoveEvent selectEvent) {
    	event = (DefaultScheduleEvent)selectEvent.getScheduleEvent();
		Object data = event.getData();
		
		//If Schedule Event was clicked we check dates and update 
		//corresponding schedule record in the persistence storage
		if (data instanceof Schedule){
			schedule = scheduleFacade.find(((Schedule)data).getId());
			schedule.setDateTimeStart(event.getStartDate());
			Date endTime = new Date(schedule.getDateTimeStart().getTime() + (schedule.getMethod().getTimeInMinutes() * 60L * 1000L));
			schedule.setDateTimeEnd(endTime);
			
	    	Boolean isValid = scheduleValidator.addScheduleValidate(schedule,  currentRoom, endTime);
			
	    	if(isValid){
	    		schedule.setDateCreated(new Date());
	    		schedule.setUserCreated(authorizedUser);
	    		scheduleFacade.edit(schedule);
	    		LOG.info("Schedule id: " + schedule.getId() + " updated");
	    		
	        	eventModel.updateEvent(event);
	        	LOG.info("Event id: " + event.getId() + " updated");
	        	
	    		final String successMessage = Message.getInstance().getMessage(Message.Schedule.SCHEDULE_EDITED);
	    		final String successTitle = Message.getInstance().getMessage(Message.Validator.VALIDATOR_SUCCESS_TITLE);
	    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	    	}else
	    		LOG.info("Validation exception. Schedule is not updated!");
		}
    	

    }
     
    // This method is addSchedule listener form Schedule.xhtml
    // It contains creating and persisting Schedule record, creating schedule event and updating schedule
    // and notifying user about success or error
    public void addSchedule(ActionEvent actionEvent) {
    	Boolean isValid = scheduleValidator.addScheduleValidate(schedule,  currentRoom, new Date(schedule.getDateTimeStart().getTime() + ((getTotalTime()+breakTime) * 60L * 1000L)));
    	
    	if(isValid){
        	if (isNew(newUser)){
            	//Setting current timestamp and user created plan record
        		newUser.setDateCreated(new Date());
        		newUser.setUserCreated(authorizedUser);
        		usersFacade.create(newUser);
        		usersFacade.addUserType(newUser, userTypesFacade.find(PATIENTS_TYPE_ID), authorizedUser);
        	}
        	
        	Date startTime = schedule.getDateTimeStart();
        	
        	for (Methods method : selectedMethods){
        		Schedule newSchedule = new ScheduleBuilder().buildFullSchedule(schedule, method, startTime);
            	scheduleFacade.create(newSchedule);
            	LOG.info("new Schedule id: " + newSchedule.getId() + " persisted");
            	
            	//Creating corresponding Schedule event
            	event = eventFromSchedule(newSchedule);
                eventModel.addEvent(event);
                
            	startTime = newSchedule.getDateTimeEnd();

        	}
        	Schedule newSchedule = new ScheduleBuilder().buildBreakSchedule(schedule, startTime);
        	scheduleFacade.create(newSchedule);
        	LOG.info("new Schedule id: " + newSchedule.getId() + " persisted");
        	
        	//Sending success message and closing dialog
    		final String successMessage = Message.getInstance().getMessage(Message.Schedule.SCHEDULE_SAVED);
    		final String successTitle = Message.getInstance().getMessage(Message.Validator.VALIDATOR_SUCCESS_TITLE);
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
    		RequestContext context = RequestContext.getCurrentInstance();
    		context.execute("PF('addScheduleDialog').hide();");
    	}else
    		LOG.info("Validation exception. New Schedule is not persisted!");

    }
    
    // This method is edit Schedule form listener form schedule.xhtml
    // It contains updating Schedule record, updating Schedule event and updating PF.schedule
    // and notifying user about success or error
    public void editSchedule(ActionEvent actionEvent) {
    	Boolean isValid = scheduleValidator.addScheduleValidate(schedule,  currentRoom, new Date(schedule.getDateTimeStart().getTime() + ((getTotalTime()+breakTime) * 60L * 1000L)));
    	
    	if(isValid){
        	if (isNew(newUser)){
            	//Setting current timestamp and user created plan record
        		newUser.setDateCreated(new Date());
        		newUser.setUserCreated(authorizedUser);
        		usersFacade.create(newUser);
        		usersFacade.addUserType(newUser, userTypesFacade.find(PATIENTS_TYPE_ID), authorizedUser);
        	}
        	
        	Date startTime = schedule.getDateTimeStart();
        	
        	for (Methods method : selectedMethods){
        		Schedule newSchedule = new ScheduleBuilder().buildFullSchedule(schedule, method, startTime);
            	scheduleFacade.edit(newSchedule);
            	LOG.info("new Schedule id: " + newSchedule.getId() + " persisted");
            	
            	//Creating corresponding Schedule event
            	event = eventFromSchedule(newSchedule);
                eventModel.addEvent(event);
                
            	startTime = newSchedule.getDateTimeEnd();

        	}
        	/*
        	//We don't have to create break after edited record it is already created
        	Schedule newSchedule = new ScheduleBuilder().buildBreakSchedule(schedule, startTime);
        	scheduleFacade.create(newSchedule);
        	LOG.info("new Schedule id: " + newSchedule.getId() + " persisted");
        	*/
        	
        	//Sending success message and closing dialog
    		final String successMessage = Message.getInstance().getMessage(Message.Schedule.SCHEDULE_EDITED);
    		final String successTitle = Message.getInstance().getMessage(Message.Validator.VALIDATOR_SUCCESS_TITLE);
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
    		RequestContext context = RequestContext.getCurrentInstance();
    		context.execute("PF('addScheduleDialog').hide();");
    	}else
    		LOG.info("Validation exception. New Schedule is not persisted!");
    }
       
    public void deleteSchedule(ActionEvent actionEvent) {
    	if (schedule != null && event != null){

    		scheduleFacade.remove(schedule);
    		eventModel.deleteEvent(event);
    		
    		LOG.info("Schedule id: " + schedule.getId() + " deleted");
    		LOG.info("Event id: " + event.getId() + " deleted");
    		
    		final String successMessage = Message.getInstance().getMessage(Message.Schedule.SCHEDULE_DELETED);
    		final String successTitle = Message.getInstance().getMessage(Message.Validator.VALIDATOR_SUCCESS_TITLE);
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
    	}
    }

    //This method controls onTransfer event in the Methods Pick List
    public void onTransfer(TransferEvent event){


        //All Users from the right picker
        List<Methods> targetList = methodsDualListModel.getTarget();

        //Indicates transfer direction true - from left to right
        Boolean addFlag = false;

        //Checking transfer direction
        if(targetList != null && targetList.contains(event.getItems().get(0)))
            addFlag = true; //Means that user transfered from left picker to right picker

        //Constructing success message
        final String successTitle = Message.getInstance().getMessage(Message.Validator.VALIDATOR_SUCCESS_TITLE);
        String successMessage;
        if(targetList != null && targetList.contains(event.getItems().get(0)))
            successMessage = Message.getInstance().getMessage(Message.Schedule.SCHEDULE_METHOD_ADD_SUCCESS_START);
        else
            successMessage = Message.getInstance().getMessage(Message.Schedule.SCHEDULE_METHOD_REMOVE_SUCCESS_START);

        //Iterating each transfered method
        for(Object methodObject : event.getItems()){
            Methods methodTransfered=(Methods)methodObject;

            //Constructing success message
            successMessage += methodTransfered.getShortName() + ", ";

            if(addFlag){
                //Add method to selected list
                selectedMethods.add(methodTransfered);
            }else{
                //Remove method from selected list
                selectedMethods.remove(methodTransfered);
            }
        }
        /*
        //Constructing success message
        if(addFlag)
            successMessage += Message.getInstance().getMessage(Message.Schedule.SCHEDULE_METHOD_ADD_SUCCESS_END);
        else
            successMessage += Message.getInstance().getMessage(Message.Schedule.SCHEDULE_METHOD_REMOVE_SUCCESS_END);

        LOG.info(successMessage);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
         */
    }
    
    //Creates Plan event from Plan record
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
    	newEvent.setStyleClass("doc" + plan.getDoctor().getId());
    	if(event != null && event.getId() != null)
    		newEvent.setId(event.getId());
    	return newEvent;
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
    	if(event != null && event.getId() != null)
    		newEvent.setId(event.getId());
    	return newEvent;
    }

    //Checks if user is new (if he has an id)
    private Boolean isNew(Users user){
    	return (user.getId() == null || usersFacade.find(user).getPhoneNumberMobile() == user.getPhoneNumberMobile());
    }
    
    private void generateCss(){
        cssStyle = "<style>";
        for(Users doctor : usersFacade.findByType(3))
            cssStyle += ".doc" + doctor.getId() + "{background-color: #" + doctor.getColor() + "}";
        cssStyle += "</style>";
    }

    //Concrete Schedule builder
    private class ScheduleBuilder extends ua.kiev.doctorvera.entities.utils.ScheduleBuilder{
    	
    	public ScheduleBuilder(){
    		super(authorizedUser);
    	}
    	
    	public Schedule buildFullSchedule(Schedule schedule, Methods method, Date startTime){
    		setDoctor(schedule.getDoctor());
    		setPatient(newUser);
    		setAssistent(schedule.getAssistent());
    		setDoctorDirected(schedule.getDoctorDirected());
    		setRoom(currentRoom);
    		setMethod(method);
    		setDateTimeStart(startTime);
    		setDateTimeEnd(new Date(startTime.getTime() + (method.getTimeInMinutes() * 60L * 1000L) ));
        	return build();
    	}
    	
    	public Schedule buildBreakSchedule(Schedule schedule, Date startTime){
    		Users breakUser = usersFacade.find(USERS_BREAK_ID);
        	setDoctor(breakUser);
        	setRoom(currentRoom);
        	setPatient(breakUser);
    		setMethod(methodsFacade.find(METHOD_BREAK_ID));
    		setDateTimeStart(startTime);
    		setDateTimeEnd(new Date(startTime.getTime() + (breakTime * 60L * 1000L) ));
    	   	return build();
    	}

    }
    
    /* --------------------------------------------*/
    /* --------- Setters && Getters ---------------*/
    /* --------------------------------------------*/
    
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
	
    public ScheduleValidator getScheduleValidator() {
		return scheduleValidator;
	}

	public void setScheduleValidator(ScheduleValidator scheduleValidator) {
		this.scheduleValidator = scheduleValidator;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public Set<String> getAllFirstNames() {
		return allFirstNames;
	}

	public Set<String> getAllMiddleNames() {
		return allMiddleNames;
	}

	public Set<String> getAllLastNames() {
		return allLastNames;
	}

	public Users getNewUser() {
		return newUser;
	}

	public void setNewUser(Users newUser) {
		this.newUser = newUser;
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

	public List<Users> getAllPatients() {
		return allPatients;
	}

	public void setAllPatients(List<Users> allPatients) {
		this.allPatients = allPatients;
	}

	public Integer getBreakTime() {
		return breakTime;
	}

	public void setBreakTime(Integer breakTime) {
		this.breakTime = breakTime;
	}

    public String getCssStyle() {
        return cssStyle;
    }

    public void setCssStyle(String cssStyle) {
        this.cssStyle = cssStyle;
    }

    public List<Methods> getSelectedMethods() {
        return selectedMethods;
    }

    public void setSelectedMethods(List<Methods> selectedMethods) {
        this.selectedMethods = selectedMethods;
    }

    public PricesFacadeLocal getPricesFacade() {
        return pricesFacade;
    }

    public void setPricesFacade(PricesFacadeLocal pricesFacade) {
        this.pricesFacade = pricesFacade;
    }

    public DualListModel<Methods> getMethodsDualListModel() {
        return methodsDualListModel;
    }

    public void setMethodsDualListModel(DualListModel<Methods> methodsDualListModel) {
        this.methodsDualListModel = methodsDualListModel;
    }

    public List<MethodTypes> getAllMethodTypes() {
        return allMethodTypes;
    }

    public void setAllMethodTypes(List<MethodTypes> allMethodTypes) {
        this.allMethodTypes = allMethodTypes;
    }

    public MethodTypes getSelectedMethodType() {
        return selectedMethodType;
    }

    public void setSelectedMethodType(MethodTypes selectedMethodType) {
        this.selectedMethodType = selectedMethodType;
    }

    public List<Users> getAllPartners() {
		return allPartners;
	}

	public void setAllPartners(List<Users> allPartners) {
		this.allPartners = allPartners;
	}

	public List<Users> getAllAssistents() {
		return allAssistents;
	}

	public void setAllAssistents(List<Users> allAssistents) {
		this.allAssistents = allAssistents;
	}
	
}
