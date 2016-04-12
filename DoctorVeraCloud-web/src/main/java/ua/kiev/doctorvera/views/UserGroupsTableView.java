package ua.kiev.doctorvera.views;

import org.primefaces.context.RequestContext;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import ua.kiev.doctorvera.entities.Policy;
import ua.kiev.doctorvera.entities.UserGroups;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.PolicyFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UserGroupsFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.resources.Message;
import ua.kiev.doctorvera.security.SecurityPolicy;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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

	//Facade for CRUD operations with Policies
	@EJB
	private PolicyFacadeLocal policyFacade;

	@Inject
	private SessionParams sessionParams;

	//Authorized User
	private Users authorizedUser;

    //All User Groups
	private List<UserGroups> allGroups;
	
	//New User Group
	private UserGroups newType;
	
	//Selected User Group
	private UserGroups selectedGroup;
	
	//Model for picklist PrimeFaces widget
	private DualListModel<Users> usersDualListModel;

	//Model for policies picklist PrimeFaces widget
	private DualListModel<Policy> policiesDualListModel;

	/**
	 * All Security policies
	 */
	List<Policy> allPolicies;

	/**
	 * All security policies groups
	 */
	private List<SecurityPolicy.SecurityPolicyGroup> allSecurityPolicyGroups = Arrays.asList(SecurityPolicy.SecurityPolicyGroup.values());

	SecurityPolicy.SecurityPolicyGroup selectedSecurityPolicy;

	
	@PostConstruct
	public void init(){
		authorizedUser = sessionParams.getAuthorizedUser();
		allGroups = userGroupsFacade.initializeLazyEntity(userGroupsFacade.findAll());
		newType = new UserGroups();
		selectedGroup = new UserGroups();
		allPolicies = policyFacade.findAll();
		constructPickList();
		constructPoliciesPickList(null);
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

	public UserGroups getSelectedGroup() {
		return selectedGroup;
	}

	public void setSelectedGroup(UserGroups selectedGroup) {
		this.selectedGroup = selectedGroup;
	}
	
	public DualListModel<Users> getUsersDualListModel() {
		return usersDualListModel;
	}

	public void setUsersDualListModel(DualListModel<Users> usersDualListModel) {
		this.usersDualListModel = usersDualListModel;
	}

	public DualListModel<Policy> getPoliciesDualListModel() {
		return policiesDualListModel;
	}

	public void setPoliciesDualListModel(DualListModel<Policy> policiesDualListModel) {
		this.policiesDualListModel = policiesDualListModel;
	}

	public List<SecurityPolicy.SecurityPolicyGroup> getAllSecurityPolicyGroups() {
		return allSecurityPolicyGroups;
	}

	public void setAllSecurityPolicyGroups(List<SecurityPolicy.SecurityPolicyGroup> allSecurityPolicyGroups) {
		this.allSecurityPolicyGroups = allSecurityPolicyGroups;
	}

	public SecurityPolicy.SecurityPolicyGroup getSelectedSecurityPolicy() {
		return selectedSecurityPolicy;
	}

	public void setSelectedSecurityPolicy(SecurityPolicy.SecurityPolicyGroup selectedSecurityPolicy) {
		this.selectedSecurityPolicy = selectedSecurityPolicy;
	}

	public void constructPickList(){
		if (selectedGroup != null && selectedGroup.getId() != null){
			List<Users> allUsers = usersFacade.findAll();
			List<Users> targetUsers = usersFacade.findByGroup(selectedGroup);
			for(Users user : targetUsers){
				allUsers.remove(user);
			}
			usersDualListModel = new DualListModel<Users>(allUsers, targetUsers);
		} else
			usersDualListModel = new DualListModel<Users>(new ArrayList<Users>(), new ArrayList<Users>());
	}

	public void constructPoliciesPickList(AjaxBehaviorEvent e){
		if (selectedGroup != null && selectedGroup.getId() != null && selectedSecurityPolicy != null){
			userGroupsFacade.initializeLazyEntity(selectedGroup);
			List<Policy> sourcePolicies = new ArrayList<>();
			List<Policy> targetPolicy = new ArrayList<>();
			for(Policy policy : selectedGroup.getPolicies()){
				if (policy.getPolicyGroup().equals(selectedSecurityPolicy.name())){
					targetPolicy.add(policy);
				}
			}
			for (Policy policy : allPolicies) {
				if (policy.getPolicyGroup().equals(selectedSecurityPolicy.name()) && !targetPolicy.contains(policy)){
					sourcePolicies.add(policy);
				}
			}
			policiesDualListModel = new DualListModel<>(sourcePolicies, targetPolicy);
		} else
			policiesDualListModel = new DualListModel<>(new ArrayList<Policy>(), new ArrayList<Policy>());
	}

	//Deletes selected User Group
	public void deleteSelectedType(){
		userGroupsFacade.remove(selectedGroup);
		allGroups.remove(selectedGroup);
		final String successMessage = Message.getMessage("USER_TYPES_DELETED");
		final String successTitle = Message.getMessage("VALIDATOR_SUCCESS_TITLE");
		Message.showMessage(successTitle, successMessage);
	}	
	
	//Updates selected User Group
    public void saveSelectedType() {
		selectedGroup.setDateCreated(new Date());
		selectedGroup.setUserCreated(authorizedUser);
		userGroupsFacade.edit(selectedGroup);
		final String successMessage = Message.getMessage("USER_TYPES_EDITED");
		final String successTitle = Message.getMessage("VALIDATOR_SUCCESS_TITLE");
		Message.showMessage(successTitle, successMessage);
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('editTypeDialog').hide()");
    }
    
    //Adds selected User Group
	public void saveNewType(){
		newType.setDateCreated(new Date());
		newType.setUserCreated(authorizedUser);
		userGroupsFacade.create(newType);
		allGroups.add(newType);
		final String successMessage = Message.getMessage("APPLICATION_SAVED");
		final String successTitle = Message.getMessage("USER_TYPES_SAVED");
		Message.showMessage(successTitle, successMessage);
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('addTypeDialog').hide()");
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
		final String successTitle = Message.getMessage("VALIDATOR_SUCCESS_TITLE");
		String successMessage;
		if(targetList != null && targetList.contains(event.getItems().get(0)))
			successMessage = Message.getMessage("USER_TYPES_ADD_SUCCESS_START");
		else
			successMessage = Message.getMessage("USER_TYPES_REMOVE_SUCCESS_START");
		
		//Iterating each transfered user
		for(Object userObject : event.getItems()){
			Users userTransfered=(Users)userObject;
			
			//Constructing success message
			successMessage += userTransfered.getFirstName() + " " + userTransfered.getLastName() + ", ";
			
			if(addFlag){
				//Add group to user transfered
				//usersFacade.addUserGroup(userTransfered, selectedGroup, authorizedUser);
				userTransfered = usersFacade.initializeLazyEntity(userTransfered);
				userTransfered.getUserGroups().add(selectedGroup);

				//Setting time and user that made changes
				userTransfered.setUserCreated(authorizedUser);
				selectedGroup.setUserCreated(authorizedUser);
				userTransfered.setDateCreated(new Date());
				selectedGroup.setDateCreated(new Date());
				usersFacade.edit(userTransfered);
				userGroupsFacade.edit(selectedGroup);
			}else{
				//Remove group from user transfered
				//usersFacade.removeUserGroup(userTransfered, selectedGroup);
				userTransfered = usersFacade.initializeLazyEntity(userTransfered);
				userTransfered.getUserGroups().remove(selectedGroup);

				//Setting time and user that made changes
				userTransfered.setUserCreated(authorizedUser);
				selectedGroup.setUserCreated(authorizedUser);
				userTransfered.setDateCreated(new Date());
				selectedGroup.setDateCreated(new Date());
				usersFacade.edit(userTransfered);
				userGroupsFacade.edit(selectedGroup);
			}
		}
		
		//Constructing success message
		if(addFlag)
			successMessage += Message.getMessage("USER_TYPES_ADD_SUCCESS_END") + " " + selectedGroup.getName();
		else
			successMessage += Message.getMessage("USER_TYPES_REMOVE_SUCCESS_END") + " " + selectedGroup.getName();
		
		LOG.info(successMessage);
		Message.showMessage(successTitle, successMessage);
	}

	//This method controls onTransfer event in the Policies Pick List
	public void onPolicyTransfer(TransferEvent event){


		//All Policies from the right picker
		List<Policy> targetList = policiesDualListModel.getTarget();

		//Indicates to add new policy to group or to remove policy from group
		Boolean addFlag = false;

		//Checking transfer direction
		if(targetList != null && targetList.contains(event.getItems().get(0)))
			addFlag = true; //Means that user transferred from left picker to right picker

		//Constructing success message
		final String successTitle = Message.getMessage("VALIDATOR_SUCCESS_TITLE");
		String successMessage;
		if(targetList != null && targetList.contains(event.getItems().get(0)))
			successMessage = Message.getMessage("USER_TYPES_POLICIES_ADD_SUCCESS_START");
		else
			successMessage = Message.getMessage("USER_TYPES_POLICIES_REMOVE_SUCCESS_START");

		//Iterating each transferred policy
		for(Object policyObject : event.getItems()){
			Policy policyTransferred = policyFacade.initializeLazyEntity((Policy)policyObject);

			//Constructing success message
			successMessage += policyTransferred.getStringId() + ", ";

			if(addFlag){
				//Add group to user transferred
				//policyFacade.addUserGroups(selectedGroup,policyTransferred,authorizedUser);
				policyTransferred.getUserGroups().add(selectedGroup);
				//Setting time and user that made changes
				policyTransferred.setUserCreated(authorizedUser);
				selectedGroup.setUserCreated(authorizedUser);
				policyTransferred.setDateCreated(new Date());
				selectedGroup.setDateCreated(new Date());
				policyFacade.edit(policyTransferred);
				userGroupsFacade.edit(selectedGroup);
			}else{
				//Remove group from user transferred
				//policyFacade.removeUserGroup(selectedGroup, policyTransferred);
				policyTransferred.getUserGroups().remove(selectedGroup);
				//Setting time and user that made changes
				policyTransferred.setUserCreated(authorizedUser);
				selectedGroup.setUserCreated(authorizedUser);
				policyTransferred.setDateCreated(new Date());
				selectedGroup.setDateCreated(new Date());
				policyFacade.edit(policyTransferred);
				userGroupsFacade.edit(selectedGroup);
			}
		}

		//Constructing success message
		if(addFlag)
			successMessage += Message.getMessage("USER_TYPES_POLICIES_ADD_SUCCESS_END") + " " + selectedGroup.getName();
		else
			successMessage += Message.getMessage("USER_TYPES_POLICIES_REMOVE_SUCCESS_END") + " " + selectedGroup.getName();

		LOG.info(successMessage);
		Message.showMessage(successTitle, successMessage);
	}

	public String getSecurityPolicyDescription(String securityPolicy){
		if(securityPolicy == null || securityPolicy.isEmpty()) return "";
		return Message.getMessage("SECURITY_POLICY_" + securityPolicy);
	}
	public String getSecurityPolicyGroupDescription(SecurityPolicy.SecurityPolicyGroup securityPolicyGroup){
		if(securityPolicyGroup == null) return "";
		return Message.getMessage("SECURITY_" + securityPolicyGroup.name() + "_DESCRIPTION");
	}

}
