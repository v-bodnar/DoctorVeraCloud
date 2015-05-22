package ua.kiev.doctorvera.validators;

import org.primefaces.validate.ClientValidator;
import ua.kiev.doctorvera.entities.Methods;
import ua.kiev.doctorvera.facadeLocal.MethodsFacadeLocal;
import ua.kiev.doctorvera.web.resources.Message;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Map;

@ManagedBean(name = "methodValidator")
@ViewScoped
public class MethodValidator implements Validator, ClientValidator {

	//private static final Logger LOG = Logger.getLogger(UsersValidator.class.getName());
	private static final String MESSAGE_TITLE = Message.getInstance().getMessage(Message.Validator.VALIDATOR_ERROR_TITLE);

	@EJB
	private MethodsFacadeLocal methodsFacade;

	@ManagedProperty(value = "#{validator}")
	public ua.kiev.doctorvera.validators.Validator validator;

	public ua.kiev.doctorvera.validators.Validator getValidator() {
		return validator;
	}

	public void setValidator(ua.kiev.doctorvera.validators.Validator validator) {
		this.validator = validator;
	}

	public MethodValidator(){

	}

	@Override
	public Map<String, Object> getMetadata() {
		return null;
	}

	@Override
	public String getValidatorId() {
		return "methodValidator";
	}

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String fieldName = (String)component.getAttributes().get("name");
        Integer id = (Integer)component.getAttributes().get("selectedId");
		String message = "";

		switch(fieldName){
			case "shortName":
                Methods methodByShortName = methodsFacade.findByShortName((String)value);
                if( id == null && methodByShortName != null)
                    message = Message.getInstance().getMessage(Message.Validator.VALIDATOR_METHOD_DUPLICATED_SHORT_NAME);
                else if(id != null && methodByShortName != null && !methodByShortName.equals(methodsFacade.find(id)))
                    message = Message.getInstance().getMessage(Message.Validator.VALIDATOR_METHOD_DUPLICATED_SHORT_NAME);
				break;
			case "timeInMinutes":
				if((Integer)value == 0 || (Integer)value < 0 || (Integer)value > 180)
					message = Message.getInstance().getMessage(Message.Validator.VALIDATOR_METHOD_TIMEINMINUTES);
		}

		if(message.equals("")) {
			//String successMessage = Message.getInstance().getMessage(Message.Messages.APPLICATION_SAVED);
			//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(successMessage));
			//String successMessage = Message.getInstance().getMessage(Message.Messages.APPLICATION_SAVED);
			//String successTitle = Message.getInstance().getMessage(Message.Validator.VALIDATOR_SUCCESS_TITLE);
			//FacesContext.getCurrentInstance().addMessage("firstName", new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
			return;
			}
		else
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, MESSAGE_TITLE, message));

	}

}