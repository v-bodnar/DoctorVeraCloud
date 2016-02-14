package ua.kiev.doctorvera.views;

import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.RoomsFacadeLocal;
import ua.kiev.doctorvera.resources.Message;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Named(value="roomsView")
@ViewScoped
public class RoomsView implements Serializable {
	
	private final static Logger LOG = Logger.getLogger(RoomsView.class.getName());
	
	@EJB
	private RoomsFacadeLocal roomsFacade;

	@Inject
	private SessionParams sessionParams;

	private Users authorizedUser;
	
	private List<Rooms> allRooms;
	
	private Rooms newRoom;
	
	private Rooms selectedRoom;
	
	public RoomsView(){}
	
	@PostConstruct
	public void init(){
		authorizedUser = sessionParams.getAuthorizedUser();
		allRooms = roomsFacade.findAll();
		this.newRoom = new Rooms();
		this.selectedRoom = new Rooms();
		LOG.info("Param:" + authorizedUser.getClass() +" injected into" + this.getClass() + "!");
	}
	
	public Users getAuthorizedUser() {
		return authorizedUser;
	}

	public void setAuthorizedUser(Users authorizedUser) {
		this.authorizedUser = authorizedUser;
	}

	public List<Rooms> getAllRooms() {
		return allRooms;
	}

	public void setAllRooms(List<Rooms> allRooms) {
		this.allRooms = allRooms;
	}

	public Rooms getNewRoom() {
		return newRoom;
	}

	public void setNewRoom(Rooms newRoom) {
		this.newRoom = newRoom;
	}

	public Rooms getSelectedRoom() {
		return selectedRoom;
	}

	public void setSelectedRoom(Rooms selectedRoom) {
		this.selectedRoom = selectedRoom;
	}
	
	public void initNewRoom() {
		this.newRoom = new Rooms();
	}

	public void deleteSelectedRoom(){
		roomsFacade.remove(selectedRoom);
		allRooms.remove(selectedRoom);
		final String successMessage = Message.getMessage("ROOMS_DELETED");
		final String successTitle = Message.getMessage("VALIDATOR_SUCCESS_TITLE");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}	
	
    public void saveSelectedRoom() {
		selectedRoom.setDateCreated(new Date());
		selectedRoom.setUserCreated(authorizedUser);
		roomsFacade.edit(selectedRoom);
		final String successMessage = Message.getMessage("ROOMS_EDITED");
		final String successTitle = Message.getMessage("VALIDATOR_SUCCESS_TITLE");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
    }
    
	public void saveNewRoom(){
		newRoom.setDateCreated(new Date());
		newRoom.setUserCreated(authorizedUser);
		roomsFacade.create(newRoom);
		allRooms.add(newRoom);
		final String successMessage = Message.getMessage("APPLICATION_SAVED");
		final String successTitle = Message.getMessage("ROOMS_SAVED");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}

}
