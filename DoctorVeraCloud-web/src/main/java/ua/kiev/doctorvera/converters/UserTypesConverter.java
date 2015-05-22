package ua.kiev.doctorvera.converters;

import ua.kiev.doctorvera.entities.UserTypes;
import ua.kiev.doctorvera.facadeLocal.UserTypesFacadeLocal;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "userTypesConverter")
public class UserTypesConverter implements Converter{

	@EJB
	private UserTypesFacadeLocal userTypesFacade;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        return userTypesFacade.find(Integer.parseInt(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        return ((UserTypes)value).getId().toString();
    }
}