package ua.kiev.doctorvera.converters;

import ua.kiev.doctorvera.entities.UserGroups;
import ua.kiev.doctorvera.facadeLocal.UserGroupsFacadeLocal;
import ua.kiev.doctorvera.resources.Message;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "userGroupsConverter")
public class UserGroupsConverter implements Converter {

    @EJB
    private UserGroupsFacadeLocal userGroupsFacade;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.equals(Message.getMessage("APPLICATION_SELECT_ONE"))) {
            return null;
        }else {
            return userGroupsFacade.find(Integer.parseInt(value));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) return null;
        return ((UserGroups) value).getId().toString();
    }
}