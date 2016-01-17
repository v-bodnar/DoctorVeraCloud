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
import ua.kiev.doctorvera.entities.Users;

/**
 * @author Bodun
 */
@Local
public interface PlanFacadeLocal extends CRUDFacade<Plan> {

    /**
     * Searches for all Plan records that have starting date between the given date range
     *
     * @param from - date to search from
     * @param to   - date to search to
     * @return List<Plan> List of existing Plan records that are not marked as deleted
     */
    List<Plan> findByStartDateBetween(Date from, Date to);


    /**
     * Searches for Plan record that have starting date exactly the same as the given one
     *
     * @param date - date to search for
     * @return Plan record that is not marked as deleted
     */
    Plan findByStartDate(Date date);

    /**
     * Searches for all Plan records that have starting date this week
     *
     * @return List<Plan> List of existing Plan records that are not marked as deleted
     */
    List<Plan> findByStartThisWeek();

    /**
     * Searches for all Plan records that are assigned to the given room
     *
     * @param room - Room to search by
     * @return List<Plan> List of existing Plan records that are not marked as deleted
     */
    List<Plan> findByRoom(Rooms room);

    /**
     * Searches for all Plan records that are assigned to the given room and have starting date between the given date range
     * inclusive from and exclusive to
     *
     * @param room - Room to search by
     * @param from - date to search from
     * @param to   - date to search to
     * @return List<Plan> List of existing Plan records that are not marked as deleted
     */
    List<Plan> findByRoomAndStartDateBetweenExclusiveTo(Rooms room, Date from, Date to);

    /**
     * Searches for all Plan records that are assigned to the given room and have end date between the given date range inclusive to and exclusive from
     *
     * @param room - Room to search by
     * @param from - date to search from
     * @param to   - date to search to
     * @return List<Plan> List of existing Plan records that are not marked as deleted
     */
    List<Plan> findByRoomAndEndDateBetweenExclusiveFrom(Rooms room, Date from, Date to);

    /**
     * Searches for all Plan records that are assigned to the given room and the
     * given date range is between start date and end date of the plan record exclusive this dates
     *
     * @param room - Room to search by
     * @param from - start date of the given date range
     * @param to   - end date of the given range
     * @return List<Plan> List of existing Plan records that are not marked as deleted
     */
    List<Plan> findByRoomAndDatesInsidePlan(Rooms room, Date from, Date to);

    /**
     * Searches for all Plan records that are assigned to the given room and
     * the given date range is between start date and end date of the plan record inclusive this dates
     *
     * @param room - Room to search by
     * @param from - start date of the given date range
     * @param to   - end date of the given range
     * @return List<Plan> List of existing Plan records that are not marked as deleted
     */
    List<Plan> findByRoomAndDatesInsidePlanOrEqual(Rooms room, Date from, Date to);

    /**
     * Searches for all Plan records that are assigned to the given room and
     * given date is between start date and end date of the plan record inclusive start date and exclusive end date
     *
     * @param room - Room to search by
     * @param date - date to search for
     * @return List<Plan> List of existing Plan records that are not marked as deleted
     */
    Plan findByRoomAndDateInside(Rooms room, Date date);

    /**
     * Searches for all Plan records that are assigned to the given employee and have starting date between the given date range
     * exclusive from and to
     *
     * @param employee - Employee to search by
     * @param from     - date to search from
     * @param to       - date to search to
     * @return List<Plan> List of existing Plan records that are not marked as deleted
     */
    List<Plan> findByDoctorAndStartDateBetweenExclusiveTo(Users employee, Date from, Date to);
}
