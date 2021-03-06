package ua.kiev.doctorvera.views;

import org.primefaces.model.menu.*;
import ua.kiev.doctorvera.entities.MessageTemplate;
import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.RoomsFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
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

    private final String planMenuHeader = Message.getMessage("MENU_ITEM_PLAN");
    private final String mainMenuHeader = Message.getMessage("MENU_HEADER");
    private final String scheduleMenuHeader = Message.getMessage("MENU_HEADER_SCHEDULE");
    private final String financeMenuHeader = Message.getMessage("MENU_HEADER_FINANCE");
    private final String settingsMenuHeader = Message.getMessage("MENU_HEADER_SETTINGS");
    private final String mainPageValue = Message.getMessage("MENU_ITEM_MAIN");
    private final String mainPageUrl = Mapping.getMessage("MAIN_PAGE");
    private final String usersPageValue = Message.getMessage("MENU_ITEM_USERS");
    private final String usersPageUrl = Mapping.getMessage("USERS_PAGE");
    private final String userGroupsPageValue = Message.getMessage("MENU_ITEM_USER_TYPES");
    private final String userGroupsPageUrl = Mapping.getMessage("USER_TYPES_PAGE");
    private final String roomsPageValue = Message.getMessage("MENU_ITEM_ROOMS");
    private final String roomsPageUrl = Mapping.getMessage("ROOMS_PAGE");
    private final String sendSMSPageValue = Message.getMessage("MENU_ITEM_SEND_SMS");
    private final String sendSMSPageUrl = Mapping.getMessage("SEND_SMS_PAGE");
    private final String methodsPageValue = Message.getMessage("MENU_ITEM_METHODS");
    private final String methodsPageUrl = Mapping.getMessage("METHODS_PAGE");
    private final String planPageUrl = Mapping.getMessage("PLAN_PAGE");
    private final String paymentsPageValue = Message.getMessage("MENU_ITEM_PAYMENTS");
    private final String paymentsPageUrl = Mapping.getMessage("PAYMENTS_PAGE");
    private final String schedulePageUrl = Mapping.getMessage("SCHEDULE_PAGE");
    private final String planGeneralPageValue = Message.getMessage("MENU_ITEM_PLAN_GENERAL");
    private final String planGeneralPageUrl = Mapping.getMessage("PLAN_GENERAL_PAGE");
    private final String scheduleGeneralPageValue = Message.getMessage("MENU_ITEM_SCHEDULE_GENERAL");
    private final String scheduleGeneralPageUrl = Mapping.getMessage("SCHEDULE_GENERAL_PAGE");
    private final String schedulePersonalPageValue = Message.getMessage("MENU_ITEM_PERSONAL_SCHEDULE");
    private final String deliveryGroupHeader = Message.getMessage("MENU_HEADER_DELIVERY");
    private final String schedulePersonalPageUrl = Mapping.getMessage("SCHEDULE_PERSONAL_PAGE");
    private final String deliveryGroupsPageValue = Message.getMessage("MENU_ITEM_DELIVERY_GROUPS");
    private final String deliveryGroupsPageURL = Mapping.getMessage("DELIVERY_GROUPS_PAGE");
    private final String smsTemplatesPageValue = Message.getMessage("MENU_ITEM_SMS_TEMPLATES");
    private final String smsTemplatesPageUrl = Mapping.getMessage("SMS_TEMPLATES_PAGE");
    private final String emailTemplatesPageValue = Message.getMessage("MENU_ITEM_EMAIL_TEMPLATES");
    private final String emailTemplatesPageUrl = Mapping.getMessage("EMAIL_TEMPLATES_PAGE");
    private final String messageSchedulerPageValue = Message.getMessage("MENU_ITEM_MESSAGE_SCHEDULER");
    private final String messageSchedulerPageUrl = Mapping.getMessage("MESSAGE_SCHEDULER_PAGE");
    private final String localizationPageValue = Message.getMessage("MENU_ITEM_LOCALIZATION");
    private final String localizationPageUrl = Mapping.getMessage("LOCALIZATION_PAGE");
    private final String settingsPageValue = Message.getMessage("MENU_ITEM_SETTINGS");
    private final String settingsPageUrl = Mapping.getMessage("SETTINGS_PAGE");
    private final String financialSettingsPageValue = Message.getMessage("MENU_ITEM_FINANCIAL_SETTINGS");
    private final String financialSettingsPageUrl = Mapping.getMessage("FINANCIAL_SETTINGS_PAGE");
    private final String statisticsPageValue = Message.getMessage("MENU_ITEM_STATISTICS");
    private final String statisticsPageUrl = Mapping.getMessage("STATISTICS_PAGE");
    private final String salaryPageValue = Message.getMessage("MENU_ITEM_SALARY");
    private final String salarySettingsPageUrl = Mapping.getMessage("SALARY_PAGE");
    private final String deliveryLogsPageValue = Message.getMessage("MENU_ITEM_DELIVERY_LOGS");
    private final String deliveryLogsPageUrl = Mapping.getMessage("DELIVERY_LOGS_PAGE");
    private static final String APPLICATION_ROOT_URL = "";
    private static final Logger LOG = Logger.getLogger(MenuView.class.getName());
    private static final String SECURITY_POLICY_PARAM_NAME = "securityPolicy";

    @EJB
    private RoomsFacadeLocal roomsFacade;

    @EJB
    private UsersFacadeLocal usersFacade;

    @Inject
    SessionParams sessionParams;

    @Inject
    SecurityUtils securityUtils;

    Users authorizedUser;

    boolean authorisedUserIsDoctor = false;

    boolean authorisedUserIsAssistant = false;


    @PostConstruct
    public void init() {
        String url = "";
        try {
            url = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
            LOG.log(Level.INFO, "URL: {0}", url);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Get url exception: {0}", e.getMessage());
        }
        authorizedUser = sessionParams.getAuthorizedUser();
        authorisedUserIsDoctor = usersFacade.isDoctor(authorizedUser);
        authorisedUserIsAssistant = usersFacade.isAssistant(authorizedUser);

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
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_PERSONAL_SCHEDULE)
                && (authorisedUserIsDoctor || authorisedUserIsAssistant));
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

        item = new DefaultMenuItem(userGroupsPageValue);
        item.setUrl(userGroupsPageUrl);
        item.setIcon("fa fa-group fa-fw");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_USER_GROUPS));
        if (url != null && url.equals(APPLICATION_ROOT_URL + userGroupsPageUrl)) {
            item.setStyleClass("ui-state-active");
        }
        mainSubmenu.addElement(item);

        item = new DefaultMenuItem(roomsPageValue);
        item.setUrl(roomsPageUrl);
        item.setIcon("fa fa-bed  fa-fw");
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
            item.setIcon("fa fa-calendar-o  fa-fw");
            item.setCommand("#{menuView.redirectToPlan(" + room.getId() + ")}");
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
            item.setIcon("fa fa-calendar fa-fw");
            item.setCommand("#{menuView.redirectToSchedule(" + room.getId() + ")}");
            item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_SCHEDULE));
            scheduleSubmenu.addElement(item);
        }
        scheduleSubmenu.setRendered(isRendered(scheduleSubmenu));
        menuModel.addElement(scheduleSubmenu);

        DefaultSubMenu financeSubmenu = new DefaultSubMenu(financeMenuHeader);

        item = new DefaultMenuItem(paymentsPageValue);
        item.setUrl(paymentsPageUrl);
        item.setIcon("fa fa-credit-card  fa-fw");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_PAYMENTS));
        if (url != null && url.equals(APPLICATION_ROOT_URL + paymentsPageUrl)) {
            item.setStyleClass("ui-state-active");
        }
        financeSubmenu.addElement(item);

        item = new DefaultMenuItem(salaryPageValue);
        item.setUrl(salarySettingsPageUrl);
        item.setIcon("fa fa-bitcoin fa-fw");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_SALARY));
        if (url != null && url.equals(APPLICATION_ROOT_URL + salarySettingsPageUrl)) {
            item.setStyleClass("ui-state-active");
        }
        financeSubmenu.addElement(item);

        item = new DefaultMenuItem(statisticsPageValue);
        item.setUrl(statisticsPageUrl);
        item.setIcon("fa fa-area-chart fa-fw");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_STATISTICS));
        if (url != null && url.equals(APPLICATION_ROOT_URL + statisticsPageUrl)) {
            item.setStyleClass("ui-state-active");
        }
        financeSubmenu.addElement(item);

        item = new DefaultMenuItem(financialSettingsPageValue);
        item.setUrl(financialSettingsPageUrl);
        item.setIcon("fa fa-money fa-fw");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_FINANCIAL_SETTINGS));
        if (url != null && url.equals(APPLICATION_ROOT_URL + financialSettingsPageUrl)) {
            item.setStyleClass("ui-state-active");
        }
        financeSubmenu.addElement(item);

        financeSubmenu.setRendered(isRendered(financeSubmenu));
        menuModel.addElement(financeSubmenu);

        DefaultSubMenu deliverySubmenu = new DefaultSubMenu(deliveryGroupHeader);

        item = new DefaultMenuItem(deliveryGroupsPageValue);
        item.setUrl(deliveryGroupsPageURL);
        item.setIcon("fa fa-group fa-fw");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_DELIVERY_GROUPS));
        if (url != null && url.equals(APPLICATION_ROOT_URL + deliveryGroupsPageURL)) {
            item.setStyleClass("ui-state-active");
        }
        deliverySubmenu.addElement(item);

        item = new DefaultMenuItem(smsTemplatesPageValue);
        item.setCommand("#{menuView.redirectToSMSTemplates}");
        item.setIcon("ui-icon-comment");
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

        item = new DefaultMenuItem(messageSchedulerPageValue);
        item.setUrl(messageSchedulerPageUrl);
        item.setIcon("ui-icon-clock");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_MESSAGE_SCHEDULER));
        if (url != null && (APPLICATION_ROOT_URL + messageSchedulerPageUrl).equals(url)) {
            item.setStyleClass("ui-state-active");
        }
        deliverySubmenu.addElement(item);

        item = new DefaultMenuItem(sendSMSPageValue);
        item.setUrl(sendSMSPageUrl);
        item.setIcon("ui-icon-comment");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_SEND_SMS));
        if (url != null && url.equals(APPLICATION_ROOT_URL + sendSMSPageUrl)) {
            item.setStyleClass("ui-state-active");
        }
        deliverySubmenu.addElement(item);

        item = new DefaultMenuItem(deliveryLogsPageValue);
        item.setUrl(deliveryLogsPageUrl);
        item.setIcon("fa fa-check-circle-o fa-fw");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_DELIVERY_LOGS));
        if (url != null && url.equals(APPLICATION_ROOT_URL + deliveryLogsPageUrl)) {
            item.setStyleClass("ui-state-active");
        }
        deliverySubmenu.addElement(item);

        deliverySubmenu.setRendered(isRendered(deliverySubmenu));
        menuModel.addElement(deliverySubmenu);

        DefaultSubMenu settingsSubmenu = new DefaultSubMenu(settingsMenuHeader);

        item = new DefaultMenuItem(localizationPageValue);
        item.setUrl(localizationPageUrl);
        item.setIcon("fa fa-language fa-fw");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_LOCALIZATION));
        if (url != null && url.equals(APPLICATION_ROOT_URL + localizationPageUrl)) {
            item.setStyleClass("ui-state-active");
        }
        settingsSubmenu.addElement(item);

        item = new DefaultMenuItem(settingsPageValue);
        item.setUrl(settingsPageUrl);
        item.setIcon("ui-icon-gear");
        item.setRendered(securityUtils.checkPermissions(SecurityPolicy.MENU_ITEM_SETTINGS));
        if (url != null && url.equals(APPLICATION_ROOT_URL + settingsPageUrl)) {
            item.setStyleClass("ui-state-active");
        }
        settingsSubmenu.addElement(item);

        settingsSubmenu.setRendered(isRendered(settingsSubmenu));
        menuModel.addElement(settingsSubmenu);

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