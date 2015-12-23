package ua.kiev.doctorvera.views;

import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import ua.kiev.doctorvera.entities.DeliveryGroup;
import ua.kiev.doctorvera.entities.UserGroups;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.DeliveryGroupFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.InitializerFacadeLocal;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by volodymyr.bodnar on 23.11.2015.
 */
@Named(value="deliveryGroupsView")
@ViewScoped
public class DeliveryGroupsView  implements Serializable {
    private final static Logger LOG = Logger.getLogger(UserGroupsTableView.class.getName());

    @EJB
    private DeliveryGroupFacadeLocal deliveryGroupFacade;

    @EJB
    private UsersFacadeLocal usersFacade;

    @EJB
    private UserGroupsFacadeLocal userGroupsFacade;

    @EJB
    private InitializerFacadeLocal initializer;

    @Inject
    private SessionParams sessionParams;

    //Authorized User
    private Users authorizedUser;

    //All Delivery Groups
    private List<DeliveryGroup> allDeliveryGroups;

    //New Delivery Group
    private DeliveryGroup newDeliveryGroup;

    //Selected Delivery Group
    private DeliveryGroup selectedGroup;

    //Model for pick list PrimeFaces widget
    private DualListModel<Users> usersDualListModel;

    //Model for pick list PrimeFaces widget
    private DualListModel<UserGroups> userGroupsDualListModel;

    @PostConstruct
    public void init(){
        authorizedUser = sessionParams.getAuthorizedUser();
        allDeliveryGroups = deliveryGroupFacade.findAll();
        newDeliveryGroup = new DeliveryGroup();
        selectedGroup = new DeliveryGroup();
        constructUsersPickList();
        constructUserGroupsPickList();
    }

    public Users getAuthorizedUser() {
        return authorizedUser;
    }

    public void constructUsersPickList(){
        if (selectedGroup != null && selectedGroup.getId() != null){
            List<Users> allUsers = usersFacade.findAll();
            List<Users> targetUsers = usersFacade.findUsersByDeliveryGroup(selectedGroup);
            for(Users user : targetUsers){
                allUsers.remove(user);
            }
            usersDualListModel = new DualListModel<Users>(allUsers, targetUsers);
        } else
            usersDualListModel = new DualListModel<Users>(new ArrayList<Users>(), new ArrayList<Users>());
    }

    public void constructUserGroupsPickList(){
        if (selectedGroup != null && selectedGroup.getId() != null){
            List<UserGroups> allUserGroups = userGroupsFacade.findAll();
            List<UserGroups> targetUserGroups = userGroupsFacade.findUserGroupsByDeliveryGroup(selectedGroup);
            for(UserGroups userGroup : targetUserGroups){
                allUserGroups.remove(userGroup);
            }
            userGroupsDualListModel = new DualListModel<>(allUserGroups, targetUserGroups);
        } else
            userGroupsDualListModel = new DualListModel<>(new ArrayList<UserGroups>(), new ArrayList<UserGroups>());
    }


    //Deletes selected Delivery Group
    public void deleteSelectedDeliveryGroup(){
        deliveryGroupFacade.remove(selectedGroup);
        allDeliveryGroups.remove(selectedGroup);
        final String successMessage = Message.getInstance().getString("DELIVERY_GROUPS_DELETED");
        final String successTitle = Message.getInstance().getString("VALIDATOR_SUCCESS_TITLE");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
    }

    //Updates selected Delivery Group
    public void saveSelectedGroup() {
        selectedGroup.setDateCreated(new Date());
        selectedGroup.setUserCreated(authorizedUser);
        deliveryGroupFacade.edit(selectedGroup);
        final String successMessage = Message.getInstance().getString("DELIVERY_GROUPS_EDITED");
        final String successTitle = Message.getInstance().getString("VALIDATOR_SUCCESS_TITLE");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
    }

    //Adds selected Delivery Group
    public void saveNewGroup(){
        newDeliveryGroup.setDateCreated(new Date());
        newDeliveryGroup.setUserCreated(authorizedUser);
        deliveryGroupFacade.create(newDeliveryGroup);
        newDeliveryGroup = deliveryGroupFacade.find(newDeliveryGroup);
        allDeliveryGroups.add(newDeliveryGroup);
        final String successMessage = Message.getInstance().getString("APPLICATION_SAVED");
        final String successTitle = Message.getInstance().getString("DELIVERY_GROUPS_SAVED");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
    }

    //This method controls onTransfer event in the Pick List
    public void onDeliveryUserGroupTransfer(TransferEvent event){
        //All Users from the right picker
        List<UserGroups> targetList = userGroupsDualListModel.getTarget();

        //Indicates to add new group to user transferred or to remove group from user
        Boolean addFlag = false;

        //Checking transfer direction
        if(targetList != null && targetList.contains(event.getItems().get(0)))
            addFlag = true; //Means that user transferred from left picker to right picker

        //Constructing success message
        final String successTitle = Message.getInstance().getString("VALIDATOR_SUCCESS_TITLE");
        String successMessage;
        if(targetList != null && targetList.contains(event.getItems().get(0)))
            successMessage = Message.getInstance().getString("DELIVERY_GROUPS_ADD_USER_GROUP_SUCCESS_START");
        else
            successMessage = Message.getInstance().getString("DELIVERY_GROUPS_REMOVE_USER_GROUP_SUCCESS_START");

        //Iterating each transferred userGroup
        for(Object userObject : event.getItems()){
            UserGroups groupTransferred=(UserGroups)userObject;

            //Constructing success message
            successMessage += groupTransferred.getName();

            if(addFlag){
                //Add group to user transferred

                selectedGroup.getUserGroups().add(groupTransferred);
                //usersFacade.addUserGroup(userTransferred, selectedGroup, authorizedUser);
                //deliveryGroupFacade.addUserGroupsToDeliveryGroup(selectedGroup, groupTransferred);
                //Setting time and user that made changes
                selectedGroup.setUserCreated(authorizedUser);
                selectedGroup.setDateCreated(new Date());
                deliveryGroupFacade.edit(selectedGroup);
            }else{
                //Remove group from user transferred
                //usersFacade.removeUserGroup(userTransferred, selectedGroup);
                initializer.initializeLazyEntity(selectedGroup);
                selectedGroup.getUserGroups().remove(groupTransferred);
                //Setting time and user that made changes
                selectedGroup.setUserCreated(authorizedUser);
                selectedGroup.setDateCreated(new Date());
                deliveryGroupFacade.edit(selectedGroup);
            }
        }

        //Constructing success message
        if(addFlag)
            successMessage += Message.getInstance().getString("DELIVERY_GROUPS_ADD_USER_GROUP_SUCCESS_END") + " " + selectedGroup.getName();
        else
            successMessage += Message.getInstance().getString("DELIVERY_GROUPS_REMOVE_USER_GROUP_SUCCESS_END") + " " + selectedGroup.getName();

        LOG.info(successMessage);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
    }

    //This method controls onTransfer event in the Pick List
    public void onUserTransfer(TransferEvent event){
        //All Users from the right picker
        List<Users> targetList = usersDualListModel.getTarget();

        //Indicates to add new group to user transferred or to remove group from user
        Boolean addFlag = false;

        //Checking transfer direction
        if(targetList != null && targetList.contains(event.getItems().get(0)))
            addFlag = true; //Means that user transferred from left picker to right picker

        //Constructing success message
        final String successTitle = Message.getInstance().getString("VALIDATOR_SUCCESS_TITLE");
        String successMessage;
        if(targetList != null && targetList.contains(event.getItems().get(0)))
            successMessage = Message.getInstance().getString("DELIVERY_GROUPS_ADD_USERS_SUCCESS_START");
        else
            successMessage = Message.getInstance().getString("DELIVERY_GROUPS_REMOVE_USERS_SUCCESS_START");

        //Iterating each transfered user
        for(Object userObject : event.getItems()){
            Users userTransferred=(Users)userObject;

            //Constructing success message
            successMessage += userTransferred.getFirstName() + " " + userTransferred.getLastName() + ", ";

            if(addFlag){
                initializer.initializeLazyEntity(selectedGroup);
                //Add group to user transferred
                selectedGroup.getUsers().add(userTransferred);
                //usersFacade.addUserGroup(userTransferred, selectedGroup, authorizedUser);
                //deliveryGroupFacade.addUsersToDeliveryGroup(selectedGroup, userTransferred);

                //Setting time and user that made changes
                selectedGroup.setUserCreated(authorizedUser);
                selectedGroup.setDateCreated(new Date());
                deliveryGroupFacade.edit(selectedGroup);
            }else{
                initializer.initializeLazyEntity(selectedGroup);
                //Remove group from user transferred
                //usersFacade.removeUserGroup(userTransferred, selectedGroup);
                selectedGroup.getUsers().remove(userTransferred);
                //Setting time and user that made changes
                selectedGroup.setUserCreated(authorizedUser);
                selectedGroup.setDateCreated(new Date());
                deliveryGroupFacade.edit(selectedGroup);
            }
        }

        //Constructing success message
        if(addFlag)
            successMessage += Message.getInstance().getString("DELIVERY_GROUPS_ADD_USERS_SUCCESS_END") + " " + selectedGroup.getName();
        else
            successMessage += Message.getInstance().getString("DELIVERY_GROUPS_REMOVE_USERS_SUCCESS_END") + " " + selectedGroup.getName();

        LOG.info(successMessage);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
    }

    /*----------------------------------------------------*/
    /*---------------Getters & Setters--------------------*/
    /*----------------------------------------------------*/

    public List<DeliveryGroup> getAllDeliveryGroups() {
        return allDeliveryGroups;
    }

    public void setAllDeliveryGroups(List<DeliveryGroup> allDeliveryGroups) {
        this.allDeliveryGroups = allDeliveryGroups;
    }

    public void setAuthorizedUser(Users authorizedUser) {
        this.authorizedUser = authorizedUser;
    }

    public DeliveryGroup getNewDeliveryGroup() {
        return newDeliveryGroup;
    }

    public void setNewDeliveryGroup(DeliveryGroup newDeliveryGroup) {
        this.newDeliveryGroup = newDeliveryGroup;
    }

    public DeliveryGroup getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(DeliveryGroup selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public DualListModel<UserGroups> getUserGroupsDualListModel() {
        return userGroupsDualListModel;
    }

    public void setUserGroupsDualListModel(DualListModel<UserGroups> userGroupsDualListModel) {
        this.userGroupsDualListModel = userGroupsDualListModel;
    }

    public DualListModel<Users> getUsersDualListModel() {
        return usersDualListModel;
    }

    public void setUsersDualListModel(DualListModel<Users> usersDualListModel) {
        this.usersDualListModel = usersDualListModel;
    }
}
