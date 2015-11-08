package ua.kiev.doctorvera.views;

import org.primefaces.context.RequestContext;
import ua.kiev.doctorvera.entities.Address;
import ua.kiev.doctorvera.entities.UserGroups;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.AddressFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UserGroupsFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.resources.Config;
import ua.kiev.doctorvera.resources.Mapping;
import ua.kiev.doctorvera.resources.Message;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;

@Named(value="userRegisterView")
@ViewScoped
public class UserRegisterView implements Serializable {
	
	@EJB
	private UsersFacadeLocal usersFacade;

	@EJB
	private UserGroupsFacadeLocal userGroupsFacade;
	
	@EJB
	private AddressFacadeLocal addressFacade;

	@Inject
	private SessionParams sessionParams;

	private Users authorizedUser;
		
	private Users user;
	private Address address;
	private final static Logger LOG = Logger.getLogger(UploadImageView.class.getName());

	private UserGroups patientsGroup;
	
	@PostConstruct
	public void init() {
		user = new Users();
		user.setUserCreated(usersFacade.findByUsername("root"));
		//user.setAvatarImage(Mapping.getInstance().getString("APPLICATION_AVATAR_DEFAULT"));
		address = new Address();
		address.setUserCreated(usersFacade.findByUsername("root"));
		patientsGroup = userGroupsFacade.find(Integer.parseInt(Config.getProperty(Config.Key.PATIENTS_USER_GROUP_ID)));
		authorizedUser = sessionParams.getAuthorizedUser();
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

	public void register() {
		address.setDateCreated(new Date());
		user.setDateCreated(new Date());
		addressFacade.create(address);
		LOG.info("AddressId: " + address.getId());
		usersFacade.create(user);
		user.setAddress(address);
		usersFacade.edit(user);
		usersFacade.addUserGroup(user, patientsGroup, authorizedUser);
		Message.showMessage(Message.getMessage("APPLICATION_SAVED"), Message.getMessage("VALIDATOR_SUCCESS_TITLE"));
		RequestContext context = RequestContext.getCurrentInstance();
		context.closeDialog("add_user");
	}
}
