package ua.kiev.doctorvera.managedbeans;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facade.UsersFacadeLocal;
import ua.kiev.doctorvera.web.resources.Message;
 
public class UserLoginView {
	@EJB
	private UsersFacadeLocal usersFacade;
	
    private Users incomingUser = new Users();
    private Users authorizedUser;
    private Boolean render = false;
    
    private final String ERROR_MESSAGE = Message.getInstance().getMessage(Message.Login.LOGIN_ERROR);
    private final String WELCOME_MESSAGE = Message.getInstance().getMessage(Message.Login.LOGIN_WELCOME);
    private final String ERROR_MESSAGE_TITLE = Message.getInstance().getMessage(Message.Login.LOGIN_ERROR_TITLE);
    private final String WELCOME_MESSAGE_TITLE = Message.getInstance().getMessage(Message.Login.LOGIN_WELCOME_TITLE);
    private final String GOODBY_MESSAGE_TITLE = Message.getInstance().getMessage(Message.Login.LOGIN_GOODBY_TITLE);
    private final String GOODBY_MESSAGE = Message.getInstance().getMessage(Message.Login.LOGIN_GOODBY);
        
    public void toggleRender() {
    	render = !render;
	}
    
    public Boolean getRender() {
		return render;
	}

	public void setRender(Boolean render) {
		this.render = render;
	}

	public Users getAuthorizedUser() {
		return authorizedUser;
	}

	public void setAuthorizedUser(Users authorizedUser) {
		this.authorizedUser = authorizedUser;
	}

	public Users getIncomingUser() {
        return incomingUser;
    }
    
    public void setIncomingUser(Users incomingUser) {
       this.incomingUser = incomingUser;
    }
    public void setIncomingUser() {
        this.incomingUser = usersFacade.find(14);
    }
    
    /*
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
    public void login(ActionEvent event) {
        RequestContext requestContext = RequestContext.getCurrentInstance();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message = null;
        boolean loggedIn = false;
        authorizedUser = usersFacade.findByCred(incomingUser.getUsername(), incomingUser.getPassword());

        if(incomingUser.getUsername() != null && incomingUser.getPassword() != null && authorizedUser != null) {
            loggedIn = true;
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, WELCOME_MESSAGE_TITLE, WELCOME_MESSAGE + authorizedUser.getFirstName() + " " + authorizedUser.getLastName());
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
        authorizedUser = null;
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, GOODBY_MESSAGE_TITLE, GOODBY_MESSAGE );       
        facesContext.addMessage(null, message);
    }
    
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
    
}
