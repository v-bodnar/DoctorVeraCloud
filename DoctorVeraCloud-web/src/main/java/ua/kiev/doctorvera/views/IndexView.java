package ua.kiev.doctorvera.views;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.chart.*;
import ua.kiev.doctorvera.entities.Schedule;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.PaymentsFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.ScheduleFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.ShareFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.resources.Message;
import ua.kiev.doctorvera.utils.StatisticsHelper;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

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

    @EJB
    private UsersFacadeLocal usersFacade;

    @EJB
    private ShareFacadeLocal shareFacade;

    private Users authorizedUser;

    private Users selectedUser;

    ShareFacadeLocal.Part function;

    private DashboardModel dashboard;

    private LineChartModel  monthAppointments;

    private LineChartModel  yearAppointments;

    private LineChartModel  monthSalary;

    private LineChartModel  yearSalary;

    //For Month Picker
    private final static Map<String,Integer> monthNames = StatisticsHelper.populateMonthNames();

    private final static DateTimeFormatter pattern = DateTimeFormat.forPattern("yyy-MM-dd");
    //For Month Picker
    private Integer selectedMonth;
    //For Year Picker
    private HashSet<Integer> years = new HashSet<>();
    //For Year Picker
    private Integer selectedYear;

    private Integer appointmentsCountMonth;

    private Integer appointmentsCountYear;

    private Float averageAppointmentsCountPerDay = 0f;

    private Float averageAppointmentsCountPerMonth = 0f;

    private Integer maxAppointmentsPerDay;

    private Integer maxAppointmentsPerMonth;

    private Integer minAppointmentsPerDay;

    private Integer minAppointmentsPerMonth;

    private Integer workingDaysInMonth;

    private Integer workingDaysInYear;

    private Float monthSalarySum = 0f;

    private Float yearSalarySum = 0f;

    private Float averageDaySalary = 0f;

    private Float averageMonthSalary = 0f;

    private Float minDaySalary = 0f;

    private Float maxDaySalary = 0f;

    private Float minMonthSalary = 0f;

    private Float maxMonthSalary = 0f;

    private String currentWeather = "";


    @PostConstruct
    public void init() {
        authorizedUser = sessionParams.getAuthorizedUser();
        if(usersFacade.isDoctor(authorizedUser)){
            function = ShareFacadeLocal.Part.DOCTOR;
        }else if(usersFacade.isAssistant(authorizedUser)){
            function = ShareFacadeLocal.Part.ASSISTANT;
        }

        dashboard = new DefaultDashboardModel();
        DashboardColumn column = new DefaultDashboardColumn();

        column.addWidget("monthAppointments");
        column.addWidget("monthSalary");
        column.addWidget("yearAppointments");
        column.addWidget("yearSalary");
        column.addWidget("weather");

        dashboard.addColumn(column);
        populateMonthStatistics();
        populateYearStatistics();
        populateYears();
        retrieveWeather();
    }

    public void populateMonthStatistics(){
        Users user = selectedUser == null ? authorizedUser : selectedUser;

        DateTime startOfMonth, endOfMonth;
        if (selectedMonth == null){
            startOfMonth = new DateTime().dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            endOfMonth = new DateTime();
        }else{
            startOfMonth = new DateTime().withMonthOfYear(selectedMonth).dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            endOfMonth = new DateTime().withMonthOfYear(selectedMonth).dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue();
        }

        List<Schedule> monthScheduleList;

        Map<Schedule, Map<String, Float>> salaryData = null;
        try {
            if(isDoctor() || isAssistant()) {
                monthScheduleList = scheduleFacade.findPayedByEmployeeAndDateBetween(user, startOfMonth.toDate(), endOfMonth.toDate());
                salaryData = shareFacade.findFinancialDataOnScheduleList(monthScheduleList);
            }else{
                salaryData = new HashMap<>();
                monthScheduleList = new LinkedList<>();
            }
        } catch (ShareFacadeLocal.ShareNotFoundException e) {
            salaryData = new HashMap<>();
            monthScheduleList = new LinkedList<>();
            Message.showErrorInDialog(e.getMessage());
        }

        Map<DateTime, Integer> thisMonthAppointments = new HashMap();
        Map<DateTime, Float> thisMonthSalary = new HashMap();
        for(Schedule schedule : monthScheduleList) {
            DateTime oneDay = new DateTime (schedule.getDateTimeStart()).dayOfYear().getDateTime().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
            if(thisMonthAppointments.get(oneDay) == null){
                thisMonthAppointments.put(oneDay, 1);
                thisMonthSalary.put(oneDay, salaryData.get(schedule).get(function.name()));
            }else{
                thisMonthAppointments.put(oneDay, thisMonthAppointments.get(oneDay) + 1);
                thisMonthSalary.put(oneDay, thisMonthSalary.get(oneDay) + salaryData.get(schedule).get(function.name()));
            }
        }
        monthAppointments = StatisticsHelper.populateMonthAppointments(startOfMonth, endOfMonth, thisMonthAppointments);
        monthSalary = StatisticsHelper.populateYearSalary(startOfMonth, endOfMonth, thisMonthSalary);
        populateMonthStatistics(monthScheduleList, salaryData);
    }

    private void populateMonthStatistics(List<Schedule> monthAppointments, Map<Schedule, Map<String, Float>> monthSalaryData){
        appointmentsCountMonth = monthAppointments.size();

        if(StatisticsHelper.getWorkingDays(monthAppointments) != 0){
            averageAppointmentsCountPerDay = ((float)monthAppointments.size() / (float)StatisticsHelper.getWorkingDays(monthAppointments));
        }
        maxAppointmentsPerDay = StatisticsHelper.getMaxAppointmentsPerDay(monthAppointments);
        minAppointmentsPerDay = StatisticsHelper.getMinAppointmentsPerDay(monthAppointments);
        for(Schedule schedule : monthAppointments){
            monthSalarySum += monthSalaryData.get(schedule).get(function.name());
            if (maxDaySalary < monthSalaryData.get(schedule).get(function.name()))
                maxDaySalary= monthSalaryData.get(schedule).get(function.name());
        }
        minDaySalary = maxDaySalary;
        for(Schedule schedule : monthAppointments){
            if (minDaySalary > monthSalaryData.get(schedule).get(function.name()) && monthSalaryData.get(schedule).get(function.name()) != 0)
                minDaySalary = monthSalaryData.get(schedule).get(function.name());
        }
        workingDaysInMonth = StatisticsHelper.getWorkingDays(monthAppointments);
        averageDaySalary = monthSalarySum / workingDaysInMonth;
    }

    public void populateYearStatistics() {
        Users user = selectedUser == null ? authorizedUser : selectedUser;

        DateTime startOfYear, endOfYear;
        if (selectedYear == null){
            startOfYear = new DateTime().monthOfYear().withMinimumValue().dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            endOfYear = new DateTime();
        }else{
            startOfYear = new DateTime().withYear(selectedYear).monthOfYear().withMinimumValue().dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            endOfYear = new DateTime().withYear(selectedYear).monthOfYear().withMaximumValue().dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue();
        }

        List<Schedule> appointments;
        Map<Schedule, Map<String, Float>> salaryData = null;
        try {
            if(isDoctor() || isAssistant()) {
                appointments = scheduleFacade.findPayedByEmployeeAndDateBetween(user, startOfYear.toDate(), endOfYear.toDate());
                salaryData = shareFacade.findFinancialDataOnScheduleList(appointments);
            }else{
                salaryData = new HashMap<>();
                appointments = new LinkedList<>();
            }
        } catch (ShareFacadeLocal.ShareNotFoundException e) {
            salaryData = new HashMap<>();
            appointments = new LinkedList<>();
            Message.showErrorInDialog(e.getMessage());
        }

        Map<DateTime, Float> thisYearSalary = new HashMap();
        Map<DateTime, Integer> thisYearAppointments = new HashMap();
        for(Schedule schedule : appointments) {
            DateTime oneMonth = new DateTime (schedule.getDateTimeStart()).monthOfYear().getDateTime().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
            if(thisYearAppointments.get(oneMonth) == null){
                thisYearAppointments.put(oneMonth, 1);
                thisYearSalary.put(oneMonth, salaryData.get(schedule).get(function.name()));
            }else{
                thisYearAppointments.put(oneMonth, thisYearAppointments.get(oneMonth) + 1);
                thisYearSalary.put(oneMonth, thisYearSalary.get(oneMonth) + salaryData.get(schedule).get(function.name()));
            }
        }
        yearAppointments = StatisticsHelper.populateYearAppointments(startOfYear, endOfYear, thisYearAppointments);
        yearSalary = StatisticsHelper.populateYearSalary(startOfYear, endOfYear, thisYearSalary);
        populateYearStatistics(appointments, salaryData);
    }

    private void populateYearStatistics(List<Schedule> yearAppointments, Map<Schedule, Map<String, Float>> yearSalaryData){
        appointmentsCountYear = yearAppointments.size();

        if(StatisticsHelper.getWorkingDays(yearAppointments) != 0) {
            averageAppointmentsCountPerMonth = ((float)yearAppointments.size() / (float)StatisticsHelper.getWorkingDays(yearAppointments));
        }
        maxAppointmentsPerMonth = StatisticsHelper.getMaxAppointmentsPerMonth(yearAppointments);

        minAppointmentsPerMonth = StatisticsHelper.getMinAppointmentsPerMonth(yearAppointments);

        workingDaysInYear = StatisticsHelper.getWorkingDays(yearAppointments);

        for(Schedule schedule : yearAppointments){
            yearSalarySum += yearSalaryData.get(schedule).get(function.name());
            if (maxMonthSalary < yearSalaryData.get(schedule).get(function.name()))
                maxMonthSalary = yearSalaryData.get(schedule).get(function.name());

        }
        minMonthSalary = maxMonthSalary;
        for(Schedule schedule : yearAppointments){
            if (minMonthSalary > yearSalaryData.get(schedule).get(function.name()) && yearSalaryData.get(schedule).get(function.name()) != 0)
                minMonthSalary = yearSalaryData.get(schedule).get(function.name());
        }

        averageMonthSalary = yearSalarySum / workingDaysInYear;
    }

    private void populateYears(){
        for( Schedule schedule : scheduleFacade.findPayedByEmployeeAndDateBetween(authorizedUser, new DateTime().minusYears(20).toDate(),new Date())){
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

    public UsersFacadeLocal getUsersFacade() {
        return usersFacade;
    }

    public void setUsersFacade(UsersFacadeLocal usersFacade) {
        this.usersFacade = usersFacade;
    }

    public void retrieveWeather() {
        try {
            URL feedSource = new URL("http://weather.yahooapis.com/forecastrss?w=20070188&u=c&lang=" + sessionParams.getCurrentLocale().getLanguage());
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedSource));
            String value = ((SyndEntry) feed.getEntries().get(0)).getDescription().getValue();

            currentWeather = value.split("<a href")[0];
        } catch (Exception e) {
            LOG.severe("Unable to retrieve weather forecast at the moment.");
            LOG.severe(e.getMessage());
        }
    }

    public String getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(String currentWeather) {
        this.currentWeather = currentWeather;
    }

    public boolean isDoctor(){
        return function == ShareFacadeLocal.Part.DOCTOR;
    }

    public boolean isAssistant(){
        return function == ShareFacadeLocal.Part.ASSISTANT;
    }
}
