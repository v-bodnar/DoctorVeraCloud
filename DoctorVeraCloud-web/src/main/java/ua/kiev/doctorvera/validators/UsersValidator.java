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
        String phone = Utils.stripPhone((String) value);

        switch (fieldName) {
            case "username":
                message = validator.checkUsername((String) value, (String) component.getAttributes().get("currentUserId"));
                break;
            case "password":
                message = ua.kiev.doctorvera.validators.Validator.checkPassword((String) value);
                break;
            case "phoneNumberHome":
                if (phone != null && !phone.isEmpty() && !phone.equals("+38"))
                    message = ua.kiev.doctorvera.validators.Validator.checkUkrainianPhone(phone);
                break;
            case "phoneNumberHomeInternational":
                if (phone != null && !phone.isEmpty() && !phone.equals("+"))
                    message = ua.kiev.doctorvera.validators.Validator.checkInternationalPhone(phone);
                break;
            case "phoneNumberMobile":
                message = ua.kiev.doctorvera.validators.Validator.checkUkrainianPhone(Utils.stripPhone((String) value));
                break;
            case "phoneNumberMobileInternational":
                message = ua.kiev.doctorvera.validators.Validator.checkInternationalPhone(Utils.stripPhone((String) value));
                break;
        }

        if (message.equals("")) {
            return;
        } else
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, MESSAGE_TITLE, message));

    }
}