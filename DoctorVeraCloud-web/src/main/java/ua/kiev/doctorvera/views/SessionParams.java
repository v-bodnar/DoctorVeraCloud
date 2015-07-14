package ua.kiev.doctorvera.views;

import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.RoomsFacadeLocal;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by Volodymyr Bodnar on 10.02.2015.
 * This class is responsible for holding SessionScoped data to be used by ViewScoped views
 */
@Named(value = "sessionParams")
@SessionScoped
public class SessionParams implements Serializable {

    @EJB
    private RoomsFacadeLocal roomsFacade;

    private Users authorizedUser;
    private Rooms planRoom;
    private Rooms scheduleRoom;
    private Users profileUser;

    public Rooms getScheduleRoom() {
        return scheduleRoom;
    }

    public void setScheduleRoom(Rooms scheduleRoom) {
        this.scheduleRoom = scheduleRoom;
    }

    public void setScheduleRoom(Integer scheduleRoomId) {
        this.scheduleRoom =  roomsFacade.find(scheduleRoomId);
    }
    public Rooms getPlanRoom() {
        return planRoom;
    }

    public void setPlanRoom(Rooms planRoom) {
        this.planRoom = planRoom;
    }

    public void setPlanRoom(Integer planRoomId) {
        this.planRoom =  roomsFacade.find(planRoomId);
    }

    public Users getProfileUser() {
        return profileUser;
    }

    public void setProfileUser(Users profileUser) {
        this.profileUser = profileUser;
    }

    public Users getAuthorizedUser() {
        return authorizedUser;
    }

    public void setAuthorizedUser(Users authorizedUser) {
        this.authorizedUser = authorizedUser;
    }
}
