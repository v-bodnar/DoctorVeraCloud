package ua.kiev.doctorvera.views;

import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import ua.kiev.doctorvera.entities.UserGroups;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.UserGroupsFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.resources.Message;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Named(value="userGroupsTableView")
@ViewScoped
public class UserGroupsTableView implements Serializable {
	
	private final static Logger LOG = Logger.getLogger(UserGroupsTableView.class.getName());
	
	//Facade for CRUD operations with User Groups
	@EJB
	private UserGroupsFacadeLocal userGroupsFacade;
	
	//Facade for CRUD operations with Users
	@EJB
	private UsersFacadeLocal usersFacade;

	@Inject
	private SessionParams sessionParams;

	//Authorized User
	private Users authorizedUser;

    //All User Groups
	private List<UserGroups> allGroups;
	
	//New User Group
	private UserGroups newType;
	
	//Selected User Group
	private UserGroups selectedType;
	
	//Model for picklist PrimeFaces widget
	private DualListModel<Users> usersDualListModel;
	
	@PostConstruct
	public void init(){
		authorizedUser = sessionParams.getAuthorizedUser();
		allGroups = userGroupsFacade.findAll();
		this.newType = new UserGroups();
		this.selectedType = new UserGroups();
		constructPickList();
	}
	
	public Users getAuthorizedUser() {
		return authorizedUser;
	}

	public void setAuthorizedUser(Users authorizedUser) {
		this.authorizedUser = authorizedUser;
	}

	public List<UserGroups> getAllGroups() {
		return allGroups;
	}

	public void setAllGroups(List<UserGroups> allGroups) {
		this.allGroups = allGroups;
	}

	public UserGroups getNewType() {
		return newType;
	}

	public void setNewType(UserGroups newType) {
		this.newType = newType;
	}
	
	public void initNewType() {
		this.newType = new UserGroups();
	}

	public UserGroups getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(UserGroups selectedType) {
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
			List<Users> targetUsers = usersFacade.findByGroup(selectedType);
			for(Users user : targetUsers){
				allUsers.remove(user);
			}
			usersDualListModel = new DualListModel<Users>(allUsers, targetUsers);
		} else
			usersDualListModel = new DualListModel<Users>(new ArrayList<Users>(), new ArrayList<Users>());
	}

	//Deletes selected User Group
	public void deleteSelectedType(){
		userGroupsFacade.remove(selectedType);
		allGroups.remove(selectedType);
		final String successMessage = Message.getInstance().getString("USER_TYPES_DELETED");
		final String successTitle = Message.getInstance().getString("VALIDATOR_SUCCESS_TITLE");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}	
	
	//Updates selected User Group
    public void saveSelectedType() {
		selectedType.setDateCreated(new Date());
		selectedType.setUserCreated(authorizedUser);
		userGroupsFacade.edit(selectedType);
		final String successMessage = Message.getInstance().getString("USER_TYPES_EDITED");
		final String successTitle = Message.getInstance().getString("VALIDATOR_SUCCESS_TITLE");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
    }
    
    //Adds selected User Group
	public void saveNewType(){
		newType.setDateCreated(new Date());
		newType.setUserCreated(authorizedUser);
		userGroupsFacade.create(newType);
		allGroups.add(newType);
		final String successMessage = Message.getInstance().getString("APPLICATION_SAVED");
		final String successTitle = Message.getInstance().getString("USER_TYPES_SAVED");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}
	
	//This method controls onTransfer event in the Pick List
	public void onTransfer(TransferEvent event){

		
		//All Users from the right picker
		List<Users> targetList = usersDualListModel.getTarget();
		
		//Indicates to add new group to user transfered or to remove group from user
		Boolean addFlag = false;
		
		//Checking transfer direction
		if(targetList != null && targetList.contains(event.getItems().get(0))) 
			addFlag = true; //Means that user transfered from left picker to right picker
			
		//Constructing success message
		final String successTitle = Message.getInstance().getString("VALIDATOR_SUCCESS_TITLE");
		String successMessage;
		if(targetList != null && targetList.contains(event.getItems().get(0)))
			successMessage = Message.getInstance().getString("USER_TYPES_ADD_SUCCESS_START");
		else
			successMessage = Message.getInstance().getString("USER_TYPES_REMOVE_SUCCESS_START");
		
		//Iterating each transfered user
		for(Object userObject : event.getItems()){
			Users userTransfered=(Users)userObject;
			
			//Constructing success message
			successMessage += userTransfered.getFirstName() + " " + userTransfered.getLastName() + ", ";
			
			if(addFlag){
				//Add group to user transfered
				usersFacade.addUserGroup(userTransfered, selectedType, authorizedUser);
				
				//Setting time and user that made changes
				userTransfered.setUserCreated(authorizedUser);
				selectedType.setUserCreated(authorizedUser);
				userTransfered.setDateCreated(new Date());
				selectedType.setDateCreated(new Date());
				usersFacade.edit(userTransfered);
				userGroupsFacade.edit(selectedType);
			}else{
				//Remove group from user transfered
				usersFacade.removeUserGroup(userTransfered, selectedType);
				
				//Setting time and user that made changes
				userTransfered.setUserCreated(authorizedUser);
				selectedType.setUserCreated(authorizedUser);
				userTransfered.setDateCreated(new Date());
				selectedType.setDateCreated(new Date());
				usersFacade.edit(userTransfered);
				userGroupsFacade.edit(selectedType);
			}
		}
		
		//Constructing success message
		if(addFlag)
			successMessage += Message.getInstance().getString("USER_TYPES_ADD_SUCCESS_END") + " " + selectedType.getName();
		else
			successMessage += Message.getInstance().getString("USER_TYPES_REMOVE_SUCCESS_END") + " " + selectedType.getName();
		
		LOG.info(successMessage);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}

}
