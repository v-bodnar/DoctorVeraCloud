package ua.kiev.doctorvera.validators;

import org.primefaces.component.calendar.Calendar;
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
import java.util.Date;
import java.util.Map;

/**
 * Created by volodymyr.bodnar on 1/4/2016.
 */
@Named(value = "dateRangeValidator")
@ViewScoped
public class DateRangeValidator implements Validator, ClientValidator, Serializable {

    @Override
    public Map<String, Object> getMetadata() {
        return null;
    }

    @Override
    public String getValidatorId() {
        return "dateRangeValidator";
    }

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {
        Date startDate =(Date) ((Calendar) uiComponent.getAttributes().get("startDate")).getValue();
        Date endDate = (Date) value;
        String validationMessage = ua.kiev.doctorvera.validators.Validator.checkDateRange(startDate,endDate);
        if (validationMessage.isEmpty()){
            return;
        }else{
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, Message.getMessage("VALIDATOR_ERROR_TITLE"), validationMessage));
        }
    }
}
