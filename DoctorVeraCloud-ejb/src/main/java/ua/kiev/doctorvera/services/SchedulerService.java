package ua.kiev.doctorvera.services;

import org.joda.time.DateTime;
import ua.kiev.doctorvera.entities.*;
import ua.kiev.doctorvera.entities.Schedule;
import ua.kiev.doctorvera.facadeLocal.*;
import ua.kiev.doctorvera.resources.Config;
import ua.kiev.doctorvera.resources.Message;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.ejb.Timer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by volodymyr.bodnar on 1/9/2016.
 */
@Singleton
@Startup
@DependsOn({"StartUpBean"})
public class SchedulerService implements ScheduleServiceLocal {

    @Resource
    private TimerService timerService;

    @EJB
    private MailServiceLocal mailService;

    @EJB
    private SMSServiceLocal smsService;

    @EJB
    private ScheduleFacadeLocal scheduleFacade;

    @EJB
    private MessageSchedulerFacadeLocal messageSchedulerFacade;

    @EJB
    private MessageTemplateFacadeLocal messageTemplateFacade;

    @EJB
    private PricesFacadeLocal pricesFacade;

    @EJB
    private UsersFacadeLocal usersFacade;

    @EJB
    private TransactionLogFacadeLocal transactionLogFacade;

    private final static String MESSAGE_SCHEDULER_PREFIX = "MESSAGE_SCHEDULER_";
    private final static String SCHEDULE_PREFIX = "SCHEDULE_";
    private final static String DOCTORS_NOTIFICATION = "DOCTORS_NOTIFICATION";
    private final static Logger LOG = Logger.getLogger(SchedulerService.class.getName());

    @PostConstruct
    public void init() {
        scheduleSendNotificationForDoctors();
    }

    /**
     * Schedules new timer for sending message scheduled in MDS
     *
     * @param scheduler data for message sending
     */
    @Override
    public void scheduleEvent(MessageScheduler scheduler) {
        System.out.println(new Date());
        int hour = new DateTime(scheduler.getTime()).getHourOfDay();
        int minute = new DateTime(scheduler.getTime()).getMinuteOfHour();
        String daysOfWeek = "*";
        for (int i = 0; i < scheduler.getDaysOfWeek().size(); i++) {
            if (i == 0) daysOfWeek = "";
            daysOfWeek += "" + (scheduler.getDaysOfWeek().get(i).ordinal() + 1);
            if (i != scheduler.getDaysOfWeek().size() - 1)
                daysOfWeek += ",";
        }

        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setInfo(MESSAGE_SCHEDULER_PREFIX + scheduler.getId());

        ScheduleExpression scheduleExpression = new ScheduleExpression();
        scheduleExpression.start(scheduler.getDateStart()).end(scheduler.getDateEnd()).hour(hour).minute(minute).dayOfWeek(daysOfWeek);

        Timer timer = timerService.createCalendarTimer(scheduleExpression, timerConfig);
        try{
            LOG.info("Created new timer, next execution: " + timer.getNextTimeout() + " in " + timer.getTimeRemaining() / 60000 + " minutes");
        }catch (NoMoreTimeoutsException e){
            LOG.info("Scheduled event will not be executed, execution date is in the past");
        }
    }

    /**
     * Removes timer for sending message scheduled in MDS and schedules new one
     *
     * @param scheduler data for message sending
     */
    @Override
    public void changeEvent(MessageScheduler scheduler) {
        removeEvent(scheduler);
        scheduleEvent(scheduler);
    }

    /**
     * Removes timer for sending message scheduled in MDS
     *
     * @param scheduler data for message sending
     */
    @Override
    public void removeEvent(MessageScheduler scheduler) {
        try {
            findTimerByInfo(MESSAGE_SCHEDULER_PREFIX + scheduler.getId()).cancel();
        } catch (TimeServiceException e) {
            LOG.severe(e.getMessage());
        }
    }

    /**
     * Schedules new timer for sending message for patients notification
     *
     * @param schedule data for message sending
     */
    @Override
    public void scheduleEvent(Schedule schedule) {
        LOG.info("" + new Date());
        Date deliveryTime = new DateTime(schedule.getDateTimeStart()).minusDays(1).withHourOfDay(14).withMinuteOfHour(0).withSecondOfMinute(0).toDate();

        if (new Date().after(deliveryTime)) {
            sendMessage(schedule);
        } else {
            TimerConfig timerConfig = new TimerConfig();
            timerConfig.setInfo(SCHEDULE_PREFIX + schedule.getId());

            Timer timer = timerService.createSingleActionTimer(deliveryTime, timerConfig);

            System.out.println("Created new single action timer, next execution: " + timer.getNextTimeout() + " in " + timer.getTimeRemaining() / 60000 + " minutes");
        }
    }

    /**
     * Removes timer for sending message for patients notification and creates new one with fresh data
     *
     * @param schedule data for message sending
     */
    @Override
    public void changeEvent(Schedule schedule) {
        LOG.info("" + new Date());
        Date deliveryTime = new DateTime(schedule.getDateTimeStart()).minusDays(1).withHourOfDay(14).withMinuteOfHour(0).withSecondOfMinute(0).toDate();

        try {
            if (new Date().after(deliveryTime)) {
                sendMessage(schedule);
            } else {
                findTimerByInfo(SCHEDULE_PREFIX + schedule.getId()).getSchedule();
            }

        } catch (TimeServiceException e) {
            LOG.severe(e.getMessage());
        }
    }

    /**
     * Removes timer for sending message for patients notification
     *
     * @param schedule data for message sending
     */
    @Override
    public void removeEvent(Schedule schedule) {
        try {
            findTimerByInfo(SCHEDULE_PREFIX + schedule.getId()).cancel();
        } catch (TimeServiceException e) {
            LOG.severe(e.getMessage());
        }
    }

    /**
     * Method removes timer for doctors notification message and schedules it one more time
     */
    @Override
    public void changeDoctorsNotificationDeliveryTime() {
        try {
            findTimerByInfo(DOCTORS_NOTIFICATION).cancel();
        } catch (TimeServiceException e) {
            LOG.severe(e.getMessage());
        }
        scheduleSendNotificationForDoctors();
    }

    /**
     * Helper method
     */
    private Timer findTimerByInfo(String info) throws TimeServiceException {
        for (Timer timer : timerService.getAllTimers()) {
            if (info.equals(timer.getInfo())) return timer;
        }
        throw new TimeServiceException("Timer with name: " + info + "was not found");
    }

    /**
     * This method is executed on any timer expiration
     *
     * @param timer timer that has to be executed
     */
    @Timeout
    public void timeout(Timer timer) {
        // Logic for sending messages
        try {
            System.out.println("" + new Date() + " Execute timer for: " + timer.getInfo() + " next execution: " + timer.getNextTimeout() + " in " + timer.getTimeRemaining() / +60000 + " minutes");
        } catch (NoMoreTimeoutsException e) {
            LOG.info("Timer: " + timer.getInfo() + " expired");
        }
        if (((String) timer.getInfo()).startsWith(MESSAGE_SCHEDULER_PREFIX)) {
            int messageSchedulerId = Integer.parseInt(((String) timer.getInfo()).replace(MESSAGE_SCHEDULER_PREFIX, ""));
            MessageScheduler messageScheduler = messageSchedulerFacade.initializeLazyEntity(messageSchedulerFacade.find(messageSchedulerId));
            sendMessage(messageScheduler);
        } else if (((String) timer.getInfo()).startsWith(SCHEDULE_PREFIX)) {
            int scheduleId = Integer.parseInt(((String) timer.getInfo()).replace(SCHEDULE_PREFIX, ""));
            Schedule schedule = scheduleFacade.initializeLazyEntity(scheduleFacade.find(scheduleId));
            sendMessage(schedule);
        } else if (((String) timer.getInfo()).startsWith(DOCTORS_NOTIFICATION)) {
            sendNotificationForDoctors();
        }
    }

    /**
     * Method sends messages scheduled in MDS
     *
     * @param messageScheduler data about scheduled message delivery
     */
    private void sendMessage(MessageScheduler messageScheduler) {

        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setUserCreated(messageScheduler.getUserCreated());
        transactionLog.setMessageTemplate(messageScheduler.getMessageTemplate());
        transactionLog.setDateCreated(new Date());
        transactionLog.setStatus(TransactionLog.Status.PROGRESS);
        transactionLog.setRecipientsCount(messageScheduler.getUser() != null ? 1 : messageScheduler.getDeliveryGroups().size());
        transactionLogFacade.create(transactionLog);

        if (messageScheduler.getMessageTemplate().getType() == MessageTemplate.Type.EMAIL) {
            //Sending EmailMessage
            if (messageScheduler.getUser() != null) {
                mailService.sendEmail(messageScheduler.getUser(), messageScheduler.getMessageTemplate().getContent(),
                        messageScheduler.getMessageTemplate().getName(), transactionLog);
            } else if (messageScheduler.getDeliveryGroups() != null && !messageScheduler.getDeliveryGroups().isEmpty()) {
                mailService.sendEmail(extractUsersFromDeliveryGroup(messageScheduler.getDeliveryGroups()),
                        messageScheduler.getMessageTemplate().getContent(),
                        messageScheduler.getMessageTemplate().getName(), transactionLog);
            }
        } else if (messageScheduler.getMessageTemplate().getType() == MessageTemplate.Type.SMS) {
            //Sending SMSMessage
            if (messageScheduler.getUser() != null) {
                smsService.sendSMS(messageScheduler.getUser(), messageScheduler.getMessageTemplate().getContent(), transactionLog);
            } else if (messageScheduler.getDeliveryGroups() != null && !messageScheduler.getDeliveryGroups().isEmpty()) {
                smsService.sendSMS(extractUsersFromDeliveryGroup(messageScheduler.getDeliveryGroups()),
                        messageScheduler.getMessageTemplate().getContent(), transactionLog);
            }
        }
    }

    /**
     * Method sends messages Email and SMS to patients after corresponding timer expiration
     *
     * @param schedule schedule record with data about appointment
     */
    private void sendMessage(Schedule schedule) {
        final Integer USERS_BREAK_ID = Integer.parseInt(Config.getMessage("USERS_BREAK_ID"));
        final Integer EMAIL_MESSAGE_TEMPLATE_FOR_SCHEDULE = Integer.parseInt(Config.getMessage("EMAIL_MESSAGE_TEMPLATE_FOR_SCHEDULE"));
        final Integer SMS_MESSAGE_TEMPLATE_FOR_SCHEDULE = Integer.parseInt(Config.getMessage("SMS_MESSAGE_TEMPLATE_FOR_SCHEDULE"));

        if (schedule.getPatient().equals(usersFacade.find(USERS_BREAK_ID)))
            return;

        //retrieving templates, they has to be assigned in configuration
        MessageTemplate emailMessageTemplate = messageTemplateFacade.initializeLazyEntity(messageTemplateFacade.find(EMAIL_MESSAGE_TEMPLATE_FOR_SCHEDULE));
        MessageTemplate smsMessageTemplate = messageTemplateFacade.initializeLazyEntity(messageTemplateFacade.find(SMS_MESSAGE_TEMPLATE_FOR_SCHEDULE));

        //Sending email
        if (emailMessageTemplate == null) {
            LOG.severe("Email message template for patient notification has not been set.");
        } else if (schedule.getPatient() != null) {
            TransactionLog emailTransactionLog = new TransactionLog();
            emailTransactionLog.setUserCreated(schedule.getUserCreated());
            emailTransactionLog.setMessageTemplate(emailMessageTemplate);
            emailTransactionLog.setDateCreated(new Date());
            emailTransactionLog.setStatus(TransactionLog.Status.PROGRESS);
            emailTransactionLog.setRecipientsCount(1);
            emailTransactionLog.setDetails(Message.getMessage("MDS_TRANSACTION_DETAILS_SCHEDULE") + " " + schedule.getId());
            transactionLogFacade.create(emailTransactionLog);

            mailService.sendEmail(schedule.getPatient(),
                    processMessageText(emailMessageTemplate.getContent(), schedule),
                    emailMessageTemplate.getName(), emailTransactionLog);
        } else {
            LOG.severe("Schedule with id: " + schedule.getId() + " does not have a patient assigned");
        }

        //Sending SMS
        if (smsMessageTemplate == null) {
            LOG.severe("SMS message template for patient notification has not been set.");
        } else if (schedule.getPatient() != null) {


            TransactionLog smsTransactionLog = new TransactionLog();
            smsTransactionLog.setUserCreated(schedule.getUserCreated());
            smsTransactionLog.setMessageTemplate(smsMessageTemplate);
            smsTransactionLog.setDateCreated(new Date());
            smsTransactionLog.setStatus(TransactionLog.Status.PROGRESS);
            smsTransactionLog.setRecipientsCount(1);
            smsTransactionLog.setDetails(Message.getMessage("MDS_TRANSACTION_DETAILS_SCHEDULE") + " " + schedule.getId());
            transactionLogFacade.create(smsTransactionLog);

            smsService.sendSMS(schedule.getPatient(), processMessageText(smsMessageTemplate.getContent(), schedule), smsTransactionLog);
        }
    }

    /**
     * Method should be used for message preparation when timers with SCHEDULE_PREFIX execute
     * This method is straightforward resolution of template idea
     * Special marks in text template are replaced by values from Schedule record
     *
     * @param text     template created by user and set in MDS configuration
     * @param schedule schedule record with values for replacement
     * @return text message ready for sending
     */
    private String processMessageText(String text, Schedule schedule) {
        if (schedule.getDateTimeStart() != null)
            text = text.replaceAll("\\$appointmentStartDate", "" + new SimpleDateFormat("yyyy-MM-dd").format(schedule.getDateTimeStart()));
        if (schedule.getDateTimeStart() != null)
            text = text.replaceAll("\\$appointmentStartTime", "" + new SimpleDateFormat("HH:mm").format(schedule.getDateTimeStart()));
        if (schedule.getMethod() != null && schedule.getMethod().getShortName() != null)
            text = text.replaceAll("\\$appointmentMethodName", schedule.getMethod().getShortName());
        if (schedule.getMethod() != null && schedule.getMethod() != null && pricesFacade.findLastPrice(schedule.getMethod()) != null)
            text = text.replaceAll("\\$appointmentMethodPrice", "" + pricesFacade.findLastPrice(schedule.getMethod()).getTotal());
        if (schedule.getDoctor() != null)
            text = text.replaceAll("\\$doctorsFirstName", schedule.getDoctor().getFirstName());
        if (schedule.getDoctor() != null)
            text = text.replaceAll("\\$doctorsLastName", schedule.getDoctor().getLastName());
        if (schedule.getPatient() != null)
            text = text.replaceAll("\\$usersFirstName", schedule.getPatient().getFirstName());
        if (schedule.getPatient() != null)
            text = text.replaceAll("\\$usersLastName", schedule.getPatient().getLastName());
        return text;
    }

    /**
     * Method creates list of users from given list of user groups
     *
     * @param deliveryGroups list of user groups
     * @return list with users of the given groups
     */
    private List<Users> extractUsersFromDeliveryGroup(List<DeliveryGroup> deliveryGroups) {
        Set<Users> users = new HashSet<>();
        for (DeliveryGroup deliveryGroup : deliveryGroups) {
            users.addAll(deliveryGroup.getUsers());
            for (UserGroups userGroup : deliveryGroup.getUserGroups()) {
                users.addAll(userGroup.getUsers());
            }
        }
        return new ArrayList<>(users);
    }

    class TimeServiceException extends Exception {
        TimeServiceException() {
            super();
        }

        TimeServiceException(String message) {
            super(message);
        }
    }

    /**
     * Method creates timer that will be executed every day in the time given in configuration
     * It has to trigger sending notifications for doctors
     */
    private void scheduleSendNotificationForDoctors() {
        String timeString = Config.getMessage("MDS_DOCTORS_NOTIFICATION_TIME");
        if (timeString == null) return;
        String[] time = timeString.split(":");
        LOG.info("" + new Date());

        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setInfo(DOCTORS_NOTIFICATION);
        ScheduleExpression schedule = new ScheduleExpression();
        schedule.hour(time[0]);
        schedule.minute(time[1]);
        Timer timer = timerService.createCalendarTimer(schedule, timerConfig);

        LOG.info("Created new periodical timer, next execution: " + timer.getNextTimeout() + " in " + timer.getTimeRemaining() / 60000 + " minutes");
        LOG.info("Timer will be executed every day at " + timeString);
    }

    /**
     * Method creates and sends email and sms notifications about appointments scheduled on the next day
     */
    private void sendNotificationForDoctors() {
        Boolean isActive = Boolean.parseBoolean(Config.getMessage("MDS_DOCTORS_NOTIFICATION_IS_ACTIVE"));
        if (!isActive) return;

        final Integer USERS_BREAK_ID = Integer.parseInt(Config.getMessage("USERS_BREAK_ID"));
        Users breakUser = usersFacade.find(USERS_BREAK_ID);

        Map<Users, List<Schedule>> schedulesMappedToEmployee = findAllSchedulesForTomorrowMappedByEmployee();
        if(schedulesMappedToEmployee.isEmpty()) return;

        LOG.info("Starting to send notifications for Doctors and Assistants");

        for (Map.Entry<Users, List<Schedule>> entry : schedulesMappedToEmployee.entrySet()) {
            //Sending email
            final Integer EMAIL_MESSAGE_TEMPLATE_FOR_SCHEDULE = Integer.parseInt(Config.getMessage("EMAIL_MESSAGE_TEMPLATE_FOR_DOCTOR"));
            MessageTemplate emailMessageTemplate = messageTemplateFacade.initializeLazyEntity(messageTemplateFacade.find(EMAIL_MESSAGE_TEMPLATE_FOR_SCHEDULE));
            if (emailMessageTemplate == null) {
                LOG.severe("Email message template for patient notification has not been set.");
            } else {
                TransactionLog emailTransactionLog = new TransactionLog();
                emailTransactionLog.setUserCreated(breakUser);
                emailTransactionLog.setMessageTemplate(emailMessageTemplate);
                emailTransactionLog.setDateCreated(new Date());
                emailTransactionLog.setStatus(TransactionLog.Status.PROGRESS);
                transactionLogFacade.create(emailTransactionLog);

                mailService.sendEmail(entry.getKey(),
                        prepareDoctorsNotificationEmail(entry.getValue(), emailMessageTemplate.getContent()),
                        emailMessageTemplate.getName(), emailTransactionLog);
            }

            //Sending sms
            final Integer SMS_MESSAGE_TEMPLATE_FOR_SCHEDULE = Integer.parseInt(Config.getMessage("SMS_MESSAGE_TEMPLATE_FOR_DOCTOR"));
            MessageTemplate smsMessageTemplate = messageTemplateFacade.initializeLazyEntity(messageTemplateFacade.find(SMS_MESSAGE_TEMPLATE_FOR_SCHEDULE));
            if (smsMessageTemplate == null) {
                LOG.severe("SMS message template for patient notification has not been set.");
            } else {


                TransactionLog smsTransactionLog = new TransactionLog();
                smsTransactionLog.setUserCreated(breakUser);
                smsTransactionLog.setMessageTemplate(smsMessageTemplate);
                smsTransactionLog.setDateCreated(new Date());
                smsTransactionLog.setStatus(TransactionLog.Status.PROGRESS);
                transactionLogFacade.create(smsTransactionLog);

                smsService.sendSMS(entry.getKey(),
                        prepareDoctorsNotificationSms(entry.getValue(), smsMessageTemplate.getContent()), smsTransactionLog);
            }
        }

    }

    private Map<Users, List<Schedule>> findAllSchedulesForTomorrowMappedByEmployee() {
        final Integer USERS_BREAK_ID = Integer.parseInt(Config.getMessage("USERS_BREAK_ID"));
        Users breakUser = usersFacade.find(USERS_BREAK_ID);

        Date startSearch = new DateTime().withTimeAtStartOfDay().plusDays(1).toDate();
        Date endSearch = new DateTime().withTimeAtStartOfDay().plusDays(2).toDate();
        List<Schedule> scheduleList = scheduleFacade.initializeLazyEntity(scheduleFacade.findByStartDateBetween(startSearch, endSearch)); // full list of schedules for next day
        Map<Users, List<Schedule>> schedulesMappedToEmployee = new HashMap<>();
        for (Schedule schedule : scheduleList) {
            if (schedule.getPatient().equals(breakUser)) continue; //skip break schedules

            if (schedulesMappedToEmployee.containsKey(schedule.getDoctor())) {
                schedulesMappedToEmployee.get(schedule.getDoctor()).add(schedule);
            } else {
                List<Schedule> doctorsSchedules = new LinkedList<>();
                doctorsSchedules.add(schedule);
                schedulesMappedToEmployee.put(schedule.getDoctor(), doctorsSchedules);
            }
            if (schedule.getAssistant() != null && schedulesMappedToEmployee.containsKey(schedule.getAssistant())) {
                schedulesMappedToEmployee.get(schedule.getAssistant()).add(schedule);
            } else if (schedule.getAssistant() != null) {
                List<Schedule> assistantsSchedules = new LinkedList<>();
                assistantsSchedules.add(schedule);
                schedulesMappedToEmployee.put(schedule.getAssistant(), assistantsSchedules);
            }
        }
        return schedulesMappedToEmployee;
    }

    /**
     * Method should be used for message preparation when timers with DOCTORS_NOTIFICATION execute
     * This method is straightforward resolution of template idea
     * Special marks in template text are replaced by values from Schedule record
     *
     * @param template     template created by user and set in MDS configuration
     * @param scheduleList schedule records with values for replacement
     * @return text message ready for sending
     */
    private String prepareDoctorsNotificationEmail(List<Schedule> scheduleList, String template) {
        Users doctor = scheduleList.get(0).getDoctor();
        if (doctor != null)
            template = template.replaceAll("\\$doctorsFirstName", doctor.getFirstName());
        if (doctor != null)
            template = template.replaceAll("\\$doctorsLastName", doctor.getLastName());
        if (scheduleList != null && !scheduleList.isEmpty()) {
            String scheduleData = "<ol>";
            for (Schedule schedule : scheduleList) {
                scheduleData += "<li>" + new SimpleDateFormat("HH:mm").format(schedule.getDateTimeStart())
                        + " " + schedule.getPatient().getFirstName()
                        + " " + schedule.getPatient().getLastName()
                        + " " + schedule.getPatient().getPhoneNumberMobile()
                        + " " + schedule.getMethod().getShortName()
                        + " " + pricesFacade.findPrice(schedule.getMethod(), schedule.getDateTimeStart()).getTotal()
                        + "</li>";
            }
            scheduleData += "</ol>";
            template = template.replaceAll("\\$scheduleList", scheduleData);
        }
        return template;
    }

    /**
     * Method should be used for message preparation when timers with DOCTORS_NOTIFICATION execute
     * This method is straightforward resolution of template idea
     * Special marks in template text are replaced by values from Schedule record
     *
     * @param template     template created by user and set in MDS configuration
     * @param scheduleList schedule records with values for replacement
     * @return text message ready for sending
     */
    private String prepareDoctorsNotificationSms(List<Schedule> scheduleList, String template) {
        Users doctor = scheduleList.get(0).getDoctor();
        if (doctor != null)
            template = template.replaceAll("\\$doctorsFirstName", doctor.getFirstName());
        if (doctor != null)
            template = template.replaceAll("\\$doctorsLastName", doctor.getLastName());
        if (scheduleList != null && !scheduleList.isEmpty()) {
            String scheduleData = "";
            int i = 0;
            for (Schedule schedule : scheduleList) {
                scheduleData += "" + i + ". "
                        + " " + new SimpleDateFormat("HH:mm").format(schedule.getDateTimeStart())
                        + " - " + new SimpleDateFormat("HH:mm").format(schedule.getDateTimeEnd())
                        + "\n";
                i++;
            }
            template = template.replaceAll("\\$scheduleList", scheduleData);
        }

        return template;
    }

}
