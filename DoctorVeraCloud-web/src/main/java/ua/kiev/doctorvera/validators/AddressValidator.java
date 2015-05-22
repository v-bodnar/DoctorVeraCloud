package ua.kiev.doctorvera.validators;

import org.primefaces.validate.ClientValidator;
import ua.kiev.doctorvera.web.resources.Message;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Map;

@ManagedBean(name = "addressValidator")
@ViewScoped
public class AddressValidator implements Validator, ClientValidator {
	
	//private static final Logger LOG = Logger.getLogger(UsersValidator.class.getName());
	private static final String MESSAGE_TITLE = Message.getInstance().getMessage(Message.Validator.VALIDATOR_REQUIRED);
	
	@Override
	public Map<String, Object> getMetadata() {
		return null;
	}

	@Override
	public String getValidatorId() {
		return "addressValidator";
	}

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String fieldName = (String)component.getAttributes().get("name");
		String message = "";

		switch(fieldName){
			case "country":
				message = ua.kiev.doctorvera.validators.Validator.checkLiteralOrNull((String)value);
				break;
			case "region":
				message = ua.kiev.doctorvera.validators.Validator.checkLiteralOrNull((String)value);
				break;
			case "city":
				message = ua.kiev.doctorvera.validators.Validator.checkLiteralOrNull((String)value);
				break;
			case "postIndex":
				message = ua.kiev.doctorvera.validators.Validator.checkNumericOrNull((String)value);
				break;
	
		}
		 
		if(message.equals("")) {
			return;
			}
		else 
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, MESSAGE_TITLE, message));
		
	}


}