package ua.kiev.doctorvera.security;

import java.io.Serializable;

/**
 * Created by volodymyr.bodnar on 05.10.2015.
 */
public enum SecurityPolicy implements Serializable{
    EMPTY(SecurityPolicyGroup.SYSTEM),
    /**
     * Menu policy block
     */
    MENU_ITEM_MAIN(SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_USERS(SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_USER_GROUPS(SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_ROOMS(SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_SEND_SMS(SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_METHODS(SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_PAYMENTS(SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_PLAN_GENERAL(SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_PLAN(SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_SCHEDULE_GENERAL(SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_SCHEDULE(SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_PERSONAL_SCHEDULE(SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_DELIVERY_GROUPS(SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_SMS_TEMPLATES(SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_EMAIL_TEMPLATES(SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_MESSAGE_SCHEDULER(SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_LOCALIZATION(SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_SETTINGS(SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_FINANCIAL_SETTINGS(SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_SALARY(SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_STATISTICS(SecurityPolicyGroup.MENU_GROUP),
    /**
     * Users page
     */
    USERS_ADD_USER(SecurityPolicyGroup.USERS_GROUP),
    USERS_DELETE_USER(SecurityPolicyGroup.USERS_GROUP),
    USERS_USERS_PROFILE(SecurityPolicyGroup.USERS_GROUP),
    USERS_ADD_USER_TO_USER_GROUP(SecurityPolicyGroup.USERS_GROUP),
    USERS_SEND_SMS(SecurityPolicyGroup.USERS_GROUP),
    /**
     * User Groups page
     */
    USER_GROUPS_ADD_USER_GROUP(SecurityPolicyGroup.USER_GROUPS_GROUP),
    USER_GROUPS_DELETE_USER_GROUP(SecurityPolicyGroup.USER_GROUPS_GROUP),
    USER_GROUPS_EDIT_USER_GROUP(SecurityPolicyGroup.USER_GROUPS_GROUP),
    USER_GROUPS_ADD_USERS(SecurityPolicyGroup.USER_GROUPS_GROUP),
    USER_GROUPS_ADD_SECURITY_POLICIES(SecurityPolicyGroup.USER_GROUPS_GROUP),
    /**
     * Main page
     */
    MAIN_PAGE_MONTH_SALARY(SecurityPolicyGroup.MAIN_PAGE_GROUP),
    MAIN_PAGE_MONTH_APPOINTMENTS(SecurityPolicyGroup.MAIN_PAGE_GROUP),
    MAIN_PAGE_YEAR_SALARY(SecurityPolicyGroup.MAIN_PAGE_GROUP),
    MAIN_PAGE_YEAR_APPOINTMENTS(SecurityPolicyGroup.MAIN_PAGE_GROUP),
    /**
     * Rooms page
     */
    ROOMS_EDIT(SecurityPolicyGroup.ROOMS_GROUP),
    ROOMS_ADD(SecurityPolicyGroup.ROOMS_GROUP),
    ROOMS_DELETE(SecurityPolicyGroup.ROOMS_GROUP),
    /**
     * Methods page
     */
    METHODS_ADD(SecurityPolicyGroup.METHODS_GROUP),
    METHODS_EDIT(SecurityPolicyGroup.METHODS_GROUP),
    METHODS_DELETE(SecurityPolicyGroup.METHODS_GROUP),
    METHODS_CHANGE_PRICE(SecurityPolicyGroup.METHODS_GROUP),
    METHODS_ADD_DOCTORS(SecurityPolicyGroup.METHODS_GROUP),
    /**
     * Payments page
     */
    PAYMENTS_ADD(SecurityPolicyGroup.PAYMENTS_GROUP),
    /**
     * Profile page
     */
    PROFILE_EDIT_ANY_USER(SecurityPolicyGroup.PROFILE_GROUP),
    PROFILE_EDIT_AVATAR(SecurityPolicyGroup.PROFILE_GROUP),
    PROFILE_EDIT_FIRST_NAME(SecurityPolicyGroup.PROFILE_GROUP),
    PROFILE_EDIT_MIDDLE_NAME(SecurityPolicyGroup.PROFILE_GROUP),
    PROFILE_EDIT_LAST_NAME(SecurityPolicyGroup.PROFILE_GROUP),
    PROFILE_EDIT_BIRTH_DATE(SecurityPolicyGroup.PROFILE_GROUP),
    PROFILE_EDIT_LOGIN(SecurityPolicyGroup.PROFILE_GROUP),
    PROFILE_EDIT_PASSWORD(SecurityPolicyGroup.PROFILE_GROUP),
    PROFILE_EDIT_PHONE_NUMBER(SecurityPolicyGroup.PROFILE_GROUP),
    PROFILE_EDIT_ADDRESS(SecurityPolicyGroup.PROFILE_GROUP),
    PROFILE_EDIT_COLOR(SecurityPolicyGroup.PROFILE_GROUP),
    PROFILE_EDIT_EMAIL(SecurityPolicyGroup.PROFILE_GROUP),
    PROFILE_EDIT_MESSAGING_ACCEPTED(SecurityPolicyGroup.PROFILE_GROUP),
    /**
     * Plan pages
     */
    PLAN_CREATE(SecurityPolicyGroup.PLAN_GROUP),
    PLAN_READ(SecurityPolicyGroup.PLAN_GROUP),
    PLAN_UPDATE(SecurityPolicyGroup.PLAN_GROUP),
    PLAN_DELETE(SecurityPolicyGroup.PLAN_GROUP),
    /**
     * Schedule pages
     */
    SCHEDULE_CREATE(SecurityPolicyGroup.SCHEDULE_GROUP),
    SCHEDULE_READ(SecurityPolicyGroup.SCHEDULE_GROUP),
    SCHEDULE_UPDATE(SecurityPolicyGroup.SCHEDULE_GROUP),
    SCHEDULE_DELETE(SecurityPolicyGroup.SCHEDULE_GROUP),
    /**
     * User Groups page
     */
    DELIVERY_GROUPS_ADD_USER_GROUP(SecurityPolicyGroup.DELIVERY_GROUPS_GROUP),
    DELIVERY_GROUPS_DELETE_USER_GROUP(SecurityPolicyGroup.DELIVERY_GROUPS_GROUP),
    DELIVERY_GROUPS_EDIT_USER_GROUP( SecurityPolicyGroup.DELIVERY_GROUPS_GROUP),
    DELIVERY_GROUPS_ADD_USERS(SecurityPolicyGroup.DELIVERY_GROUPS_GROUP),
    DELIVERY_GROUPS_ADD_SECURITY_POLICIES(SecurityPolicyGroup.DELIVERY_GROUPS_GROUP),
    /**
     * SMS Templates page
     */
    SMS_TEMPLATES_ADD(SecurityPolicyGroup.SMS_TEMPLATES_GROUP),
    SMS_TEMPLATES_DELETE(SecurityPolicyGroup.SMS_TEMPLATES_GROUP),
    SMS_TEMPLATES_UPDATE(SecurityPolicyGroup.SMS_TEMPLATES_GROUP),
    SMS_TEMPLATES_SEND(SecurityPolicyGroup.SMS_TEMPLATES_GROUP),
    /**
     * EMAIL Templates page
     */
    EMAIL_TEMPLATES_ADD(SecurityPolicyGroup.EMAIL_TEMPLATES_GROUP),
    EMAIL_TEMPLATES_DELETE(SecurityPolicyGroup.EMAIL_TEMPLATES_GROUP),
    EMAIL_TEMPLATES_UPDATE(SecurityPolicyGroup.EMAIL_TEMPLATES_GROUP),
    EMAIL_TEMPLATES_SEND(SecurityPolicyGroup.EMAIL_TEMPLATES_GROUP),
    /**
     * Delivery Scheduler page
     */
    MESSAGE_SCHEDULER_ADD(SecurityPolicyGroup.MESSAGE_SCHEDULER_GROUP),
    MESSAGE_SCHEDULER_DELETE(SecurityPolicyGroup.MESSAGE_SCHEDULER_GROUP),
    MESSAGE_SCHEDULER_UPDATE(SecurityPolicyGroup.MESSAGE_SCHEDULER_GROUP),
    /**
     * Localization page
     */
    LOCALIZATION_ADD_LOCALE(SecurityPolicyGroup.LOCALIZATION_GROUP),
    LOCALIZATION_ADD_CONSTANT(SecurityPolicyGroup.LOCALIZATION_GROUP),
    LOCALIZATION_EDIT_CONSTANT(SecurityPolicyGroup.LOCALIZATION_GROUP),
    /**
     * Settings page
     */
    SETTINGS_MDS_TAB(SecurityPolicyGroup.SETTINGS_GROUP),
    SETTINGS_MDS_SAVE(SecurityPolicyGroup.SETTINGS_GROUP),
    SETTINGS_APPLICATION_TAB(SecurityPolicyGroup.SETTINGS_GROUP),
    SETTINGS_APPLICATION_SAVE(SecurityPolicyGroup.SETTINGS_GROUP),
    SETTINGS_PATHS_TAB(SecurityPolicyGroup.SETTINGS_GROUP),
    SETTINGS_PATHS_SAVE(SecurityPolicyGroup.SETTINGS_GROUP),
    SETTINGS_TEMPLATES_TAB(SecurityPolicyGroup.SETTINGS_GROUP),
    SETTINGS_TEMPLATES_SAVE(SecurityPolicyGroup.SETTINGS_GROUP),
    /**
     * Financial settings page
     */
    FINANCIAL_SETTINGS_CREATE(SecurityPolicyGroup.FINANCIAL_SETTINGS_GROUP),
    FINANCIAL_SETTINGS_READ(SecurityPolicyGroup.FINANCIAL_SETTINGS_GROUP),
    FINANCIAL_SETTINGS_UPDATE(SecurityPolicyGroup.FINANCIAL_SETTINGS_GROUP),
    FINANCIAL_SETTINGS_DELETE(SecurityPolicyGroup.FINANCIAL_SETTINGS_GROUP),
    /**
     * Salary page
     */
    SALARY_UPDATE(SecurityPolicyGroup.SALARY_GROUP),
    SALARY_DELETE(SecurityPolicyGroup.SALARY_GROUP),
    /**
     * Statistics page
     */
    STATISTICS_FINANCIAL(SecurityPolicyGroup.STATISTICS_GROUP),
    STATISTICS_APPOINTMENTS(SecurityPolicyGroup.STATISTICS_GROUP),
    STATISTICS_METHODS(SecurityPolicyGroup.STATISTICS_GROUP),
    ;

    private SecurityPolicyGroup policyGroup;

    SecurityPolicy(SecurityPolicyGroup policyGroup) {
        this.policyGroup = policyGroup;
    }

    public SecurityPolicyGroup getPolicyGroup() {
        return policyGroup;
    }

    public void setPolicyGroup(SecurityPolicyGroup policyGroup) {
        this.policyGroup = policyGroup;
    }

    public String getName(){return name();}

    public enum SecurityPolicyGroup implements Serializable{
        SYSTEM("SECURITY_SYSTEM_DESCRIPTION"),
        MENU_GROUP("SECURITY_MENU_GROUP_DESCRIPTION"),
        USERS_GROUP("SECURITY_USERS_GROUP_DESCRIPTION"),
        USER_GROUPS_GROUP("SECURITY_USER_GROUPS_GROUP_DESCRIPTION"),
        MAIN_PAGE_GROUP("SECURITY_MAIN_PAGE_GROUP_DESCRIPTION"),
        ROOMS_GROUP("SECURITY_ROOMS_GROUP_DESCRIPTION"),
        PAYMENTS_GROUP("SECURITY_PAYMENTS_GROUP_DESCRIPTION"),
        METHODS_GROUP("SECURITY_METHODS_GROUP_DESCRIPTION"),
        PROFILE_GROUP("SECURITY_PROFILE_GROUP_DESCRIPTION"),
        PLAN_GROUP("SECURITY_PLAN_GROUP_DESCRIPTION"),
        SCHEDULE_GROUP("SECURITY_SCHEDULE_GROUP_DESCRIPTION"),
        DELIVERY_GROUPS_GROUP("SECURITY_DELIVERY_GROUPS_GROUP_DESCRIPTION"),
        SMS_TEMPLATES_GROUP("SECURITY_SMS_TEMPLATES_GROUP_DESCRIPTION"),
        EMAIL_TEMPLATES_GROUP("SECURITY_EMAIL_TEMPLATES_GROUP_DESCRIPTION"),
        MESSAGE_SCHEDULER_GROUP("SECURITY_MESSAGE_SCHEDULER_GROUP_DESCRIPTION"),
        LOCALIZATION_GROUP("SECURITY_LOCALIZATION_GROUP_DESCRIPTION"),
        SETTINGS_GROUP("SECURITY_SETTINGS_GROUP_DESCRIPTION"),
        FINANCIAL_SETTINGS_GROUP("SECURITY_FINANCIAL_SETTINGS_GROUP_DESCRIPTION"),
        SALARY_GROUP("SECURITY_SALARY_GROUP_DESCRIPTION"),
        STATISTICS_GROUP("SECURITY_STATISTICS_GROUP_DESCRIPTION");
        SecurityPolicyGroup(String description){
            this.description = description;
        }
        private String description;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

}
