package ua.kiev.doctorvera.validators;

import org.primefaces.validate.ClientValidator;
import ua.kiev.doctorvera.facadeLocal.PricesFacadeLocal;
import ua.kiev.doctorvera.resources.Message;

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
import java.util.Date;
import java.util.Map;

@Named(value = "priceValidator")
@ViewScoped
public class PriceValidator implements Validator, ClientValidator,Serializable {
	
	//private static final Logger LOG = Logger.getLogger(UsersValidator.class.getName());
	
	@EJB
	private PricesFacadeLocal pricesFacade;
	
	@Inject
	private ua.kiev.doctorvera.validators.Validator validator;
	
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
					message = Message.getMessage("VALIDATOR_PRICE_ZERO");
				break;
			case "date":
				if(((Date)value).before(new Date()))
					message = Message.getMessage("VALIDATOR_PRICE_AFTER_NOW");
				
				break;	
				
		}
		 
		if(message.equals("")) {
			//String successMessage = Message.getMessage(Message.Messages.APPLICATION_SAVED);
			//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(successMessage));
			//String successMessage = Message.getMessage(Message.Messages.APPLICATION_SAVED);
			//String successTitle = Message.getMessage(Message.Validator.VALIDATOR_SUCCESS_TITLE);
			//FacesContext.getCurrentInstance().addMessage("firstName", new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
			return;
			}
		else 
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, Message.getMessage("VALIDATOR_ERROR_TITLE"), message));
		
	}

}