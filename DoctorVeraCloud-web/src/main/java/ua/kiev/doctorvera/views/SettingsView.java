package ua.kiev.doctorvera.views;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import ua.kiev.doctorvera.entities.*;
import ua.kiev.doctorvera.facadeLocal.*;
import ua.kiev.doctorvera.resources.Config;
import ua.kiev.doctorvera.resources.Message;
import ua.kiev.doctorvera.services.SQLServiceLocal;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
/**
 * Created by volodymyr.bodnar on 2/15/2016.
 */
@Named
@ViewScoped
public class SettingsView implements Serializable {
    private static final Logger LOG = Logger.getLogger(SettingsView.class.getName());

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

    @EJB
    FileRepositoryFacadeLocal fileRepositoryFacade;

    @EJB
    MethodTypesFacadeLocal methodTypesFacade;

    @EJB
    SQLServiceLocal sqlService;

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
    private List<MethodTypes> methodTypes;

    @PostConstruct
    public void init(){
        authorizedUser = sessionParams.getAuthorizedUser();
        for(MessageBundle constant : messageBundleFacade.findAllMessagesByType(MessageBundle.Type.CONFIG)){
            allConstants.put(constant.getMessageKey(), constant);
        }
        allPaths = messageBundleFacade.findAllMessagesByType(MessageBundle.Type.MAPPING);
        smsTemplates = messageTemplateFacade.findByType(MessageTemplate.Type.SMS, true);
        emailTemplates = messageTemplateFacade.findByType(MessageTemplate.Type.EMAIL, true);
        userGroups = userGroupsFacade.findAll();
        users = usersFacade.findAll();
        methods = methodsFacade.findAll();
        methodTypes = methodTypesFacade.findAll();
    }

    public void saveSettings(String key){
        if(key == null || key.isEmpty()) return;
        MessageBundle constant = allConstants.get(key);
        constant.setDateCreated(new Date());
        constant.setUserCreated(authorizedUser);
        messageBundleFacade.edit(constant);
        Message.showMessage(Message.getMessage("APPLICATION_SUCCESSFUL_TITLE"), Message.getMessage("APPLICATION_SAVED"));
        LOG.info("Changes to settings constants saved");
        Config.clear();
    }

    public void savePaths(MessageBundle path){
        path.setDateCreated(new Date());
        path.setUserCreated(authorizedUser);
        messageBundleFacade.edit(path);
        Message.showMessage(Message.getMessage("APPLICATION_SUCCESSFUL_TITLE"), Message.getMessage("APPLICATION_SAVED"));
        LOG.info("Changes to settings paths saved");
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        UploadedFile uploadedFile = event.getFile();
        String  constantName = (String)event.getComponent().getAttributes().get("constantName");
        LOG.info("Time " + new Date() + " Starting to upload file:");
        Integer fileId;
        if (uploadedFile != null) {
            MessageBundle constant = allConstants.get(constantName);
            FileRepository oldFile = fileRepositoryFacade.find(Integer.parseInt(constant.getValue()));
            if (oldFile != null)
                fileRepositoryFacade.remove(oldFile);
            fileId = fileRepositoryFacade.saveFile(uploadedFile.getContents(), uploadedFile.getFileName(), authorizedUser);
            LOG.info("File successfully uploaded id: " + fileId);
            constant.setValue("" + fileId);
            messageBundleFacade.edit(constant);
            Message.showMessage(Message.getMessage("APPLICATION_SUCCESSFUL_TITLE"), Message.getMessage("APPLICATION_SAVED"));
            LOG.info("Changes to settings constant " + constantName + " saved");
            Config.clear();
        }
    }


    public void databaseBackupUpload(FileUploadEvent event) throws IOException {
        UploadedFile uploadedFile = event.getFile();
        String  constantName = (String)event.getComponent().getAttributes().get("constantName");
        LOG.info("Time " + new Date() + " Starting to upload file:");
        Integer fileId;
        if (uploadedFile != null) {
            LOG.info("File successfully uploaded id: ");
            Message.showMessage(Message.getMessage("APPLICATION_SUCCESSFUL_TITLE"), Message.getMessage("APPLICATION_SAVED"));
            LOG.info("Changes to settings constant " + constantName + " saved");
        }
    }

    public StreamedContent downloadTemplate(String templateName){
        MessageBundle constant = allConstants.get(templateName);
        FileRepository file = fileRepositoryFacade.find(Integer.parseInt(constant.getValue()));
        InputStream inputStream = new ByteArrayInputStream(file.getFile());
        return new DefaultStreamedContent(inputStream, file.getMimeType(), file.getFileNameWithExtention());
    }

    public String getFilename(String templateName) throws IOException {
        MessageBundle constant = allConstants.get(templateName);
        FileRepository file = fileRepositoryFacade.find(Integer.parseInt(constant.getValue()));
        if(file == null){
            return null;
        }else{
            return file.getFileNameWithExtention();
        }

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

    public List<MethodTypes> getMethodTypes() {
        return methodTypes;
    }

    public void setMethodTypes(List<MethodTypes> methodTypes) {
        this.methodTypes = methodTypes;
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

    public String findMethodType(String id){
        if(id == null || id.isEmpty()){
            return "";
        }else {
            return methodTypesFacade.find(Integer.parseInt(id)).getShortName();
        }
    }

    public StreamedContent getExistingDump() throws IOException {
        return fileRepositoryFacade.getExistingDataBaseDump();
    }

    public StreamedContent createNewDump() throws IOException {
        return sqlService.generateNewDatabaseDump();
    }

    public void dropDatabase(){
        sqlService.dropDatabase();
    }
}
