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

import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.entities.Schedule;
import ua.kiev.doctorvera.entities.Users;

/**
 * Interface for declaring main operations with Generic (Identified) entity
 * @author Volodymyr Bodnar
 */
@Local
public interface ScheduleFacadeLocal{
    /**
    @return EntityManager got from container
    */
    EntityManager getEntityManager();

    /**
    Creates new entity representation(record) in the persistent storage(Data Base)
    @param  entity  concrete NEW entity to write
    */
    void create(Schedule entity);

    /**
    Updates existing entity representation(record) in the persistent storage(Data Base)
    @param  entity existing entity to be updated
    */
    void edit(Schedule entity);

    /**
    Marks existing entity as deleted in the persistent storage(Data Base)
    @param  entity existing entity to be marked as deleted
    */
    void remove(Schedule entity);

    /**
    @param id Unique object identifier
    @return Schedule Identified existing entity represented by unique identifier
    */
    Schedule find(Integer id);

    /**
    @return List<Schedule> List of all Identified existing entities that are not marked as deleted
    */
    List<Schedule> findAll();

    /**
    @return List<Schedule> List of Identified existing entities that are not marked as deleted from range
    @param range
    */
    List<Schedule> findRange(int[] range);

    /**
    @return int records count
    */
    int count();

    /**
    <strong>Permanently</strong> removes entity from the persistent storage(Data Base)
    @param entity - Entity that has to be removed
    */
    void removeFromDB(Schedule entity);

    /**
    Searches for all Schedule records that are assigned to the given room and
    start date is between the given date range inclusive from and exclusive to
    @return List<Plan> List of existing Schedule records that are not marked as deleted
    @param room - Room to search by
    @param from - start date of the given date range
    @param to - end date of the given range
    */
    List<Schedule> findByRoomAndStartDateBetweenExclusiveTo(Rooms room, Date from, Date to);

    /**
    Searches for all Schedule records that are assigned to the given room and
    end date is between the given date range inclusive to and exclusive from
    @return List<Plan> List of existing Schedule records that are not marked as deleted
    @param room - Room to search by
    @param from - start date of the given date range
    @param to - end date of the given range
    */
    List<Schedule> findByRoomAndEndDateBetweenExclusiveFrom(Rooms room, Date from, Date to);

    /**
    Searches for all Schedule records that are assigned to the given room and
    end date is between the given date range inclusive from and exclusive to
    @return List<Plan> List of existing Schedule records that are not marked as deleted
    @param room - Room to search by
    @param from - start date of the given date range
    @param to - end date of the given range
    */
    List<Schedule> findByRoomAndDatesInsideSchedule(Rooms room, Date from, Date to);

    /**
    Searches for all Schedule records that are assigned to the given room and
    the given date range is between start date and end date of the schedule record inclusive this dates
    @return List<Plan> List of existing Schedule records that are not marked as deleted
    @param room - Room to search by
    @param from - start date of the given date range
    @param to - end date of the given range
    */
    List<Schedule> findByRoomAndDatesInsideScheduleOrEqual(Rooms room, Date from, Date to);

    /**
    Searches for all Schedule records that are assigned to the given room and
    start date and end date of the schedule record is between the given date range inclusive this dates
    @return List<Plan> List of existing Schedule records that are not marked as deleted
    @param room - Room to search by
    @param from - start date of the given date range
    @param to - end date of the given range
    */
    List<Schedule> findByRoomAndDatesOutsideScheduleOrEqual(Rooms room, Date from, Date to);

    /**
    Searches for all Schedule records that are assigned to the given room and
    the given date is between start date and end date of the schedule record inclusive start date and exclusive end date
    @return List<Plan> List of existing Schedule records that are not marked as deleted
    @param room - Room to search by
    @param date - start date of the given date range
    */
    Schedule findByRoomAndDateInside(Rooms room, Date date);

    /**
    Searches for all Schedule records that have starting date between the given date range
    @return List<Schedule> List of existing Schedule records that are not marked as deleted
    @param from - date to search from
    @param to - date to search to
    */
    List<Schedule> findByStartDateBetween(Date from, Date to);

    /**
     Searches for current Schedule records child
     @return Schedule child record
     @param schedule - record whose child to search
     */
    Schedule findChildSchedule(Schedule schedule);

    /**
     * Searches for all Schedule records that have starting date between the given date range
     * And with the given doctor
     * @param employee Employee to search for
     * @param from
     * @param to
     * @return List<Schedule> List of existing Schedule records that are not marked as deleted
     */
    List<Schedule>findByEmployeeAndDateBetween(Users employee, Date from, Date to);
}
