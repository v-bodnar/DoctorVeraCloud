package ua.kiev.doctorvera.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.primefaces.model.chart.*;
import ua.kiev.doctorvera.entities.Schedule;
import ua.kiev.doctorvera.resources.Message;

import java.util.*;

/**
 * Created by volodymyr.bodnar on 3/17/2016.
 */
public class StatisticsHelper {

    public final static Map<String, Integer> monthNames = populateMonthNames();
    private final static DateTimeFormatter pattern = DateTimeFormat.forPattern("yyy-MM-dd");

    public static LineChartModel populateYearSalary(DateTime startDate, DateTime endDate, Map<DateTime, Float> salaryData){

        LineChartModel yearSalary = new LineChartModel();
        ChartSeries salary = new ChartSeries();
        salary.setLabel(Message.getMessage("INDEX_YEAR_SALARY"));

        for(DateTime date = startDate; date.isBefore(endDate); date = date.plusDays(1)){
            salary.set(date.toString(pattern), salaryData.get(date) == null ? 0 : salaryData.get(date));
        }

        yearSalary.addSeries(salary);
        yearSalary.setTitle(Message.getMessage("INDEX_YEAR_SALARY_CUR"));
        yearSalary.setLegendPosition("ne");
        yearSalary.setAnimate(true);
        yearSalary.setZoom(true);
        yearSalary.setSeriesColors("459e00");

        DateAxis axis = new DateAxis(Message.getMessage("INDEX_MONTH_OF_YEAR"));
        axis.setTickAngle(-50);
        yearSalary.getAxes().put(AxisType.X, axis);

        Axis yAxis = yearSalary.getAxis(AxisType.Y);
        yAxis.setLabel(Message.getMessage("INDEX_YEAR_SALARY_CUR"));

        return yearSalary;
    }

    public static LineChartModel populateMonthAppointments(DateTime startDate, DateTime endDate, Map<DateTime, Integer> salaryData){
        LineChartModel monthAppointments = new LineChartModel();
        ChartSeries appointments = new ChartSeries();
        appointments.setLabel(Message.getMessage("INDEX_APPOINTMENTS_COUNT"));

        for(DateTime dayOfMonth = startDate; dayOfMonth.isBefore(endDate); dayOfMonth = dayOfMonth.plusDays(1)){
            appointments.set(dayOfMonth.toString(pattern), salaryData.get(dayOfMonth) == null ? 0 : salaryData.get(dayOfMonth));
        }

        monthAppointments.addSeries(appointments);
        monthAppointments.setTitle(Message.getMessage("INDEX_APPOINTMENTS_COUNT"));
        monthAppointments.setLegendPosition("ne");
        monthAppointments.setAnimate(true);
        monthAppointments.setZoom(true);
        monthAppointments.setSeriesColors("459e00");

        DateAxis axis = new DateAxis(Message.getMessage("INDEX_DAY_OF_MONTH"));
        axis.setTickAngle(-50);
        monthAppointments.getAxes().put(AxisType.X, axis);

        Axis yAxis = monthAppointments.getAxis(AxisType.Y);
        yAxis.setLabel(Message.getMessage("INDEX_APPOINTMENTS_COUNT"));
        return monthAppointments;
    }

    public static LineChartModel populateMonthSalary(DateTime startDate, DateTime endDate, Map<DateTime, Float> salaryData){
        LineChartModel monthSalary = new LineChartModel();
        ChartSeries salary = new ChartSeries();
        salary.setLabel(Message.getMessage("INDEX_MONTH_SALARY"));

        for(DateTime date = startDate; date.isBefore(endDate); date = date.plusDays(1)){
            salary.set(date.toString(pattern), salaryData.get(date) == null ? 0 : salaryData.get(date));
        }

        monthSalary.addSeries(salary);
        monthSalary.setTitle(Message.getMessage("INDEX_MONTH_SALARY_CUR"));
        monthSalary.setLegendPosition("ne");
        monthSalary.setAnimate(true);
        monthSalary.setZoom(true);
        monthSalary.setSeriesColors("459e00");

        DateAxis axis = new DateAxis(Message.getMessage("INDEX_DAY_OF_MONTH"));
        axis.setTickAngle(-50);
        monthSalary.getAxes().put(AxisType.X, axis);

        Axis yAxis = monthSalary.getAxis(AxisType.Y);
        yAxis.setLabel(Message.getMessage("INDEX_MONTH_SALARY_CUR"));

        return monthSalary;
    }

    public static LineChartModel populateYearAppointments(DateTime startDate, DateTime endDate, Map<DateTime, Integer> appointmentData){
        LineChartModel yearAppointments = new LineChartModel();
        ChartSeries appointments = new ChartSeries();
        appointments.setLabel(Message.getMessage("INDEX_APPOINTMENTS_COUNT"));

        for(DateTime monthOfYear = startDate; monthOfYear.isBefore(endDate); monthOfYear = monthOfYear.plusDays(1)){
            appointments.set(monthOfYear.toString(pattern), appointmentData.get(monthOfYear) == null ? 0 : appointmentData.get(monthOfYear));
        }

        yearAppointments.addSeries(appointments);
        yearAppointments.setTitle(Message.getMessage("INDEX_APPOINTMENTS_COUNT"));
        yearAppointments.setLegendPosition("ne");
        yearAppointments.setAnimate(true);
        yearAppointments.setZoom(true);
        yearAppointments.setSeriesColors("459e00");

        DateAxis axis = new DateAxis(Message.getMessage("INDEX_MONTH_OF_YEAR"));
        axis.setTickAngle(-50);
        yearAppointments.getAxes().put(AxisType.X, axis);

        Axis yAxis = yearAppointments.getAxis(AxisType.Y);
        yAxis.setLabel(Message.getMessage("INDEX_APPOINTMENTS_COUNT"));

        return  yearAppointments;
    }

    public static Integer getWorkingDays(List<Schedule> appointments){
        HashSet<Integer> daysOfTheYear = new HashSet<>();
        for(Schedule schedule : appointments){
            daysOfTheYear.add(new DateTime(schedule.getDateTimeStart()).getDayOfYear());
        }
        return daysOfTheYear.size();
    }

    public static Integer getMinAppointmentsPerDay(List<Schedule> appointments){
        HashMap<Integer, Integer> appointmentPerDay = new HashMap<>();
        Integer count;
        for(Schedule schedule : appointments){
            count = appointmentPerDay.get(new DateTime(schedule.getDateTimeStart()).getDayOfYear());
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

    public static Integer getMaxAppointmentsPerDay(List<Schedule> appointments){
        HashMap<Integer, Integer> appointmentPerDay = new HashMap<>();
        Integer count;
        for(Schedule schedule : appointments){
            count = appointmentPerDay.get(new DateTime(schedule.getDateTimeStart()).getDayOfYear());
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

    public static Integer getMinAppointmentsPerMonth(List<Schedule> appointments){
        HashMap<Integer, Integer> appointmentPerMonth = new HashMap<>();
        Integer count;
        for(Schedule schedule : appointments){
            count = appointmentPerMonth.get(monthNames.get("CALENDAR_MONTH_" + new DateTime(schedule.getDateTimeStart()).getMonthOfYear()));
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

    public static Integer getMaxAppointmentsPerMonth(List<Schedule> appointments){
        HashMap<Integer, Integer> appointmentPerMonth = new HashMap<>();
        Integer count;
        for(Schedule schedule : appointments){
            count = appointmentPerMonth.get(monthNames.get("CALENDAR_MONTH_" + new DateTime(schedule.getDateTimeStart()).getMonthOfYear()));
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

    public static Map<String, Integer> populateMonthNames(){
        Map<String, Integer> monthNames = new LinkedHashMap<>();
        monthNames.put(Message.getMessage("CALENDAR_MONTH_1"), 1);
        monthNames.put(Message.getMessage("CALENDAR_MONTH_2").toString(), 2);
        monthNames.put(Message.getMessage("CALENDAR_MONTH_3").toString(), 3);
        monthNames.put(Message.getMessage("CALENDAR_MONTH_4").toString(), 4);
        monthNames.put(Message.getMessage("CALENDAR_MONTH_5").toString(), 5);
        monthNames.put(Message.getMessage("CALENDAR_MONTH_6").toString(), 6);
        monthNames.put(Message.getMessage("CALENDAR_MONTH_7").toString(), 7);
        monthNames.put(Message.getMessage("CALENDAR_MONTH_8").toString(), 8);
        monthNames.put(Message.getMessage("CALENDAR_MONTH_9").toString(), 9);
        monthNames.put(Message.getMessage("CALENDAR_MONTH_10").toString(), 10);
        monthNames.put(Message.getMessage("CALENDAR_MONTH_11").toString(), 11);
        monthNames.put(Message.getMessage("CALENDAR_MONTH_12").toString(), 12);
        return monthNames;
    }
}
