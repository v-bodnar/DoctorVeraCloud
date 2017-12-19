package ua.kiev.doctorvera.converters;

import ua.kiev.doctorvera.entities.MessageScheduler;
import ua.kiev.doctorvera.facadeLocal.MessageSchedulerFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.MessageSchedulerFacadeLocal;
import ua.kiev.doctorvera.resources.Message;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Created by volodymyr.bodnar on 25.11.2015.
 */
@FacesConverter(value = "messageSchedulerConverter")
public class MessageSchedulerConverter implements Converter {

    @EJB
    private MessageSchedulerFacadeLocal messageSchedulerFacade = (MessageSchedulerFacadeLocal) new InitialContext().lookup("java:global/DoctorVeraCloud-ear-0.0.1-SNAPSHOT/DoctorVeraCloud-ejb-0.0.1-SNAPSHOT/MessageSchedulerFacade");

    public MessageSchedulerConverter() throws NamingException {
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.equals(Message.getMessage("APPLICATION_SELECT_ONE"))) {
            return null;
        }else {
            return messageSchedulerFacade.find(Integer.parseInt(value));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) return null;
        return ((MessageScheduler) value).getId().toString();
    }
}