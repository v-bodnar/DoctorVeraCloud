package ua.kiev.doctorvera.views;

import org.primefaces.context.RequestContext;
import ua.kiev.doctorvera.entities.Address;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.AddressFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.resources.Message;
import ua.kiev.doctorvera.security.Secure;
import ua.kiev.doctorvera.security.SecurityPolicy;
import ua.kiev.doctorvera.security.SecurityUtils;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Named(value="userProfileView")
@SessionScoped
public class UserProfileView implements Serializable{
	
	@EJB
	private UsersFacadeLocal usersFacade;
	
	@EJB
	private AddressFacadeLocal addressFacade;

	@Inject
	SessionParams sessionParams;

	@Inject
	SecurityUtils securityUtils;

	private Users authorizedUser;
		
	private Users user;
	private Users userCreated;	
	private Address address;
	
	public UserProfileView(){};
	
	public void init(String userId) {
		user = usersFacade.find(Integer.parseInt(userId));
		userCreated = usersFacade.find(user.getUserCreated());
		address = user.getAddress();
		if (address == null) address = new Address();
		authorizedUser = sessionParams.getAuthorizedUser();
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

	public Users getAuthorizedUser() {
		return authorizedUser;
	}

	public void setAuthorizedUser(Users authorizedUser) {
		this.authorizedUser = authorizedUser;
	}

	public void showCropper() {
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("minHeight", 470);
        options.put("closeOnEscape", true);
        RequestContext.getCurrentInstance().openDialog("/private/crop_image.xhtml", options, null);
    }
	@Secure
	public void securityTest(){
		Message.showError("", "asdfadsfadsf");
	}

	public void save(){
		if (address != null && address.getId() != null) {
			addressFacade.edit(address);
		}else if(address != null && address.getId() == null){
			address.setUserCreated(authorizedUser);
			address.setDateCreated(new Date());
			addressFacade.create(address);
			user.setAddress(address);
		}
		usersFacade.edit(user);
		final String successMessage = Message.getInstance().getString("APPLICATION_SAVED");
		final String successTitle = Message.getInstance().getString("VALIDATOR_SUCCESS_TITLE");
		Message.showMessage(successTitle, successMessage);
	}

	public Boolean checkPermission(String policy){
		if (securityUtils.checkPermissions(SecurityPolicy.PROFILE_EDIT_ANY_USER)){
			return securityUtils.checkPermissions(policy);
		}else{
			return securityUtils.checkPermissions(policy) && authorizedUser.equals(user);
		}
	}

	
}
