package ua.kiev.doctorvera.converters;

import ua.kiev.doctorvera.entities.Policy;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.PolicyFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "policyConverter")
public class PolicyConverter implements Converter{

	@EJB
	private PolicyFacadeLocal policyFacade;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        return policyFacade.find(Integer.parseInt(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if (value == null) 
            return ""; // Required by spec.
        
        return ((Policy)value).getId().toString();
    }
}