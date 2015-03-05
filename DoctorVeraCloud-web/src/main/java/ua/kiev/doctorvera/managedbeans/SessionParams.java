package ua.kiev.doctorvera.managedbeans;

import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facade.RoomsFacadeLocal;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Created by Volodymyr Bodnar on 10.02.2015.
 * This class is responsible for holding SessionScoped data to be used by ViewScoped views
 */
@ManagedBean(name = "sessionParams")
@SessionScoped
public class SessionParams {

    @EJB
    private RoomsFacadeLocal roomsFacade;

    private Rooms planRoom;
    private Rooms scheduleRoom;
    private Users profileUser;

    public Rooms getPlanRoom() {
        return planRoom;
    }

    public void setPlanRoom(Rooms planRoom) {
        this.planRoom = planRoom;
    }

    public void setPlanRoom(Integer planRoomId) {
        this.planRoom =  roomsFacade.find(planRoomId);
    }

    public Rooms getScheduleRoom() {
        return scheduleRoom;
    }

    public void setScheduleRoom(Rooms scheduleRoom) {
        this.scheduleRoom = scheduleRoom;
    }

    public void setScheduleRoom(Integer scheduleRoomId) {
        this.scheduleRoom =  roomsFacade.find(scheduleRoomId);
    }

    public void setScheduleRoom(String scheduleRoomId) {
        this.scheduleRoom =  roomsFacade.find(Integer.parseInt(scheduleRoomId));
    }


    public Users getProfileUser() {
        return profileUser;
    }

    public void setProfileUser(Users profileUser) {
        this.profileUser = profileUser;
    }
}
