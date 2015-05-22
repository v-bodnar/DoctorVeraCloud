package ua.kiev.doctorvera.managedbeans;

import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import ua.kiev.doctorvera.entities.UserTypes;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.UserTypesFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.web.resources.Message;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@ManagedBean(name="userTypesTableView")
@ViewScoped
public class UserTypesTableView {
	
	private final static Logger LOG = Logger.getLogger(UserTypesTableView.class.getName());
	
	//Facade for CRUD operations with User Groups
	@EJB
	private UserTypesFacadeLocal userTypesFacade;
	
	//Facade for CRUD operations with Users
	@EJB
	private UsersFacadeLocal usersFacade;
	
	//Authorized User
    @ManagedProperty(value="#{userLoginView.authorizedUser}")
	private Users authorizedUser;

    //All User Groups
	private List<UserTypes> allTypes;
	
	//New User Group
	private UserTypes newType;
	
	//Selected User Group
	private UserTypes selectedType;
	
	//Model for picklist PrimeFaces widget
	private DualListModel<Users> usersDualListModel;
	
	@PostConstruct
	public void init(){
		allTypes = userTypesFacade.findAll();
		this.newType = new UserTypes();
		this.selectedType = new UserTypes();
		constructPickList();
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
	
	public DualListModel<Users> getUsersDualListModel() {
		return usersDualListModel;
	}

	public void setUsersDualListModel(DualListModel<Users> usersDualListModel) {
		this.usersDualListModel = usersDualListModel;
	}

	public void constructPickList(){
		if (selectedType != null && selectedType.getId() != null){
			List<Users> allUsers = usersFacade.findAll();
			List<Users> targetUsers = usersFacade.findByType(selectedType);
			for(Users user : targetUsers){
				allUsers.remove(user);
			}
			usersDualListModel = new DualListModel<Users>(allUsers, targetUsers);
		} else
			usersDualListModel = new DualListModel<Users>(new ArrayList<Users>(), new ArrayList<Users>());
	}

	//Deletes selected User Group
	public void deleteSelectedType(){
		userTypesFacade.remove(selectedType);
		allTypes.remove(selectedType);
		final String successMessage = Message.getInstance().getMessage(Message.UserTypes.USER_TYPES_DELETED);
		final String successTitle = Message.getInstance().getMessage(Message.Validator.VALIDATOR_SUCCESS_TITLE);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}	
	
	//Updates selected User Group
    public void saveSelectedType() {
		selectedType.setDateCreated(new Date());
		selectedType.setUserCreated(authorizedUser);
		userTypesFacade.edit(selectedType);
		final String successMessage = Message.getInstance().getMessage(Message.UserTypes.USER_TYPES_EDITED);
		final String successTitle = Message.getInstance().getMessage(Message.Validator.VALIDATOR_SUCCESS_TITLE);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
    }
    
    //Adds selected User Group
	public void saveNewType(){
		newType.setDateCreated(new Date());
		newType.setUserCreated(authorizedUser);
		userTypesFacade.create(newType);
		allTypes.add(newType);
		final String successMessage = Message.getInstance().getMessage(Message.Messages.APPLICATION_SAVED);
		final String successTitle = Message.getInstance().getMessage(Message.UserTypes.USER_TYPES_SAVED);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}
	
	//This method controls onTransfer event in the Pick List
	public void onTransfer(TransferEvent event){

		
		//All Users from the right picker
		List<Users> targetList = usersDualListModel.getTarget();
		
		//Indicates to add new type to user transfered or to remove type from user
		Boolean addFlag = false;
		
		//Checking transfer direction
		if(targetList != null && targetList.contains(event.getItems().get(0))) 
			addFlag = true; //Means that user transfered from left picker to right picker
			
		//Constructing success message
		final String successTitle = Message.getInstance().getMessage(Message.Validator.VALIDATOR_SUCCESS_TITLE);
		String successMessage;
		if(targetList != null && targetList.contains(event.getItems().get(0)))
			successMessage = Message.getInstance().getMessage(Message.UserTypes.USER_TYPES_ADD_SUCCESS_START);
		else
			successMessage = Message.getInstance().getMessage(Message.UserTypes.USER_TYPES_REMOVE_SUCCESS_START);
		
		//Iterating each transfered user
		for(Object userObject : event.getItems()){
			Users userTransfered=(Users)userObject;
			
			//Constructing success message
			successMessage += userTransfered.getFirstName() + " " + userTransfered.getLastName() + ", ";
			
			if(addFlag){
				//Add type to user transfered
				usersFacade.addUserType(userTransfered, selectedType, authorizedUser);
				
				//Setting time and user that made changes
				userTransfered.setUserCreated(authorizedUser);
				selectedType.setUserCreated(authorizedUser);
				userTransfered.setDateCreated(new Date());
				selectedType.setDateCreated(new Date());
				usersFacade.edit(userTransfered);
				userTypesFacade.edit(selectedType);
			}else{
				//Remove type from user transfered
				usersFacade.removeUserType(userTransfered, selectedType);
				
				//Setting time and user that made changes
				userTransfered.setUserCreated(authorizedUser);
				selectedType.setUserCreated(authorizedUser);
				userTransfered.setDateCreated(new Date());
				selectedType.setDateCreated(new Date());
				usersFacade.edit(userTransfered);
				userTypesFacade.edit(selectedType);
			}
		}
		
		//Constructing success message
		if(addFlag)
			successMessage += Message.getInstance().getMessage(Message.UserTypes.USER_TYPES_ADD_SUCCESS_END) + " " + selectedType.getName();
		else
			successMessage += Message.getInstance().getMessage(Message.UserTypes.USER_TYPES_REMOVE_SUCCESS_END) + " " + selectedType.getName();
		
		LOG.info(successMessage);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}

}
