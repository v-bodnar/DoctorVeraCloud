package ua.kiev.doctorvera.converters;

import ua.kiev.doctorvera.entities.UserGroups;
import ua.kiev.doctorvera.facadeLocal.UserGroupsFacadeLocal;
import ua.kiev.doctorvera.resources.Message;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "userTypesConverter")
public class UserTypesConverter implements Converter {

    @EJB
    private UserGroupsFacadeLocal userTypesFacade;

    private static final String SELECTOR_DEFAULT = Message.getMessage("APPLICATION_SELECT_ONE");

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value.equals(SELECTOR_DEFAULT)) {
            return null;
        }else {
            return userTypesFacade.find(Integer.parseInt(value));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) return null;
        return ((UserGroups) value).getId().toString();
    }
}