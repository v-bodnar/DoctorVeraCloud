package ua.kiev.doctorvera.views;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import ua.kiev.doctorvera.entities.DeliveryGroup;
import ua.kiev.doctorvera.entities.MessageTemplate;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.*;
import ua.kiev.doctorvera.resources.Config;
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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by volodymyr.bodnar on 26.11.2015.
 */
@Named(value= "messageTemplatesView")
@ViewScoped
public class MessageTemplatesView implements Serializable{

    private final static Integer MAXIMUM_CYRILLIC_SYMBOLS_IN_SMS = Integer.parseInt(Config.getInstance().getProperty("MDS_MAXIMUM_CYRILLIC_SYMBOLS_IN_SMS"));
    private final static Integer MAXIMUM_LATIN_SYMBOLS_IN_SMS = Integer.parseInt(Config.getInstance().getProperty("MDS_MAXIMUM_LATIN_SYMBOLS_IN_SMS"));

    @EJB
    private MessageTemplateFacadeLocal messageTemplateFacade;

    @Inject
    private SessionParams sessionParams;

    //Authorized User
    private Users authorizedUser;

    //All Delivery Groups
    private LazyDataModel<MessageTemplate>  allMessageTemplates;

    //New Delivery Group
    private MessageTemplate newMessageTemplate;

    //Selected Delivery Group
    private MessageTemplate selectedMessageTemplate;

    //Represents type of rendered view SMS or EMAIL
    private MessageTemplate.Type messageType;

    //Count of characters in sms content text area
    private Integer letterCounter = 0;
    //Number of sms messages that will be sent, depends on MAXIMUM symbols from Config and letterCounter
    private Integer messageCounter = 0;

    @PostConstruct
    public void init(){
        authorizedUser = sessionParams.getAuthorizedUser();
        messageType = sessionParams.getDeliveryMessageType();
        allMessageTemplates = new TemplatesLazyModel();
        newMessageTemplate = new MessageTemplate();
    }

    public void createNewTemplate(){
        selectedMessageTemplate = null;
        newMessageTemplate = new MessageTemplate();
    }

    //Deletes selected message Template
    public void deleteSelectedTemplate(){
        messageTemplateFacade.remove(selectedMessageTemplate);
        final String successMessage = Message.getInstance().getString("MESSAGE_TEMPLATES_DELETED");
        final String successTitle = Message.getInstance().getString("VALIDATOR_SUCCESS_TITLE");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
    }

    //Updates selected message Template
    public void saveSelectedTemplate() {
        selectedMessageTemplate.setDateCreated(new Date());
        selectedMessageTemplate.setUserCreated(authorizedUser);
        messageTemplateFacade.edit(selectedMessageTemplate);
        final String successMessage = Message.getInstance().getString("MESSAGE_TEMPLATES_UPDATED");
        final String successTitle = Message.getInstance().getString("VALIDATOR_SUCCESS_TITLE");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('editTemplateDialog').hide()");
    }

    //Creates new message Template from selected message Template
    public void saveSelectedTemplateAsNew() {
        selectedMessageTemplate.setDateCreated(new Date());
        selectedMessageTemplate.setUserCreated(authorizedUser);
        selectedMessageTemplate.setId(null);
        messageTemplateFacade.create(selectedMessageTemplate);
        final String successMessage = Message.getInstance().getString("MESSAGE_TEMPLATES_SAVED");
        final String successTitle = Message.getInstance().getString("VALIDATOR_SUCCESS_TITLE");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('editTemplateDialog').hide()");
    }

    //Adds selected message Template
    public void saveNewTemplate(){
        newMessageTemplate.setDateCreated(new Date());
        newMessageTemplate.setUserCreated(authorizedUser);
        newMessageTemplate.setType(messageType);
        messageTemplateFacade.create(newMessageTemplate);
        final String successMessage = Message.getInstance().getString("APPLICATION_SAVED");
        final String successTitle = Message.getInstance().getString("MESSAGE_TEMPLATES_SAVED");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('addTemplateDialog').hide()");
    }

    public void updateLetterCounter(){
        if(newMessageTemplate.getContent() != null && newMessageTemplate.getContent() != null){
            final Pattern cyrillicPattern = Pattern.compile("[А-Яа-яЇї]");
            String content = newMessageTemplate.getContent();
            letterCounter = content.length();
            if (cyrillicPattern.matcher(content).find()){
                messageCounter = content.length()/MAXIMUM_CYRILLIC_SYMBOLS_IN_SMS + 1;
            }else{
                messageCounter = content.length()/MAXIMUM_LATIN_SYMBOLS_IN_SMS + 1;
            }
            letterCounter = newMessageTemplate.getContent().length();
        }
        if(newMessageTemplate.getContent() != null && newMessageTemplate.getContent() != null){
            letterCounter = newMessageTemplate.getContent().length();
        }
    }

    public class TemplatesLazyModel extends LazyDataModel<MessageTemplate> {

        List<MessageTemplate> allPaginatedFilteredTemplates = new ArrayList<>();


        public TemplatesLazyModel() {
        }

        @Override
        public MessageTemplate getRowData(String rowKey) {
            if (allPaginatedFilteredTemplates == null) return null;
            for(MessageTemplate template : allPaginatedFilteredTemplates) {
                if(template.getId().equals(Integer.parseInt(rowKey)))
                    return template;
            }
            return null;
        }

        @Override
        public Object getRowKey(MessageTemplate template) {
            return template.getId();
        }

        @Override
        public List<MessageTemplate> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
            filters.put("type", messageType);
            allPaginatedFilteredTemplates = messageTemplateFacade.initializeLazyEntity(messageTemplateFacade.findAll(first, pageSize, sortField, sortOrder, filters));
            setRowCount(messageTemplateFacade.count(first, pageSize, filters));
            return allPaginatedFilteredTemplates;
        }
    }

    /*----------------------------------------------------*/
    /*---------------Getters & Setters--------------------*/
    /*----------------------------------------------------*/

    public LazyDataModel<MessageTemplate> getAllMessageTemplates() {
        return allMessageTemplates;
    }

    public void setAllMessageTemplates(LazyDataModel<MessageTemplate> allMessageTemplates) {
        this.allMessageTemplates = allMessageTemplates;
    }

    public MessageTemplate getNewMessageTemplate() {
        return newMessageTemplate;
    }

    public void setNewMessageTemplate(MessageTemplate newMessageTemplate) {
        this.newMessageTemplate = newMessageTemplate;
    }

    public MessageTemplate getSelectedMessageTemplate() {
        return selectedMessageTemplate;
    }

    public void setSelectedMessageTemplate(MessageTemplate selectedMessageTemplate) {
        this.selectedMessageTemplate = selectedMessageTemplate;
    }

    public MessageTemplate.Type getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageTemplate.Type messageType) {
        this.messageType = messageType;
    }

    public Integer getLetterCounter() {
        return letterCounter;
    }

    public void setLetterCounter(Integer letterCounter) {
        this.letterCounter = letterCounter;
    }

    public Integer getMessageCounter() {
        return messageCounter;
    }

    public void setMessageCounter(Integer messageCounter) {
        this.messageCounter = messageCounter;
    }
}
