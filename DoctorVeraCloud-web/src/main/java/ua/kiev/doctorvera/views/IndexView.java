package ua.kiev.doctorvera.views;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.chart.*;
import ua.kiev.doctorvera.entities.Payments;
import ua.kiev.doctorvera.entities.Schedule;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.PaymentsFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.ScheduleFacadeLocal;
import ua.kiev.doctorvera.resources.Message;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

import static ua.kiev.doctorvera.resources.Message.Messages.*;

/**
 * Created by volodymyr.bodnar on 21.07.2015.
 */
@Named(value = "indexView")
@ViewScoped
public class IndexView implements Serializable{
    private final static Logger LOG = Logger.getLogger(IndexView.class.getName());

    @Inject
    private SessionParams sessionParams;

    @EJB
    private ScheduleFacadeLocal scheduleFacade;

    @EJB
    private PaymentsFacadeLocal paymentsFacade;

    private Users authorizedUser;

    private DashboardModel dashboard;

    private LineChartModel  monthAppointments;

    private LineChartModel  yearAppointments;

    private LineChartModel  monthSalary;

    private LineChartModel  yearSalary;

    //For Month Picker
    private Map<String,Integer> monthNames = new LinkedHashMap<>();
    //For Month Picker
    private Integer selectedMonth;
    //For Year Picker
    private HashSet<Integer> years = new HashSet<>();
    //For Year Picker
    private Integer selectedYear;

    private Integer appointmentsCountMonth;

    private Integer appointmentsCountYear;

    private Integer averageAppointmentsCountPerDay;

    private Integer averageAppointmentsCountPerMonth;

    private Integer maxAppointmentsPerDay;

    private Integer maxAppointmentsPerMonth;

    private Integer minAppointmentsPerDay;

    private Integer minAppointmentsPerMonth;

    private Integer workingDaysInMonth;

    private Integer workingDaysInYear;

    private Float monthSalarySum;

    private Float yearSalarySum;

    private Float averageDaySalary;

    private Float averageMonthSalary;

    private Float minDaySalary;

    private Float maxDaySalary;

    private Float minMonthSalary;

    private Float maxMonthSalary;


    @PostConstruct
    public void init() {
        authorizedUser = sessionParams.getAuthorizedUser();

        dashboard = new DefaultDashboardModel();
        DashboardColumn column = new DefaultDashboardColumn();

        column.addWidget("monthAppointments");
        column.addWidget("monthSalary");
        column.addWidget("yearAppointments");
        column.addWidget("yearSalary");

        dashboard.addColumn(column);

        populateMonthAppointments();
        populateYearAppointments();
        populateMonthSalary();
        populateYearSalary();
        populateMonthNames();
        populateYears();
        populateStatistics();
    }

    public void populateMonthAppointments(){
        DateTimeFormatter pattern = DateTimeFormat.forPattern("yyy-MM-dd");
        DateTime startOfMonth;
        DateTime endOfMonth;
        if (selectedMonth == null){
            startOfMonth = new DateTime().dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            endOfMonth = new DateTime();
        }else{
            startOfMonth = new DateTime().withMonthOfYear(selectedMonth).dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            endOfMonth = new DateTime().withMonthOfYear(selectedMonth).dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue();
        }

        monthAppointments = new LineChartModel();
        ChartSeries appointments = new ChartSeries();
        appointments.setLabel(INDEX_APPOINTMENTS_COUNT.toString());
        for(DateTime dayOfMonth = startOfMonth; dayOfMonth.isBefore(endOfMonth); dayOfMonth = dayOfMonth.plusDays(1)){
            appointments.set(dayOfMonth.toString(pattern), scheduleFacade.findByEmployeeAndDateBetween(authorizedUser,
                    dayOfMonth.toDate(), dayOfMonth.millisOfDay().withMaximumValue().toDate()).size());
        }

        monthAppointments.addSeries(appointments);
        monthAppointments.setTitle(INDEX_APPOINTMENTS_COUNT.toString());
        monthAppointments.setLegendPosition("ne");
        monthAppointments.setAnimate(true);
        monthAppointments.setZoom(true);
        monthAppointments.setSeriesColors("459e00");

        DateAxis axis = new DateAxis(INDEX_DAY_OF_MONTH.toString());
        axis.setTickAngle(-50);
        monthAppointments.getAxes().put(AxisType.X, axis);

        Axis yAxis = monthAppointments.getAxis(AxisType.Y);
        yAxis.setLabel(INDEX_APPOINTMENTS_COUNT.toString());
    }

    public void populateYearAppointments(){
        DateTimeFormatter pattern = DateTimeFormat.forPattern("yyy-MM-dd");
        DateTime startOfYear;
        DateTime endOfYear;
        if (selectedYear == null){
            startOfYear = new DateTime().monthOfYear().withMinimumValue().dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            endOfYear = new DateTime();
        }else{
            startOfYear = new DateTime().withYear(selectedYear).monthOfYear().withMinimumValue().dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            endOfYear = new DateTime().withYear(selectedYear).monthOfYear().withMaximumValue().dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue();
        }

        yearAppointments = new LineChartModel();
        ChartSeries appointments = new ChartSeries();
        appointments.setLabel(INDEX_APPOINTMENTS_COUNT.toString());
        for(DateTime monthOfYear = startOfYear; monthOfYear.isBefore(endOfYear); monthOfYear = monthOfYear.plusDays(1)){
            appointments.set(monthOfYear.toString(pattern), scheduleFacade.findByEmployeeAndDateBetween(authorizedUser,
                    monthOfYear.toDate(), monthOfYear.millisOfDay().withMaximumValue().toDate()).size());
        }

        yearAppointments.addSeries(appointments);
        yearAppointments.setTitle(INDEX_APPOINTMENTS_COUNT.toString());
        yearAppointments.setLegendPosition("ne");
        yearAppointments.setAnimate(true);
        yearAppointments.setZoom(true);
        yearAppointments.setSeriesColors("459e00");

        DateAxis axis = new DateAxis(INDEX_MONTH_OF_YEAR.toString());
        axis.setTickAngle(-50);
        yearAppointments.getAxes().put(AxisType.X, axis);

        Axis yAxis = yearAppointments.getAxis(AxisType.Y);
        yAxis.setLabel(INDEX_APPOINTMENTS_COUNT.toString());
    }

    public void populateMonthSalary(){
        DateTimeFormatter pattern = DateTimeFormat.forPattern("yyy-MM-dd");
        DateTime startOfMonth;
        DateTime endOfMonth;
        if (selectedMonth == null){
            startOfMonth = new DateTime().dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            endOfMonth = new DateTime();
        }else{
            startOfMonth = new DateTime().withMonthOfYear(selectedMonth).dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            endOfMonth = new DateTime().withMonthOfYear(selectedMonth).dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue();
        }

        monthSalary = new LineChartModel();
        ChartSeries salary = new ChartSeries();
        salary.setLabel(INDEX_MONTH_SALARY.toString());

        for(DateTime dayOfMonth = startOfMonth; dayOfMonth.isBefore(endOfMonth); dayOfMonth = dayOfMonth.plusDays(1)){
            List<Schedule> appointmentsOfMonth = scheduleFacade.findByEmployeeAndDateBetween(authorizedUser,
                    dayOfMonth.toDate(), dayOfMonth.millisOfDay().withMaximumValue().toDate());
            salary.set(dayOfMonth.toString(pattern), getSalarySum(appointmentsOfMonth));
        }

        monthSalary.addSeries(salary);
        monthSalary.setTitle(INDEX_MONTH_SALARY_CUR.toString());
        monthSalary.setLegendPosition("ne");
        monthSalary.setAnimate(true);
        monthSalary.setZoom(true);
        monthSalary.setSeriesColors("459e00");

        DateAxis axis = new DateAxis(INDEX_DAY_OF_MONTH.toString());
        axis.setTickAngle(-50);
        monthSalary.getAxes().put(AxisType.X, axis);

        Axis yAxis = monthSalary.getAxis(AxisType.Y);
        yAxis.setLabel(INDEX_MONTH_SALARY_CUR.toString());
    }
    
    public void populateYearSalary(){
        DateTimeFormatter pattern = DateTimeFormat.forPattern("yyy-MM-dd");
        DateTime startOfYear;
        DateTime endOfYear;
        if (selectedYear == null){
            startOfYear = new DateTime().monthOfYear().withMinimumValue().dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            endOfYear = new DateTime();
        }else{
            startOfYear = new DateTime().withYear(selectedYear).monthOfYear().withMinimumValue().dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            endOfYear = new DateTime().withYear(selectedYear).monthOfYear().withMaximumValue().dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue();
        }

        yearSalary = new LineChartModel();
        ChartSeries salary = new ChartSeries();
        salary.setLabel(INDEX_YEAR_SALARY.toString());
        for(DateTime monthOfYear = startOfYear; monthOfYear.isBefore(endOfYear); monthOfYear = monthOfYear.plusMonths(1)){
            List<Schedule> appointmentsOfYear = scheduleFacade.findByEmployeeAndDateBetween(authorizedUser,
                    startOfYear.toDate(), monthOfYear.dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue().toDate());
            salary.set(monthOfYear.toString(pattern), getSalarySum(appointmentsOfYear));
        }

        yearSalary.addSeries(salary);
        yearSalary.setTitle(INDEX_YEAR_SALARY_CUR.toString());
        yearSalary.setLegendPosition("ne");
        yearSalary.setAnimate(true);
        yearSalary.setSeriesColors("459e00");

        DateAxis axis = new DateAxis(INDEX_MONTH_OF_YEAR.toString());
        axis.setTickAngle(-50);
        yearSalary.getAxes().put(AxisType.X, axis);

        Axis yAxis = yearSalary.getAxis(AxisType.Y);
        yAxis.setLabel(INDEX_YEAR_SALARY_CUR.toString());
    }

    public void populateStatistics(){
        DateTime startOfMonth;
        DateTime endOfMonth;
        if (selectedMonth == null){
            startOfMonth = new DateTime().dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            endOfMonth = new DateTime();
        }else{
            startOfMonth = new DateTime().withMonthOfYear(selectedMonth).dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            endOfMonth = new DateTime().withMonthOfYear(selectedMonth).dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue();
        }

        DateTime startOfYear;
        DateTime endOfYear;
        if (selectedYear == null){
            startOfYear = new DateTime().monthOfYear().withMinimumValue().dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            endOfYear = new DateTime();
        }else{
            startOfYear = new DateTime().withYear(selectedYear).monthOfYear().withMinimumValue().dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            endOfYear = new DateTime().withYear(selectedYear).monthOfYear().withMaximumValue().dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue();
        }

        List<Schedule> monthAppointments = scheduleFacade.findByEmployeeAndDateBetween(authorizedUser, startOfMonth.toDate(), endOfMonth.toDate());

        List<Schedule> yearAppointments = scheduleFacade.findByEmployeeAndDateBetween(authorizedUser, startOfYear.toDate(), endOfYear.toDate());

        appointmentsCountMonth = monthAppointments.size();

        appointmentsCountYear = yearAppointments.size();

        if(getWorkingDays(monthAppointments) == 0){
            averageAppointmentsCountPerDay = 0;
        }else {
            averageAppointmentsCountPerDay = (monthAppointments.size() / getWorkingDays(monthAppointments));
        }

        if(getWorkingDays(yearAppointments) == 0) {
            averageAppointmentsCountPerMonth = 0;
        }else{
            averageAppointmentsCountPerMonth = (yearAppointments.size() / getWorkingDays(yearAppointments));
        }
        maxAppointmentsPerDay = getMaxAppointmentsPerDay(monthAppointments);

        maxAppointmentsPerMonth = getMaxAppointmentsPerMonth(yearAppointments);

        minAppointmentsPerDay = getMinAppointmentsPerDay(monthAppointments);

        minAppointmentsPerMonth = getMinAppointmentsPerMonth(yearAppointments);

        workingDaysInMonth = getWorkingDays(monthAppointments);

        workingDaysInYear = getWorkingDays(yearAppointments);

        //TODO Salary statistics
        //monthSalarySum;

        //yearSalarySum;

        //averageDaySalary;

        //averageMonthSalary;

        //minDaySalary;

        //maxDaySalary;

        //minMonthSalary;

        //maxMonthSalary;

    }
    // TODO move logic to ejb
    private Integer getWorkingDays(List<Schedule> appointments){
        HashSet<Integer> daysOfTheYear = new HashSet<>();
        for(Schedule schedule : appointments){
            daysOfTheYear.add(new DateTime(schedule.getDateTimeStart()).getDayOfYear());
        }
        return daysOfTheYear.size();
    }

    private Integer getMinAppointmentsPerDay(List<Schedule> appointments){
        HashMap<Integer, Integer> appointmentPerDay = new HashMap<>();
        for(Schedule schedule : appointments){
            Integer count = appointmentPerDay.get(new DateTime(schedule.getDateTimeStart()).getDayOfYear());
            if (count == null) count = 0;
            appointmentPerDay.put(new DateTime(schedule.getDateTimeStart()).getDayOfYear()
                    , count + 1);
        }
        if(appointmentPerDay.size() == 0){
            return 0;
        }else {
            return new TreeSet<>(appointmentPerDay.values()).first();
        }
    }

    private Integer getMaxAppointmentsPerDay(List<Schedule> appointments){
        HashMap<Integer, Integer> appointmentPerDay = new HashMap<>();
        for(Schedule schedule : appointments){
            Integer count = appointmentPerDay.get(new DateTime(schedule.getDateTimeStart()).getDayOfYear());
            if (count == null) count = 0;
            appointmentPerDay.put(new DateTime(schedule.getDateTimeStart()).getDayOfYear()
                    , count + 1);
        }
        if(appointmentPerDay.size() == 0){
            return 0;
        }else {
            return new TreeSet<>(appointmentPerDay.values()).last();
        }
    }

    private Integer getMinAppointmentsPerMonth(List<Schedule> appointments){
        HashMap<Integer, Integer> appointmentPerMonth = new HashMap<>();
        for(Schedule schedule : appointments){
            Integer count = appointmentPerMonth.get(monthNames.get("CALENDAR_MONTH_" + new DateTime(schedule.getDateTimeStart()).getMonthOfYear()));
            if (count == null) count = 0;
            appointmentPerMonth.put(monthNames.get(new DateTime(schedule.getDateTimeStart()).getMonthOfYear())
                    , count + 1);
        }
        if(appointmentPerMonth.size() == 0){
            return 0;
        }else {
            return new TreeSet<>(appointmentPerMonth.values()).first();
        }
    }

    private Integer getMaxAppointmentsPerMonth(List<Schedule> appointments){
        HashMap<Integer, Integer> appointmentPerMonth = new HashMap<>();
        for(Schedule schedule : appointments){
            Integer count = appointmentPerMonth.get(monthNames.get("CALENDAR_MONTH_" + new DateTime(schedule.getDateTimeStart()).getMonthOfYear()));
            if (count == null) count = 0;
            appointmentPerMonth.put(monthNames.get(new DateTime(schedule.getDateTimeStart()).getMonthOfYear())
                    , count + 1);
        }
        if(appointmentPerMonth.size() == 0){
            return 0;
        }else {
            return new TreeSet<>(appointmentPerMonth.values()).last();
        }
    }

    private float getSalarySum(List<Schedule> appointments){
        float salarySum = 0f; //Sum of payments
        for(Schedule schedule : appointments) {
            Payments payment = paymentsFacade.findBySchedule(schedule);
            if(payment != null) {
                salarySum +=payment.getTotal();
            }
        }
        return salarySum;
    }

    private void populateMonthNames(){
        monthNames.put(CALENDAR_MONTH_1.toString(), 1);
        monthNames.put(CALENDAR_MONTH_2.toString(), 2);
        monthNames.put(CALENDAR_MONTH_3.toString(), 3);
        monthNames.put(CALENDAR_MONTH_4.toString(), 4);
        monthNames.put(CALENDAR_MONTH_5.toString(), 5);
        monthNames.put(CALENDAR_MONTH_6.toString(), 6);
        monthNames.put(CALENDAR_MONTH_7.toString(), 7);
        monthNames.put(CALENDAR_MONTH_8.toString(), 8);
        monthNames.put(CALENDAR_MONTH_9.toString(), 9);
        monthNames.put(CALENDAR_MONTH_10.toString(), 10);
        monthNames.put(CALENDAR_MONTH_11.toString(), 11);
        monthNames.put(CALENDAR_MONTH_12.toString(), 12);
    }
    private void populateYears(){
        //HashSet<String> years = new HashSet();
        for( Schedule schedule : scheduleFacade.findByEmployeeAndDateBetween(authorizedUser, new DateTime().minusYears(100).toDate(),new Date())){
            years.add(new DateTime(schedule.getDateTimeStart()).getYear());
        }
    }

    public LineChartModel getMonthAppointments() {
        return monthAppointments;
    }

    public void setMonthAppointments(LineChartModel monthAppointments) {
        this.monthAppointments = monthAppointments;
    }

    public Map<String,Integer> getMonthNames() {
        return monthNames;
    }

    public void setMonthNames(Map<String,Integer> monthNames) {
        this.monthNames = monthNames;
    }

    public LineChartModel getMonthSalary() {
        return monthSalary;
    }

    public void setMonthSalary(LineChartModel monthSalary) {
        this.monthSalary = monthSalary;
    }

    public Integer getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(Integer selectedYear) {
        this.selectedYear = selectedYear;
    }

    public LineChartModel getYearAppointments() {
        return yearAppointments;
    }

    public void setYearAppointments(LineChartModel yearAppointments) {
        this.yearAppointments = yearAppointments;
    }

    public HashSet<Integer> getYears() {
        return years;
    }

    public void setYears(HashSet<Integer> years) {
        this.years = years;
    }

    public LineChartModel getYearSalary() {
        return yearSalary;
    }

    public void setYearSalary(LineChartModel yearSalary) {
        this.yearSalary = yearSalary;
    }

    public DashboardModel getDashboard() {
        return dashboard;
    }

    public void setDashboard(DashboardModel dashboard) {
        this.dashboard = dashboard;
    }

    public Integer getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(Integer selectedMonth) {
        this.selectedMonth = selectedMonth;
    }

    public Integer getAppointmentsCountMonth() {
        return appointmentsCountMonth;
    }

    public void setAppointmentsCountMonth(Integer appointmentsCountMonth) {
        this.appointmentsCountMonth = appointmentsCountMonth;
    }

    public Integer getAppointmentsCountYear() {
        return appointmentsCountYear;
    }

    public void setAppointmentsCountYear(Integer appointmentsCountYear) {
        this.appointmentsCountYear = appointmentsCountYear;
    }

    public Integer getAverageAppointmentsCountPerDay() {
        return averageAppointmentsCountPerDay;
    }

    public void setAverageAppointmentsCountPerDay(Integer averageAppointmentsCountPerDay) {
        this.averageAppointmentsCountPerDay = averageAppointmentsCountPerDay;
    }

    public Integer getAverageAppointmentsCountPerMonth() {
        return averageAppointmentsCountPerMonth;
    }

    public void setAverageAppointmentsCountPerMonth(Integer averageAppointmentsCountPerMonth) {
        this.averageAppointmentsCountPerMonth = averageAppointmentsCountPerMonth;
    }

    public Float getAverageDaySalary() {
        return averageDaySalary;
    }

    public void setAverageDaySalary(Float averageDaySalary) {
        this.averageDaySalary = averageDaySalary;
    }

    public Float getAverageMonthSalary() {
        return averageMonthSalary;
    }

    public void setAverageMonthSalary(Float averageMonthSalary) {
        this.averageMonthSalary = averageMonthSalary;
    }

    public Integer getMaxAppointmentsPerDay() {
        return maxAppointmentsPerDay;
    }

    public void setMaxAppointmentsPerDay(Integer maxAppointmentsPerDay) {
        this.maxAppointmentsPerDay = maxAppointmentsPerDay;
    }

    public Integer getMaxAppointmentsPerMonth() {
        return maxAppointmentsPerMonth;
    }

    public void setMaxAppointmentsPerMonth(Integer maxAppointmentsPerMonth) {
        this.maxAppointmentsPerMonth = maxAppointmentsPerMonth;
    }

    public Float getMaxDaySalary() {
        return maxDaySalary;
    }

    public void setMaxDaySalary(Float maxDaySalary) {
        this.maxDaySalary = maxDaySalary;
    }

    public Float getMaxMonthSalary() {
        return maxMonthSalary;
    }

    public void setMaxMonthSalary(Float maxMonthSalary) {
        this.maxMonthSalary = maxMonthSalary;
    }

    public Integer getMinAppointmentsPerDay() {
        return minAppointmentsPerDay;
    }

    public void setMinAppointmentsPerDay(Integer minAppointmentsPerDay) {
        this.minAppointmentsPerDay = minAppointmentsPerDay;
    }

    public Integer getMinAppointmentsPerMonth() {
        return minAppointmentsPerMonth;
    }

    public void setMinAppointmentsPerMonth(Integer minAppointmentsPerMonth) {
        this.minAppointmentsPerMonth = minAppointmentsPerMonth;
    }

    public Float getMinDaySalary() {
        return minDaySalary;
    }

    public void setMinDaySalary(Float minDaySalary) {
        this.minDaySalary = minDaySalary;
    }

    public Float getMinMonthSalary() {
        return minMonthSalary;
    }

    public void setMinMonthSalary(Float minMonthSalary) {
        this.minMonthSalary = minMonthSalary;
    }

    public Float getMonthSalarySum() {
        return monthSalarySum;
    }

    public void setMonthSalarySum(Float monthSalarySum) {
        this.monthSalarySum = monthSalarySum;
    }

    public Integer getWorkingDaysInMonth() {
        return workingDaysInMonth;
    }

    public void setWorkingDaysInMonth(Integer workingDaysInMonth) {
        this.workingDaysInMonth = workingDaysInMonth;
    }

    public Integer getWorkingDaysInYear() {
        return workingDaysInYear;
    }

    public void setWorkingDaysInYear(Integer workingDaysInYear) {
        this.workingDaysInYear = workingDaysInYear;
    }

    public Float getYearSalarySum() {
        return yearSalarySum;
    }

    public void setYearSalarySum(Float yearSalarySum) {
        this.yearSalarySum = yearSalarySum;
    }
}
