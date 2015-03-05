package ua.kiev.doctorvera.converters;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ua.kiev.doctorvera.entities.Methods;
import ua.kiev.doctorvera.facade.MethodsFacadeLocal;

@FacesConverter(value = "methodsConverter")
public class MethodsConverter implements Converter{

	@EJB
	private MethodsFacadeLocal methodsFacade;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value){
    	//Checking for null and empty value
    	if(value != null && value instanceof String && value !=""){
    		
    		//If value is numeric we are searching by Id
    		if(isNumeric(value))
    			return methodsFacade.find(Integer.parseInt(value));
    		//If value is not Numeric we are searching by Short Name
    		else 
    			return methodsFacade.findByShortName(value);


    	} else
    		return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value){
        if(value != null && !value.equals("")) {
                return ((Methods) value).getShortName();
        } else 
        	return null;
    }
    
    //Checks if a String is Integer
    private static boolean isNumeric(String value){
    	try{
    		Integer.parseInt(value);
    		return true;
    	}catch(NumberFormatException e){
    		return false;
    	}
    }
}