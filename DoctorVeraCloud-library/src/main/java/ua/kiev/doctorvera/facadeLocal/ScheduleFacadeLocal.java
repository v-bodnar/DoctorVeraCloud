/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facadeLocal;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import ua.kiev.doctorvera.entities.Methods;
import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.entities.Schedule;
import ua.kiev.doctorvera.entities.Users;

/**
 * Interface for declaring main operations with Generic (Identified) entity
 *
 * @author Volodymyr Bodnar
 */
@Local
public interface ScheduleFacadeLocal  extends CRUDFacade<Schedule>{

    /**
     * Searches for all Schedule records that are assigned to the given room and
     * start date is between the given date range inclusive from and exclusive to
     *
     * @param room - Room to search by
     * @param from - start date of the given date range
     * @param to   - end date of the given range
     * @return List<Plan> List of existing Schedule records that are not marked as deleted
     */
    List<Schedule> findByRoomAndStartDateBetweenExclusiveTo(Rooms room, Date from, Date to);

    /**
     * Searches for all Schedule records that are assigned to the given room and
     * end date is between the given date range inclusive to and exclusive from
     *
     * @param room - Room to search by
     * @param from - start date of the given date range
     * @param to   - end date of the given range
     * @return List<Plan> List of existing Schedule records that are not marked as deleted
     */
    List<Schedule> findByRoomAndEndDateBetweenExclusiveFrom(Rooms room, Date from, Date to);

    /**
     * Searches for all Schedule records that are assigned to the given room and
     * end date is between the given date range inclusive from and exclusive to
     *
     * @param room - Room to search by
     * @param from - start date of the given date range
     * @param to   - end date of the given range
     * @return List<Plan> List of existing Schedule records that are not marked as deleted
     */
    List<Schedule> findByRoomAndDatesInsideSchedule(Rooms room, Date from, Date to);

    /**
     * Searches for all Schedule records that are assigned to the given room and
     * the given date range is between start date and end date of the schedule record inclusive this dates
     *
     * @param room - Room to search by
     * @param from - start date of the given date range
     * @param to   - end date of the given range
     * @return List<Plan> List of existing Schedule records that are not marked as deleted
     */
    List<Schedule> findByRoomAndDatesInsideScheduleOrEqual(Rooms room, Date from, Date to);

    /**
     * Searches for all Schedule records that are assigned to the given room and
     * start date and end date of the schedule record is between the given date range inclusive this dates
     *
     * @param room - Room to search by
     * @param from - start date of the given date range
     * @param to   - end date of the given range
     * @return List<Plan> List of existing Schedule records that are not marked as deleted
     */
    List<Schedule> findByRoomAndDatesOutsideScheduleOrEqual(Rooms room, Date from, Date to);

    /**
     * Searches for all Schedule records that are assigned to the given room and
     * the given date is between start date and end date of the schedule record inclusive start date and exclusive end date
     *
     * @param room - Room to search by
     * @param date - start date of the given date range
     * @return chedule List of existing Schedule records that are not marked as deleted
     */
    Schedule findByRoomAndDateInside(Rooms room, Date date);

    /**
     * Searches for all Schedule records that have starting date between the given date range
     *
     * @param from - date to search from
     * @param to   - date to search to
     * @return List<Schedule> List of existing Schedule records that are not marked as deleted
     */
    List<Schedule> findByStartDateBetween(Date from, Date to);


    /**
     * Searches for all Schedule records that have starting date between the given date range
     *
     * @param from - date to search from
     * @param to   - date to search to
     * @param doctor - doctor that provides research
     * @return List<Schedule> List of existing Schedule records that are not marked as deleted
     */
    List<Schedule> findByStartDateBetweenAndDoctorAndRoom(Date from, Date to, Users doctor, Rooms room);

    /**
     * Searches for all Schedule records that have starting date between the given date range
     * and are payed(payment exists for them)
     *
     * @param from - date to search from
     * @param to   - date to search to
     * @return List<Schedule> List of existing Schedule records that are not marked as deleted
     */
    List<Schedule> findPayedByStartDateBetween(Date from, Date to);

    /**
     * Searches for current Schedule records child
     *
     * @param schedule - record whose child to search
     * @return Schedule child record
     */
    Schedule findChildSchedule(Schedule schedule);

    /**
     * Searches for all Schedule records that have starting date between the given date range
     * And with the given doctor
     *
     * @param employee Employee to search for
     * @param from
     * @param to
     * @return List<Schedule> List of existing Schedule records that are not marked as deleted
     */
    List<Schedule> findByEmployeeAndDateBetween(Users employee, Date from, Date to);

    /**
     * Searches for all Schedule records that have starting date between the given date range
     * And with the given doctor, and are payed(payment exists for them)
     *
     * @param employee Employee to search for
     * @param from
     * @param to
     * @return List<Schedule> List of existing Schedule records that are not marked as deleted
     */
    List<Schedule> findPayedByEmployeeAndDateBetween(Users employee, Date from, Date to);

    /**
     * Searches for all Schedule records that have starting date between the given date range
     * And with the given doctor and method, and are payed(payment exists for them)
     *
     * @param employee Employee to search for
     * @param method Method to search for
     * @param from
     * @param to
     * @return List<Schedule> List of existing Schedule records that are not marked as deleted
     */
    List<Schedule> findPayedByEmployeeAndMethodAndDateBetween(Users employee, Methods method, Date from, Date to);

    /**
     * Searches for all Schedule records that have starting date between the given date range
     * And with the given method, and are payed(payment exists for them)
     *
     * @param method Method to search for
     * @param method Method to search for
     * @param from
     * @param to
     * @return List<Schedule> List of existing Schedule records that are not marked as deleted
     */
    List<Schedule> findPayedByMethodAndDateBetween(Methods method, Date from, Date to);

    /**
     * Searches for all Schedule records that have starting date between the given date range
     *
     * @param from
     * @param to
     * @return List<Schedule> List of existing Schedule records that are not marked as deleted
     */
    List<Schedule> findPayedByDateBetween(Date from, Date to);

    /**
     * Searches for all Schedule records that are assigned to the given employee and
     * start date is between the given date range inclusive from and exclusive to
     *
     * @param employee - employee to search by
     * @param from     - start date of the given date range
     * @param to       - end date of the given range
     * @return List<Plan> List of existing Schedule records that are not marked as deleted
     */
    List<Schedule> findByEmployeeAndStartDateBetweenExclusiveTo(Users employee, Date from, Date to);

    /**
     * Searches for all Schedule records that are assigned to the given employee and
     * start date is between the given date range inclusive from and exclusive to
     *
     * @param employee - employee to search by
     * @param from     - start date of the given date range
     * @param to       - end date of the given range
     * @return List<Plan> List of existing Schedule records that are not marked as deleted
     */
    List<Schedule> findPayedByEmployeeAndStartDateBetweenExclusiveTo(Users employee, Date from, Date to);

    /**
     * Searches for all Schedule records that are assigned to the given room and
     * end date is between the given date range exclusive from and to
     *
     * @param employee - employee to search by
     * @param from     - start date of the given date range
     * @param to       - end date of the given range
     * @return List<Plan> List of existing Schedule records that are not marked as deleted
     */
    List<Schedule> findByEmployeeAndEndDateBetweenExclusiveFrom(Users employee, Date from, Date to);
}
