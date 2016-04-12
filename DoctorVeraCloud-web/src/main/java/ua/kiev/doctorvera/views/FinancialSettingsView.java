package ua.kiev.doctorvera.views;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import ua.kiev.doctorvera.entities.*;
import ua.kiev.doctorvera.facadeLocal.*;
import ua.kiev.doctorvera.resources.Config;
import ua.kiev.doctorvera.resources.Message;
import ua.kiev.doctorvera.utils.DateUtils;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;
import java.util.Locale;
import java.util.logging.Logger;
/**
 * Created by volodymyr.bodnar on 2/15/2016.
 */
@Named
@ViewScoped
public class FinancialSettingsView implements Serializable {
    private static final Logger LOG = Logger.getLogger(FinancialSettingsView.class.getName());

    @Inject
    private SessionParams sessionParams;

    @EJB
    private ShareFacadeLocal shareFacade;

    @EJB
    private MethodsFacadeLocal methodsFacade;

    @EJB
    private UsersFacadeLocal usersFacade;

    private Users authorizedUser;

    private List<Share> allShares;
    private Share selectedShare;
    private boolean fixed;

    private DualListModel<Methods> methodsDualListModel;
    private DualListModel<Users> doctorsDualListModel;
    private DualListModel<Users> assistantsDualListModel;

    @PostConstruct
    public void init(){
        authorizedUser = sessionParams.getAuthorizedUser();
        allShares = shareFacade.initializeLazyEntity(shareFacade.findAll());
        constructMethodsPickList();
        constructDoctorsPickList();
        constructAssistantsPickList();
    }

    public void constructMethodsPickList(){
        if(selectedShare != null && selectedShare.getId() == null) {
            List<Methods> allMethods = methodsFacade.findAll();
            List<Methods> targetMethods = selectedShare.getMethods() == null ? new ArrayList<>() : (List)selectedShare.getMethods();
            allMethods.removeAll(targetMethods);
            methodsDualListModel = new DualListModel<>(allMethods, targetMethods);
        } else if (selectedShare != null){
            List<Methods> allMethods = methodsFacade.findAll();
            List<Methods> targetMethods = selectedShare.getMethods() == null ? new ArrayList<>() : (List)selectedShare.getMethods();
            allMethods.removeAll(targetMethods);
            methodsDualListModel = new DualListModel<>(allMethods, targetMethods);
        }else{
            methodsDualListModel = new DualListModel<>(new ArrayList<Methods>(), new ArrayList<Methods>());
        }
    }

    public void constructDoctorsPickList(){
        if(selectedShare != null  && selectedShare.getId() == null) {
            List<Users> allDoctors = usersFacade.findByGroup(Integer.parseInt(Config.getMessage("DOCTORS_USER_GROUP_ID")));
            List<Users> targetDoctors = selectedShare.getDoctors() == null ? new ArrayList<>() : (List)selectedShare.getDoctors();
            allDoctors.removeAll(targetDoctors);
            doctorsDualListModel = new DualListModel<>(allDoctors, targetDoctors);
        } else if (selectedShare != null){
            List<Users> allDoctors = usersFacade.findByGroup(Integer.parseInt(Config.getMessage("DOCTORS_USER_GROUP_ID")));
            List<Users> targetDoctors = selectedShare.getDoctors() == null ? new ArrayList<>() : (List)selectedShare.getDoctors();
            allDoctors.removeAll(targetDoctors);
            doctorsDualListModel = new DualListModel<>(allDoctors, targetDoctors);
        }else{
            doctorsDualListModel = new DualListModel<>(new ArrayList<Users>(), new ArrayList<Users>());
        }
    }

    public void constructAssistantsPickList(){
        if(selectedShare != null && selectedShare.getId() == null) {
            List<Users> allAssistants = usersFacade.findByGroup(Integer.parseInt(Config.getMessage("ASSISTANTS_USER_GROUP_ID")));
            List<Users> targetAssistants = selectedShare.getAssistants() == null ? new ArrayList<>() : (List)selectedShare.getAssistants();
            allAssistants.removeAll(targetAssistants);
            assistantsDualListModel = new DualListModel<>(allAssistants, targetAssistants);
        } else if (selectedShare != null){
            List<Users> allAssistants = usersFacade.findByGroup(Integer.parseInt(Config.getMessage("ASSISTANTS_USER_GROUP_ID")));
            List<Users> targetAssistants = selectedShare.getAssistants() == null ? new ArrayList<>() : (List)selectedShare.getAssistants();
            allAssistants.removeAll(targetAssistants);
            assistantsDualListModel = new DualListModel<>(allAssistants, targetAssistants);
        }else{
            assistantsDualListModel = new DualListModel<>(new ArrayList<Users>(), new ArrayList<Users>());
        }
    }

    public void saveShare(){
        selectedShare.setUserCreated(authorizedUser);
        selectedShare.setDateCreated(new Date());
        if(selectedShare.getId() == null) {
            shareFacade.create(selectedShare);
            allShares.add(selectedShare);
        }else{
            shareFacade.edit(selectedShare);
        }
        final String successMessage = Message.getMessage("APPLICATION_SAVED");
        final String successTitle = Message.getMessage("VALIDATOR_SUCCESS_TITLE");
        Message.showMessage(successTitle, successMessage);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('addShareDialog').hide()");
    }

    public void deleteSelectedShare(){
        allShares.remove(selectedShare);
        selectedShare.setUserCreated(authorizedUser);
        selectedShare.setDateCreated(new Date());
        shareFacade.remove(selectedShare);
        final String successMessage = Message.getMessage("FINANCIAL_SETTINGS_REMOVED");
        final String successTitle = Message.getMessage("VALIDATOR_SUCCESS_TITLE");
        Message.showMessage(successTitle, successMessage);
    }

    public void createNewShare(){
        selectedShare = new Share();
        constructMethodsPickList();
        constructDoctorsPickList();
        constructAssistantsPickList();
    }

    public void selectShare(){
        constructMethodsPickList();
        constructDoctorsPickList();
        constructAssistantsPickList();
    }

    public void onMethodTransfer(TransferEvent event){
        //All Methods from the right picker
        List<Methods> targetList = methodsDualListModel.getTarget();

        //Constructing success message
        final String successTitle = Message.getMessage("VALIDATOR_SUCCESS_TITLE");
        String successMessage;
        if(targetList != null && targetList.contains(event.getItems().get(0)))
            successMessage = Message.getMessage("FINANCIAL_SETTINGS_ADD_METHOD_SUCCESS");
        else
            successMessage = Message.getMessage("FINANCIAL_SETTINGS_REMOVE_METHOD_SUCCESS");

        //Iterating each transferred method
        for(Object methodObject : event.getItems()) {
            Methods methodTransferred = (Methods) methodObject;

            //Constructing success message
            successMessage += methodTransferred.getShortName() + ", ";
        }
        selectedShare.setMethods(methodsDualListModel.getTarget());
        LOG.info(successMessage);
        Message.showMessage(successTitle, successMessage);
    }

    public void onDoctorTransfer(TransferEvent event){
        //All doctors from the right picker
        List<Users> targetList = doctorsDualListModel.getTarget();

        //Constructing success message
        final String successTitle = Message.getMessage("VALIDATOR_SUCCESS_TITLE");
        String successMessage;
        if(targetList != null && targetList.contains(event.getItems().get(0)))
            successMessage = Message.getMessage("FINANCIAL_SETTINGS_ADD_DOCTOR_SUCCESS");
        else
            successMessage = Message.getMessage("FINANCIAL_SETTINGS_REMOVE_DOCTOR_SUCCESS");

        //Iterating each transferred user
        for(Object userObject : event.getItems()) {
            Users userTransferred = (Users) userObject;

            //Constructing success message
            successMessage += userTransferred.getFirstName() + " " + userTransferred.getLastName() + ", ";
        }
        selectedShare.setDoctors(doctorsDualListModel.getTarget());
        LOG.info(successMessage);
        Message.showMessage(successTitle, successMessage);
    }

    public void onAssistantTransfer(TransferEvent event){
        //All doctors from the right picker
        List<Users> targetList = assistantsDualListModel.getTarget();

        //Constructing success message
        final String successTitle = Message.getMessage("VALIDATOR_SUCCESS_TITLE");
        String successMessage;
        if(targetList != null && targetList.contains(event.getItems().get(0)))
            successMessage = Message.getMessage("FINANCIAL_SETTINGS_ADD_ASSISTANT_SUCCESS");
        else
            successMessage = Message.getMessage("FINANCIAL_SETTINGS_REMOVE_ASSISTANT_SUCCESS");

        //Iterating each transferred user
        for(Object userObject : event.getItems()) {
            Users userTransferred = (Users) userObject;

            //Constructing success message
            successMessage += userTransferred.getFirstName() + " " + userTransferred.getLastName() + ", ";
        }
        selectedShare.setAssistants(assistantsDualListModel.getTarget());
        LOG.info(successMessage);
        Message.showMessage(successTitle, successMessage);
    }

    public boolean filterByDate(Object value, Object filter, Locale locale){
        return DateUtils.filterByDate(value, filter, locale);
    }

    public List<Share> getAllShares() {
        return allShares;
    }

    public void setAllShares(List<Share> allShares) {
        this.allShares = allShares;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public DualListModel<Methods> getMethodsDualListModel() {
        return methodsDualListModel;
    }

    public void setMethodsDualListModel(DualListModel<Methods> methodsDualListModel) {
        this.methodsDualListModel = methodsDualListModel;
    }

    public Share getSelectedShare() {
        return selectedShare;
    }

    public void setSelectedShare(Share selectedShare) {
        this.selectedShare = selectedShare;
    }

    public DualListModel<Users> getDoctorsDualListModel() {
        return doctorsDualListModel;
    }

    public void setDoctorsDualListModel(DualListModel<Users> doctorsDualListModel) {
        this.doctorsDualListModel = doctorsDualListModel;
    }

    public DualListModel<Users> getAssistantsDualListModel() {
        return assistantsDualListModel;
    }

    public void setAssistantsDualListModel(DualListModel<Users> assistantsDualListModel) {
        this.assistantsDualListModel = assistantsDualListModel;
    }
}
