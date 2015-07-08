package ua.kiev.doctorvera.validators;

import org.primefaces.validate.ClientValidator;
import ua.kiev.doctorvera.facadeLocal.PricesFacadeLocal;
import ua.kiev.doctorvera.resources.Message;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Date;
import java.util.Map;

@ManagedBean(name = "priceValidator")
@ViewScoped
public class PriceValidator implements Validator, ClientValidator {
	
	//private static final Logger LOG = Logger.getLogger(UsersValidator.class.getName());
	private static final String MESSAGE_TITLE = Message.getInstance().getString("VALIDATOR_ERROR_TITLE");
	
	@EJB
	private PricesFacadeLocal pricesFacade;
	
	@ManagedProperty(value = "#{validator}")
	public ua.kiev.doctorvera.validators.Validator validator;
	
	public ua.kiev.doctorvera.validators.Validator getValidator() {
		return validator;
	}

	public void setValidator(ua.kiev.doctorvera.validators.Validator validator) {
		this.validator = validator;
	}

	public PriceValidator(){
		
	}
	
	@Override
	public Map<String, Object> getMetadata() {
		return null;
	}

	@Override
	public String getValidatorId() {
		return "priceValidator";
	}

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String fieldName = (String)component.getAttributes().get("name");
		String message = "";

		switch(fieldName){
			case "notZero":
				if((Float)value==0)
					message = Message.getInstance().getString("VALIDATOR_PRICE_ZERO");
				break;
			case "date":
				if(((Date)value).after(new Date()))
					message = Message.getInstance().getString("VALIDATOR_PRICE_AFTER_NOW");
				
				break;	
				
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