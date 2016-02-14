package ua.kiev.doctorvera.views;

import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import ua.kiev.doctorvera.entities.DeliveryGroup;
import ua.kiev.doctorvera.entities.MessageScheduler;
import ua.kiev.doctorvera.entities.MessageTemplate;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.DeliveryGroupFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.MessageSchedulerFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.MessageTemplateFacadeLocal;
import ua.kiev.doctorvera.resources.Message;
import ua.kiev.doctorvera.services.ScheduleServiceLocal;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

/**
 * Created by volodymyr.bodnar on 12/23/2015.
 */
@Named(value="messageSchedulerView")
@ViewScoped
public class MessageSchedulerView implements Serializable {

    @EJB
    private MessageSchedulerFacadeLocal messageSchedulerFacade;

    @EJB
    private ScheduleServiceLocal scheduleService;

    @EJB
    private MessageTemplateFacadeLocal messageTemplateFacade;

    @EJB
    private DeliveryGroupFacadeLocal deliveryGroupFacade;

    @Inject
    private SessionParams sessionParams;

    //Authorized User
    private Users authorizedUser;

    private List<MessageTemplate> allMessageTemplates;
    private List<DeliveryGroup> allDeliveryGroups;
    private LazyDataModel<MessageScheduler> allMessageScheduler;

    private MessageScheduler newMessageScheduler;

    //Selected Delivery Group
    private MessageScheduler selectedMessageScheduler;

    private String groupFilter;

    private String dayFilter;

    @PostConstruct
    public void init(){
        authorizedUser = sessionParams.getAuthorizedUser();
        allMessageScheduler = new SchedulerLazyModel();
        allMessageTemplates = messageTemplateFacade.initializeLazyEntity(messageTemplateFacade.findAll());
        allDeliveryGroups = deliveryGroupFacade.initializeLazyEntity(deliveryGroupFacade.findAll());
        newMessageScheduler = new MessageScheduler();
    }

    public void createNewScheduler(){
        selectedMessageScheduler = null;
        newMessageScheduler = new MessageScheduler();
    }

    //Deletes selected message Scheduler
    public void deleteSelectedScheduler(){
        messageSchedulerFacade.remove(selectedMessageScheduler);
        scheduleService.removeEvent(selectedMessageScheduler);
        final String successMessage = Message.getMessage("MESSAGE_SCHEDULER_DELETED");
        final String successTitle = Message.getMessage("VALIDATOR_SUCCESS_TITLE");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
    }

    //Updates selected message Scheduler
    public void saveSelectedScheduler() {
        selectedMessageScheduler.setDateCreated(new Date());
        selectedMessageScheduler.setUserCreated(authorizedUser);
        messageSchedulerFacade.edit(selectedMessageScheduler);
        scheduleService.changeEvent(selectedMessageScheduler);
        final String successMessage = Message.getMessage("MESSAGE_SCHEDULER_UPDATED");
        final String successTitle = Message.getMessage("VALIDATOR_SUCCESS_TITLE");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('editSchedulerDialog').hide()");
    }

    //Adds selected message Scheduler
    public void saveNewScheduler(){
        newMessageScheduler.setDateCreated(new Date());
        newMessageScheduler.setUserCreated(authorizedUser);
        messageSchedulerFacade.create(newMessageScheduler);
        scheduleService.scheduleEvent(newMessageScheduler);
        final String successMessage = Message.getMessage("APPLICATION_SAVED");
        final String successTitle = Message.getMessage("MESSAGE_SCHEDULER_SAVED");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('addSchedulerDialog').hide()");
    }

    public class SchedulerLazyModel extends LazyDataModel<MessageScheduler> {

        List<MessageScheduler> allPaginatedFilteredScheduler = new ArrayList<>();


        public SchedulerLazyModel() {
        }

        @Override
        public MessageScheduler getRowData(String rowKey) {
            if (allPaginatedFilteredScheduler == null) return null;
            for(MessageScheduler scheduler : allPaginatedFilteredScheduler) {
                if(scheduler.getId().equals(Integer.parseInt(rowKey)))
                    return scheduler;
            }
            return null;
        }

        @Override
        public Object getRowKey(MessageScheduler scheduler) {
            return scheduler.getId();
        }

        @Override
        public List<MessageScheduler> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
            allPaginatedFilteredScheduler = messageSchedulerFacade.initializeLazyEntity(messageSchedulerFacade.findAll(first, pageSize, sortField, sortOrder, filters), sortOrder, sortField);
            setRowCount(messageSchedulerFacade.count(first, pageSize, filters));
            return allPaginatedFilteredScheduler;
        }


        public boolean filterByDate(Object value, Object filter, Locale locale) throws ParseException {
            String filterText = (filter == null) ? null : filter.toString().trim();
            if(filterText == null||filterText.equals("")) {
                return true;
            }

            if(value == null) {
                return false;
            }

            return ((Date)value).compareTo((Date)filter) > 0;
        }
    }

    public void resetSelectorFiltersFilter(){
        groupFilter = null;
        dayFilter = null;
    }

    /*----------------------------------------------------*/
    /*---------------Getters & Setters--------------------*/
    /*----------------------------------------------------*/

    public LazyDataModel<MessageScheduler> getAllMessageScheduler() {
        return allMessageScheduler;
    }

    public void setAllMessageScheduler(LazyDataModel<MessageScheduler> allMessageScheduler) {
        this.allMessageScheduler = allMessageScheduler;
    }

    public MessageScheduler getNewMessageScheduler() {
        return newMessageScheduler;
    }

    public void setNewMessageScheduler(MessageScheduler newMessageScheduler) {
        this.newMessageScheduler = newMessageScheduler;
    }

    public MessageScheduler getSelectedMessageScheduler() {
        return selectedMessageScheduler;
    }

    public void setSelectedMessageScheduler(MessageScheduler selectedMessageScheduler) {
        this.selectedMessageScheduler = selectedMessageScheduler;
    }

    public List<MessageTemplate> getAllMessageTemplates() {
        return allMessageTemplates;
    }

    public void setAllMessageTemplates(List<MessageTemplate> allMessageTemplates) {
        this.allMessageTemplates = allMessageTemplates;
    }

    public List<DeliveryGroup> getAllDeliveryGroups() {
        return allDeliveryGroups;
    }

    public void setAllDeliveryGroups(List<DeliveryGroup> allDeliveryGroups) {
        this.allDeliveryGroups = allDeliveryGroups;
    }

    public String getGroupFilter() {
        return groupFilter;
    }

    public void setGroupFilter(String groupFilter) {
        this.groupFilter = groupFilter;
    }

    public MessageScheduler.DayOfWeek[] getDaysOfWeek(){
        return MessageScheduler.DayOfWeek.values();
    }

    public void setDayFilter(String dayFilter) {
        this.dayFilter = dayFilter;
    }

    public String getDayFilter() {
        return dayFilter;
    }

    public String getRowStyle(MessageScheduler scheduler){
        if(scheduler.getDateEnd().before(new Date())) return "oldSchedulerRow";
        else if (scheduler.getDateStart().after(new Date())) return "futureSchedulerRow";
        else return "";
    }
}
