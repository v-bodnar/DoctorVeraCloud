package ua.kiev.doctorvera.validators;

import org.primefaces.validate.ClientValidator;
import ua.kiev.doctorvera.entities.Plan;
import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.entities.Schedule;
import ua.kiev.doctorvera.facadeLocal.PlanFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.RoomsFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.ScheduleFacadeLocal;
import ua.kiev.doctorvera.web.resources.Message;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

@ManagedBean(name = "planValidator")
@SessionScoped
public class PlanValidator implements Validator, ClientValidator {
	
    //@ManagedProperty(value="#{planView.plan}")
	//private Plan plan;
	
	private final String errorTitle = Message.getInstance().getMessage(Message.Validator.VALIDATOR_ERROR_TITLE) + "\n";
	private final String errorUpdateMessage = Message.getInstance().getMessage(Message.Plan.PLAN_VALIDATE_SCHEDULE_UPDATE);
	
	@EJB
	private RoomsFacadeLocal roomsFacade;
	
	@EJB
	private PlanFacadeLocal planFacade;
	
	@EJB
	private ScheduleFacadeLocal scheduleFacade;
	
	@Override
	public Map<String, Object> getMetadata() {
		return null;
	}

	@Override
	public String getValidatorId() {
		return "planValidator";
	}

	//Validating plan form so that start date can't be after end date
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		UIInput dateTimeStartInput = (UIInput)context.getViewRoot().findComponent(":planForm:dateTimeStart");
		UIInput dateTimeEndInput = (UIInput)context.getViewRoot().findComponent(":planForm:dateTimeEnd");
		
		Date start = (Date)dateTimeStartInput.getValue();
		Date end = (Date)dateTimeEndInput.getValue();
		
		String message = "";

		if(start != null && end != null && !start.before(end)) 
			message = Message.getInstance().getMessage(Message.Plan.PLAN_VALIDATE_DATE);
		
		if(message.equals(""))
			return;
		else 
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, errorTitle, message));
	}
	
	//Validation on addPlan event and part of validation on update event
	public Boolean addPlanValidate(Date start, Date end, Rooms room, Plan currentPlan){
		//The list of Plan records crossed with the current Plan record
		//Must have zero size or error message should be shown
		HashSet<Plan> plansCrossed = crossPlan(start, end, room);
		String errorMessage = null;
		
		if(plansCrossed.size() != 0){
			errorMessage = Message.getInstance().getMessage(Message.Plan.PLAN_VALIDATE_SCHEDULE_UPDATE);
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage fMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorTitle , errorMessage);
			context.addMessage(null, fMessage);
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for (Plan plan : plansCrossed){
				if(currentPlan!=null && currentPlan.equals(plan)) continue;
				errorMessage =
					formater.format(plan.getDateTimeStart()) + " - " + 
					formater.format(plan.getDateTimeEnd()) + " " + 
					plan.getDoctor().getFirstName() + plan.getDoctor().getLastName() + "\n";
				fMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorTitle , errorMessage);
				context.addMessage(null, fMessage);
				return true;
			}
			
    		//throw new ValidatorException(fMessage);
		}
		return false;
	}
	
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
    
    public boolean resizePlanValidate(Date newStart, Date newEnd, Date oldStart, Date oldEnd, Rooms currentRoom, Plan currentPlan){
		//Validation for crossing other Plan records
		Boolean error = crossPlanValidateOnResize(newStart, newEnd, currentRoom, currentPlan);
		if(error) return true;
		
		//Check if changes are legal (if there are no scheduled records in time intervals changed)
		if(oldStart.before(newStart))
			error = crossScheduleValidateOnResize(oldStart, newStart, currentRoom);
		else if(oldStart.after(newStart))
			error = crossScheduleValidateOnResize(newStart, oldStart, currentRoom);
		if(oldEnd.before(newEnd))	
			error = crossScheduleValidateOnResize(oldEnd, newEnd, currentRoom);
		else if(oldEnd.after(newEnd))	
			error = crossScheduleValidateOnResize(newEnd, oldEnd, currentRoom);
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
		plansCrossed.remove(currentPlan);
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
		planList.addAll(planFacade.findByRoomAndStartDateBetween(room, start, end));
		planList.addAll(planFacade.findByRoomAndEndDateBetween(room, start, end));
		planList.addAll(planFacade.findByRoomAndDatesInsidePlan(room, start, end));
		return planList;
	}
	
	private HashSet<Schedule> getScheduleRecords(Date start, Date end, Rooms room){	
		HashSet<Schedule> scheduleList = new HashSet<Schedule>();
		scheduleList.addAll(scheduleFacade.findByRoomAndStartDateBetween(room, start, end));
		scheduleList.addAll(scheduleFacade.findByRoomAndEndDateBetween(room, start, end));
		scheduleList.addAll(scheduleFacade.findByRoomAndDatesInsideSchedule(room, start, end));
		return scheduleList;
	}
	
}