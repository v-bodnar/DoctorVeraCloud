package ua.kiev.doctorvera.views;

import ua.kiev.doctorvera.entities.*;
import ua.kiev.doctorvera.facadeLocal.*;
import ua.kiev.doctorvera.resources.Message;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by volodymyr.bodnar on 2/15/2016.
 */
@Named
@ViewScoped
public class SettingsView implements Serializable {
    private static final Logger LOG = Logger.getLogger(LocalizationView.class.getName());

    @EJB
    LocaleFacadeLocal localeFacade;

    @EJB
    MessageBundleFacadeLocal messageBundleFacade;

    @EJB
    MessageTemplateFacadeLocal messageTemplateFacade;

    @EJB
    UsersFacadeLocal usersFacade;

    @EJB
    UserGroupsFacadeLocal userGroupsFacade;

    @EJB
    MethodsFacadeLocal methodsFacade;

    @Inject
    private SessionParams sessionParams;

    private Users authorizedUser;

    private Map<String, MessageBundle> allConstants = new TreeMap<>();
    private List<MessageBundle> allPaths;

    private List<MessageTemplate> smsTemplates;
    private List<MessageTemplate> emailTemplates;
    private List<UserGroups> userGroups;
    private List<Users> users;
    private List<Methods> methods;

    @PostConstruct
    public void init(){
        authorizedUser = sessionParams.getAuthorizedUser();
        for(MessageBundle constant : messageBundleFacade.findAllMessagesByType(MessageBundle.Type.CONFIG)){
            allConstants.put(constant.getMessageKey(), constant);
        }
//        for(MessageBundle path : messageBundleFacade.findAllMessagesByType(MessageBundle.Type.MAPPING)){
//            allPaths.put(path.getMessageKey(), path);
//        }
        allPaths = messageBundleFacade.findAllMessagesByType(MessageBundle.Type.MAPPING);
        smsTemplates = messageTemplateFacade.findByType(MessageTemplate.Type.SMS, true);
        emailTemplates = messageTemplateFacade.findByType(MessageTemplate.Type.EMAIL, true);
        userGroups = userGroupsFacade.findAll();
        users = usersFacade.findAll();
        methods = methodsFacade.findAll();
    }

    public void saveSettings(String key){
        if(key == null || key.isEmpty()) return;
        MessageBundle constant = allConstants.get(key);
        constant.setDateCreated(new Date());
        constant.setUserCreated(authorizedUser);
        messageBundleFacade.edit(constant);
        Message.showMessage(Message.getMessage("APPLICATION_SUCCESSFUL_TITLE"), Message.getMessage("APPLICATION_SAVED"));
        LOG.info("Changes to settings constants saved");
    }

    public void savePaths(MessageBundle path){
        path.setDateCreated(new Date());
        path.setUserCreated(authorizedUser);
        messageBundleFacade.edit(path);
        Message.showMessage(Message.getMessage("APPLICATION_SUCCESSFUL_TITLE"), Message.getMessage("APPLICATION_SAVED"));
        LOG.info("Changes to settings paths saved");
    }

    public Map<String, MessageBundle> getAllConstants() {
        return allConstants;
    }

    public void setAllConstants(Map<String, MessageBundle> allConstants) {
        this.allConstants = allConstants;
    }

    public List<MessageTemplate> getSmsTemplates() {
        return smsTemplates;
    }

    public void setSmsTemplates(List<MessageTemplate> smsTemplates) {
        this.smsTemplates = smsTemplates;
    }

    public List<MessageTemplate> getEmailTemplates() {
        return emailTemplates;
    }

    public void setEmailTemplates(List<MessageTemplate> emailTemplates) {
        this.emailTemplates = emailTemplates;
    }

    public List<UserGroups> getUserGroups() {
        return userGroups;
    }

    public List<Users> getUsers() {
        return users;
    }

    public List<Methods> getMethods() {
        return methods;
    }

    public List<MessageBundle> getAllPaths() {
        return allPaths;
    }

    public void setAllPaths(List<MessageBundle> allPaths) {
        this.allPaths = allPaths;
    }

    public String findUser(String id){
        if(id == null || id.isEmpty()){
            return "";
        }else {
            Users user = usersFacade.find(Integer.parseInt(id));
            return user.getFirstName() + " " + user.getLastName();
        }
    }

    public String findUserGroup(String id){
        if(id == null || id.isEmpty()){
            return "";
        }else {
            return userGroupsFacade.find(Integer.parseInt(id)).getName();
        }
    }

    public String findTemplate(String id){
        if(id == null || id.isEmpty()){
            return "";
        }else {
            return messageTemplateFacade.find(Integer.parseInt(id)).getName();
        }
    }

    public String findMethod(String id){
        if(id == null || id.isEmpty()){
            return "";
        }else {
            return methodsFacade.find(Integer.parseInt(id)).getShortName();
        }
    }
}
