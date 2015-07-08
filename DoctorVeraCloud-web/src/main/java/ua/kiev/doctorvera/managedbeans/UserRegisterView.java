package ua.kiev.doctorvera.managedbeans;

import ua.kiev.doctorvera.entities.Address;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.AddressFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.resources.Mapping;
import ua.kiev.doctorvera.resources.Message;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.Date;
import java.util.logging.Logger;

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
		user.setAvatarImage(Mapping.getInstance().getString("APPLICATION_AVATAR_DEFAULT"));
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
		final String successMessage = Message.getInstance().getString("APPLICATION_SAVED");
		final String successTitle = Message.getInstance().getString("VALIDATOR_SUCCESS_TITLE");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}
}
