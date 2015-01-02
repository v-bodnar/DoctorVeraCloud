package ua.kiev.doctorvera.converters;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facade.UsersFacadeLocal;

@FacesConverter(value = "usersConverter")
public class UsersConverter implements Converter{

	@EJB
	private UsersFacadeLocal usersFacade;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        return usersFacade.find(Integer.parseInt(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        return ((Users)value).getId().toString();
    }
}