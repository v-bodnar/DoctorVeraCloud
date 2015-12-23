package ua.kiev.doctorvera.views;

import org.primefaces.model.menu.*;
import ua.kiev.doctorvera.entities.MessageTemplate;
import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.facadeLocal.RoomsFacadeLocal;
import ua.kiev.doctorvera.resources.Mapping;
import ua.kiev.doctorvera.resources.Message;
import ua.kiev.doctorvera.security.SecurityPolicy;
import ua.kiev.doctorvera.security.SecurityUtils;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named(value = "menuView")
@ViewScoped
public class MenuView implements Serializable {

    private MenuModel menuModel;

    private final String planMenuHeader = Message.getInstance().getString("MENU_ITEM_PLAN");
    private final String mainMenuHeader = Message.getInstance().getString("MENU_HEADER");
    private final String scheduleMenuHeader = Message.getInstance().getString("MENU_HEADER_SCHEDULE");
    private final String financeMenuHeader = Message.getInstance().getString("MENU_HEADER_FINANCE");
    private final String mainPageValue = Message.getInstance().getString("MENU_ITEM_MAIN");
    private final String mainPageUrl = Mapping.getInstance().getString("MAIN_PAGE");
    private final String usersPageValue = Message.getInstance().getString("MENU_ITEM_USERS");
    private final String usersPageUrl = Mapping.getInstance().getString("USERS_PAGE");
    private final String userTypesPageValue = Message.getInstance().getString("MENU_ITEM_USER_TYPES");
    private final String userTypesPageUrl = Mapping.getInstance().getString("USER_TYPES_PAGE");
    private final String roomsPageValue = Message.getInstance().getString("MENU_ITEM_ROOMS");
    private final String roomsPageUrl = Mapping.getInstance().getString("ROOMS_PAGE");
    private final String sendSMSPageValue = Message.getInstance().getString("MENU_ITEM_SEND_SMS");
    private final String sendSMSPageUrl = Mapping.getInstance().getString("SEND_SMS_PAGE");
    private final String methodsPageValue = Message.getInstance().getString("MENU_ITEM_METHODS");
    private final String methodsPageUrl = Mapping.getInstance().getString("METHODS_PAGE");
    private final String planPageUrl = Mapping.getInstance().getString("PLAN_PAGE");
    private final String paymentsPageValue = Message.getInstance().getString("MENU_ITEM_PAYMENTS");
    private final String paymentsPageUrl = Mapping.getInstance().getString("PAYMENTS_PAGE");
    private final String schedulePageUrl = Mapping.getInstance().getString("SCHEDULE_PAGE");
    private final String planGeneralPageValue = Message.getInstance().getString("MENU_ITEM_PLAN_GENERAL");
    private final String planGeneralPageUrl = Mapping.getInstance().getString("PLAN_GENERAL_PAGE");
    private final String scheduleGeneralPageValue = Message.getInstance().getString("MENU_ITEM_SCHEDULE_GENERAL");
    private final String scheduleGeneralPageUrl = Mapping.getInstance().getString("SCHEDULE_GENERAL_PAGE");
    private final String schedulePersonalPageValue = Message.getInstance().getString("MENU_ITEM_PERSONAL_SCHEDULE");
    private final String deliveryGroupHeader = Message.getInstance().getString("MENU_HEADER_DELIVERY");
    private final String schedulePersonalPageUrl = Mapping.getInstance().getString("SCHEDULE_PERSONAL_PAGE");
    private final String deliveryGroupsPageValue = Message.getInstance().getString("MENU_ITEM_DELIVERY_GROUPS");
    private final String deliveryGroupsPageURL = Mapping.getInstance().getString("DELIVERY_GROUPS_PAGE");
    private final String smsTemplatesPageValue = Message.getInstance().getString("MENU_ITEM_SMS_TEMPLATES");
    private final String smsTemplatesPageUrl = Mapping.getInstance().getString("SMS_TEMPLATES_PAGE");
    private final String emailTemplatesPageValue = Message.getInstance().getString("MENU_ITEM_EMAIL_TEMPLATES");
    private final String emailTemplatesPageUrl = Mapping.getInstance().getString("EMAIL_TEMPLATES_PAGE");
    private static final String APPLICATION_ROOT_URL = Mapping.getInstance().getString("APPLICATION_ROOT_PATH");
    private static final Logger LOG = Logger.getLogger(MenuView.class.getName());
    private static final String SECURITY_POLICY_PARAM_NAME = "securityPolicy";

    @EJB
    private RoomsFacadeLocal roomsFacade;

    @Inject
    SessionParams sessionParams;

    @Inject
    SecurityUtils securityUtils;


    @PostConstruct
    public void init() {
        String url = "";
        try {
            url = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
            LOG.log(Level.INFO, "URL: {0}", url);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Get url exception: {0}", e.getMessage());
        }

        List<Rooms> allRooms = roomsFacade.findAll();
        menuModel = new DynamicMenuModel();
        DefaultSubMenu mainSubmenu = new DefaultSubMenu(mainMenuHeader);

        DefaultMenuItem item = new DefaultMenuItem(mainPageValue);
        item.setUrl(mainPageUrl);
        item.setIcon("ui-icon-home");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_MAIN));
        if (url != null && url.equals(APPLICATION_ROOT_URL + mainPageUrl)) {
            item.setStyleClass("ui-state-active");
        }
        mainSubmenu.addElement(item);

        item = new DefaultMenuItem(schedulePersonalPageValue);
        item.setIcon("ui-icon-calendar");
        item.setCommand("#{menuView.redirectToSchedule(null)}");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_PERSONAL_SCHEDULE));
        if (url != null && url.equals(APPLICATION_ROOT_URL + schedulePageUrl)) {
            item.setStyleClass("ui-state-active");
        }
        mainSubmenu.addElement(item);

        item = new DefaultMenuItem(usersPageValue);
        item.setUrl(usersPageUrl);
        item.setIcon("ui-icon-person");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_USERS));
        if (url != null && url.equals(APPLICATION_ROOT_URL + usersPageUrl)) {
            item.setStyleClass("ui-state-active");
        }
        mainSubmenu.addElement(item);

        item = new DefaultMenuItem(userTypesPageValue);
        item.setUrl(userTypesPageUrl);
        item.setIcon("ui-icon-tag");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_USER_GROUPS));
        if (url != null && url.equals(APPLICATION_ROOT_URL + userTypesPageUrl)) {
            item.setStyleClass("ui-state-active");
        }
        mainSubmenu.addElement(item);

        item = new DefaultMenuItem(roomsPageValue);
        item.setUrl(roomsPageUrl);
        item.setIcon("ui-icon-home");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_ROOMS));
        if (url != null && url.equals(APPLICATION_ROOT_URL + roomsPageUrl)) {
            item.setStyleClass("ui-state-active");
        }
        mainSubmenu.addElement(item);

        item = new DefaultMenuItem(methodsPageValue);
        item.setUrl(methodsPageUrl);
        item.setIcon("ui-icon-signal-diag");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_METHODS));
        if (url != null && url.equals(APPLICATION_ROOT_URL + methodsPageUrl)) {
            item.setStyleClass("ui-state-active");
        }
        mainSubmenu.addElement(item);
        mainSubmenu.setRendered(isRendered(mainSubmenu));

        menuModel.addElement(mainSubmenu);

        DefaultSubMenu planSubmenu = new DefaultSubMenu(planMenuHeader);

        item = new DefaultMenuItem(planGeneralPageValue);
        item.setUrl(planGeneralPageUrl);
        item.setIcon("ui-icon-calendar");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_PLAN_GENERAL));
        if (url != null && url.equals(APPLICATION_ROOT_URL + planGeneralPageUrl)) {
            item.setStyleClass("ui-state-active");
        }
        planSubmenu.addElement(item);

        for (Rooms room : allRooms) {
            item = new DefaultMenuItem(room.getName());
            item.setIcon("ui-icon-calendar");
            item.setCommand("#{menuView.redirectToPlan(" + room.getId() + ")}");
            item.setIcon("ui-icon-calendar");
            item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_PLAN));
            planSubmenu.addElement(item);
        }
        planSubmenu.setRendered(isRendered(planSubmenu));
        menuModel.addElement(planSubmenu);

        DefaultSubMenu scheduleSubmenu = new DefaultSubMenu(scheduleMenuHeader);

        item = new DefaultMenuItem(scheduleGeneralPageValue);
        item.setUrl(scheduleGeneralPageUrl);
        item.setIcon("ui-icon-calendar");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_SCHEDULE_GENERAL));
        if (url != null && url.equals(APPLICATION_ROOT_URL + scheduleGeneralPageUrl)) {
            item.setStyleClass("ui-state-active");
        }
        scheduleSubmenu.addElement(item);

        for (Rooms room : allRooms) {
            item = new DefaultMenuItem(room.getName());
            item.setIcon("ui-icon-calendar");
            item.setCommand("#{menuView.redirectToSchedule(" + room.getId() + ")}");
            item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_SCHEDULE));
            scheduleSubmenu.addElement(item);
        }
        scheduleSubmenu.setRendered(isRendered(scheduleSubmenu));
        menuModel.addElement(scheduleSubmenu);

        DefaultSubMenu financeSubmenu = new DefaultSubMenu(financeMenuHeader);

        item = new DefaultMenuItem(paymentsPageValue);
        item.setUrl(paymentsPageUrl);
        item.setIcon("ui-icon-cart");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_PAYMENTS));
        financeSubmenu.addElement(item);
        if (url != null && url.equals(APPLICATION_ROOT_URL + paymentsPageUrl)) {
            item.setStyleClass("ui-state-active");
        }
        financeSubmenu.setRendered(isRendered(financeSubmenu));
        menuModel.addElement(financeSubmenu);

        DefaultSubMenu deliverySubmenu = new DefaultSubMenu(deliveryGroupHeader);

        item = new DefaultMenuItem(deliveryGroupsPageValue);
        item.setUrl(deliveryGroupsPageURL);
        item.setIcon("ui-icon-mail-closed");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_DELIVERY_GROUPS));
        if (url != null && url.equals(APPLICATION_ROOT_URL + deliveryGroupsPageURL)) {
            item.setStyleClass("ui-state-active");
        }
        deliverySubmenu.addElement(item);

        item = new DefaultMenuItem(smsTemplatesPageValue);
        item.setCommand("#{menuView.redirectToSMSTemplates}");
        item.setIcon("ui-icon-mail-closed");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_SMS_TEMPLATES));
        String smsParam = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("SMS");
        if (url != null && (APPLICATION_ROOT_URL + smsTemplatesPageUrl).contains(url) && smsParam != null && !smsParam.isEmpty()) {
            item.setStyleClass("ui-state-active");
        }
        deliverySubmenu.addElement(item);

        item = new DefaultMenuItem(emailTemplatesPageValue);
        item.setCommand("#{menuView.redirectToEmailTemplates}");
        item.setIcon("ui-icon-mail-closed");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_EMAIL_TEMPLATES));
        String emailParam = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("EMAIL");
        if (url != null && (APPLICATION_ROOT_URL + emailTemplatesPageUrl).contains(url) && emailParam != null && !emailParam.isEmpty()) {
            item.setStyleClass("ui-state-active");
        }
        deliverySubmenu.addElement(item);

        item = new DefaultMenuItem(sendSMSPageValue);
        item.setUrl(sendSMSPageUrl);
        item.setIcon("ui-icon-mail-closed");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_SEND_SMS));
        if (url != null && url.equals(APPLICATION_ROOT_URL + sendSMSPageUrl)) {
            item.setStyleClass("ui-state-active");
        }
        deliverySubmenu.addElement(item);

        deliverySubmenu.setRendered(isRendered(deliverySubmenu));
        menuModel.addElement(deliverySubmenu);

    }
    private boolean isRendered(DefaultSubMenu menuGroup){
        for(MenuElement menuElement : menuGroup.getElements()){
            if(menuElement.isRendered()) return true;
        }
        return false;
    }

    public MenuModel getMenuModel() {
        return menuModel;
    }

    /**
     * Used in setCommand for Schedule Menu Items
     */
    public void redirectToSchedule(Integer roomId){
        sessionParams.setScheduleRoom(roomId);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(schedulePageUrl);
        } catch (IOException e) {
            LOG.severe(e.getMessage());
        }
    }

    /**
     * Used in setCommand for Plan Menu Items
     */
    public void redirectToPlan(Integer roomId){
        sessionParams.setPlanRoom(roomId);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(planPageUrl);
        } catch (IOException e) {
            LOG.severe(e.getMessage());
        }
    }

    /**
     * Used in setCommand for SMS Templates Items
     */
    public void redirectToSMSTemplates(){
        sessionParams.setDeliveryMessageType(MessageTemplate.Type.SMS);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(smsTemplatesPageUrl);
        } catch (IOException e) {
            LOG.severe(e.getMessage());
        }
    }

    /**
     * Used in setCommand for SMS Templates Items
     */
    public void redirectToEmailTemplates(){
        sessionParams.setDeliveryMessageType(MessageTemplate.Type.EMAIL);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(emailTemplatesPageUrl);
        } catch (IOException e) {
            LOG.severe(e.getMessage());
        }
    }
}