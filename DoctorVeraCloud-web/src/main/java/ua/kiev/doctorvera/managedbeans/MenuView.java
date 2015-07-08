package ua.kiev.doctorvera.managedbeans;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.facadeLocal.RoomsFacadeLocal;
import ua.kiev.doctorvera.resources.Mapping;
import ua.kiev.doctorvera.resources.Message;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.List;
 
@ManagedBean(name = "menuView")
@ViewScoped
public class MenuView {
     
    private MenuModel menuModel;
    
    private final String planMenuHeader = Message.getInstance().getString("MENU_ITEM_PLAN");
    private final String mainMenuHeader = Message.getInstance().getString("MENU_HEADER");
    private final String scheduleMenuHeader = Message.getInstance().getString("MENU_HEADER_SCHEDULE");
    //private final String managerMenuHeader = Message.getInstance().getMessage("MENU_HEADER_MANAGER);
    private final String financeMenuHeader = Message.getInstance().getString("MENU_HEADER_FINANCE");
    private final String  mainPageValue = Message.getInstance().getString("MENU_ITEM_MAIN");
    private final String  mainPageUrl= Mapping.getInstance().getString("MAIN_PAGE");
    private final String  usersPageValue = Message.getInstance().getString("MENU_ITEM_USERS");
    private final String  usersPageUrl= Mapping.getInstance().getString("USERS_PAGE");
    private final String  addUserPageValue = Message.getInstance().getString("MENU_ITEM_ADD_USER");
    private final String  addUserPageUrl= Mapping.getInstance().getString("USER_ADD_PAGE");
    private final String  userTypesPageValue = Message.getInstance().getString("MENU_ITEM_USER_TYPES");
    private final String  userTypesPageUrl= Mapping.getInstance().getString("USER_TYPES_PAGE");
    private final String  roomsPageValue = Message.getInstance().getString("MENU_ITEM_ROOMS");
    private final String  roomsPageUrl= Mapping.getInstance().getString("ROOMS_PAGE");
    private final String  sendSMSPageValue = Message.getInstance().getString("MENU_ITEM_SEND_SMS");
    private final String  sendSMSPageUrl= Mapping.getInstance().getString("SEND_SMS_PAGE");
    private final String  methodsPageValue = Message.getInstance().getString("MENU_ITEM_METHODS");
    private final String  methodsPageUrl= Mapping.getInstance().getString("METHODS_PAGE");
    private final String planPageUrl = Mapping.getInstance().getString("PLAN_PAGE");
    private final String  paymentsPageValue = Message.getInstance().getString("MENU_ITEM_PAYMENTS");
    private final String  paymentsPageUrl= Mapping.getInstance().getString("PAYMENTS_PAGE");
    //private final String  schedulePageValue = Message.getInstance().getMessage("MENU_ITEM_SCHEDULE);
    private final String  schedulePageUrl= Mapping.getInstance().getString("SCHEDULE_PAGE");
    private final String  planGeneralPageValue = Message.getInstance().getString("MENU_ITEM_PLAN_GENERAL");
    private final String  planGeneralPageUrl= Mapping.getInstance().getString("PLAN_GENERAL_PAGE");
    private final String  scheduleGeneralPageValue = Message.getInstance().getString("MENU_ITEM_SCHEDULE_GENERAL");
    private final String  scheduleGeneralPageUrl= Mapping.getInstance().getString("SCHEDULE_GENERAL_PAGE");
    
	@EJB
	private RoomsFacadeLocal roomsFacade;
	
    @PostConstruct
    public void init() {
    	
    	List<Rooms> allRooms = roomsFacade.findAll();
        menuModel = new DefaultMenuModel();
		DefaultSubMenu mainSubmenu = new DefaultSubMenu(mainMenuHeader);
		       
        DefaultMenuItem item = new DefaultMenuItem(mainPageValue);
        item.setUrl(mainPageUrl);
        item.setIcon("ui-icon-home");
        mainSubmenu.addElement(item);
        
        item = new DefaultMenuItem(usersPageValue);
        item.setUrl(usersPageUrl);
        item.setIcon("ui-icon-person");
        mainSubmenu.addElement(item);
        
        item = new DefaultMenuItem(addUserPageValue);
        item.setUrl(addUserPageUrl);
        item.setIcon("ui-icon-plus");
        mainSubmenu.addElement(item);
        
        item = new DefaultMenuItem(userTypesPageValue);
        item.setUrl(userTypesPageUrl);
        item.setIcon("ui-icon-tag");
        mainSubmenu.addElement(item);
        
        item = new DefaultMenuItem(roomsPageValue);
        item.setUrl(roomsPageUrl);
        item.setIcon("ui-icon-home");
        mainSubmenu.addElement(item);
        
        item = new DefaultMenuItem(methodsPageValue);
        item.setUrl(methodsPageUrl);
        item.setIcon("ui-icon-signal-diag");
        mainSubmenu.addElement(item);
        
        item = new DefaultMenuItem(sendSMSPageValue);
        item.setUrl(sendSMSPageUrl);
        item.setIcon("ui-icon-mail-closed");
        mainSubmenu.addElement(item);
        
        menuModel.addElement(mainSubmenu);


        DefaultSubMenu planSubmenu = new DefaultSubMenu(planMenuHeader);
        
        item = new DefaultMenuItem(planGeneralPageValue);
        item.setUrl(planGeneralPageUrl);
        item.setIcon("ui-icon-calendar");
        planSubmenu.addElement(item);
        
        for(Rooms room : allRooms){
	        item = new DefaultMenuItem(room.getName());
	        item.setIcon("ui-icon-calendar");
	        item.setCommand("#{sessionParams.setPlanRoom(" + room.getId() + ")}");
	        item.setOncomplete("window.location.replace('"+ planPageUrl +"');");
	        planSubmenu.addElement(item);
        }
	    
        menuModel.addElement(planSubmenu);

        DefaultSubMenu scheduleSubmenu = new DefaultSubMenu(scheduleMenuHeader);
        
        item = new DefaultMenuItem(scheduleGeneralPageValue);
        item.setUrl(scheduleGeneralPageUrl);
        item.setIcon("ui-icon-calendar");
        scheduleSubmenu.addElement(item);
        
        for(Rooms room : allRooms){
	        item = new DefaultMenuItem(room.getName());
	        item.setIcon("ui-icon-calendar");
            item.setCommand("#{sessionParams.setScheduleRoom(" + room.getId() + ")}");
	        item.setOncomplete("window.location.replace('"+ schedulePageUrl +"');");
	        scheduleSubmenu.addElement(item);
        }

        menuModel.addElement(scheduleSubmenu);
        
        DefaultSubMenu financeSubmenu = new DefaultSubMenu(financeMenuHeader);
        
        item = new DefaultMenuItem(paymentsPageValue);
        item.setUrl(paymentsPageUrl);
        item.setIcon("ui-icon-cart");
        financeSubmenu.addElement(item);
        
        menuModel.addElement(financeSubmenu);
    }

	public MenuModel getMenuModel() {
        return menuModel;
    }
     
}