package ua.kiev.doctorvera.managedbeans;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import ua.kiev.doctorvera.entities.UserTypes;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facade.AddressFacadeLocal;
import ua.kiev.doctorvera.facade.UserTypesFacadeLocal;
import ua.kiev.doctorvera.facade.UsersFacadeLocal;
import ua.kiev.doctorvera.web.resources.Message;

@ManagedBean(name="scheduleGeneralView")
@ViewScoped
public class ScheduleGeneralView {
	
	private final static Logger LOG = Logger.getLogger(UserTypesTableView.class.getName());
	
	@EJB
	private UsersFacadeLocal usersFacade;
	
	@EJB
	private UserTypesFacadeLocal userTypesFacade;
	
	@EJB
	private AddressFacadeLocal addressFacade;
	
	//Authorized User
    @ManagedProperty(value="#{userLoginView.authorizedUser}")
	private Users authorizedUser;
	
	private List<Users> allUsers;
	
	private Users selectedUser;
	
	public ScheduleGeneralView(){}
	
	//Model for picklist PrimeFaces widget
	private DualListModel<UserTypes> userTypesDualListModel;
	
	@PostConstruct
	public void init(){
		allUsers = usersFacade.findAll();
		//System.out.println(addressFacade.toString());
		constructPickList();
	}
	
	public void constructPickList(){
		if (selectedUser != null && selectedUser.getId() != null){
			List<UserTypes> allTypes = userTypesFacade.findAll();
			List<UserTypes> targetUsers = userTypesFacade.findByUser(selectedUser);
			for(UserTypes userType : targetUsers){
				allTypes.remove(userType);
			}
			userTypesDualListModel = new DualListModel<UserTypes>(allTypes, targetUsers);
		} else
			userTypesDualListModel = new DualListModel<UserTypes>(new ArrayList<UserTypes>(), new ArrayList<UserTypes>());
	}

	public List<Users> getAllUsers() {
		return allUsers;
	}

	public void setAllUsers(List<Users> allUsers) {
		this.allUsers = allUsers;
	}

	public Users getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(Users selectedUser) {
		this.selectedUser = selectedUser;
	}
	
	public DualListModel<UserTypes> getUserTypesDualListModel() {
		return userTypesDualListModel;
	}

	public void setUserTypesDualListModel(DualListModel<UserTypes> userTypesDualListModel) {
		this.userTypesDualListModel = userTypesDualListModel;
	}
	
	public void setAuthorizedUser(Users authorizedUser) {
		this.authorizedUser = authorizedUser;
	}

	public void deleteUser(){
		addressFacade.remove(addressFacade.find(selectedUser.getAddressId()));
		usersFacade.remove(selectedUser);
		allUsers.remove(selectedUser);
		final String successMessage = Message.getInstance().getMessage(Message.UsersDetails.USERS_DELETE_CONFIRM_TITLE);
		final String successTitle = Message.getInstance().getMessage(Message.UsersDetails.USERS_DELETED);
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
		List<UserTypes> targetList = userTypesDualListModel.getTarget();
		
		//Indicates to add new type to user transfered or to remove type from user
		Boolean addFlag = false;
		
		//Checking transfer direction
		if(targetList != null && targetList.contains(event.getItems().get(0))) 
			addFlag = true; //Means that user transfered from left picker to right picker
			
		//Constructing success message
		final String successTitle = Message.getInstance().getMessage(Message.Validator.VALIDATOR_SUCCESS_TITLE);
		String successMessage;
		if(targetList != null && targetList.contains(event.getItems().get(0)))
			successMessage = Message.getInstance().getMessage(Message.Users.USERS_ADD_SUCCESS_START) + " " +
				selectedUser.getFirstName() + " " + selectedUser.getFirstName() +
				Message.getInstance().getMessage(Message.Users.USERS_ADD_SUCCESS_END)+ " ";
		else
			successMessage = Message.getInstance().getMessage(Message.Users.USERS_REMOVE_SUCCESS_START) + " " +
				selectedUser.getFirstName() + " " + selectedUser.getFirstName() +
				Message.getInstance().getMessage(Message.Users.USERS_REMOVE_SUCCESS_END) + " ";

		//Iterating each transfered user
		for(Object userTypeObject : event.getItems()){
			UserTypes userTypeTransfered=(UserTypes)userTypeObject;
			
				//Constructing success message
				successMessage += userTypeTransfered.getName() + ", ";
			
			if(addFlag){
				//Add type to user transfered
				userTypesFacade.addUser(selectedUser, userTypeTransfered, authorizedUser);
				
				//Setting time and user that made changes
				userTypeTransfered.setUserCreated(authorizedUser);
				selectedUser.setUserCreated(authorizedUser);
				userTypeTransfered.setDateCreated(new Date());
				selectedUser.setDateCreated(new Date());
				userTypesFacade.edit(userTypeTransfered);
				usersFacade.edit(selectedUser);
			}else{
				//Remove type from user transfered
				userTypesFacade.removeUser(selectedUser, userTypeTransfered);
				
				//Setting time and user that made changes
				userTypeTransfered.setUserCreated(authorizedUser);
				selectedUser.setUserCreated(authorizedUser);
				userTypeTransfered.setDateCreated(new Date());
				selectedUser.setDateCreated(new Date());
				userTypesFacade.edit(userTypeTransfered);
				usersFacade.edit(selectedUser);
			}
		}

		LOG.info(successMessage);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}

}
