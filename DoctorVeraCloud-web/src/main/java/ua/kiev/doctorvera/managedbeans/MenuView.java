package ua.kiev.doctorvera.managedbeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.facade.RoomsFacadeLocal;
import ua.kiev.doctorvera.web.resources.Mapping;
import ua.kiev.doctorvera.web.resources.Message;
 
@ManagedBean(name = "menuView")
@ViewScoped
public class MenuView {
     
    private MenuModel menuModel;
    
    private final String planMenuHeader = Message.getInstance().getMessage(Message.Menu.MENU_ITEM_PLAN);
    private final String mainMenuHeader = Message.getInstance().getMessage(Message.Menu.MENU_HEADER);
    private final String  mainPageValue = Message.getInstance().getMessage(Message.Menu.MENU_ITEM_MAIN);
    private final String  mainPageUrl= Mapping.getInstance().getProperty(Mapping.Page.MAIN_PAGE);
    private final String  usersPageValue = Message.getInstance().getMessage(Message.Menu.MENU_ITEM_USERS);
    private final String  usersPageUrl= Mapping.getInstance().getProperty(Mapping.Page.USERS_PAGE);
    private final String  addUserPageValue = Message.getInstance().getMessage(Message.Menu.MENU_ITEM_ADD_USER);
    private final String  addUserPageUrl= Mapping.getInstance().getProperty(Mapping.Page.USER_ADD_PAGE);
    private final String  userTypesPageValue = Message.getInstance().getMessage(Message.Menu.MENU_ITEM_USER_TYPES);
    private final String  userTypesPageUrl= Mapping.getInstance().getProperty(Mapping.Page.USER_TYPES_PAGE);
    private final String  roomsPageValue = Message.getInstance().getMessage(Message.Menu.MENU_ITEM_ROOMS);
    private final String  roomsPageUrl= Mapping.getInstance().getProperty(Mapping.Page.ROOMS_PAGE);
    private final String  sendSMSPageValue = Message.getInstance().getMessage(Message.Menu.MENU_ITEM_SEND_SMS);
    private final String  sendSMSPageUrl= Mapping.getInstance().getProperty(Mapping.Page.SEND_SMS_PAGE);
    private final String planPageUrl = Mapping.getInstance().getProperty(Mapping.Page.PLAN_PAGE);
    
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
        
        item = new DefaultMenuItem(sendSMSPageValue);
        item.setUrl(sendSMSPageUrl);
        item.setIcon("ui-icon-mail-closed");
        mainSubmenu.addElement(item);
        
        menuModel.addElement(mainSubmenu);
        
        DefaultSubMenu planSubmenu = new DefaultSubMenu(planMenuHeader);
        
        for(Rooms room : allRooms){
	        item = new DefaultMenuItem(room.getName());
	        //item.setUrl(planPageUrl);
	        item.setIcon("ui-icon-calendar");
	        item.setCommand("#{planView.setCurrentRoom}");
	        item.setParam("currentRoom", room.getId() );
	        item.setOncomplete("window.location.replace('"+ planPageUrl +"');");
	        planSubmenu.addElement(item);
        }
	    
        menuModel.addElement(planSubmenu);
        
    }

	public MenuModel getMenuModel() {
        return menuModel;
    }  
     
}