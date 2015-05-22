package ua.kiev.doctorvera.converters;

import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

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
        if (value == null) 
            return ""; // Required by spec.
        
        return ((Users)value).getId().toString();
    }
}