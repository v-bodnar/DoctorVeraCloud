package ua.kiev.doctorvera.views;

import org.primefaces.context.RequestContext;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import ua.kiev.doctorvera.entities.UserGroups;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.AddressFacadeLocal;
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
import java.text.ParseException;
import java.util.*;
import java.util.logging.Logger;

@Named(value="usersTableView")
@ViewScoped
public class UsersTableView implements Serializable {
	
	private final static Logger LOG = Logger.getLogger(UserGroupsTableView.class.getName());
	
	@EJB
	private UsersFacadeLocal usersFacade;
	
	@EJB
	private UserGroupsFacadeLocal userGroupsFacade;
	
	@EJB
	private AddressFacadeLocal addressFacade;

	@Inject
	private SessionParams sessionParams;

	private UsersLazyModel allUsers;

	private Users authorizedUser;

	//private LazyDataModel<Users> allUsers;
	//private List<Users> allUsers;
	private List<Users> filteredUsers;
	private List<UserGroups> allUserGroups;
	private List<String> allUserGroupsNames = new ArrayList<>();

	public List<String> getAllUserGroupsNames() {
		return allUserGroupsNames;
	}

	public void setAllUserGroupsNames(List<String> allUserGroupsNames) {
		this.allUserGroupsNames = allUserGroupsNames;
	}

	public List<UserGroups> getAllUserGroups() {
		return allUserGroups;
	}

	public void setAllUserGroups(List<UserGroups> allUserGroups) {
		this.allUserGroups = allUserGroups;
	}

	public List<Users> getFilteredUsers() {
		return filteredUsers;
	}

	public void setFilteredUsers(List<Users> filteredUsers) {
		this.filteredUsers = filteredUsers;
	}

	private Users selectedUser;
	
	public UsersTableView(){}
	
	//Model for picklist PrimeFaces widget
	private DualListModel<UserGroups> userTypesDualListModel;

	private String groupFilter;
	
	@PostConstruct
	public void init(){
		authorizedUser = sessionParams.getAuthorizedUser();
		allUsers = new UsersLazyModel();
		allUserGroups = userGroupsFacade.initializeLazyEntity(userGroupsFacade.findAll());
		for(UserGroups group : allUserGroups){
			allUserGroupsNames.add(group.getName());
		}
		//System.out.println(addressFacade.toString());
		constructPickList();
	}
	
	public void constructPickList(){
		if (selectedUser != null && selectedUser.getId() != null){
			List<UserGroups> allTypes = userGroupsFacade.findAll();
			List<UserGroups> targetUsers = userGroupsFacade.findByUser(selectedUser);
			for(UserGroups userType : targetUsers){
				allTypes.remove(userType);
			}
			userTypesDualListModel = new DualListModel<UserGroups>(allTypes, targetUsers);
		} else
			userTypesDualListModel = new DualListModel<UserGroups>(new ArrayList<UserGroups>(), new ArrayList<UserGroups>());
	}

//	public List<Users> getAllUsers() {
//		return allUsers;
//	}
//
//	public void setAllUsers(List<Users> allUsers) {
//		this.allUsers = allUsers;
//	}


//	public LazyDataModel<Users> getAllUsers() {
//		return allUsers;
//	}
//
//	public void setAllUsers(LazyDataModel<Users> allUsers) {
//		this.allUsers = allUsers;
//	}


	public UsersLazyModel getAllUsers() {
		return allUsers;
	}

	public void setAllUsers(UsersLazyModel allUsers) {
		this.allUsers = allUsers;
	}

	public Users getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(Users selectedUser) {
		this.selectedUser = selectedUser;
	}
	
	public DualListModel<UserGroups> getUserTypesDualListModel() {
		return userTypesDualListModel;
	}

	public void setUserTypesDualListModel(DualListModel<UserGroups> userTypesDualListModel) {
		this.userTypesDualListModel = userTypesDualListModel;
	}
	
	public void setAuthorizedUser(Users authorizedUser) {
		this.authorizedUser = authorizedUser;
	}

	public UserGroupsFacadeLocal getUserGroupsFacade() {
		return userGroupsFacade;
	}

	public void setUserGroupsFacade(UserGroupsFacadeLocal userGroupsFacade) {
		this.userGroupsFacade = userGroupsFacade;
	}

	public void deleteUser(){
		addressFacade.remove(selectedUser.getAddress());
		usersFacade.remove(selectedUser);
		//allUsers.remove(selectedUser);
		final String successMessage = Message.getMessage("USERS_DELETE_CONFIRM_TITLE");
		final String successTitle = Message.getMessage("USERS_DELETED");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}
	
	public boolean filterByBirthDate(Object value, Object filter, Locale locale) throws ParseException {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if(filterText == null||filterText.equals("")) {
            return true;
        }
         
        if(value == null) {
            return false;
        }
        
        return ((Date)value).compareTo((Date)filter) > 0;
    }
	
	//This method controls onTransfer event in the Pick List
	public void onTransfer(TransferEvent event){

		
		//All Users from the right picker
		List<UserGroups> targetList = userTypesDualListModel.getTarget();
		
		//Indicates to add new group to user transferred or to remove group from user
		Boolean addFlag = false;
		
		//Checking transfer direction
		if(targetList != null && targetList.contains(event.getItems().get(0))) 
			addFlag = true; //Means that user transfered from left picker to right picker
			
		//Constructing success message
		final String successTitle = Message.getMessage("VALIDATOR_SUCCESS_TITLE");
		String successMessage;
		if(targetList != null && targetList.contains(event.getItems().get(0)))
			successMessage = Message.getMessage("USERS_ADD_SUCCESS_START") + " " +
				selectedUser.getFirstName() + " " + selectedUser.getFirstName() +
				Message.getMessage("USERS_ADD_SUCCESS_END")+ " ";
		else
			successMessage = Message.getMessage("USERS_REMOVE_SUCCESS_START") + " " +
				selectedUser.getFirstName() + " " + selectedUser.getFirstName() +
				Message.getMessage("USERS_REMOVE_SUCCESS_END") + " ";

		//Iterating each transfered user
		for(Object userTypeObject : event.getItems()){
			UserGroups userGroupTransfered=(UserGroups)userTypeObject;
			
				//Constructing success message
				successMessage += userGroupTransfered.getName() + ", ";
			
			if(addFlag){
				//Add group to user transfered
				selectedUser.getUserGroups().add(userGroupTransfered);
				
				//Setting time and user that made changes
				userGroupTransfered.setUserCreated(authorizedUser);
				selectedUser.setUserCreated(authorizedUser);
				userGroupTransfered.setDateCreated(new Date());
				selectedUser.setDateCreated(new Date());

				userGroupsFacade.edit(userGroupTransfered);
				usersFacade.edit(selectedUser);
			}else{
				//Remove group from user transferred
				selectedUser.getUserGroups().remove(userGroupTransfered);
				
				//Setting time and user that made changes
				userGroupTransfered.setUserCreated(authorizedUser);
				selectedUser.setUserCreated(authorizedUser);
				userGroupTransfered.setDateCreated(new Date());
				selectedUser.setDateCreated(new Date());

				userGroupsFacade.edit(userGroupTransfered);
				usersFacade.edit(selectedUser);
			}
		}

		LOG.info(successMessage);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}

	public boolean filterByUserGroup(Object value, Object filter, Locale locale){
		String filterText = (filter == null) ? null : filter.toString().trim();

		if(filterText == null||filterText.equals("")) {
			return true;
		}

		if(value == null) {
			return false;
		}

		List<UserGroups> userGroups = (List<UserGroups>) value;
		for(UserGroups userGroup : userGroups){
			if(userGroup.getName().equals(filterText)) return true;
		}
		return false;
	}

	public void createNewUserDialog(){
		Map<String,Object> options = new HashMap<>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		options.put("contentHeight", 600);
		options.put("contentWidth", 800);
		RequestContext.getCurrentInstance().openDialog("add_user", options, null);
	}

	public String getGroupFilter() {
		return groupFilter;
	}

	public void setGroupFilter(String groupFilter) {
		this.groupFilter = groupFilter;
	}

	public void resetGroupFilter(){
		groupFilter = null;
	}

	public void error() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Contact admin."));
	}

	public class UsersLazyModel extends LazyDataModel<Users> {

		List<Users> allPaginatedFilteredTemplates = new ArrayList<>();

		public UsersLazyModel() {
		}

		@Override
		public Users getRowData(String rowKey) {
			for(Users user : allPaginatedFilteredTemplates) {
				if(user.getId().equals(Integer.parseInt(rowKey)))
					return user;
			}
			return null;
		}

		@Override
		public Object getRowKey(Users user) {
			return user.getId();
		}

		@Override
		public List<Users> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
			allPaginatedFilteredTemplates = usersFacade.initializeLazyEntity(usersFacade.findAll(first, pageSize, sortField, sortOrder, filters), sortOrder, sortField);
			setRowCount(usersFacade.count(first, pageSize, filters));
			return allPaginatedFilteredTemplates;
		}
	}
}
