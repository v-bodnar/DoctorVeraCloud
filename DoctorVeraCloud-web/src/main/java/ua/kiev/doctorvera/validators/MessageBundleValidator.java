package ua.kiev.doctorvera.validators;

import org.primefaces.validate.ClientValidator;
import ua.kiev.doctorvera.facadeLocal.LocaleFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.MessageBundleFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.PricesFacadeLocal;
import ua.kiev.doctorvera.resources.Message;
import ua.kiev.doctorvera.views.LocalizationView;
import ua.kiev.doctorvera.views.SessionParams;

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

@Named(value = "messageBundleValidator")
@ViewScoped
public class MessageBundleValidator implements Validator, ClientValidator,Serializable {

    @EJB
    private LocaleFacadeLocal localeFacade;

    @EJB
    private MessageBundleFacadeLocal messageBundleFacade;

    private String validationMessage = "";

    public MessageBundleValidator(){}

    @Override
    public Map<String, Object> getMetadata() {
        return null;
    }

    @Override
    public String getValidatorId() {
        return "messageBundleValidator";
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String fieldName = (String)component.getAttributes().get("name");
        validationMessage = "";
        switch(fieldName){
            case "messageKey":
                boolean duplicated = messageBundleFacade.findMessageByLocaleAndKey((String)value, localeFacade.findLocale(SessionParams.DEFAULT_LOCALE)) != null;
                if(duplicated) validationMessage = Message.getMessage("LOCALIZATION_MESSAGE_ADD_ERROR_DUPLICATION");
                break;
            case "messageValue":
                if(((String)value).startsWith(LocalizationView.NEW_MESSAGE))
                    validationMessage = Message.getMessage("LOCALIZATION_MESSAGE_SAVE_ERROR");
                break;
        }

        if (validationMessage.equals("")) {
            return;
        } else
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, Message.getMessage("VALIDATOR_ERROR_TITLE"), validationMessage));
    }

}