package ua.kiev.doctorvera.converters;

import ua.kiev.doctorvera.entities.Policy;
import ua.kiev.doctorvera.facadeLocal.PolicyFacadeLocal;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@FacesConverter(value = "policyConverter")
public class PolicyConverter implements Converter{


	private PolicyFacadeLocal policyFacade = (PolicyFacadeLocal) new InitialContext().lookup("java:global/DoctorVeraCloud-ear-0.0.1-SNAPSHOT/DoctorVeraCloud-ejb-0.0.1-SNAPSHOT/PolicyFacade");

    public PolicyConverter() throws NamingException {
    }

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