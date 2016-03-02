package ua.kiev.doctorvera.converters;

import ua.kiev.doctorvera.entities.MessageTemplate;
import ua.kiev.doctorvera.facadeLocal.MessageTemplateFacadeLocal;
import ua.kiev.doctorvera.resources.Message;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Created by volodymyr.bodnar on 25.11.2015.
 */
@FacesConverter(value = "messageTemplateConverter")
public class MessageTemplateConverter implements Converter {

    @EJB
    private MessageTemplateFacadeLocal messageTemplateFacade;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.equals(Message.getMessage("APPLICATION_SELECT_ONE"))) {
            return null;
        }else {
            return messageTemplateFacade.find(Integer.parseInt(value));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) return null;
        return ((MessageTemplate) value).getId().toString();
    }
}