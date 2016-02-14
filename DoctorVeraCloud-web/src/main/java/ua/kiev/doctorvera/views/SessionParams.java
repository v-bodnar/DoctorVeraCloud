package ua.kiev.doctorvera.views;

import ua.kiev.doctorvera.entities.MessageTemplate;
import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.RoomsFacadeLocal;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Volodymyr Bodnar on 10.02.2015.
 * This class is responsible for holding SessionScoped data to be used by ViewScoped views
 */
@Named(value = "sessionParams")
@SessionScoped
public class SessionParams implements Serializable {

    public static final Locale DEFAULT_LOCALE = new Locale("ru", "Ru");

    @EJB
    private RoomsFacadeLocal roomsFacade;

    private Users authorizedUser;
    private Rooms planRoom;
    private Rooms scheduleRoom;
    private Users profileUser;
    private Locale currentLocale;
    //Preferred by browser Locale
    private Locale preferredLocale;

    public void setDefaultLocale(){
        if(authorizedUser.getLocale() == null){
            currentLocale = new Locale("ru", "RU");
        }else{
            currentLocale = new Locale(authorizedUser.getLocale().getLanguageCode(), authorizedUser.getLocale().getCountryCode());
        }

        FacesContext.getCurrentInstance().getViewRoot().setLocale(currentLocale);
        Locale.setDefault(currentLocale);
        ResourceBundle.clearCache();
    }

    //Holds value of the delivery messageType
    private MessageTemplate.Type deliveryMessageType;

    public Rooms getScheduleRoom() {
        return scheduleRoom;
    }

    public void setScheduleRoom(Rooms scheduleRoom) {
        this.scheduleRoom = scheduleRoom;
    }

    public void setScheduleRoom(Integer scheduleRoomId) {
        if (scheduleRoomId != null )
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
        if(currentLocale == null){
            setDefaultLocale();
        }
    }

    public MessageTemplate.Type getDeliveryMessageType() {
        return deliveryMessageType;
    }

    public void setDeliveryMessageType(MessageTemplate.Type deliveryMessageType) {
        this.deliveryMessageType = deliveryMessageType;
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }

    public void setCurrentLocale(Locale locale){
        currentLocale = locale;
    }

    public Locale getPreferredLocale() {
        return preferredLocale;
    }

    public void setPreferredLocale(Locale preferredLocale) {
        this.preferredLocale = preferredLocale;
    }
}
