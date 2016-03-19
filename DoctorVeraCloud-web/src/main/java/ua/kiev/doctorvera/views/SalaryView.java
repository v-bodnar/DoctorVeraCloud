package ua.kiev.doctorvera.views;

import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;
import ua.kiev.doctorvera.entities.Payments;
import ua.kiev.doctorvera.entities.Schedule;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.*;
import ua.kiev.doctorvera.resources.Config;
import ua.kiev.doctorvera.resources.Message;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by volodymyr.bodnar on 3/6/2016.
 */
@Named
@ViewScoped
public class SalaryView implements Serializable{
    private static final Logger LOG = Logger.getLogger(SalaryView.class.getName());

    @Inject
    private SessionParams sessionParams;

    @Inject
    private PaymentsView paymentsView;

    @EJB
    private ShareFacadeLocal shareFacade;

    @EJB
    private MethodsFacadeLocal methodsFacade;

    @EJB
    private UsersFacadeLocal usersFacade;

    @EJB
    private ScheduleFacadeLocal scheduleFacade;

    @EJB
    private PaymentsFacadeLocal paymentsFacade;

    private Users authorizedUser;

    private List<Schedule> scheduleList;

    private Date dateStart;
    private Date dateEnd;
    private Users selectedEmployee;
    private List<Users> allEmployees;

    private Map<Schedule, Map<String, Float>> financialData;
    private Float test = 102f;


    @PostConstruct
    public void init(){
        authorizedUser = sessionParams.getAuthorizedUser();
        scheduleList = scheduleFacade.findPayedByStartDateBetween(DateTime.now().withDayOfMonth(1).toDate(), new Date());
        try {
            financialData = shareFacade.findFinancialDataOnScheduleList(scheduleList);
        } catch (ShareFacadeLocal.ShareNotFoundException e) {
            financialData = new HashMap<>();
            Message.showErrorInDialog(e.getMessage());
        }
        allEmployees = usersFacade.findByGroup(Integer.parseInt(Config.getMessage("EMPLOYEE_USER_GROUP_ID")));
    }

    public void searchAppointments(){
        scheduleList = scheduleFacade.findPayedByEmployeeAndDateBetween(selectedEmployee, dateStart, dateEnd);
        try {
            financialData = shareFacade.findFinancialDataOnScheduleList(scheduleList);
        } catch (ShareFacadeLocal.ShareNotFoundException e) {
            financialData = new HashMap<>();
            Message.showErrorInDialog(e.getMessage());
        }
    }

    public void fillPaymentForm(){
        if(selectedEmployee != null){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            paymentsView.setIsPositive(false);
            Payments payment = new Payments();
            payment.setUserCreated(authorizedUser);
            payment.setDateCreated(new Date());
            payment.setDataTime(new Date());
            payment.setRecipient(selectedEmployee);
            if(usersFacade.isDoctor(selectedEmployee)){
                payment.setTotal(getDoctorsSum());
            }else if(usersFacade.isAssistant(selectedEmployee)){
                payment.setTotal(getAssistantsSum());
            }else{
                payment.setTotal(getDoctorDirectedSum());
            }

            String description = Message.getMessage("SALARY_PAYMENT_FORM_DESCRIPTION_START")
                    + " " + selectedEmployee.getLastName() + " " + selectedEmployee.getFirstName()
                    + " " + formatter.format(dateStart) + " - " + formatter.format(dateEnd) + ". ";
            description += Message.getMessage("SALARY_PAYMENT_FORM_DESCRIPTION_END") + " ";
            for(Schedule schedule : scheduleList){
                description += schedule.getId();
                if(scheduleList.indexOf(schedule) != scheduleList.size() - 1){
                    description += ", ";
                }
            }
            description += ".";
            payment.setDescription(description);

            if(payment.getTotal() == 0){
                Message.showError(Message.getMessage("APPLICATION_ERROR"), Message.getMessage("SALARY_CONFIGURATION_NOT_FOUND"));
                RequestContext.getCurrentInstance().execute("PF('addPaymentDialog').hide();");
            }

            paymentsView.setNewPayment(payment);
        }else{
            Message.showError(Message.getMessage("APPLICATION_ERROR"), Message.getMessage("SALARY_NO_EMPLOYEE_SELECTED"));
            RequestContext.getCurrentInstance().execute("PF('addPaymentDialog').hide();");
        }
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Users getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(Users selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }

    public List<Users> getAllEmployees() {
        return allEmployees;
    }

    public void setAllEmployees(List<Users> allEmployees) {
        this.allEmployees = allEmployees;
    }

    public Map<Schedule, Map<String, Float>> getFinancialData() {
        return financialData;
    }

    public void setFinancialData(Map<Schedule, Map<String, Float>> financialData) {
        this.financialData = financialData;
    }

    public List<Schedule> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }

    public float getCostSum(){
        float sum = 0;
        if(financialData.values().isEmpty()) return sum;
        for(Map<String, Float> map : financialData.values())
            sum += map.get(ShareFacadeLocal.Part.COST.name());
        return sum;
    }
    public float getPaidSum(){
        float sum = 0;
        if(financialData.values().isEmpty()) return sum;
        for(Map<String, Float> map : financialData.values())
            sum += map.get(ShareFacadeLocal.Part.PAID.name());
        return sum;
    }
    public float getDoctorsSum(){
        float sum = 0;
        if(financialData.values().isEmpty()) return sum;
        for(Map<String, Float> map : financialData.values())
            sum += map.get(ShareFacadeLocal.Part.DOCTOR.name());
        return sum;
    }
    public float getDoctorDirectedSum(){
        float sum = 0;
        if(financialData.values().isEmpty()) return sum;
        for(Map<String, Float> map : financialData.values())
            sum += map.get(ShareFacadeLocal.Part.DOCTOR_DIRECTED.name());
        return sum;
    }
    public float getAssistantsSum(){
        float sum = 0;
        if(financialData.values().isEmpty()) return sum;
        for(Map<String, Float> map : financialData.values())
            sum += map.get(ShareFacadeLocal.Part.ASSISTANT.name());
        return sum;
    }
    public float getCenterSum(){
        float sum = 0;
        if(financialData.values().isEmpty()) return sum;
        for(Map<String, Float> map : financialData.values())
            sum += map.get(ShareFacadeLocal.Part.CENTER.name());
        return sum;
    }

    public Float getTest() {
        return test;
    }

    public void setTest(Float test) {
        this.test = test;
    }
}
