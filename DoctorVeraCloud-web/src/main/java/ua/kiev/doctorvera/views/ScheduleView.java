package ua.kiev.doctorvera.views;

import org.primefaces.context.RequestContext;
import org.primefaces.event.*;
import org.primefaces.model.*;
import ua.kiev.doctorvera.entities.*;
import ua.kiev.doctorvera.facadeLocal.*;
import ua.kiev.doctorvera.resources.Mapping;
import ua.kiev.doctorvera.resources.Message;
import ua.kiev.doctorvera.utils.RandomPasswordGenerator;
import ua.kiev.doctorvera.utils.Utils;
import ua.kiev.doctorvera.validators.PlanValidator;
import ua.kiev.doctorvera.validators.ScheduleValidator;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

import static ua.kiev.doctorvera.resources.Message.Messages.*;

@Named(value = "scheduleView")
@ViewScoped
public class ScheduleView implements Serializable {

    private final static Logger LOG = Logger.getLogger(ScheduleView.class.getName());
    private final static long FIVE_MINUTES_IN_MILLIS = 360000;//millisecs

    private final Integer PATIENTS_TYPE_ID = Integer.parseInt(Mapping.getInstance().getString("PATIENTS_TYPE_ID"));
    private final Integer ASSISTENTS_TYPE_ID = Integer.parseInt(Mapping.getInstance().getString("ASSISTENTS_TYPE_ID"));
    private final Integer METHOD_BREAK_ID = Integer.parseInt(Mapping.getInstance().getString("METHOD_BREAK_ID"));
    private final Integer USERS_BREAK_ID = Integer.parseInt(Mapping.getInstance().getString("USERS_BREAK_ID"));
    private final Integer DOCTORS_TYPE_ID = Integer.parseInt(Mapping.getInstance().getString("DOCTORS_TYPE_ID"));
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

    @Inject
    private SessionParams sessionParams;

    @Inject
    private PlanValidator planValidator;

    @Inject
    private ScheduleValidator scheduleValidator;

    private Users authorizedUser;

    private Rooms currentRoom;

    //Data for Schedule Form Autocomplete inputs
    private final Set<String> allFirstNames = new HashSet<String>();
    private final Set<String> allMiddleNames = new HashSet<String>();
    private final Set<String> allLastNames = new HashSet<String>();

    //New User
    private Users patient;

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

    //For creation new plan
    private Plan plan = new Plan();

    private List<Rooms> allRooms;

    private List<Users> allDoctors;

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
    public void init() {
        authorizedUser = sessionParams.getAuthorizedUser();
        currentRoom = sessionParams.getScheduleRoom();
        allRooms = roomsFacade.findAll();
        allDoctors = usersFacade.findByType(DOCTORS_TYPE_ID);
        allPatients = usersFacade.findByType(PATIENTS_TYPE_ID);
        allAssistents = usersFacade.findByType(ASSISTENTS_TYPE_ID);
        schedule = new Schedule();
        patient = new Users();
        selectedMethods = new ArrayList<Methods>();
        allMethodTypes = methodTypesFacade.findAll();
        selectedMethodType = methodTypesFacade.findAll().get(0);
        breakTime = 5;

        constructPickList();

        //We will need these names for form autocomplete
        for (Users user : usersFacade.findAll()) {
            allFirstNames.add(user.getFirstName());
            allMiddleNames.add(user.getMiddleName());
            allLastNames.add(user.getLastName());
        }

        //Schedule populating
        eventModel = new LazyScheduleModel() {
            private static final long serialVersionUID = 8535371059490008142L;

            @Override
            public void loadEvents(Date start, Date end) {
                allPlan = planFacade.findByRoomAndStartDateBetweenExclusiveTo(currentRoom, start, end);
                for (Plan plan : allPlan) {
                    eventModel.addEvent(eventFromPlan(plan));
                }

                allSchedule = scheduleFacade.findByRoomAndStartDateBetweenExclusiveTo(currentRoom, start, end);
                for (Schedule schedule : allSchedule) {
                    DefaultScheduleEvent event = eventFromSchedule(schedule);
                    if (isBreakSchedule(schedule)) {
                        event.setEditable(false);
                    }
                    eventModel.addEvent(event);
                }

            }
        };
        generateCss();
    }

    /**
     * When any empty date in the schedule is clicked
     * this method have to control data from addPlanDialog
     */
    public void onDateSelect(SelectEvent selectEvent) {
        plan = new Plan();
        plan.setDateTimeStart(((Date) selectEvent.getObject()));
        plan.setDateTimeEnd(((Date) selectEvent.getObject()));
    }

    /**
     * When some schedule record was clicked this method is called
     *
     * @param selectEvent selected schedule record
     */
    public void onEventSelect(SelectEvent selectEvent) {
        event = (DefaultScheduleEvent) selectEvent.getObject();
        Object data = event.getData();
        selectedMethods.clear();

        //If Plan Event was clicked we create new Schedule Event
        // and prepare all fields for addSchedule Dialog Window
        if (data instanceof Plan) {
            Plan plan = planFacade.find(((Plan) event.getData()).getId());
            schedule = new Schedule();
            patient = new Users();
            selectedMethodType = null;
            schedule.setDoctor(plan.getDoctor());
            schedule.setDateTimeStart(getEarliestTime(plan));
            selectedMethods.clear();
            selectedMethodType = methodTypesFacade.findAll().get(0);
            constructPickList();
            event = new DefaultScheduleEvent();
            RequestContext.getCurrentInstance().execute("PF('addScheduleDialog').show()");
            // Else if Schedule event was clicked we update existing Schedule event
            // and preparing fields for addPlan Dialog Window
        } else if (data instanceof Schedule) {
            schedule = scheduleFacade.find(((Schedule) event.getData()).getId());
            patient = schedule.getPatient();
            selectedMethodType = schedule.getMethod().getMethodType();
            selectedMethods.add(schedule.getMethod());
            constructPickList();
            if(!isBreakSchedule(schedule)){
                RequestContext.getCurrentInstance().execute("PF('addScheduleDialog').show()");
            }
        }
    }

    //Checks dates of moved Schedule event and updates persistent state of this record
    public void onEventMove(ScheduleEntryMoveEvent selectEvent) {
        event = (DefaultScheduleEvent) selectEvent.getScheduleEvent();
        Object data = event.getData();

        //If Schedule Event was clicked we check dates and update
        //corresponding schedule record in the persistence storage
        if (data instanceof Schedule) {
            schedule = (Schedule) data;
            List<Schedule> nextSchedule = scheduleFacade.findByRoomAndStartDateBetweenExclusiveTo(schedule.getRoom(), event.getEndDate(), new Date(event.getEndDate().getTime() + 10l));
            nextSchedule.remove(scheduleFacade.find(schedule.getId()));
            nextSchedule.remove(scheduleFacade.findChildSchedule(schedule));
            Schedule breakSchedule = scheduleFacade.findChildSchedule(schedule);
            Boolean nextScheduleHasSamePatient = nextSchedule.size() == 1 && nextSchedule.get(0).getPatient().equals(schedule.getPatient());
            Boolean isValid;
            Long breakTime = 5l;
            if(breakSchedule != null) {
                breakTime = breakSchedule.getDateTimeEnd().getTime() - breakSchedule.getDateTimeStart().getTime();
            }
            //If next schedule record is for the same patient, there is no need for break
            if (nextScheduleHasSamePatient) {
                isValid = scheduleValidator.addScheduleValidate(schedule, currentRoom, event.getEndDate());
            } else if (breakSchedule == null) {
                isValid = scheduleValidator.addScheduleValidate(schedule, currentRoom, new Date(event.getEndDate().getTime() + (breakTime * 60L * 1000L)));
            } else {
                isValid = scheduleValidator.addScheduleValidate(schedule, currentRoom, new Date(event.getEndDate().getTime() + breakTime));
            }

            if (isValid) {
                schedule.setDateTimeStart(event.getStartDate());
                schedule.setDateTimeEnd(event.getEndDate());
                schedule.setDateCreated(new Date());
                schedule.setUserCreated(authorizedUser);
                scheduleFacade.edit(schedule);

                //Creating corresponding Schedule event
                //event = eventFromSchedule(schedule);
                //eventModel.updateEvent(event);

                //If next schedule record is for the same patient, there is no need for break
                if (breakSchedule == null && !nextScheduleHasSamePatient) {
                    //Creating break schedule record for current methods group
                    Schedule newSchedule = createBreakSchedule(schedule.getDateTimeEnd());
                    schedule = scheduleFacade.find(schedule.getId());
                    newSchedule.setParentSchedule(schedule);
                    scheduleFacade.create(newSchedule);

                    //Creating corresponding Schedule event
                    event = eventFromSchedule(newSchedule);
                    event.setEditable(false);
                    eventModel.addEvent(event);
                } else if (breakSchedule != null && !nextScheduleHasSamePatient) {
                    DefaultScheduleEvent breakEvent = findScheduleEvent(breakSchedule);
                    breakSchedule.setDateTimeStart(event.getEndDate());
                    breakSchedule.setDateTimeEnd(new Date(event.getEndDate().getTime() + breakTime));
                    breakEvent.setStartDate(event.getEndDate());
                    breakEvent.setEndDate(new Date(event.getEndDate().getTime() + breakTime));
                    scheduleFacade.edit(breakSchedule);
                    //eventModel.updateEvent(breakEvent);
                } else if (breakSchedule != null && nextScheduleHasSamePatient) {
                    DefaultScheduleEvent breakEvent = findScheduleEvent(breakSchedule);
                    scheduleFacade.remove(breakSchedule);
                }

                Message.showMessage(VALIDATOR_SUCCESS_TITLE, SCHEDULE_EDITED);//Sending success message
                RequestContext.getCurrentInstance().execute("PF('addScheduleDialog').hide();");
            } else {
                LOG.info("Validation exception. New Schedule is not persisted!");
            }
        } else if(data instanceof Plan){
            event = (DefaultScheduleEvent)selectEvent.getScheduleEvent();
            plan = planFacade.find(((Plan)event.getData()).getId());

            //Validation for crossing other Plan records
            //Check if changes are legal (if there are no scheduled records in time intervals changed)
            if(planValidator.resizePlanValidate(event.getStartDate(), event.getEndDate(),plan.getDateTimeStart(), plan.getDateTimeEnd(), currentRoom, plan)) return;
            plan.setDateCreated(new Date());
            plan.setUserCreated(authorizedUser);
            plan.setDateTimeStart(event.getStartDate());
            plan.setDateTimeEnd(event.getEndDate());

            planFacade.edit(plan);
            LOG.info("Plan id: " + plan.getId() + " updated");

            eventModel.updateEvent(event);
            LOG.info("Event id: " + event.getId() + " updated");

            Message.showMessage(PLAN_ADD_DIALOG_TITLE, PLAN_EDITED);//Sending success message
        }
    }

    public void onEventResize(ScheduleEntryResizeEvent selectEvent) {
        event = (DefaultScheduleEvent) selectEvent.getScheduleEvent();
        Object data = event.getData();

        //If Schedule Event was clicked we check dates and update
        //corresponding schedule record in the persistence storage
        if (data instanceof Plan) {
            plan = planFacade.find(((Plan) event.getData()).getId());

            //Validation for crossing other Plan records
            //Check if changes are legal (if there are no scheduled records in time intervals changed)
            if (planValidator.resizePlanValidate(event.getStartDate(), event.getEndDate(), plan.getDateTimeStart(), plan.getDateTimeEnd(), currentRoom, plan))
                return;

            plan.setDateCreated(new Date());
            plan.setUserCreated(authorizedUser);
            plan.setDateTimeStart(event.getStartDate());
            plan.setDateTimeEnd(event.getEndDate());

            planFacade.edit(plan);

            eventModel.updateEvent(event);
            Message.showMessage(PLAN_ADD_DIALOG_TITLE, PLAN_EDITED);//Sending success message
        }
    }

    /*
     * Method searches for first created methodType in the db
     * @return first created methodType

    private MethodTypes methodTypesFacade.findAll().get(0)() {
        try {
            return methodTypesFacade.findAll().get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }*/

    /**
     * Method returns sum of selected methods prices
     *
     * @return sum of selected methods prices
     */
    public Float getTotalPrice() {
        Float totalPrice = new Float(0);
        for (Methods method : selectedMethods) {
            totalPrice += pricesFacade.findLastPrice(method).getTotal();
        }
        return totalPrice;
    }

    /**
     * Method returns full time that selected methods will take
     *
     * @return full time that selected methods will take
     */
    public Integer getTotalTime() {
        Integer totalTime = 0;
        for (Methods method : selectedMethods) {
            totalTime += method.getTimeInMinutes();
        }
        return totalTime;
    }

    /**
     * This method determines the earliest available time in the given Plan Record
     *
     * @param plan plan record in which to search for first available time
     * @return date and time of the last scheduled record in the given plan or start date and time of the given plan record
     */
    private Date getEarliestTime(Plan plan) {
        List<Schedule> scheduledRecords = scheduleFacade.findByRoomAndEndDateBetweenExclusiveFrom(currentRoom, plan.getDateTimeStart(), plan.getDateTimeEnd());
        //Sorting by Date Start
        Collections.sort(scheduledRecords, new Comparator<Schedule>() {
            @Override
            public int compare(Schedule schedule1, Schedule schedule2) {

                return schedule1.getDateTimeEnd().compareTo(schedule2.getDateTimeEnd());
            }
        });

        if (scheduledRecords.size() == 0)
            return plan.getDateTimeStart();
        else {
            return scheduledRecords.get(scheduledRecords.size() - 1).getDateTimeEnd();
        }
    }

    /**
     * Constructs Methods Picklist
     */
    public void constructPickList() {
        if (selectedMethodType != null && selectedMethodType.getId() != null) {
            List<Methods> allMethods = methodsFacade.findByType(selectedMethodType);

            for (Methods method : selectedMethods) {
                allMethods.remove(method);
            }
            methodsDualListModel = new DualListModel<Methods>(allMethods, selectedMethods);
        } else
            methodsDualListModel = new DualListModel<Methods>(new ArrayList<Methods>(), new ArrayList<Methods>());
    }


    /**
     * Fills users data if phone number fits existing one
     */
    public void fillUserData() {
        List<Users> usersByPhone = usersFacade.findByPhoneNumberMobile(patient.getPhoneNumberMobile());

        if (usersByPhone.size() == 1)
            patient = usersByPhone.get(0);
    }

    /**
     * This method is addSchedule listener form Schedule.xhtml
     * It contains creating and persisting Schedule record, break record, creating schedule event and updating schedule
     * and notifying user about success or error
     */
    public void addSchedule(ActionEvent actionEvent) {
        //Dates validation
        Boolean isValid = scheduleValidator.addScheduleValidate(schedule, currentRoom, new Date(schedule.getDateTimeStart().getTime() + ((getTotalTime() + breakTime) * 60L * 1000L)));

        if (isValid) {

            if (isNew(patient)) { // Creating new user
                createpatient(patient, authorizedUser);
                usersFacade.create(patient);
                usersFacade.addUserType(patient, userTypesFacade.find(PATIENTS_TYPE_ID), authorizedUser);
                //TODO send email with password
            }

            Date startTime = schedule.getDateTimeStart();
            for (Methods method : selectedMethods) {
                //Creating schedule record for current method
                createNewSchedule(method, startTime);
                startTime = schedule.getDateTimeEnd();
                scheduleFacade.create(schedule);

                //Creating corresponding Schedule event
                event = eventFromSchedule(schedule);
                eventModel.addEvent(event);
            }

            //Creating break schedule record for current methods group
            Schedule newSchedule = createBreakSchedule(startTime);
            newSchedule.setParentSchedule(schedule);
            scheduleFacade.create(newSchedule);

            //Creating corresponding Schedule event
            event = eventFromSchedule(newSchedule);
            event.setEditable(false);
            eventModel.addEvent(event);

            Message.showMessage(VALIDATOR_SUCCESS_TITLE, SCHEDULE_SAVED);//Sending success message
            RequestContext.getCurrentInstance().execute("PF('addScheduleDialog').hide();"); //Closing dialog window
        } else {
            LOG.warning("Validation exception. New Schedule is not persisted!");
        }

    }

    /**
     * This method is edit Schedule form listener form schedule.xhtml
     * It contains updating Schedule record, corresponing break record, updating Schedule event and updating PF.schedule
     * and notifying user about success or error
     *
     * @param actionEvent
     */
    public void editSchedule(ActionEvent actionEvent) {
        Long validationEndDate = schedule.getDateTimeStart().getTime() + getTotalTime() * 60L * 1000L;
        List<Schedule> nextSchedule = scheduleFacade.findByRoomAndStartDateBetweenExclusiveTo(schedule.getRoom(), new Date(validationEndDate), new Date(validationEndDate + 10l));
        nextSchedule.remove(scheduleFacade.find(schedule.getId()));
        nextSchedule.remove(scheduleFacade.findChildSchedule(schedule));
        Boolean isValid;
        Boolean nextScheduleHasSamePatient = nextSchedule.size() == 1 && nextSchedule.get(0).getPatient().equals(schedule.getPatient());
        //If next schedule record is for the same patient, there is no need for break
        if (nextScheduleHasSamePatient) {
            isValid = scheduleValidator.addScheduleValidate(schedule, currentRoom, new Date(schedule.getDateTimeStart().getTime() + (getTotalTime() * 60L * 1000L)));
        } else {
            isValid = scheduleValidator.addScheduleValidate(schedule, currentRoom, new Date(schedule.getDateTimeStart().getTime() + ((getTotalTime() + breakTime) * 60L * 1000L)));
        }

        if (isValid) {
            if (isNew(patient)) { // Creating new user
                createpatient(patient, authorizedUser);
                usersFacade.create(patient);
                usersFacade.addUserType(patient, userTypesFacade.find(PATIENTS_TYPE_ID), authorizedUser);
                //TODO send email with password
            }

            // In case that more than one method is selected we delete edited record
            // and create new schedule records for each method
            Date startTime = schedule.getDateTimeStart();
            if (selectedMethods.size() > 1) {
                Schedule breakSchedule = schedule.getParentSchedule();

                //Removing current schedule record
                scheduleFacade.remove(schedule);
                for (Methods method : selectedMethods) {
                    //Creating schedule record for current method
                    createNewSchedule(method, startTime);
                    startTime = schedule.getDateTimeEnd();
                    scheduleFacade.create(schedule);

                    //Creating corresponding Schedule event
                    event = eventFromSchedule(schedule);
                    eventModel.addEvent(event);
                }
                schedule.setParentSchedule(breakSchedule);

            } else {
                editSchedule();
                scheduleFacade.edit(schedule);

                //Creating corresponding Schedule event
                event = eventFromSchedule(schedule);
                eventModel.addEvent(event);

                startTime = schedule.getDateTimeEnd();
            }

            //If next schedule record is for the same patient, there is no need for break
            Schedule breakSchedule = scheduleFacade.findChildSchedule(schedule);
            if (breakSchedule == null && !nextScheduleHasSamePatient) {
                //Creating break schedule record for current methods group
                Schedule newSchedule = createBreakSchedule(startTime);
                schedule = scheduleFacade.find(schedule.getId());
                newSchedule.setParentSchedule(schedule);
                scheduleFacade.create(newSchedule);

                //Creating corresponding Schedule event
                event = eventFromSchedule(newSchedule);
                event.setEditable(false);
                eventModel.addEvent(event);
            } else if (breakSchedule != null && !nextScheduleHasSamePatient) {
                breakSchedule.setDateTimeStart(new Date(schedule.getDateTimeStart().getTime() + getTotalTime() * 60L * 1000L));
                breakSchedule.setDateTimeEnd(new Date(startTime.getTime() + (breakTime * 60L * 1000L)));
                scheduleFacade.edit(breakSchedule);
            } else if (breakSchedule != null && nextScheduleHasSamePatient){
                DefaultScheduleEvent breakEvent = findScheduleEvent(breakSchedule);
                scheduleFacade.remove(breakSchedule);
            }

            Message.showMessage(VALIDATOR_SUCCESS_TITLE, SCHEDULE_EDITED);//Sending success message
            RequestContext.getCurrentInstance().execute("PF('addScheduleDialog').hide();");
        } else
            LOG.info("Validation exception. New Schedule is not persisted!");
    }

    public void deleteSchedule(ActionEvent actionEvent) {
        if (schedule != null && event != null) {
            Schedule breakRecord = scheduleFacade.findChildSchedule(schedule);
            if (breakRecord != null) {
                scheduleFacade.remove(breakRecord);
            }
            scheduleFacade.remove(schedule);
            eventModel.deleteEvent(event);

            LOG.info("Schedule id: " + schedule.getId() + " deleted");
            LOG.info("Event id: " + event.getId() + " deleted");

            Message.showMessage(VALIDATOR_SUCCESS_TITLE, SCHEDULE_DELETED);
        }
    }

    // This method is add Plan form listener form schedule.xhtml
    // It contains creating and persisting Plan record, creating schedule event and updating schedule
    // and notifying user about success or error
    public void addPlan(ActionEvent actionEvent) {
        //Setting current timestamp and user created plan record
        plan.setDateCreated(new Date());
        plan.setUserCreated(authorizedUser);

        //When we create new Plan record Id of Plan and event are null
        if (plan.getId() == null || event.getId() == null) {
            //Validation for crossing other Plan records
            if (planValidator.addPlanValidate(plan.getDateTimeStart(), plan.getDateTimeEnd(), currentRoom, null))
                return;

            planFacade.create(plan);
            event = eventFromPlan(plan);
            eventModel.addEvent(event);

            Message.showMessage(PLAN_ADD_DIALOG_TITLE, PLAN_SAVED);
            RequestContext.getCurrentInstance().execute("PF('addPlanDialog').hide();");
        } else
            LOG.info("Something goes wrong event Id:" + event.getId() + " Plan entity: " + plan);
    }

    //This method controls onTransfer event in the Methods Pick List
    public void onTransfer(TransferEvent event) {


        //All Users from the right picker
        List<Methods> targetList = methodsDualListModel.getTarget();

        //Indicates transfer direction true - from left to right
        Boolean addFlag = false;

        //Checking transfer direction
        if (targetList != null && targetList.contains(event.getItems().get(0)))
            addFlag = true; //Means that user transfered from left picker to right picker

        //Constructing success message
        final String successTitle = Message.getInstance().getString("VALIDATOR_SUCCESS_TITLE");
        String successMessage;
        if (targetList != null && targetList.contains(event.getItems().get(0)))
            successMessage = Message.getInstance().getString("SCHEDULE_METHOD_ADD_SUCCESS_START");
        else
            successMessage = Message.getInstance().getString("SCHEDULE_METHOD_REMOVE_SUCCESS_START");

        //Iterating each transfered method
        for (Object methodObject : event.getItems()) {
            Methods methodTransfered = (Methods) methodObject;

            //Constructing success message
            successMessage += methodTransfered.getShortName() + ", ";

            if (addFlag) {
                //Add method to selected list
                selectedMethods.add(methodTransfered);
            } else {
                //Remove method from selected list
                selectedMethods.remove(methodTransfered);
            }
        }
        /*
        //Constructing success message
        if(addFlag)
            successMessage += Message.getInstance().getMessage("SCHEDULE_METHOD_ADD_SUCCESS_END);
        else
            successMessage += Message.getInstance().getMessage("SCHEDULE_METHOD_REMOVE_SUCCESS_END);

        LOG.info(successMessage);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
         */
    }

    //Creates Plan event from Plan record
    private DefaultScheduleEvent eventFromPlan(Plan plan) {
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
        newEvent.setStyleClass("fc-plan doc" + plan.getDoctor().getId());
        if (event != null && event.getId() != null)
            newEvent.setId(event.getId());
        return newEvent;
    }

    //Creates Schedule event from Schedule record
    private DefaultScheduleEvent eventFromSchedule(Schedule schedule) {
        DefaultScheduleEvent newEvent;
        //If we process break record schedule will not have a patient
        if (schedule.getPatient() != null) {
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
        } else {
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
        newEvent.setStyleClass("fc-schedule doc" + schedule.getDoctor().getId());
        if (event != null && event.getId() != null)
            newEvent.setId(event.getId());
        return newEvent;
    }

    //Checks if user is new (if he has an id)
    private Boolean isNew(Users user) {
        return (user.getId() == null || usersFacade.find(user).getPhoneNumberMobile() == user.getPhoneNumberMobile());
    }

    private void generateCss() {
        cssStyle = "<style>";
        for (Users doctor : usersFacade.findByType(3))
            cssStyle += ".doc" + doctor.getId() + "{background-color: #" + doctor.getColor() + "}";
        cssStyle += "</style>";
    }

    public DefaultScheduleEvent findScheduleEvent(Schedule schedule) {
        List<ScheduleEvent> events = eventModel.getEvents();
        for (ScheduleEvent event : events) {
            if (event.getData().equals(schedule)) return (DefaultScheduleEvent) event;
        }
        return null;
    }


    public void createpatient(Users patient, Users authorizedUser) {
        patient.setDateCreated(new Date());
        patient.setUserCreated(authorizedUser);
        patient.setUsername(Utils.generateUsername(patient.getLastName(), patient.getFirstName()));
        patient.setPassword(RandomPasswordGenerator.generatePswd(8, 10, 2, 2, 1).toString());
    }

    public void createNewSchedule(Methods method, Date startTime) {
        schedule.setId(null);
        schedule.setPatient(patient);
        schedule.setRoom(currentRoom);
        schedule.setMethod(method);
        schedule.setDateTimeStart(startTime);
        schedule.setDateTimeEnd(new Date(startTime.getTime() + (method.getTimeInMinutes() * 60L * 1000L)));
        schedule.setUserCreated(authorizedUser);
        schedule.setDateCreated(new Date());
        schedule.setDeleted(false);
    }

    public void editSchedule() {
        schedule.setPatient(patient);
        schedule.setRoom(currentRoom);
        schedule.setDateTimeEnd(new Date(schedule.getDateTimeStart().getTime() + (schedule.getMethod().getTimeInMinutes() * 60L * 1000L)));
        schedule.setUserCreated(authorizedUser);
        schedule.setDateCreated(new Date());
        schedule.setDeleted(false);
    }

    public Schedule createBreakSchedule(Date startTime) {
        Schedule schedule = new Schedule();
        Users breakUser = usersFacade.find(USERS_BREAK_ID);
        schedule.setDoctor(breakUser);
        schedule.setRoom(currentRoom);
        schedule.setPatient(breakUser);
        schedule.setMethod(methodsFacade.find(METHOD_BREAK_ID));
        schedule.setDateTimeStart(startTime);
        schedule.setDateTimeEnd(new Date(startTime.getTime() + (breakTime * 60L * 1000L)));
        schedule.setUserCreated(authorizedUser);
        schedule.setDateCreated(new Date());
        return schedule;
    }

    private boolean isBreakSchedule(Schedule schedule) {
        return (schedule.getParentSchedule() != null);
    }

    public String onFlowProcess(FlowEvent event) {
            return event.getNewStep();
    }

    /* --------------------------------------------*/
    /* --------- Setters && Getters ---------------*/
    /* --------------------------------------------*/
    public PlanValidator getPlanValidator() {
        return planValidator;
    }

    public void setPlanValidator(PlanValidator planValidator) {
        this.planValidator = planValidator;
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

    public void printForm(){
        // TODO implement
        Message.showError(LOGIN_ERROR_TITLE, APPLICATION_NOT_IMPLEMENTED);
    }

    public void printSecureAgreement(){
        // TODO implement
        Message.showError(LOGIN_ERROR_TITLE, APPLICATION_NOT_IMPLEMENTED);
    }

    public void printPrintIncomeForm(){
        // TODO implement
        Message.showError(LOGIN_ERROR_TITLE, APPLICATION_NOT_IMPLEMENTED);
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

    public Users getpatient() {
        return patient;
    }

    public void setpatient(Users patient) {
        this.patient = patient;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = (DefaultScheduleEvent) event;
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

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public List<Rooms> getAllRooms() {
        return allRooms;
    }

    public void setAllRooms(List<Rooms> allRooms) {
        this.allRooms = allRooms;
    }

    public List<Users> getAllDoctors() {
        return allDoctors;
    }

    public void setAllDoctors(List<Users> allDoctors) {
        this.allDoctors = allDoctors;
    }

    public Users getAuthorizedUser() {
        return authorizedUser;
    }

    public void setAuthorizedUser(Users authorizedUser) {
        this.authorizedUser = authorizedUser;
    }

    public Rooms getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Rooms currentRoom) {
        this.currentRoom = currentRoom;
    }
}
