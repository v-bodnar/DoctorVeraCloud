package ua.kiev.doctorvera.services;

import ua.kiev.doctorvera.entities.MessageScheduler;
import ua.kiev.doctorvera.entities.Schedule;
import ua.kiev.doctorvera.entities.TransactionLog;
import ua.kiev.doctorvera.entities.Users;

import javax.ejb.Local;

/**
 * Created by volodymyr.bodnar on 1/9/2016.
 */
@Local
public interface ScheduleServiceLocal {
    /**
     * Schedules new timer for sending message scheduled in MDS
     * @param scheduler data for message sending
     */
    void scheduleEvent(MessageScheduler scheduler);

    /**
     * Removes timer for sending message scheduled in MDS and schedules new one
     * @param scheduler data for message sending
     */
    void changeEvent(MessageScheduler scheduler);

    /**
     * Removes timer for sending message scheduled in MDS
     * @param scheduler data for message sending
     */
    void removeEvent(MessageScheduler scheduler);

    /**
     * Schedules new timer for sending message for patients notification
     * @param schedule data for message sending
     */
    void scheduleEvent(Schedule schedule);

    /**
     * Removes timer for sending message for patients notification and creates new one with fresh data
     * @param schedule data for message sending
     */
    void changeEvent(Schedule schedule);

    /**
     * Removes timer for sending message for patients notification
     * @param schedule data for message sending
     */
    void removeEvent(Schedule schedule);

    /**
     * Method removes timer for doctors notification message and schedules it one more time
     */
    void changeDoctorsNotificationDeliveryTime();
}
