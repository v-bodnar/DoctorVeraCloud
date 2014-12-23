package ua.kiev.doctorvera.managedbeans;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import ua.kiev.doctorvera.entities.UserTypes;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facade.UserTypesFacadeLocal;
import ua.kiev.doctorvera.web.resources.Message;

@ManagedBean(name="userTypesTableView")
@ViewScoped
public class UserTypesTableView {
	
	private final static Logger LOG = Logger.getLogger(UploadImageView.class.getName());
	
	@EJB
	private UserTypesFacadeLocal userTypesFacade;
	
    @ManagedProperty(value="#{userLoginView.authorizedUser}")
	private Users authorizedUser;

	private List<UserTypes> allTypes;
	
	private UserTypes newType;
	
	private UserTypes selectedType;
	
	public UserTypesTableView(){}
	
	@PostConstruct
	public void init(){
		allTypes = userTypesFacade.findAll();
		this.newType = new UserTypes();
		this.selectedType = new UserTypes();
		LOG.info("Param:" + authorizedUser.getClass() +" injected into" + this.getClass() + "!");
	}
	
	public Users getAuthorizedUser() {
		return authorizedUser;
	}

	public void setAuthorizedUser(Users authorizedUser) {
		this.authorizedUser = authorizedUser;
	}

	public List<UserTypes> getAllTypes() {
		return allTypes;
	}

	public void setAllTypes(List<UserTypes> allTypes) {
		this.allTypes = allTypes;
	}

	public UserTypes getNewType() {
		return newType;
	}

	public void setNewType(UserTypes newType) {
		this.newType = newType;
	}
	
	public void initNewType() {
		this.newType = new UserTypes();
	}

	public UserTypes getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(UserTypes selectedType) {
		this.selectedType = selectedType;
	}

	public void deleteSelectedType(){
		userTypesFacade.remove(selectedType);
		allTypes.remove(selectedType);
		final String successMessage = Message.getInstance().getMessage(Message.UserTypes.USER_TYPES_DELETED);
		final String successTitle = Message.getInstance().getMessage(Message.Validator.VALIDATOR_SUCCESS_TITLE);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}	
	
    public void saveSelectedType() {
		selectedType.setDateCreated(new Date());
		selectedType.setUserCreated(authorizedUser);
		userTypesFacade.edit(selectedType);
		final String successMessage = Message.getInstance().getMessage(Message.UserTypes.USER_TYPES_EDITED);
		final String successTitle = Message.getInstance().getMessage(Message.Validator.VALIDATOR_SUCCESS_TITLE);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
    }
    
	public void saveNewType(){
		newType.setDateCreated(new Date());
		newType.setUserCreated(authorizedUser);
		userTypesFacade.create(newType);
		allTypes.add(newType);
		final String successMessage = Message.getInstance().getMessage(Message.Messages.APPLICATION_SAVED);
		final String successTitle = Message.getInstance().getMessage(Message.UserTypes.USER_TYPES_SAVED);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}

}
