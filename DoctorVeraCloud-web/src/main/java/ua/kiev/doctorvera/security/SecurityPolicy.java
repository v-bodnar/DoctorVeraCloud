package ua.kiev.doctorvera.security;

import ua.kiev.doctorvera.resources.Message;
import java.io.Serializable;

/**
 * Created by volodymyr.bodnar on 05.10.2015.
 */
public enum SecurityPolicy implements Serializable{
    EMPTY(Message.getInstance().getString("SECURITY_EMPTY"), "EMPTY", Message.getInstance().getString("SECURITY_SYSTEM")),
    /**
     * Menu policy block
     */
    MENU_ITEM_PLAN(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_PLAN"), "MENU_ITEM_PLAN", Message.getInstance().getString("SECURITY_MENU_GROUP")),
    MENU_ITEM_MAIN(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_MAIN"), "MENU_ITEM_MAIN", Message.getInstance().getString("SECURITY_MENU_GROUP")),
    MENU_ITEM_USERS(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_USERS"), "MENU_ITEM_PLAN", Message.getInstance().getString("SECURITY_MENU_GROUP")),
    MENU_ITEM_ADD_USER(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_ADD_USER"), "MENU_ITEM_ADD_USER", Message.getInstance().getString("SECURITY_MENU_GROUP")),
    MENU_ITEM_USER_TYPES(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_USER_TYPES"), "MENU_ITEM_USER_TYPES", Message.getInstance().getString("SECURITY_MENU_GROUP")),
    MENU_ITEM_ROOMS(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_ROOMS"), "MENU_ITEM_ROOMS", Message.getInstance().getString("SECURITY_MENU_GROUP")),
    MENU_ITEM_SEND_SMS(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_SEND_SMS"), "MENU_ITEM_SEND_SMS", Message.getInstance().getString("SECURITY_MENU_GROUP")),
    MENU_ITEM_METHODS(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_METHODS"), "MENU_ITEM_METHODS", Message.getInstance().getString("SECURITY_MENU_GROUP")),
    MENU_ITEM_PAYMENTS(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_PAYMENTS"), "MENU_ITEM_PAYMENTS", Message.getInstance().getString("SECURITY_MENU_GROUP")),
    MENU_ITEM_PLAN_GENERAL(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_PLAN_GENERAL"), "MENU_ITEM_PLAN_GENERAL", Message.getInstance().getString("SECURITY_MENU_GROUP")),
    MENU_ITEM_SCHEDULE_GENERAL(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_SCHEDULE_GENERAL"), "MENU_ITEM_SCHEDULE_GENERAL", Message.getInstance().getString("SECURITY_MENU_GROUP")),
    MENU_ITEM_PERSONAL_SCHEDULE(Message.getInstance().getString("SECURITY_POLICY_MENU_ITEM_PERSONAL_SCHEDULE"), "MENU_ITEM_PERSONAL_SCHEDULE", Message.getInstance().getString("SECURITY_MENU_GROUP"));

    private String name;
    private String stringId;
    private String policyGroup;

    SecurityPolicy(String name, String stringId, String policyGroup) {
        this.name = name;
        this.stringId = stringId;
        this.policyGroup = policyGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPolicyGroup() {
        return policyGroup;
    }

    public void setPolicyGroup(String policyGroup) {
        this.policyGroup = policyGroup;
    }

    public String getStringId() {
        return stringId;
    }

    public void setStringId(String stringId) {
        this.stringId = stringId;
    }
}
