package ua.kiev.doctorvera.validators;

import org.primefaces.validate.ClientValidator;
import ua.kiev.doctorvera.resources.Message;
import ua.kiev.doctorvera.utils.Utils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Map;

@Named(value = "usersValidator")
@ViewScoped
public class UsersValidator implements Validator, ClientValidator,Serializable {

    //private static final Logger LOG = Logger.getLogger(UsersValidator.class.getName());
    private static final String MESSAGE_TITLE = Message.getInstance().getString("VALIDATOR_REQUIRED");

    @Inject
    private ua.kiev.doctorvera.validators.Validator validator;


    public ua.kiev.doctorvera.validators.Validator getValidator() {
        return validator;
    }

    public void setValidator(ua.kiev.doctorvera.validators.Validator validator) {
        this.validator = validator;
    }

    public UsersValidator() {

    }

    @Override
    public Map<String, Object> getMetadata() {
        return null;
    }

    @Override
    public String getValidatorId() {
        return "usersValidator";
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String fieldName = (String) component.getAttributes().get("name");
        String message = "";
        String locale = getLocale();

        switch (fieldName) {
            case "firstName":
                if (locale.equals("Cyr"))
                    message = ua.kiev.doctorvera.validators.Validator.checkNameCyr((String) value);
                else
                    message = ua.kiev.doctorvera.validators.Validator.checkNameLat((String) value);
                break;
            case "lastName":
                if (locale.equals("Cyr"))
                    message = ua.kiev.doctorvera.validators.Validator.checkNameCyr((String) value);
                else
                    message = ua.kiev.doctorvera.validators.Validator.checkNameLat((String) value);
                break;
            case "middleName":
                if (locale.equals("Cyr"))
                    message = ua.kiev.doctorvera.validators.Validator.checkNameCyr((String) value);
                else
                    message = ua.kiev.doctorvera.validators.Validator.checkNameLat((String) value);
                break;
            case "username":
                message = validator.checkUsername((String) value, (String) component.getAttributes().get("currentUserId"));
                break;
            case "password":
                message = ua.kiev.doctorvera.validators.Validator.checkPassword((String) value);
                break;
            case "phoneNumberHome":
                message = ua.kiev.doctorvera.validators.Validator.checkPhoneOrNull(Utils.stripPhone((String) value));
                break;
            case "phoneNumberMobile":
                message = ua.kiev.doctorvera.validators.Validator.checkPhone(Utils.stripPhone((String) value));
                break;

        }

        if (message.equals("")) {
            //String successMessage = Message.getInstance().getMessage(Message.Messages.APPLICATION_SAVED);
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(successMessage));
            //String successMessage = Message.getInstance().getMessage(Message.Messages.APPLICATION_SAVED);
            //String successTitle = Message.getInstance().getMessage(Message.Validator.VALIDATOR_SUCCESS_TITLE);
            //FacesContext.getCurrentInstance().addMessage("firstName", new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
            return;
        } else
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, MESSAGE_TITLE, message));

    }

    private String getLocale() {
        //TODO
        return "Cyr";
    }

}