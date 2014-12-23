package ua.kiev.doctorvera.managedbeans;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facade.AddressFacadeLocal;
import ua.kiev.doctorvera.facade.UsersFacadeLocal;
import ua.kiev.doctorvera.web.resources.Message;

@ManagedBean(name="usersTableView")
@ViewScoped
public class UsersTableView {
	
	@EJB
	private UsersFacadeLocal usersFacade;
	
	@EJB
	private AddressFacadeLocal addressFacade;
	
	private List<Users> allUsers;
	
	private Users selectedUser;
	
	public UsersTableView(){}
	
	@PostConstruct
	public void init(){
		allUsers = usersFacade.findAll();
		//System.out.println(addressFacade.toString());
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
	

}
