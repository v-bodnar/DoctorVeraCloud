package ua.kiev.doctorvera.managedbeans;

import java.util.Date;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import ua.kiev.doctorvera.entities.Address;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facade.AddressFacadeLocal;
import ua.kiev.doctorvera.facade.UsersFacadeLocal;
import ua.kiev.doctorvera.web.resources.Mapping;
import ua.kiev.doctorvera.web.resources.Message;

@ManagedBean(name="userRegisterView")
@ViewScoped
public class UserRegisterView {
	
	@EJB
	private UsersFacadeLocal usersFacade;
	
	@EJB
	private AddressFacadeLocal addressFacade;
		
	private Users user;
	private Address address;
	private final static Logger LOG = Logger.getLogger(UploadImageView.class.getName());
	
	@PostConstruct
	public void init() {
		user = new Users();
		user.setUserCreated(usersFacade.findByUsername("root"));
		user.setAvatarImage(Mapping.getInstance().getProperty(Mapping.Path.APPLICATION_AVATAR_DEFAULT));
		address = new Address();
		address.setUserCreated(usersFacade.findByUsername("root"));
		
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

	public void setAddress(Address address) {
		this.address = address;
	}
	
	public void register(){
		address.setDateCreated(new Date());
		user.setDateCreated(new Date());
		addressFacade.create(address);
		LOG.info("AddressId: " + address.getId());
		usersFacade.create(user);
		user.setAddress(address);
		usersFacade.edit(user);
		final String successMessage = Message.getInstance().getMessage(Message.Messages.APPLICATION_SAVED);
		final String successTitle = Message.getInstance().getMessage(Message.Validator.VALIDATOR_SUCCESS_TITLE);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}
}
