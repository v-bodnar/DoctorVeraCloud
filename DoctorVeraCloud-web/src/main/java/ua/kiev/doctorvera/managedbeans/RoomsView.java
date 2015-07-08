package ua.kiev.doctorvera.managedbeans;

import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.RoomsFacadeLocal;
import ua.kiev.doctorvera.resources.Message;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@ManagedBean(name="roomsView")
@ViewScoped
public class RoomsView {
	
	private final static Logger LOG = Logger.getLogger(RoomsView.class.getName());
	
	@EJB
	private RoomsFacadeLocal roomsFacade;
	
    @ManagedProperty(value="#{userLoginView.authorizedUser}")
	private Users authorizedUser;
	
	private List<Rooms> allRooms;
	
	private Rooms newRoom;
	
	private Rooms selectedRoom;
	
	public RoomsView(){}
	
	@PostConstruct
	public void init(){
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
		final String successMessage = Message.getInstance().getString("ROOMS_DELETED");
		final String successTitle = Message.getInstance().getString("VALIDATOR_SUCCESS_TITLE");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}	
	
    public void saveSelectedRoom() {
		selectedRoom.setDateCreated(new Date());
		selectedRoom.setUserCreated(authorizedUser);
		roomsFacade.edit(selectedRoom);
		final String successMessage = Message.getInstance().getString("ROOMS_EDITED");
		final String successTitle = Message.getInstance().getString("VALIDATOR_SUCCESS_TITLE");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
    }
    
	public void saveNewRoom(){
		newRoom.setDateCreated(new Date());
		newRoom.setUserCreated(authorizedUser);
		roomsFacade.create(newRoom);
		allRooms.add(newRoom);
		final String successMessage = Message.getInstance().getString("APPLICATION_SAVED");
		final String successTitle = Message.getInstance().getString("ROOMS_SAVED");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}

}
