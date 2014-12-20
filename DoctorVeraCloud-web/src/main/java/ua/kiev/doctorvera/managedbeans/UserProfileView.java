package ua.kiev.doctorvera.managedbeans;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import ua.kiev.doctorvera.entities.Address;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facade.AddressFacadeLocal;
import ua.kiev.doctorvera.facade.UsersFacadeLocal;
import ua.kiev.doctorvera.web.resources.Message;

@ManagedBean(name="userProfileView")
@SessionScoped
public class UserProfileView {
	
	@EJB
	private UsersFacadeLocal usersFacade;
	
	@EJB
	private AddressFacadeLocal addressFacade;
		
	private Users user;
	private Users userCreated;	
	private Address address;
	
	public UserProfileView(){};
	
	public void init(String userId) {
		user = usersFacade.find(Integer.parseInt(userId));
		userCreated = usersFacade.find(user.getUserCreatedId());
		address = addressFacade.find(user.getAddressId());
	}
	
	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
	
    public Users getUserCreated() {
		return userCreated;
	}

	public void setUserCreated(Users userCreated) {
		this.userCreated = userCreated;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void refresh(){
		init(user.getId().toString());
	}
	public void showCropper() {
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("minHeight", 470);
        options.put("closeOnEscape", true);
        RequestContext.getCurrentInstance().openDialog("/private/crop_image.xhtml", options, null);
    }
	
	public void save(){
		usersFacade.edit(user);
		addressFacade.edit(address);
		final String successMessage = Message.getInstance().getMessage(Message.Messages.APPLICATION_SAVED);
		final String successTitle = Message.getInstance().getMessage(Message.Validator.VALIDATOR_SUCCESS_TITLE);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}
	
}
