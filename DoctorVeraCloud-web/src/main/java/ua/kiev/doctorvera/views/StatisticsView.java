package ua.kiev.doctorvera.views;

import org.joda.time.DateTime;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;
import ua.kiev.doctorvera.entities.Methods;
import ua.kiev.doctorvera.entities.Payments;
import ua.kiev.doctorvera.entities.Schedule;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.*;
import ua.kiev.doctorvera.resources.Config;
import ua.kiev.doctorvera.resources.Message;
import ua.kiev.doctorvera.utils.StatisticsHelper;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by volodymyr.bodnar on 21.07.2015.
 */
@Named
@ViewScoped
public class StatisticsView implements Serializable{
    private final static Logger LOG = Logger.getLogger(StatisticsView.class.getName());

    @Inject
    private SessionParams sessionParams;

    @EJB
    private ScheduleFacadeLocal scheduleFacade;

    @EJB
    private PaymentsFacadeLocal paymentsFacade;

    @EJB
    private UsersFacadeLocal usersFacade;

    @EJB
    private ShareFacadeLocal shareFacade;

    @EJB
    private MethodsFacadeLocal methodsFacade;

    private Users authorizedUser;

    private Users selectedEmployee;

    private List<Users> allEmployees;

    private Date startDate;

    private Date endDate;

    private Methods selectedMethod;

    private List<Methods> allMethods;

    private LineChartModel periodAppointments;
    private LineChartModel doctorsSalaryChart;
    private LineChartModel totalPayedChart;
    private LineChartModel clinicsSalaryChart;
    private PieChartModel methodsChart;
    private PieChartModel employeeChart;

    private Integer appointmentsCountMonth;

    private Float averageAppointmentsCountPerDay = 0f;

    private Float averageAppointmentsCountPerMonth = 0f;

    private Integer maxAppointmentsPerDay;

    private Integer minAppointmentsPerDay;

    private Integer workingDays;

    private Float salarySum = 0f;

    private Float clinicsSalary = 0f;

    private Float paidSum = 0f;

    private Float averageClinicsSalary = 0f;

    private Float averagePayedSum = 0f;

    private Float averageDaySalary = 0f;

    private Float minDaySalary = 0f;

    private Float maxDaySalary = 0f;

    private Float fullBalance = 0f;

    private Float periodBalance = 0f;

    private Float periodIncome = 0f;

    private Float periodOutcome = 0f;

    @PostConstruct
    public void init() {
        authorizedUser = sessionParams.getAuthorizedUser();
        populateStatistics();
        allMethods = methodsFacade.initializeLazyEntity(methodsFacade.findAll());
        allEmployees = usersFacade.findByGroup(Integer.parseInt(Config.getMessage("EMPLOYEE_USER_GROUP_ID")));
    }

    public void populateStatistics(){
        DateTime startOfPeriod, endOfPeriod;
        if (startDate == null || endDate == null){
            startOfPeriod = new DateTime().dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            endOfPeriod = new DateTime();
        }else{
            startOfPeriod = new DateTime(startDate).millisOfDay().withMinimumValue();
            endOfPeriod = new DateTime(endDate).millisOfDay().withMaximumValue();
        }

        fullBalance = 0f;
        for(Payments payment : paymentsFacade.findAll()){
            fullBalance += payment.getTotal();
        }
        List<Payments> paymentsForPeriod = paymentsFacade.findByDate(startOfPeriod.toDate(), endOfPeriod.toDate());
        periodBalance = 0f;
        periodIncome = 0f;
        periodOutcome = 0f;
        for(Payments payment : paymentsForPeriod){
            periodBalance += payment.getTotal();
            if(payment.getTotal() > 0)
                periodIncome += payment.getTotal();
            if(payment.getTotal() < 0)
                periodOutcome += payment.getTotal();
        }

        List<Schedule> scheduleList;

        Map<Schedule, Map<String, Float>> salaryData = null;
        try {
            if(selectedEmployee == null && selectedMethod == null) {
                scheduleList = scheduleFacade.findPayedByDateBetween(startOfPeriod.toDate(), endOfPeriod.toDate());
            }else if (selectedMethod == null){
                scheduleList = scheduleFacade.findPayedByEmployeeAndDateBetween(selectedEmployee, startOfPeriod.toDate(), endOfPeriod.toDate());
            }else if(selectedEmployee == null){
                scheduleList = scheduleFacade.findPayedByMethodAndDateBetween(selectedMethod, startOfPeriod.toDate(), endOfPeriod.toDate());
            }else{
                scheduleList = scheduleFacade.findPayedByEmployeeAndMethodAndDateBetween(selectedEmployee, selectedMethod, startOfPeriod.toDate(), endOfPeriod.toDate());
            }
            salaryData = shareFacade.findFinancialDataOnScheduleList(scheduleList);
        } catch (ShareFacadeLocal.ShareNotFoundException e) {
            salaryData = new HashMap<>();
            scheduleList = new LinkedList<>();
            Message.showErrorInDialog(e.getMessage());
        }

        Map<DateTime, Integer> appointmentsPerDay = new HashMap();
        Map<DateTime, Float> doctorsSalaryPerDay = new HashMap();
        Map<DateTime, Float> clinicsSalaryPerDay = new HashMap();
        Map<DateTime, Float> paidPerDay = new HashMap();
        Map<String, Number> methods = new HashMap();
        Map<String, Number> doctorsAppointments = new HashMap();

        for(Schedule schedule : scheduleList) {
            DateTime oneDay = new DateTime (schedule.getDateTimeStart()).dayOfYear().getDateTime().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
            methods.put(schedule.getMethod().getShortName(),
                    methods.get(schedule.getMethod().getShortName()) == null ? 1 : (Integer)methods.get(schedule.getMethod().getShortName()) + 1 );
            String doctorKey = schedule.getDoctor().getFirstName() + " " + schedule.getDoctor().getLastName();
            doctorsAppointments.put(doctorKey,
                    doctorsAppointments.get(doctorKey) == null ? 1 : (Integer)doctorsAppointments.get(doctorKey) + 1 );
            if(appointmentsPerDay.get(oneDay) == null){
                appointmentsPerDay.put(oneDay, 1);
                doctorsSalaryPerDay.put(oneDay, salaryData.get(schedule).get(determineEmployeeFunction(selectedEmployee).name()));
                paidPerDay.put(oneDay, salaryData.get(schedule).get(ShareFacadeLocal.Part.PAID.name()));
                clinicsSalaryPerDay.put(oneDay, salaryData.get(schedule).get(ShareFacadeLocal.Part.CENTER.name()));
            }else{
                appointmentsPerDay.put(oneDay, appointmentsPerDay.get(oneDay) + 1);
                doctorsSalaryPerDay.put(oneDay, doctorsSalaryPerDay.get(oneDay) + salaryData.get(schedule).get(determineEmployeeFunction(selectedEmployee).name()));
                paidPerDay.put(oneDay, paidPerDay.get(oneDay) + salaryData.get(schedule).get(ShareFacadeLocal.Part.PAID.name()));
                clinicsSalaryPerDay.put(oneDay, clinicsSalaryPerDay.get(oneDay) + salaryData.get(schedule).get(ShareFacadeLocal.Part.CENTER.name()));
            }
        }
        periodAppointments = StatisticsHelper.populateMonthAppointments(startOfPeriod, endOfPeriod, appointmentsPerDay);
        periodAppointments.setTitle(Message.getMessage("STATISTICS_APPOINTMENTS_CHART_TITLE"));
        doctorsSalaryChart = StatisticsHelper.populateMonthSalary(startOfPeriod, endOfPeriod, doctorsSalaryPerDay);
        doctorsSalaryChart.setTitle(Message.getMessage("STATISTICS_EMPLOYEE_SALARY_CHART_TITLE"));
        clinicsSalaryChart = StatisticsHelper.populateMonthSalary(startOfPeriod, endOfPeriod, clinicsSalaryPerDay);
        clinicsSalaryChart.setTitle(Message.getMessage("STATISTICS_CLINICS_INCOME_CHART_TITLE"));
        totalPayedChart = StatisticsHelper.populateMonthSalary(startOfPeriod, endOfPeriod, paidPerDay);
        totalPayedChart.setTitle(Message.getMessage("STATISTICS_TOTAL_INCOME_CHART_TITLE"));

        methodsChart = new PieChartModel();
        methodsChart.setData(methods);
        methodsChart.setTitle(Message.getMessage("STATISTICS_METHODS_CHART_TITLE"));
        methodsChart.setLegendPosition("ne");

        employeeChart = new PieChartModel();
        employeeChart.setData(doctorsAppointments);
        employeeChart.setTitle(Message.getMessage("STATISTICS_EMPLOYEE_PIE_CHART_TITLE"));
        employeeChart.setLegendPosition("ne");

        populateStatistics(scheduleList, salaryData);
    }

    private ShareFacadeLocal.Part determineEmployeeFunction(Users employee){
        if(employee == null) return ShareFacadeLocal.Part.DOCTOR;
        if (usersFacade.isDoctor(employee)) {
            return ShareFacadeLocal.Part.DOCTOR;
        } else if (usersFacade.isAssistant(employee)) {
            return ShareFacadeLocal.Part.ASSISTANT;
        }else
            return null;
    }

    private void populateStatistics(List<Schedule> scheduleList, Map<Schedule, Map<String, Float>> monthSalaryData){
        appointmentsCountMonth = scheduleList.size();

        if(StatisticsHelper.getWorkingDays(scheduleList) != 0){
            averageAppointmentsCountPerDay = ((float)scheduleList.size() / (float)StatisticsHelper.getWorkingDays(scheduleList));
        }
        maxAppointmentsPerDay = StatisticsHelper.getMaxAppointmentsPerDay(scheduleList);
        minAppointmentsPerDay = StatisticsHelper.getMinAppointmentsPerDay(scheduleList);
        salarySum = 0f;
        clinicsSalary = 0f;
        paidSum = 0f;

        for(Schedule schedule : scheduleList){
            clinicsSalary += monthSalaryData.get(schedule).get(ShareFacadeLocal.Part.CENTER.name());
            paidSum += monthSalaryData.get(schedule).get(ShareFacadeLocal.Part.PAID.name());

            if(selectedEmployee != null) {
                salarySum += monthSalaryData.get(schedule).get(determineEmployeeFunction(selectedEmployee).name());
                if (maxDaySalary < monthSalaryData.get(schedule).get(determineEmployeeFunction(selectedEmployee).name()))
                    maxDaySalary = monthSalaryData.get(schedule).get(determineEmployeeFunction(selectedEmployee).name());
            }
        }

        minDaySalary = maxDaySalary;
        for(Schedule schedule : scheduleList){
            if (minDaySalary > monthSalaryData.get(schedule).get(determineEmployeeFunction(selectedEmployee).name()) && selectedEmployee != null
                    && monthSalaryData.get(schedule).get(determineEmployeeFunction(selectedEmployee).name()) != 0)
                minDaySalary = monthSalaryData.get(schedule).get(determineEmployeeFunction(selectedEmployee).name());
        }
        workingDays = StatisticsHelper.getWorkingDays(scheduleList);
        averageDaySalary = salarySum / workingDays;
        averageClinicsSalary = clinicsSalary / workingDays;
        averagePayedSum = paidSum / workingDays;
    }


    public LineChartModel getPeriodAppointments() {
        return periodAppointments;
    }

    public void setPeriodAppointments(LineChartModel periodAppointments) {
        this.periodAppointments = periodAppointments;
    }

    public Integer getAppointmentsCountMonth() {
        return appointmentsCountMonth;
    }

    public void setAppointmentsCountMonth(Integer appointmentsCountMonth) {
        this.appointmentsCountMonth = appointmentsCountMonth;
    }

    public Float getAverageAppointmentsCountPerDay() {
        return averageAppointmentsCountPerDay;
    }

    public void setAverageAppointmentsCountPerDay(Float averageAppointmentsCountPerDay) {
        this.averageAppointmentsCountPerDay = averageAppointmentsCountPerDay;
    }

    public Float getAverageAppointmentsCountPerMonth() {
        return averageAppointmentsCountPerMonth;
    }

    public void setAverageAppointmentsCountPerMonth(Float averageAppointmentsCountPerMonth) {
        this.averageAppointmentsCountPerMonth = averageAppointmentsCountPerMonth;
    }

    public Float getAverageDaySalary() {
        return averageDaySalary;
    }

    public void setAverageDaySalary(Float averageDaySalary) {
        this.averageDaySalary = averageDaySalary;
    }

    public Integer getMaxAppointmentsPerDay() {
        return maxAppointmentsPerDay;
    }

    public void setMaxAppointmentsPerDay(Integer maxAppointmentsPerDay) {
        this.maxAppointmentsPerDay = maxAppointmentsPerDay;
    }

    public Float getMaxDaySalary() {
        return maxDaySalary;
    }

    public void setMaxDaySalary(Float maxDaySalary) {
        this.maxDaySalary = maxDaySalary;
    }

    public Integer getMinAppointmentsPerDay() {
        return minAppointmentsPerDay;
    }

    public void setMinAppointmentsPerDay(Integer minAppointmentsPerDay) {
        this.minAppointmentsPerDay = minAppointmentsPerDay;
    }

    public Float getMinDaySalary() {
        return minDaySalary;
    }

    public void setMinDaySalary(Float minDaySalary) {
        this.minDaySalary = minDaySalary;
    }

    public Integer getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(Integer workingDays) {
        this.workingDays = workingDays;
    }

    public Users getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(Users selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Methods getSelectedMethod() {
        return selectedMethod;
    }

    public void setSelectedMethod(Methods selectedMethod) {
        this.selectedMethod = selectedMethod;
    }

    public List<Methods> getAllMethods() {
        return allMethods;
    }

    public void setAllMethods(List<Methods> allMethods) {
        this.allMethods = allMethods;
    }

    public Float getSalarySum() {
        return salarySum;
    }

    public void setSalarySum(Float salarySum) {
        this.salarySum = salarySum;
    }

    public List<Users> getAllEmployees() {
        return allEmployees;
    }

    public void setAllEmployees(List<Users> allEmployees) {
        this.allEmployees = allEmployees;
    }

    public Float getAveragePayedSum() {
        return averagePayedSum;
    }

    public Float getAverageClinicsSalary() {
        return averageClinicsSalary;
    }

    public Float getPaidSum() {
        return paidSum;
    }

    public Float getClinicsSalary() {
        return clinicsSalary;
    }

    public LineChartModel getClinicsSalaryChart() {
        return clinicsSalaryChart;
    }

    public LineChartModel getTotalPayedChart() {
        return totalPayedChart;
    }

    public LineChartModel getDoctorsSalaryChart() {
        return doctorsSalaryChart;
    }

    public PieChartModel getMethodsChart() {
        return methodsChart;
    }

    public PieChartModel getEmployeeChart() {
        return employeeChart;
    }

    public Float getPeriodOutcome() {
        return periodOutcome;
    }

    public void setPeriodOutcome(Float periodOutcome) {
        this.periodOutcome = periodOutcome;
    }

    public Float getPeriodIncome() {
        return periodIncome;
    }

    public void setPeriodIncome(Float periodIncome) {
        this.periodIncome = periodIncome;
    }

    public Float getPeriodBalance() {
        return periodBalance;
    }

    public void setPeriodBalance(Float periodBalance) {
        this.periodBalance = periodBalance;
    }

    public Float getFullBalance() {
        return fullBalance;
    }

    public void setFullBalance(Float fullBalance) {
        this.fullBalance = fullBalance;
    }
}
