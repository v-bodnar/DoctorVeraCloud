package ua.kiev.doctorvera.managedbeans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import ua.kiev.doctorvera.entities.Address;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facade.AddressFacadeLocal;
import ua.kiev.doctorvera.facade.UsersFacadeLocal;
import ua.kiev.doctorvera.web.resources.Message;

public class UserRegisterView {
	@EJB
	private UsersFacadeLocal usersFacade;
	
	@EJB
	private AddressFacadeLocal addressFacade;
		
	private Users user;
	private Address address;
	
	private String pass;
	
	@PostConstruct
	public void init() {
		user = new Users();
		user.setUserCreated(usersFacade.findByUsername("root"));
		address = new Address();
	}
	
	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Address getAddress() {
		return address;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	public void register(){
		addressFacade.create(address);
		usersFacade.create(user);
		user.setAddress(address);
		usersFacade.edit(user);
		final String successMessage = Message.getInstance().getMessage(Message.Messages.APPLICATION_SAVED);
		final String successTitle = Message.getInstance().getMessage(Message.Validator.VALIDATOR_SUCCESS_TITLE);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}
}
