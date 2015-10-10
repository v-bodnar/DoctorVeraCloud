package ua.kiev.doctorvera.views;

import org.primefaces.model.DualListModel;
import ua.kiev.doctorvera.entities.UserGroups;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.resources.Message;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

@Named(value="userAddType")
@ViewScoped
public class UsersAddType implements Serializable {
	
	private final static Logger LOG = Logger.getLogger(UploadImageView.class.getName());
	
    //@Named(value="#{userTypesTableView}")
    //private UserGroupsTableView userTypesTableView;
    
	private UserGroups userType;
	
	//@EJB
	//private UserGroupsFacadeLocal userTypesFacade;
	
	@EJB
	private UsersFacadeLocal usersFacade;
	
	//@EJB
	//private UsersHasUserGroupsFacadeLocal usersHasUserTypesFacadeLocal;
	
	//private UserGroups userType;
	
	private DualListModel<Users> users;
	
	@PostConstruct
	public void init(){
		userType = new UserGroups();
		List<Users> allUsers = usersFacade.findAll();
		List<Users> targetUsers = usersFacade.findByGroup(userType);
		users = new DualListModel<Users>(allUsers, targetUsers);
	}
	
	//public void setUserTypesTableView(UserGroupsTableView userTypesTableView) {
	//	this.userTypesTableView = userTypesTableView;
	//}

	public UserGroups getUserType() {
		return userType;
	}

	public void setUserType(UserGroups userType) {
		this.userType = userType;
	}

	public DualListModel<Users> getUsers() {
		return users;
	}

	public void setUsers(DualListModel<Users> users) {
		this.users = users;
	}
	
	public void onTransfer(){
		final String successMessage = Message.getInstance().getString("APPLICATION_SAVED");
		final String successTitle = Message.getInstance().getString("USER_TYPES_SAVED");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}

}
