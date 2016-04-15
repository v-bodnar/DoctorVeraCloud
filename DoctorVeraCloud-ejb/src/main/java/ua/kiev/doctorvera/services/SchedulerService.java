package ua.kiev.doctorvera.services;

import org.joda.time.DateTime;
import ua.kiev.doctorvera.entities.*;
import ua.kiev.doctorvera.entities.Schedule;
import ua.kiev.doctorvera.facadeLocal.*;
import ua.kiev.doctorvera.resources.Config;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.ejb.Timer;
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
    TransactionLogFacadeLocal transactionLogFacade;

    private final static String MESSAGE_SCHEDULER_PREFIX = "MESSAGE_SCHEDULER_";
    private final static String SCHEDULE_PREFIX = "SCHEDULE_";
    private final static Logger LOG = Logger.getLogger(SchedulerService.class.getName());

    @Override
    public void scheduleEvent(MessageScheduler scheduler){
        System.out.println(new Date());
        int hour = new DateTime(scheduler.getTime()).getHourOfDay();
        int minute = new DateTime(scheduler.getTime()).getMinuteOfHour();
        String daysOfWeek = "*";
        for(int i = 0; i < scheduler.getDaysOfWeek().size(); i++){
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

        System.out.println("Created new timer, next execution: " + timer.getNextTimeout() + " in " + timer.getTimeRemaining()/60000 + " minutes");
    }
    @Override
    public void changeEvent(MessageScheduler scheduler){
        removeEvent(scheduler);
        scheduleEvent(scheduler);
    }

    @Override
    public void removeEvent(MessageScheduler scheduler) {
        try {
            findTimerByInfo(MESSAGE_SCHEDULER_PREFIX + scheduler.getId()).cancel();
        }catch (TimeServiceException e){
            LOG.severe(e.getMessage());
        }
    }

    @Override
    public void scheduleEvent(Schedule schedule) {
        LOG.info("" + new Date());
        Date deliveryTime = new DateTime(schedule.getDateTimeStart()).minusDays(1).withHourOfDay(14).withMinuteOfHour(0).withSecondOfMinute(0).toDate();

        if(new Date().after(deliveryTime)){
            sendMessage(schedule);
        }else{
            TimerConfig timerConfig = new TimerConfig();
            timerConfig.setInfo(SCHEDULE_PREFIX + schedule.getId());

            Timer timer = timerService.createSingleActionTimer(deliveryTime, timerConfig);

            System.out.println("Created new single action timer, next execution: " + timer.getNextTimeout() + " in " + timer.getTimeRemaining()/60000 + " minutes");
        }
    }

    @Override
    public void changeEvent(Schedule schedule) {
        LOG.info("" + new Date());
        Date deliveryTime = new DateTime(schedule.getDateTimeStart()).minusDays(1).withHourOfDay(14).withMinuteOfHour(0).withSecondOfMinute(0).toDate();

        try {
            if(new Date().after(deliveryTime)){
                sendMessage(schedule);
            }else{
                findTimerByInfo(SCHEDULE_PREFIX + schedule.getId()).getSchedule();
            }

        }catch (TimeServiceException e){
            LOG.severe(e.getMessage());
        }
    }

    @Override
    public void removeEvent(Schedule schedule) {
        try {
        findTimerByInfo(SCHEDULE_PREFIX + schedule.getId()).cancel();
        }catch (TimeServiceException e){
            LOG.severe(e.getMessage());
        }
    }



    private Timer findTimerByInfo(String info) throws TimeServiceException{
        for(Timer timer : timerService.getAllTimers()){
            if(info.equals(timer.getInfo())) return timer;
        }
        throw new TimeServiceException("Timer with name: " + info + "was not found");
    }

    @Timeout
    public void timeout(Timer timer) {
        // Logic for sending messages
        try {
            System.out.println("" + new Date() + "Execute timer for: " + timer.getInfo() + "next execution: " + timer.getNextTimeout() + "in " + timer.getTimeRemaining() / +60000 + "minutes");
        }catch (NoMoreTimeoutsException e){
            LOG.info("Timer: " + timer.getInfo() + " expired");
        }
        if(((String)timer.getInfo()).startsWith(MESSAGE_SCHEDULER_PREFIX)){
            int messageSchedulerId = Integer.parseInt(((String)timer.getInfo()).replace(MESSAGE_SCHEDULER_PREFIX,""));
            MessageScheduler messageScheduler = messageSchedulerFacade.initializeLazyEntity(messageSchedulerFacade.find(messageSchedulerId));
            sendMessage(messageScheduler);
        }else if(((String)timer.getInfo()).startsWith(SCHEDULE_PREFIX)){
            int scheduleId = Integer.parseInt(((String)timer.getInfo()).replace(SCHEDULE_PREFIX,""));
            Schedule schedule = scheduleFacade.initializeLazyEntity(scheduleFacade.find(scheduleId));
            sendMessage(schedule);
        }
    }

    private void sendMessage(MessageScheduler messageScheduler){

        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setUserCreated(messageScheduler.getUserCreated());
        transactionLog.setMessageTemplate(messageScheduler.getMessageTemplate());
        transactionLog.setDateCreated(new Date());
        transactionLog.setStatus(TransactionLog.Status.PROGRESS);
        transactionLogFacade.create(transactionLog);

        if(messageScheduler.getMessageTemplate().getType() == MessageTemplate.Type.EMAIL){
            //Sending EmailMessage
            if(messageScheduler.getUser() != null){
                mailService.sendEmail(messageScheduler.getUser(), messageScheduler.getMessageTemplate().getContent(),
                        messageScheduler.getMessageTemplate().getName(), transactionLog);
            }else if(messageScheduler.getDeliveryGroups() != null && !messageScheduler.getDeliveryGroups().isEmpty()){
                mailService.sendEmail(extractUsersFromDeliveryGroup(messageScheduler.getDeliveryGroups()),
                        messageScheduler.getMessageTemplate().getContent(),
                        messageScheduler.getMessageTemplate().getName(),transactionLog);
            }
        }else if(messageScheduler.getMessageTemplate().getType() == MessageTemplate.Type.SMS){
            //Sending SMSMessage
            if(messageScheduler.getUser() != null){
                smsService.sendSMS(messageScheduler.getUser(), messageScheduler.getMessageTemplate().getContent(), transactionLog);
            }else if(messageScheduler.getDeliveryGroups() != null && !messageScheduler.getDeliveryGroups().isEmpty()){
                smsService.sendSMS(extractUsersFromDeliveryGroup(messageScheduler.getDeliveryGroups()),
                        messageScheduler.getMessageTemplate().getContent(), transactionLog);
            }
        }
    }

    private void sendMessage(Schedule schedule){
        final Integer USERS_BREAK_ID = Integer.parseInt(Config.getInstance().getString("USERS_BREAK_ID"));
        final Integer EMAIL_MESSAGE_TEMPLATE_FOR_SCHEDULE = Integer.parseInt(Config.getInstance().getString("EMAIL_MESSAGE_TEMPLATE_FOR_SCHEDULE"));
        final Integer SMS_MESSAGE_TEMPLATE_FOR_SCHEDULE = Integer.parseInt(Config.getInstance().getString("SMS_MESSAGE_TEMPLATE_FOR_SCHEDULE"));

        if (schedule.getPatient().equals(usersFacade.find(USERS_BREAK_ID)))
            return;

        MessageTemplate emailMessageTemplate = messageTemplateFacade.initializeLazyEntity(messageTemplateFacade.find(EMAIL_MESSAGE_TEMPLATE_FOR_SCHEDULE));
        MessageTemplate smsMessageTemplate = messageTemplateFacade.initializeLazyEntity(messageTemplateFacade.find(SMS_MESSAGE_TEMPLATE_FOR_SCHEDULE));

        if(schedule.getPatient() != null){
            TransactionLog emailTransactionLog = new TransactionLog();
            emailTransactionLog.setUserCreated(schedule.getUserCreated());
            emailTransactionLog.setMessageTemplate(emailMessageTemplate);
            emailTransactionLog.setDateCreated(new Date());
            emailTransactionLog.setStatus(TransactionLog.Status.PROGRESS);
            transactionLogFacade.create(emailTransactionLog);

            mailService.sendEmail(schedule.getPatient(),
                    processMessageText(emailMessageTemplate.getContent(), schedule),
                    emailMessageTemplate.getName(), emailTransactionLog);

            TransactionLog smsTransactionLog = new TransactionLog();
            smsTransactionLog.setUserCreated(schedule.getUserCreated());
            smsTransactionLog.setMessageTemplate(smsMessageTemplate);
            smsTransactionLog.setDateCreated(new Date());
            smsTransactionLog.setStatus(TransactionLog.Status.PROGRESS);
            transactionLogFacade.create(smsTransactionLog);

            smsService.sendSMS(schedule.getPatient(), processMessageText(smsMessageTemplate.getContent(), schedule), smsTransactionLog);
        }
    }

    private String processMessageText(String text, Schedule schedule){
        if(schedule.getDateTimeStart() != null)
            text = text.replace("$appointmentStartDate", "" + new SimpleDateFormat("yyyy-MM-dd").format(schedule.getDateTimeStart()));
        if(schedule.getDateTimeStart() != null)
            text = text.replace("$appointmentStartTime", "" + new SimpleDateFormat("HH:mm").format(schedule.getDateTimeStart()));
        if(schedule.getMethod() != null && schedule.getMethod().getShortName() != null)
            text = text.replace("$appointmentMethodName", schedule.getMethod().getShortName());
        if(schedule.getMethod() != null && schedule.getMethod() != null && pricesFacade.findLastPrice(schedule.getMethod())!=null)
            text = text.replace("$appointmentMethodPrice", "" + pricesFacade.findLastPrice(schedule.getMethod()).getTotal());
        if(schedule.getDoctor() != null)
            text = text.replace("$doctorsFirstName", schedule.getDoctor().getFirstName());
        if(schedule.getDoctor()  != null)
            text = text.replace("$doctorsLastName", schedule.getDoctor().getLastName());
        return text;
    }

    private List<Users> extractUsersFromDeliveryGroup(List<DeliveryGroup> deliveryGroups){
        Set<Users> users = new HashSet<>();
        for(DeliveryGroup deliveryGroup : deliveryGroups){
            users.addAll(deliveryGroup.getUsers());
            for(UserGroups userGroup : deliveryGroup.getUserGroups()){
                users.addAll(userGroup.getUsers());
            }
        }
        return new ArrayList<>(users);
    }

    class TimeServiceException extends Exception{
        TimeServiceException(){
            super();
        }
        TimeServiceException(String message){
            super(message);
        }
    }
}
