package ua.kiev.doctorvera.views;

import org.primefaces.context.RequestContext;
import ua.kiev.doctorvera.entities.*;
import ua.kiev.doctorvera.facadeLocal.LocaleFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.MessageBundleFacadeLocal;
import ua.kiev.doctorvera.resources.Message;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Created by volodymyr.bodnar on 1/19/2016.
 */
@Named
@ViewScoped
public class LocalizationView implements Serializable{

    private static final Logger LOG = Logger.getLogger(LocalizationView.class.getName());

    public static final String NEW_MESSAGE = "@default: ";
    private List<Locale> availableLocales;

    private Map<String, MessageBundle> defaultLanguageMessages = new HashMap<>();

    private List<MessageBundle> allMessages = new ArrayList<>();

    @EJB
    LocaleFacadeLocal localeFacade;

    @EJB
    MessageBundleFacadeLocal messageBundleFacade;

    @Inject
    private SessionParams sessionParams;

    private Users authorizedUser;

    @PostConstruct
    public void init(){
        authorizedUser = sessionParams.getAuthorizedUser();
        availableLocales = localeFacade.findAll(null);
        allMessages = messageBundleFacade.findAllMessagesByLocale(localeFacade.findLocale(SessionParams.DEFAULT_LOCALE));
        for(MessageBundle message : allMessages){
            defaultLanguageMessages.put(message.getMessageKey(), message);
        }
    }

    public void onTabChange(Locale locale){
        ua.kiev.doctorvera.entities.Locale localeEntity = localeFacade.findLocale(locale);
        allMessages = messageBundleFacade.findAllMessagesByLocale(localeEntity);
        Set<String> currentLocaleKeys = new HashSet<>();
        for(MessageBundle message : allMessages){
            currentLocaleKeys.add(message.getMessageKey());
        }
        for(String key : defaultLanguageMessages.keySet()){
            if (!currentLocaleKeys.contains(key)){
                MessageBundle newMessage = new MessageBundle();
                newMessage.setMessageKey(defaultLanguageMessages.get(key).getMessageKey());
                newMessage.setValue(NEW_MESSAGE + defaultLanguageMessages.get(key).getValue());
                newMessage.setType(MessageBundle.Type.MESSAGE);
                newMessage.setLocale(localeEntity);
                newMessage.setUserCreated(authorizedUser);
                allMessages.add(newMessage);
            }
        }
        Collections.sort(allMessages);
    }

    public void save(MessageBundle message){
        if(message.getValue() == null || message.getValue().isEmpty() || message.getValue().startsWith(NEW_MESSAGE))
            throw new ValidatorException(new FacesMessage(Message.getMessage("LOCALIZATION_MESSAGE_SAVE_ERROR")));

        message.setDateCreated(new Date());

        if(message.getId() == null){
            messageBundleFacade.create(message);
        }else{
            messageBundleFacade.edit(message);
        }
        Message.showMessage(Message.getMessage("APPLICATION_SUCCESSFUL_TITLE"), Message.getMessage("LOCALIZATION_MESSAGE_SAVE_SUCCESS"));
        LOG.info("Changes to constant saved");
    }

    public List<Locale> getAvailableLocales() {
        return availableLocales;
    }

    public void setAvailableLocales(List<Locale> availableLocales) {
        this.availableLocales = availableLocales;
    }

    public List<MessageBundle> getAllMessages() {
        return allMessages;
    }

    public void setAllMessages(List<MessageBundle> allMessages) {
        this.allMessages = allMessages;
    }

    //***************************************************************************//
                        //Add Locale Dialog//
    //***************************************************************************//
    private ua.kiev.doctorvera.entities.Locale newLocale = new ua.kiev.doctorvera.entities.Locale();

    public void initNewLocale(){
        newLocale = new ua.kiev.doctorvera.entities.Locale();
    }
    public void createNewLocale() throws IOException {
        newLocale.setUserCreated(authorizedUser);
        newLocale.setDateCreated(new Date());
        localeFacade.create(newLocale);
        Message.showMessage(Message.getMessage("APPLICATION_SUCCESSFUL_TITLE"), Message.getMessage("LOCALIZATION_NEW_LOCALE_CREATE_SUCCESS"));
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('addLocaleDialog').hide()");

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect(((HttpServletRequest) externalContext.getRequest()).getRequestURI());
        LOG.info("New locale created");
    }

    public ua.kiev.doctorvera.entities.Locale getNewLocale() {
        return newLocale;
    }

    public void setNewLocale(ua.kiev.doctorvera.entities.Locale newLocale) {
        this.newLocale = newLocale;
    }

    //***************************************************************************//
                            //Add Message Dialog//
    //***************************************************************************//
    private MessageBundle newMessage = new MessageBundle();

    public void initNewMessage(){
        newMessage = new MessageBundle();
        newMessage.setType(MessageBundle.Type.MESSAGE);
        newMessage.setLocale(localeFacade.findLocale(SessionParams.DEFAULT_LOCALE));
    }

    public void createNewMessage(){
        newMessage.setDateCreated(new Date());
        newMessage.setUserCreated(authorizedUser);
        messageBundleFacade.create(newMessage);

        allMessages.add(0,newMessage);

        Message.showMessage(Message.getMessage("APPLICATION_SUCCESSFUL_TITLE"), Message.getMessage("LOCALIZATION_NEW_MESSAGE_CREATE_SUCCESS"));
        RequestContext context = RequestContext.getCurrentInstance();
        context.update(":settingsForm:languageGrid");
        context.execute("PF('addMessageDialog').hide()");
        LOG.info("New constant created");
    }

    public MessageBundle getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(MessageBundle newMessage) {
        this.newMessage = newMessage;
    }
}
