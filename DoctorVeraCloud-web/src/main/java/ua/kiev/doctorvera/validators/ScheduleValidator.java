package ua.kiev.doctorvera.validators;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.validate.ClientValidator;

import ua.kiev.doctorvera.entities.Plan;
import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.entities.Schedule;
import ua.kiev.doctorvera.facade.PlanFacadeLocal;
import ua.kiev.doctorvera.facade.RoomsFacadeLocal;
import ua.kiev.doctorvera.facade.ScheduleFacadeLocal;
import ua.kiev.doctorvera.web.resources.Message;

@ManagedBean(name = "scheduleValidator")
@SessionScoped
public class ScheduleValidator implements Validator, ClientValidator {
	
    //@ManagedProperty(value="#{planView.plan}")
	//private Plan plan;
	
	private final String ERROR_TITLE = Message.getInstance().getMessage(Message.Validator.VALIDATOR_ERROR_TITLE) + "\n";
	private final String ERROR_UPDATE_MESSAGE = Message.getInstance().getMessage(Message.Plan.PLAN_VALIDATE_SCHEDULE_UPDATE);
	
	@EJB
	private RoomsFacadeLocal roomsFacade;
	
	@EJB
	private PlanFacadeLocal planFacade;
	
	@EJB
	private ScheduleFacadeLocal scheduleFacade;
	
    @ManagedProperty(value="#{sessionParams.scheduleRoom}")
    private Rooms currentRoom;
	
	@Override
	public Map<String, Object> getMetadata() {
		return null;
	}

	@Override
	public String getValidatorId() {
		return "scheduleValidator";
	}

	public void setCurrentRoom(Rooms currentRoom) {
		this.currentRoom = currentRoom;
	}

	//Validating plan form so that start date can't be after end date
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String fieldName = (String)component.getAttributes().get("name");
		String message = "";

		switch(fieldName){
			case "dateTimeStart":
				message = validateStartTime((Date)value);
				break;
		}
		 
		if(message.equals("")) {
			return;
			}
		else 
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_TITLE, message));
		
		
		
	}
	
	//Checking if start time is inside any Plans record and not inside any Schedule record
	private String validateStartTime(Date startTime){
		//Checking is date is inside of any Plan record
		if(planFacade.findByRoomAndDateInside(currentRoom, startTime) == null)
			return Message.getInstance().getMessage(Message.Schedule.SCHEDULE_VALIDATE_NOT_IN_PLAN_START);
		
		//Checking if date is not crossed with any existing schedule record
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Schedule scheduleCrossed = scheduleFacade.findByRoomAndDateInside(currentRoom, startTime);
		if(scheduleCrossed != null)
			return Message.getInstance().getMessage(Message.Schedule.SCHEDULE_VALIDATE_SCHEDULE_UPDATE)+
					formater.format(scheduleCrossed.getDateTimeStart()) + " - " + 
					formater.format(scheduleCrossed.getDateTimeEnd()) + " " + 
					scheduleCrossed.getDoctor().getFirstName() + scheduleCrossed.getDoctor().getLastName() + "\n";
		
		return "";
	}
	
	
	//Validation on addSchedule event and part of validation on update event
	public Boolean addScheduleValidate(Schedule currentSchedule, Rooms currentRoom, Date end){
		FacesContext context = FacesContext.getCurrentInstance();
		String errorMessage = null;
		
		//Schedule event start and end time must be inside plans record time interval 
		if(!isInsidePlan(currentSchedule, currentRoom, end)){
			errorMessage = Message.getInstance().getMessage(Message.Schedule.SCHEDULE_VALIDATE_NOT_IN_PLAN);
			FacesMessage fMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_TITLE , errorMessage);
			context.addMessage(null, fMessage);
			return false;
		}
		
		//Must have zero size or error message should be shown(means other Schedule events are recorded already)
		HashSet<Schedule> schedulesCrossed = crossSchedule(currentRoom, currentSchedule.getDateTimeStart(), end);
		if (currentSchedule.getId() != null)
			schedulesCrossed.remove(currentSchedule);
		
		if(schedulesCrossed.size() != 0){
			errorMessage = Message.getInstance().getMessage(Message.Schedule.SCHEDULE_VALIDATE_SCHEDULE_UPDATE);
			FacesMessage fMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_TITLE , errorMessage);
			context.addMessage(null, fMessage);
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			for (Schedule scheduleCrossed : schedulesCrossed){
				errorMessage =
					formater.format(scheduleCrossed.getDateTimeStart()) + " - " + 
					formater.format(scheduleCrossed.getDateTimeEnd()) + " " + 
					scheduleCrossed.getDoctor().getFirstName() + scheduleCrossed.getDoctor().getLastName() + "\n";
				fMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_TITLE , errorMessage);
				context.addMessage(null, fMessage);

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
		HashSet<Schedule> scheduleList = new HashSet<Schedule>();
		scheduleList.addAll(scheduleFacade.findByRoomAndStartDate(currentRoom, start, end));
		scheduleList.addAll(scheduleFacade.findByRoomAndEndDate(currentRoom, start, end));
		scheduleList.addAll(scheduleFacade.findByRoomAndDatesInside(currentRoom, start, end));
		return scheduleList;
	};

	
	
	
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