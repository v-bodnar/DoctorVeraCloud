package ua.kiev.doctorvera.views;

import org.primefaces.context.RequestContext;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.resources.Message;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Named(value="userLoginView")
@ViewScoped
public class UserLoginView implements Serializable {
	@EJB
	private UsersFacadeLocal usersFacade;
	
    private Users incomingUser = new Users();
    private Boolean render = false;

    @Inject
    private SessionParams sessionParams;

//    @Inject
//    private SecurityUtils securityUtils;

    private final String ERROR_MESSAGE = Message.getMessage("LOGIN_ERROR");
    private final String WELCOME_MESSAGE = Message.getMessage("LOGIN_WELCOME");
    private final String ERROR_MESSAGE_TITLE = Message.getMessage("LOGIN_ERROR_TITLE");
    private final String WELCOME_MESSAGE_TITLE = Message.getMessage("LOGIN_WELCOME_TITLE");
    private final String GOODBY_MESSAGE_TITLE = Message.getMessage("LOGIN_GOODBY_TITLE");
    private final String GOODBY_MESSAGE = Message.getMessage("LOGIN_GOODBY");
        
    public UserLoginView(){};
    
    public void toggleRender() {
    	render = !render;
	}
    
    public Boolean getRender() {
		return render;
	}

	public void setRender(Boolean render) {
		this.render = render;
	}

	public Users getIncomingUser() {
        return incomingUser;
    }
    
    public void setIncomingUser(Users incomingUser) {
       this.incomingUser = incomingUser;
    }
    /*
    public void setIncomingUser() {
        this.incomingUser = usersFacade.find(14);
    }
    public String getUsername() {
        return incomingUser.getUsername();
    }
 
    public void setUsername(String username) {
    	incomingUser.setUsername(username);
    }
 
    public String getPassword() {
    	return incomingUser.getPassword();
    }
 
    public void setPassword(String password) {
    	incomingUser.setPassword(password);;
    }
   */
    public void refresh(){
    	sessionParams.setAuthorizedUser(usersFacade.find(sessionParams.getAuthorizedUser().getId()));
    }
    
    public void login(ActionEvent event) {
        //securityUtils.isAlreadySynchronized();
        RequestContext requestContext = RequestContext.getCurrentInstance();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message = null;
        boolean loggedIn = false;
        sessionParams.setAuthorizedUser(usersFacade.findByCred(incomingUser.getUsername(), incomingUser.getPassword()));

        if(incomingUser.getUsername() != null && incomingUser.getPassword() != null && sessionParams.getAuthorizedUser() != null) {
            loggedIn = true;
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, WELCOME_MESSAGE_TITLE, WELCOME_MESSAGE + sessionParams.getAuthorizedUser().getFirstName() + " " + sessionParams.getAuthorizedUser().getLastName());
        } else {
            loggedIn = false;
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, ERROR_MESSAGE_TITLE, ERROR_MESSAGE);
        }
        facesContext.addMessage(null, message);
        requestContext.addCallbackParam("loggedIn", loggedIn);
    }  
    
    public void logout(ActionEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message = null;
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        request.getSession().invalidate();
        sessionParams.setAuthorizedUser(null);
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, GOODBY_MESSAGE_TITLE, GOODBY_MESSAGE );       
        facesContext.addMessage(null, message);
    }
   /*
    public void checkAuth() {	
        RequestContext requestContext = RequestContext.getCurrentInstance();
        //FacesContext facesContext = FacesContext.getCurrentInstance();
        boolean loggedIn = false;      
        Integer id = Integer.parseInt((String)requestContext.getAttributes().get("authorizedUserId"));
        if (id != null) authorizedUser = usersFacade.find(id);
        if (authorizedUser == null) 
        	{
        		requestContext.addCallbackParam("loggedIn", loggedIn);
        	}else{
        		requestContext.addCallbackParam("loggedIn", true);
        	}

    }
    */
}
