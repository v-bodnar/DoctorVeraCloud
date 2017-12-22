/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.Plan;
import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.PlanFacadeLocal;
import ua.kiev.doctorvera.utils.DateUtils;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/**
 * @author Bodun
 */
@Stateless
public class PlanFacade extends AbstractFacade<Plan> implements PlanFacadeLocal {

    public PlanFacade() {
        super(Plan.class);
    }

    /**
     * Searches for all Plan records that have starting date between the given date range
     *
     * @param from - date to search from
     * @param to   - date to search to
     * @return List<Plan> List of existing Plan records that are not marked as deleted
     */
    @Override
    public List<Plan> findByStartDateBetween(Date from, Date to) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Plan> cq = cb.createQuery(Plan.class);
        Root<Plan> root = cq.from(Plan.class);
        Predicate datePredicate = cb.and(cb.between(root.<Date>get("dateTimeStart"), from, to));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicate, deletedPredicate);
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Searches for all Plan records that have starting date between the given date range and provided by given doctor
     *
     * @param from - date to search from
     * @param to   - date to search to
     * @param doctor - doctor that provides research
     * @return List<Plan> List of existing Plan records that are not marked as deleted
     */
    @Override
    public List<Plan> findByStartDateBetweenAndDoctor(Date from, Date to, Users doctor){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Plan> cq = cb.createQuery(Plan.class);
        Root<Plan> root = cq.from(Plan.class);
        Predicate doctorPredicate = cb.and(cb.equal(root.get("doctor"), doctor));
        Predicate datePredicate = cb.and(cb.between(root.<Date>get("dateTimeStart"), from, to));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicate, deletedPredicate, doctorPredicate);
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Searches for all Plan records that have starting date between the given date range
     * and provided by given doctor and in specified room
     *
     * @param room - Room to search by
     * @param from - date to search from
     * @param to   - date to search to
     * @param doctor - doctor that provides research
     * @return List<Plan> List of existing Plan records that are not marked as deleted
     */
    @Override
    public List<Plan> findByStartDateBetweenAndDoctorAndRoom(Date from, Date to, Users doctor, Rooms room){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Plan> cq = cb.createQuery(Plan.class);
        Root<Plan> root = cq.from(Plan.class);
        Predicate doctorPredicate = cb.and(cb.equal(root.get("doctor"), doctor));
        Predicate roomPredicate = cb.and(cb.equal(root.<Rooms>get("room"), room));
        Predicate datePredicate = cb.and(cb.between(root.<Date>get("dateTimeStart"), from, to));
        Predicate datePredicate2 = cb.and(cb.notEqual(root.<Date>get("dateTimeStart"), to));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicate, datePredicate2, deletedPredicate, roomPredicate, doctorPredicate);
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }



    /**
     * Searches for Plan record that have starting date exactly the same as the given one
     *
     * @param date - date to search for
     * @return Plan record that is not marked as deleted
     */
    @Override
    public Plan findByStartDate(Date date) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Plan> cq = cb.createQuery(Plan.class);
        Root<Plan> root = cq.from(Plan.class);
        Predicate datePredicate = cb.and(cb.equal(root.<Date>get("dateTimeStart"), date));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicate, deletedPredicate);
        cq.distinct(true);
        List<Plan> resultList = getEntityManager().createQuery(cq).getResultList();
        if (resultList.size() == 1) return resultList.get(0);
        else return null;
    }

    /**
     * Searches for all Plan records that have starting date this week
     *
     * @return List<Plan> List of existing Plan records that are not marked as deleted
     */
    @Override
    public List<Plan> findByStartThisWeek() {
        return findByStartDateBetween(DateUtils.getWeekStart(), DateUtils.getWeekEnd());
    }

    /**
     * Searches for all Plan records that are assigned to the given room
     *
     * @param room - Room to search by
     * @return List<Plan> List of existing Plan records that are not marked as deleted
     */
    @Override
    public List<Plan> findByRoom(Rooms room) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Plan> cq = cb.createQuery(Plan.class);
        Root<Plan> root = cq.from(Plan.class);
        Predicate roomPredicate = cb.and(cb.equal(root.<Rooms>get("room"), room));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(roomPredicate, deletedPredicate);
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Searches for all Plan records that are assigned to the given room and have starting date between the given date range
     *  exclusive from and to
     *
     * @param room - Room to search by
     * @param from - date to search from
     * @param to   - date to search to
     * @return List<Plan> List of existing Plan records that are not marked as deleted
     */
    @Override
    public List<Plan> findByRoomAndStartDateBetweenExclusiveTo(Rooms room, Date from, Date to) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Plan> cq = cb.createQuery(Plan.class);
        Root<Plan> root = cq.from(Plan.class);
        Predicate roomPredicate = cb.and(cb.equal(root.<Rooms>get("room"), room));
        Predicate datePredicate = cb.and(cb.between(root.<Date>get("dateTimeStart"), from, to));
        Predicate datePredicate2 = cb.and(cb.notEqual(root.<Date>get("dateTimeStart"), to));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicate, datePredicate2, deletedPredicate, roomPredicate);
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }
    /**
     * Searches for all Plan records that are assigned to the given room and have end date between the given date range exclusive from and to
     *
     * @param room - Room to search by
     * @param from - date to search from
     * @param to   - date to search to
     * @return List<Plan> List of existing Plan records that are not marked as deleted
     */
    @Override
    public List<Plan> findByRoomAndEndDateBetweenExclusiveFrom(Rooms room, Date from, Date to) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Plan> cq = cb.createQuery(Plan.class);
        Root<Plan> root = cq.from(Plan.class);
        Predicate roomPredicate = cb.and(cb.equal(root.<Rooms>get("room"), room));
        Predicate datePredicate = cb.and(cb.between(root.<Date>get("dateTimeEnd"), from, to));
        Predicate datePredicate2 = cb.and(cb.notEqual(root.<Date>get("dateTimeEnd"), from));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicate, deletedPredicate,datePredicate2, roomPredicate);
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Searches for all Plan records that are assigned to the given room and the
     * given date range is between start date and end date of the plan record exclusive this dates
     *
     * @param room - Room to search by
     * @param from - start date of the given date range
     * @param to   - end date of the given range
     * @return List<Plan> List of existing Plan records that are not marked as deleted
     */
    @Override
    public List<Plan> findByRoomAndDatesInsidePlan(Rooms room, Date from, Date to) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Plan> cq = cb.createQuery(Plan.class);
        Root<Plan> root = cq.from(Plan.class);
        Predicate roomPredicate = cb.and(cb.equal(root.<Rooms>get("room"), room));
        Predicate datePredicateFrom = cb.and(cb.lessThan(root.<Date>get("dateTimeStart"), from));
        Predicate datePredicateTo = cb.and(cb.greaterThan(root.<Date>get("dateTimeEnd"), to));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicateFrom, datePredicateTo, deletedPredicate, roomPredicate);
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Searches for all Plan records that are assigned to the given room and
     * the given date range is between start date and end date of the plan record inclusive this dates
     *
     * @param room - Room to search by
     * @param from - start date of the given date range
     * @param to   - end date of the given range
     * @return List<Plan> List of existing Plan records that are not marked as deleted
     */
    @Override
    public List<Plan> findByRoomAndDatesInsidePlanOrEqual(Rooms room, Date from, Date to) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Plan> cq = cb.createQuery(Plan.class);
        Root<Plan> root = cq.from(Plan.class);
        Predicate roomPredicate = cb.and(cb.equal(root.<Rooms>get("room"), room));
        Predicate datePredicateFrom = cb.and(cb.lessThanOrEqualTo(root.<Date>get("dateTimeStart"), from));
        Predicate datePredicateTo = cb.and(cb.greaterThanOrEqualTo(root.<Date>get("dateTimeEnd"), to));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicateFrom, datePredicateTo, deletedPredicate, roomPredicate);
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Searches for all Plan records that are assigned to the given room and
     * given date is between start date and end date of the plan record inclusive start date and exclusive end date
     *
     * @param room - Room to search by
     * @param date - date to search for
     * @return List of existing Plan records that are not marked as deleted
     */
    @Override
    public Plan findByRoomAndDateInside(Rooms room, Date date) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Plan> cq = cb.createQuery(Plan.class);
        Root<Plan> root = cq.from(Plan.class);
        Predicate roomPredicate = cb.and(cb.equal(root.<Rooms>get("room"), room));
        Predicate datePredicateFrom = cb.and(cb.lessThanOrEqualTo(root.<Date>get("dateTimeStart"), date));
        Predicate datePredicateTo = cb.and(cb.greaterThan(root.<Date>get("dateTimeEnd"), date));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicateFrom, datePredicateTo, deletedPredicate, roomPredicate);
        cq.distinct(true);
        if(getEntityManager().createQuery(cq).getResultList().isEmpty())
            return null;
        else
            return getEntityManager().createQuery(cq).getSingleResult();
    }

    /**
     * Searches for all Plan records that are assigned to the given employee and have starting date between the given date range
     *  exclusive from and to
     *
     * @param employee - Employee to search by
     * @param from - date to search from
     * @param to   - date to search to
     * @return List<Plan> List of existing Plan records that are not marked as deleted
     */
    @Override
    public List<Plan> findByDoctorAndStartDateBetweenExclusiveTo(Users employee, Date from, Date to) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Plan> cq = cb.createQuery(Plan.class);
        Root<Plan> root = cq.from(Plan.class);
        Predicate doctorPredicate = cb.and(cb.equal(root.<Users>get("doctor"), employee));
        Predicate datePredicate = cb.and(cb.between(root.<Date>get("dateTimeStart"), from, to));
        Predicate datePredicate2 = cb.and(cb.notEqual(root.<Date>get("dateTimeStart"), to));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicate, datePredicate2, deletedPredicate, doctorPredicate);
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

}
