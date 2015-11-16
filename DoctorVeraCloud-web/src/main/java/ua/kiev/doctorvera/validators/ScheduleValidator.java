package ua.kiev.doctorvera.validators;

import org.joda.time.DateTime;
import org.primefaces.validate.ClientValidator;
import ua.kiev.doctorvera.entities.Plan;
import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.entities.Schedule;
import ua.kiev.doctorvera.facadeLocal.PlanFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.RoomsFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.ScheduleFacadeLocal;
import ua.kiev.doctorvera.resources.Message;
import ua.kiev.doctorvera.views.SessionParams;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import static ua.kiev.doctorvera.resources.Message.Messages.*;

@Named(value = "scheduleValidator")
@ViewScoped
public class ScheduleValidator implements Validator, ClientValidator,Serializable {
	
    //@Named(value="#{planView.plan}")
	//private Plan plan;
	
	private final String ERROR_TITLE = Message.getInstance().getString("VALIDATOR_ERROR_TITLE") + "\n";
	private final String ERROR_UPDATE_MESSAGE = Message.getInstance().getString("PLAN_VALIDATE_SCHEDULE_UPDATE");

	@EJB
	private RoomsFacadeLocal roomsFacade;
	
	@EJB
	private PlanFacadeLocal planFacade;
	
	@EJB
	private ScheduleFacadeLocal scheduleFacade;

	@PostConstruct
	public void init(){}

	@Override
	public Map<String, Object> getMetadata() {
		return null;
	}

	@Override
	public String getValidatorId() {
		return "scheduleValidator";
	}

	//Validating plan form so that start date can't be after end date
	public void validateStartDate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		Schedule schedule = (Schedule)component.getAttributes().get("schedule");
		String message = validateStartTime((Date)value, schedule);

		if(message.equals("")) {
			return;
			}
		else 
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_TITLE, message));
		
		
		
	}

	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {

	}

	//Checking if start time is inside any Plans record and not inside any Schedule record
	private String validateStartTime(Date startDate, Schedule schedule){
		String message = "";
		//Checking if date is inside of any Plan record
		if(planFacade.findByRoomAndDateInside(schedule.getRoom(), startDate) == null)
			return Message.getInstance().getString("SCHEDULE_VALIDATE_NOT_IN_PLAN_START");
		
		//Checking if date is not crossed with any existing schedule record
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Schedule scheduleCrossed = scheduleFacade.findByRoomAndDateInside(schedule.getRoom(), startDate);
		if (scheduleCrossed == null) return "";
		//Creating Validation Error message for crossed schedule
		Schedule breakSchedule = scheduleFacade.findChildSchedule(schedule);
		if (!scheduleCrossed.equals(schedule) && !scheduleCrossed.equals(breakSchedule)) {
			message += Message.getInstance().getString("SCHEDULE_VALIDATE_SCHEDULE_UPDATE") +
					formater.format(scheduleCrossed.getDateTimeStart()) + " - " +
					formater.format(scheduleCrossed.getDateTimeEnd()) + " " +
					scheduleCrossed.getDoctor().getFirstName() + scheduleCrossed.getDoctor().getLastName() + "\n";
		}
		if (breakSchedule==null) return message;

		startDate = computeBreakStartDate(startDate, schedule);
		scheduleCrossed = scheduleFacade.findByRoomAndDateInside(schedule.getRoom(), startDate);
		if (scheduleCrossed == null) return "";
		if (!scheduleCrossed.equals(schedule) && !scheduleCrossed.equals(breakSchedule)) {
			message += Message.getInstance().getString("SCHEDULE_VALIDATE_SCHEDULE_UPDATE") +
					formater.format(scheduleCrossed.getDateTimeStart()) + " - " +
					formater.format(scheduleCrossed.getDateTimeEnd()) + " " +
					scheduleCrossed.getDoctor().getFirstName() + scheduleCrossed.getDoctor().getLastName() + "\n";
		}
		return message;
	}

	//Calculates end time of the scheduled record depending on methods duration. This will be the start of the break
	private Date computeBreakStartDate(Date startDate, Schedule schedule){
		DateTime startTime = new DateTime(startDate);
		return (startTime.plusMinutes(schedule.getMethod().getTimeInMinutes())).toDate();
	}
	
	
	//Validation on addSchedule event and part of validation on update event
	public Boolean addScheduleValidate(Schedule currentSchedule, Rooms currentRoom, Date end){
		
		//Schedule event start and end time must be inside plans record time interval 
		if(!isInsidePlan(currentSchedule, currentRoom, end)){
			Message.showError(ERROR_TITLE, SCHEDULE_VALIDATE_NOT_IN_PLAN);
			return false;
		}
		
		//Must have zero size or error message should be shown(means other Schedule events are recorded already)
		HashSet<Schedule> schedulesCrossed = crossSchedule(currentRoom, currentSchedule.getDateTimeStart(), end);
		for(Iterator<Schedule> iter = schedulesCrossed.iterator(); iter.hasNext();) {
			Schedule schedule = iter.next();
			//Current schedule can't cross itself
			if (currentSchedule.getId() != null && (currentSchedule.getId().equals(schedule.getId()))) {
				iter.remove();
			}
			//Current schedule record can't cross it's break record
			if ((schedule.getParentSchedule()!= null && currentSchedule.getId().equals(schedule.getParentSchedule().getId()))){
				iter.remove();
			}
		}

		if(schedulesCrossed.size() != 0){
			Message.showError(ERROR_TITLE, SCHEDULE_VALIDATE_SCHEDULE_UPDATE);

			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			for (Schedule scheduleCrossed : schedulesCrossed){
				String errorMessage =
					formater.format(scheduleCrossed.getDateTimeStart()) + " - " + 
					formater.format(scheduleCrossed.getDateTimeEnd()) + " " + 
					scheduleCrossed.getDoctor().getFirstName() + scheduleCrossed.getDoctor().getLastName() + "\n";
				Message.showError(ERROR_TITLE, errorMessage);

			}
			return false;
		}else
			return true;
	}
	
	private  Boolean isInsidePlan(Schedule currentSchedule, Rooms currentRoom, Date end){
		List<Plan> insidePlans = planFacade.findByRoomAndDatesInsidePlanOrEqual(currentRoom, currentSchedule.getDateTimeStart(), end);
		if (insidePlans.size() == 1) 
			return true;
		else 
			return false;
	}
	
	private HashSet<Schedule> crossSchedule(Rooms currentRoom, Date start, Date end){
		HashSet<Schedule> scheduleList = new HashSet<>();
		scheduleList.addAll(scheduleFacade.findByRoomAndStartDateBetweenExclusiveTo(currentRoom, start, end));
		scheduleList.addAll(scheduleFacade.findByRoomAndEndDateBetweenExclusiveFrom(currentRoom, start, end));
		scheduleList.addAll(scheduleFacade.findByRoomAndDatesInsideSchedule(currentRoom, start, end));
		return scheduleList;
	}

	
	
	
	/*
    public boolean updatePlanValidate(Date newStart, Date newEnd, Date oldStart, Date oldEnd, Rooms currentRoom, Plan currentPlan){
		//Validation for crossing other Plan records
		Boolean error = addPlanValidate(newStart, newEnd, currentRoom, currentPlan);
		if(error) return true;
		
		//Check if changes are legal (if there are no scheduled records in time intervals changed)
		if(oldStart.before(newStart))
			error = crossScheduleValidateOnUpdate(oldStart, newStart, currentRoom);
		else if(oldStart.after(newStart))
			error = crossScheduleValidateOnUpdate(newStart, oldStart, currentRoom);
		if(oldEnd.before(newEnd))	
			error = crossScheduleValidateOnUpdate(oldEnd, newEnd, currentRoom);
		else if(oldEnd.after(newEnd))	
			error = crossScheduleValidateOnUpdate(newEnd, oldEnd, currentRoom);
		if(error)  return true;
		return false;
    }
    
	public Boolean deletePlanValidate(Date start, Date end, Rooms room){
		//The list of Schedule records in dates between start and end
		//Must have zero size or error message should be shown
		HashSet<Schedule> scheduleCrossed = getScheduleRecords(start, end, room);
		String errorMessage = null;
		
		if(scheduleCrossed.size() != 0){
			errorMessage = errorUpdateMessage;
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage fMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorTitle , errorMessage);
			context.addMessage(null, fMessage);
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for (Schedule schedule : scheduleCrossed){
				errorMessage =
					formater.format(schedule.getDateTimeStart()) + " - " + 
					formater.format(schedule.getDateTimeEnd()) + " " + 
					schedule.getDoctor().getFirstName() + schedule.getDoctor().getLastName() + "\n";
				fMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorTitle , errorMessage);
				context.addMessage(null, fMessage);
			}
			return true;
    		//throw new ValidatorException(fMessage);
		}
		return false;
	}
    
	//Part of validation on move, resize events
	private Boolean crossPlanValidateOnResize(Date start, Date end, Rooms room, Plan currentPlan){
		//The list of Plan records crossed with the current Plan record
		//Must have zero size or error message should be shown
		HashSet<Plan> plansCrossed = crossPlan(start, end, room);
		String errorMessage = null;
		
		if(plansCrossed.size() != 0){
			errorMessage = errorUpdateMessage;

			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for (Plan plan : plansCrossed){
				if(currentPlan!=null && currentPlan.equals(plan)) continue;
				errorMessage +=
					formater.format(plan.getDateTimeStart()) + " - " + 
					formater.format(plan.getDateTimeEnd()) + " " + 
					plan.getDoctor().getFirstName() + plan.getDoctor().getLastName() + "\n";
			}
			FacesMessage fMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorTitle , errorMessage);
			FacesContext.getCurrentInstance().addMessage(null, fMessage);
			return true;
    		//throw new ValidatorException(fMessage);
		}
		return false;
	}
	
	private Boolean crossScheduleValidateOnUpdate(Date start, Date end, Rooms room){
		//The list of Schedule records in dates between start and end
		//Must have zero size or error message should be shown
		HashSet<Schedule> scheduleCrossed = getScheduleRecords(start, end, room);
		String errorMessage = null;
		
		if(scheduleCrossed.size() != 0){
			errorMessage = errorUpdateMessage;
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage fMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorTitle , errorMessage);
			context.addMessage(null, fMessage);
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for (Schedule schedule : scheduleCrossed){
				errorMessage =
					formater.format(schedule.getDateTimeStart()) + " - " + 
					formater.format(schedule.getDateTimeEnd()) + " " + 
					schedule.getDoctor().getFirstName() + schedule.getDoctor().getLastName() + "\n";
				fMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorTitle , errorMessage);
				context.addMessage(null, fMessage);
			}
			return true;
    		//throw new ValidatorException(fMessage);
		}
		return false;
	}
	
	private Boolean crossScheduleValidateOnResize(Date start, Date end, Rooms room){
		//The list of Schedule records in dates between start and end
		//Must have zero size or error message should be shown
		HashSet<Schedule> scheduleCrossed = getScheduleRecords(start, end, room);
		String errorMessage = null;
		
		if(scheduleCrossed.size() != 0){
			errorMessage = errorUpdateMessage;
			
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for (Schedule schedule : scheduleCrossed){
				errorMessage +=
					formater.format(schedule.getDateTimeStart()) + " - " + 
					formater.format(schedule.getDateTimeEnd()) + " " + 
					schedule.getDoctor().getFirstName() + schedule.getDoctor().getLastName() + "\n";
			}
			
			FacesMessage fMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorTitle , errorMessage);
			FacesContext.getCurrentInstance().addMessage(null, fMessage);
			return true;
    		//throw new ValidatorException(fMessage);
		}
		return false;
	}
	private HashSet<Plan> crossPlan(Date start, Date end, Rooms room){
		HashSet<Plan> planList = new HashSet<Plan>();
		planList.addAll(planFacade.findByRoomAndStartDate(room, start, end));
		planList.addAll(planFacade.findByRoomAndEndDate(room, start, end));
		planList.addAll(planFacade.findByRoomAndDatesInside(room, start, end));
		return planList;
	}
	
	*/
	
}