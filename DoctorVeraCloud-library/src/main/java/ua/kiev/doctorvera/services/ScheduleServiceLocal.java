package ua.kiev.doctorvera.services;

import ua.kiev.doctorvera.entities.MessageScheduler;
import ua.kiev.doctorvera.entities.Schedule;

import javax.ejb.Local;

/**
 * Created by volodymyr.bodnar on 1/9/2016.
 */
@Local
public interface ScheduleServiceLocal {
    void scheduleEvent(MessageScheduler scheduler);
    void changeEvent(MessageScheduler scheduler);
    void removeEvent(MessageScheduler scheduler);
    void scheduleEvent(Schedule schedule);
    void changeEvent(Schedule schedule);
    void removeEvent(Schedule schedule);
}
