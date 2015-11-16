package ua.kiev.doctorvera.security;

import ua.kiev.doctorvera.resources.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by volodymyr.bodnar on 05.10.2015.
 */
public enum SecurityPolicy implements Serializable{
    EMPTY(Message.getInstance().getString("SECURITY_EMPTY"), SecurityPolicyGroup.SYSTEM),
    /**
     * Menu policy block
     */
    MENU_ITEM_MAIN(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_MAIN"), SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_USERS(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_USERS"), SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_USER_GROUPS(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_USER_GROUPS"), SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_ROOMS(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_ROOMS"), SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_SEND_SMS(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_SEND_SMS"), SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_METHODS(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_METHODS"), SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_PAYMENTS(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_PAYMENTS"),SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_PLAN_GENERAL(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_PLAN_GENERAL"), SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_PLAN(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_PLAN"), SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_SCHEDULE_GENERAL(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_SCHEDULE_GENERAL"), SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_SCHEDULE(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_SCHEDULE"), SecurityPolicyGroup.MENU_GROUP),
    MENU_ITEM_PERSONAL_SCHEDULE(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_PERSONAL_SCHEDULE"), SecurityPolicyGroup.MENU_GROUP),
    /**
     * Users page
     */
    USERS_ADD_USER(Message.getInstance().getString("SECURITY_POLICY_USERS_ADD_USER"), SecurityPolicyGroup.USERS_GROUP),
    USERS_DELETE_USER(Message.getInstance().getString("SECURITY_POLICY_USERS_DELETE_USER"), SecurityPolicyGroup.USERS_GROUP),
    USERS_USERS_PROFILE(Message.getInstance().getString("SECURITY_POLICY_USERS_USERS_PROFILE"), SecurityPolicyGroup.USERS_GROUP),
    USERS_ADD_USER_TO_USER_GROUP(Message.getInstance().getString("SECURITY_POLICY_USERS_ADD_USER_TO_USER_GROUP"), SecurityPolicyGroup.USERS_GROUP),
    USERS_SEND_SMS(Message.getInstance().getString("SECURITY_POLICY_USERS_SEND_SMS"), SecurityPolicyGroup.USERS_GROUP),
    /**
     * User Groups page
     */
    USER_GROUPS_ADD_USER_GROUP(Message.getInstance().getString("SECURITY_POLICY_USER_GROUPS_ADD_USER_GROUP"), SecurityPolicyGroup.USER_GROUPS_GROUP),
    USER_GROUPS_DELETE_USER_GROUP(Message.getInstance().getString("SECURITY_POLICY_USER_GROUPS_DELETE_USER_GROUP"), SecurityPolicyGroup.USER_GROUPS_GROUP),
    USER_GROUPS_EDIT_USER_GROUP(Message.getInstance().getString("SECURITY_POLICY_USER_GROUPS_EDIT_USER_GROUP"), SecurityPolicyGroup.USER_GROUPS_GROUP),
    USER_GROUPS_ADD_USERS(Message.getInstance().getString("SECURITY_POLICY_USER_GROUPS_ADD_USERS"), SecurityPolicyGroup.USER_GROUPS_GROUP),
    USER_GROUPS_ADD_SECURITY_POLICIES(Message.getInstance().getString("SECURITY_POLICY_USER_GROUPS_ADD_SECURITY_POLICIES"), SecurityPolicyGroup.USER_GROUPS_GROUP),
    /**
     * Main page
     */
    MAIN_PAGE_MONTH_SALARY(Message.getInstance().getString("SECURITY_POLICY_MAIN_PAGE_MONTH_SALARY"), SecurityPolicyGroup.MAIN_PAGE_GROUP),
    MAIN_PAGE_MONTH_APPOINTMENTS(Message.getInstance().getString("SECURITY_POLICY_MAIN_PAGE_MONTH_APPOINTMENTS"), SecurityPolicyGroup.MAIN_PAGE_GROUP),
    MAIN_PAGE_YEAR_SALARY(Message.getInstance().getString("SECURITY_POLICY_MAIN_PAGE_YEAR_SALARY"), SecurityPolicyGroup.MAIN_PAGE_GROUP),
    MAIN_PAGE_YEAR_APPOINTMENTS(Message.getInstance().getString("SECURITY_POLICY_MAIN_PAGE_YEAR_APPOINTMENTS"), SecurityPolicyGroup.MAIN_PAGE_GROUP),
    /**
     * Rooms page
     */
    ROOMS_EDIT(Message.getInstance().getString("SECURITY_POLICY_ROOMS_EDIT"), SecurityPolicyGroup.ROOMS_GROUP),
    ROOMS_ADD(Message.getInstance().getString("SECURITY_POLICY_ROOMS_ADD"), SecurityPolicyGroup.ROOMS_GROUP),
    ROOMS_DELETE(Message.getInstance().getString("SECURITY_POLICY_ROOMS_DELETE"), SecurityPolicyGroup.ROOMS_GROUP),
    /**
     * Methods page
     */
    METHODS_ADD(Message.getInstance().getString("SECURITY_POLICY_METHODS_ADD"), SecurityPolicyGroup.METHODS_GROUP),
    METHODS_EDIT(Message.getInstance().getString("SECURITY_POLICY_METHODS_EDIT"), SecurityPolicyGroup.METHODS_GROUP),
    METHODS_DELETE(Message.getInstance().getString("SECURITY_POLICY_METHODS_DELETE"), SecurityPolicyGroup.METHODS_GROUP),
    METHODS_CHANGE_PRICE(Message.getInstance().getString("SECURITY_POLICY_METHODS_CHANGE_PRICE"), SecurityPolicyGroup.METHODS_GROUP),
    METHODS_ADD_DOCTORS(Message.getInstance().getString("SECURITY_POLICY_METHODS_ADD_DOCTORS"), SecurityPolicyGroup.METHODS_GROUP),
    /**
     * Payments page
     */
    PAYMENTS_ADD(Message.getInstance().getString("SECURITY_POLICY_PAYMENTS_ADD"), SecurityPolicyGroup.PAYMENTS_GROUP),
    /**
     * Profile page
     */
    PROFILE_EDIT_ANY_USER(Message.getInstance().getString("SECURITY_POLICY_PROFILE_EDIT_ANY_USER"), SecurityPolicyGroup.PROFILE_GROUP),
    PROFILE_EDIT_AVATAR(Message.getInstance().getString("SECURITY_POLICY_PROFILE_EDIT_AVATAR"), SecurityPolicyGroup.PROFILE_GROUP),
    PROFILE_EDIT_FIRST_NAME(Message.getInstance().getString("SECURITY_POLICY_PROFILE_EDIT_FIRST_NAME"), SecurityPolicyGroup.PROFILE_GROUP),
    PROFILE_EDIT_MIDDLE_NAME(Message.getInstance().getString("SECURITY_POLICY_PROFILE_EDIT_MIDDLE_NAME"), SecurityPolicyGroup.PROFILE_GROUP),
    PROFILE_EDIT_LAST_NAME(Message.getInstance().getString("SECURITY_POLICY_PROFILE_EDIT_LAST_NAME"), SecurityPolicyGroup.PROFILE_GROUP),
    PROFILE_EDIT_BIRTH_DATE(Message.getInstance().getString("SECURITY_POLICY_PROFILE_EDIT_BIRTH_DATE"), SecurityPolicyGroup.PROFILE_GROUP),
    PROFILE_EDIT_LOGIN(Message.getInstance().getString("SECURITY_POLICY_PROFILE_EDIT_LOGIN"), SecurityPolicyGroup.PROFILE_GROUP),
    PROFILE_EDIT_PASSWORD(Message.getInstance().getString("SECURITY_POLICY_PROFILE_EDIT_PASSWORD"), SecurityPolicyGroup.PROFILE_GROUP),
    PROFILE_EDIT_PHONE_NUMBER(Message.getInstance().getString("SECURITY_POLICY_PROFILE_EDIT_PHONE_NUMBER"), SecurityPolicyGroup.PROFILE_GROUP),
    PROFILE_EDIT_ADDRESS(Message.getInstance().getString("SECURITY_POLICY_PROFILE_EDIT_ADDRESS"), SecurityPolicyGroup.PROFILE_GROUP),
    PROFILE_EDIT_COLOR(Message.getInstance().getString("SECURITY_POLICY_PROFILE_EDIT_COLOR"), SecurityPolicyGroup.PROFILE_GROUP),
    /**
     * Plan pages
     */
    PLAN_CREATE(Message.getInstance().getString("SECURITY_POLICY_PLAN_CREATE"), SecurityPolicyGroup.PLAN_GROUP),
    PLAN_READ(Message.getInstance().getString("SECURITY_POLICY_PLAN_READ"), SecurityPolicyGroup.PLAN_GROUP),
    PLAN_UPDATE(Message.getInstance().getString("SECURITY_POLICY_PLAN_UPDATE"), SecurityPolicyGroup.PLAN_GROUP),
    PLAN_DELETE(Message.getInstance().getString("SECURITY_POLICY_PLAN_DELETE"), SecurityPolicyGroup.PLAN_GROUP),
    /**
     * Schedule pages
     */
    SCHEDULE_CREATE(Message.getInstance().getString("SECURITY_POLICY_SCHEDULE_CREATE"), SecurityPolicyGroup.SCHEDULE_GROUP),
    SCHEDULE_READ(Message.getInstance().getString("SECURITY_POLICY_SCHEDULE_READ"), SecurityPolicyGroup.SCHEDULE_GROUP),
    SCHEDULE_UPDATE(Message.getInstance().getString("SECURITY_POLICY_SCHEDULE_UPDATE"), SecurityPolicyGroup.SCHEDULE_GROUP),
    SCHEDULE_DELETE(Message.getInstance().getString("SECURITY_POLICY_SCHEDULE_DELETE"), SecurityPolicyGroup.SCHEDULE_GROUP);

    private String name;
    private SecurityPolicyGroup policyGroup;

    SecurityPolicy(String name, SecurityPolicyGroup policyGroup) {
        this.name = name;
        this.policyGroup = policyGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SecurityPolicyGroup getPolicyGroup() {
        return policyGroup;
    }

    public void setPolicyGroup(SecurityPolicyGroup policyGroup) {
        this.policyGroup = policyGroup;
    }

    public enum SecurityPolicyGroup implements Serializable{
        SYSTEM(Message.getInstance().getString("SECURITY_SYSTEM_DESCRIPTION")),
        MENU_GROUP(Message.getInstance().getString("SECURITY_MENU_GROUP_DESCRIPTION")),
        USERS_GROUP(Message.getInstance().getString("SECURITY_USERS_GROUP_DESCRIPTION")),
        USER_GROUPS_GROUP(Message.getInstance().getString("SECURITY_USER_GROUPS_GROUP_DESCRIPTION")),
        MAIN_PAGE_GROUP(Message.getInstance().getString("SECURITY_MAIN_PAGE_GROUP_DESCRIPTION")),
        ROOMS_GROUP(Message.getInstance().getString("SECURITY_ROOMS_GROUP_DESCRIPTION")),
        PAYMENTS_GROUP(Message.getInstance().getString("SECURITY_PAYMENTS_GROUP_DESCRIPTION")),
        METHODS_GROUP(Message.getInstance().getString("SECURITY_METHODS_GROUP_DESCRIPTION")),
        PROFILE_GROUP(Message.getInstance().getString("SECURITY_PROFILE_GROUP_DESCRIPTION")),
        PLAN_GROUP(Message.getInstance().getString("SECURITY_PLAN_GROUP_DESCRIPTION")),
        SCHEDULE_GROUP(Message.getInstance().getString("SECURITY_SCHEDULE_GROUP_DESCRIPTION"));
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
