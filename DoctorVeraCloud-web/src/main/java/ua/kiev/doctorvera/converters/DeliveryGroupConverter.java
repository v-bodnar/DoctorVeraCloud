package ua.kiev.doctorvera.converters;

import ua.kiev.doctorvera.entities.DeliveryGroup;
import ua.kiev.doctorvera.facadeLocal.DeliveryGroupFacadeLocal;
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
@FacesConverter(value = "deliveryGroupConverter")
public class DeliveryGroupConverter  implements Converter {

    private DeliveryGroupFacadeLocal deliveryGroupFacade = (DeliveryGroupFacadeLocal) new InitialContext().lookup("java:global/DoctorVeraCloud-ear-0.0.1-SNAPSHOT/DoctorVeraCloud-ejb-0.0.1-SNAPSHOT/DeliveryGroupFacade");

    public DeliveryGroupConverter() throws NamingException {
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.equals(Message.getMessage("APPLICATION_SELECT_ONE"))) {
            return null;
        }else {
            return deliveryGroupFacade.find(Integer.parseInt(value));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) return null;
        return  ((DeliveryGroup) value).getId().toString();
    }
}