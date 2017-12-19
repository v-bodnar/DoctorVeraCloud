package ua.kiev.doctorvera.converters;

import ua.kiev.doctorvera.entities.DeliveryGroup;
import ua.kiev.doctorvera.facadeLocal.DeliveryGroupFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.LocaleFacadeLocal;
import ua.kiev.doctorvera.resources.Message;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Created by volodymyr.bodnar on 2/6/2016.
 */
@FacesConverter(value = "localeConverter")
public class LocaleConverter   implements Converter {

    LocaleFacadeLocal localeFacade = (LocaleFacadeLocal) new InitialContext().lookup("java:global/DoctorVeraCloud-ear-0.0.1-SNAPSHOT/DoctorVeraCloud-ejb-0.0.1-SNAPSHOT/LocaleFacade");

    public LocaleConverter() throws NamingException {
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.equals(Message.getMessage("APPLICATION_SELECT_ONE"))) {
            return null;
        }else {
            return localeFacade.findByCode(value);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) return null;
        return  value.toString();
    }
}
