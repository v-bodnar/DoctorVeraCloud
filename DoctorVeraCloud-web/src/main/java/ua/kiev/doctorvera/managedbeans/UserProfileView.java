package ua.kiev.doctorvera.managedbeans;

import javax.ejb.EJB;

import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facade.UsersFacadeLocal;

public class UserProfileView {
	
	@EJB
	private UsersFacadeLocal usersFacade;
	
	private Users user;

	public UsersFacadeLocal getUsersFacade() {
		return usersFacade;
	}

	public void setUsersFacade(UsersFacadeLocal usersFacade) {
		this.usersFacade = usersFacade;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
	
	
}
