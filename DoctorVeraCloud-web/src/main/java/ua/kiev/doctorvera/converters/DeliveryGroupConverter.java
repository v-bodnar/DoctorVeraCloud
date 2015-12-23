package ua.kiev.doctorvera.converters;

import ua.kiev.doctorvera.entities.DeliveryGroup;
import ua.kiev.doctorvera.facadeLocal.DeliveryGroupFacadeLocal;
import ua.kiev.doctorvera.resources.Message;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Created by volodymyr.bodnar on 25.11.2015.
 */
@FacesConverter(value = "deliveryGroupConverter")
public class DeliveryGroupConverter  implements Converter {

    @EJB
    private DeliveryGroupFacadeLocal deliveryGroupFacade;

    private static final String SELECTOR_DEFAULT = Message.getMessage("APPLICATION_SELECT_ONE");

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.equals(SELECTOR_DEFAULT)) {
            return null;
        }else {
            return deliveryGroupFacade.find(Integer.parseInt(value));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) return null;
        return ((DeliveryGroup) value).getId().toString();
    }
}