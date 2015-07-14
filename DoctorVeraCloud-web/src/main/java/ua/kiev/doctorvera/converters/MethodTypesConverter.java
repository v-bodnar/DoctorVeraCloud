package ua.kiev.doctorvera.converters;

import ua.kiev.doctorvera.entities.MethodTypes;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.MethodTypesFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.MethodsFacadeLocal;
import ua.kiev.doctorvera.views.SessionParams;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.Date;
import java.util.List;

@FacesConverter(value = "methodTypesConverter")
public class MethodTypesConverter implements Converter{

	@EJB
	private MethodTypesFacadeLocal methodTypesFacade;

    @EJB
    private MethodsFacadeLocal methodsFacade;
	
	private Users authorizedUser;
	
    
    @SuppressWarnings({"deprecation" })
	@Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
    	authorizedUser = ((SessionParams)FacesContext.getCurrentInstance().getApplication().getVariableResolver().resolveVariable(context,"sessionParams")).getAuthorizedUser();
    	//Checking for null and empty value
    	if(value != null && value instanceof String && value !=""){
    		
    		//If value is numeric we are searching by Id
    		if(isNumeric(value))
    			return methodTypesFacade.find(Integer.parseInt(value));
    		//If value is not Numeric we are searching by Short Name
    		else if(exists(value))
    			return methodTypesFacade.findByShortName(value).get(0);
    		//If MethodType has not been found we create new one
    		else
    			return createNewMethodType(value);

    	} else
    		return null;
    	
    }
    
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value){
    	if(value != null && value instanceof MethodTypes)
    		return ((MethodTypes)value).getShortName().toString();
    	else 
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
    //Checking if Method Type with the given Short Name exists
    private boolean exists (String shortName){
    	List<MethodTypes> methodTypes = methodTypesFacade.findByShortName(shortName);
		if (methodTypes.size() == 0) {
			return false;
        } else if (methodTypes.size() == 1){
            return true;
        } else{
            return true;
        }
    }
    
    //Creates new Method Type with the given @param shortName
    private MethodTypes createNewMethodType(final String shortName){
            MethodTypes methodType = new MethodTypes();
            methodType.setDateCreated(new Date());
            methodType.setShortName(shortName);
            methodType.setFullName(shortName);
            methodType.setUserCreated(authorizedUser);
            methodTypesFacade.create(methodType);
            return methodType;
    }

    
    
}