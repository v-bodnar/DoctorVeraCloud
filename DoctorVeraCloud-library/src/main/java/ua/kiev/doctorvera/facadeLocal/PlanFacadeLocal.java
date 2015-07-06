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

import ua.kiev.doctorvera.entities.Plan;
import ua.kiev.doctorvera.entities.Rooms;

/**
 *
 * @author Bodun
 */
@Local
public interface PlanFacadeLocal {
    /*
    @return EntityManager got from container
    */
    EntityManager getEntityManager();

    /**
    Creates new entity representation(record) in the persistent storage(Data Base)
    @param  entity  concrete NEW entity to write
    */
    void create(Plan entity);

    /**
    Updates existing entity representation(record) in the persistent storage(Data Base)
    @param  entity existing entity to be updated
    */
    void edit(Plan entity);

    /**
    Marks existing entity as deleted in the persistent storage(Data Base)
    @param  entity existing entity to be marked as deleted
    */
    void remove(Plan entity);

    /**
    @param id Unique object identifier
    @return Plan Identified existing entity represented by unique identifier
    */
    Plan find(Integer id);

    /**
    @return List<Plan> List of all Identified existing entities that are not marked as deleted
    */
    List<Plan> findAll();

    /**
    @return List<Plan> List of Identified existing entities that are not marked as deleted from range
    @param range
    */
    List<Plan> findRange(int[] range);

    /**
    @return int records count
    */
    int count();

    /**
    <strong>Permanently</strong> removes entity from the persistent storage(Data Base)
    @param entity - Entity that has to be removed
    */
    void removeFromDB(Plan entity);

    /**
    Searches for all Plan records that have starting date between the given date range
    @return List<Plan> List of existing Plan records that are not marked as deleted
    @param from - date to search from
    @param to - date to search to
    */
    List<Plan> findByStartDateBetween(Date from, Date to);

    /**
    Searches for Plan record that have starting date exactly the same as the given one
    @return Plan record that is not marked as deleted
    @param date - date to search for
    */
    Plan findByStartDate(Date date);

    /**
    Searches for all Plan records that have starting date this week
    @return List<Plan> List of existing Plan records that are not marked as deleted
    */
    List<Plan> findByStartThisWeek();

    /**
    Searches for all Plan records that are assigned to the given room
    @return List<Plan> List of existing Plan records that are not marked as deleted
    @param room - Room to search by
    */
    List<Plan> findByRoom(Rooms room);

    /**
    Searches for all Plan records that are assigned to the given room and have starting date between the given date range
    inclusive from and exclusive to
    @return List<Plan> List of existing Plan records that are not marked as deleted
    @param room - Room to search by
    @param from - date to search from
    @param to - date to search to
    */
    List<Plan> findByRoomAndStartDateBetween(Rooms room, Date from, Date to);

    /**
    Searches for all Plan records that are assigned to the given room and have end date between the given date range inclusive from and to
    @return List<Plan> List of existing Plan records that are not marked as deleted
    @param room - Room to search by
    @param from - date to search from
    @param to - date to search to
    */
    List<Plan> findByRoomAndEndDateBetween(Rooms room, Date from, Date to);

    /**
    Searches for all Plan records that are assigned to the given room and the
    given date range is between start date and end date of the plan record exclusive this dates
    @return List<Plan> List of existing Plan records that are not marked as deleted
    @param room - Room to search by
    @param from - start date of the given date range
    @param to - end date of the given range
    */
    List<Plan> findByRoomAndDatesInsidePlan(Rooms room, Date from, Date to);

    /**
    Searches for all Plan records that are assigned to the given room and
    the given date range is between start date and end date of the plan record inclusive this dates
    @return List<Plan> List of existing Plan records that are not marked as deleted
    @param room - Room to search by
    @param from - start date of the given date range
    @param to - end date of the given range
    */
    List<Plan> findByRoomAndDatesInsidePlanOrEqual(Rooms room, Date from, Date to);

    /**
    Searches for all Plan records that are assigned to the given room and
    given date is between start date and end date of the plan record inclusive start date and exclusive end date
    @return List<Plan> List of existing Plan records that are not marked as deleted
    @param room - Room to search by
    @param date - date to search for
    */
    Plan findByRoomAndDateInside(Rooms room, Date date);
}
