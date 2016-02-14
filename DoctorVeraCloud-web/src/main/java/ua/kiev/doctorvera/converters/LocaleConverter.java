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

/**
 * Created by volodymyr.bodnar on 2/6/2016.
 */
@FacesConverter(value = "localeConverter")
public class LocaleConverter   implements Converter {
    @EJB
    LocaleFacadeLocal localeFacade;

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
