package ua.kiev.doctorvera.validators;

import org.primefaces.validate.ClientValidator;
import ua.kiev.doctorvera.resources.Message;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Map;

@Named(value = "addressValidator")
@ViewScoped
public class AddressValidator implements Validator, ClientValidator, Serializable {
	
	//private static final Logger LOG = Logger.getLogger(UsersValidator.class.getName());
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
				message = ua.kiev.doctorvera.validators.Validator.checkNumericOrNull(("" + value).replaceAll("\\D*", ""));
				break;
	
		}
		 
		if(message.equals("")) {
			return;
			}
		else 
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, Message.getMessage("VALIDATOR_REQUIRED"), message));
		
	}


}