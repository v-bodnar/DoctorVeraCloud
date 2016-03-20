/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.*;
import ua.kiev.doctorvera.facadeLocal.ScheduleFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Class for implementing main operations with User entity
 *
 * @author Volodymyr Bodnar
 */
@Stateless
public class ScheduleFacade extends AbstractFacade<Schedule> implements ScheduleFacadeLocal {

    public ScheduleFacade() {
        super(Schedule.class);
    }
    private final Integer USERS_BREAK_ID = 2;
    @EJB
    private UsersFacadeLocal usersFacade;
    /**
     * Searches for all Schedule records that are assigned to the given room and
     * start date is between the given date range inclusive from and exclusive to
     *
     * @param room - Room to search by
     * @param from - start date of the given date range
     * @param to   - end date of the given range
     * @return List<Plan> List of existing Schedule records that are not marked as deleted
     */
    @Override
    public List<Schedule> findByRoomAndStartDateBetweenExclusiveTo(Rooms room, Date from, Date to) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
        Root<Schedule> root = cq.from(Schedule.class);
        Predicate roomPredicate = cb.and(cb.equal(root.<Rooms>get("room"), room));
        Predicate datePredicate = cb.and(cb.between(root.<Date>get("dateTimeStart"), from, to));
        Predicate datePredicate2 = cb.and(cb.notEqual(root.<Date>get("dateTimeStart"), to));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicate, datePredicate2, deletedPredicate, roomPredicate);
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Searches for all Schedule records that are assigned to the given room and
     * end date is between the given date range exclusive from and to
     *
     * @param room - Room to search by
     * @param from - start date of the given date range
     * @param to   - end date of the given range
     * @return List<Plan> List of existing Schedule records that are not marked as deleted
     */
    @Override
    public List<Schedule> findByRoomAndEndDateBetweenExclusiveFrom(Rooms room, Date from, Date to) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
        Root<Schedule> root = cq.from(Schedule.class);
        Predicate roomPredicate = cb.and(cb.equal(root.<Rooms>get("room"), room));
        Predicate datePredicate = cb.and(cb.between(root.<Date>get("dateTimeEnd"), from, to));
        Predicate datePredicate2 = cb.and(cb.notEqual(root.<Date>get("dateTimeEnd"), from));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicate, datePredicate2, deletedPredicate, roomPredicate);
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Searches for all Schedule records that are assigned to the given room and
     * end date is between the given date range inclusive to and exclusive from
     *
     * @param room - Room to search by
     * @param from - start date of the given date range
     * @param to   - end date of the given range
     * @return List<Plan> List of existing Schedule records that are not marked as deleted
     */
    @Override
    public List<Schedule> findByRoomAndDatesInsideSchedule(Rooms room, Date from, Date to) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
        Root<Schedule> root = cq.from(Schedule.class);
        Predicate roomPredicate = cb.and(cb.equal(root.<Rooms>get("room"), room));
        Predicate datePredicateFrom = cb.and(cb.lessThan(root.<Date>get("dateTimeStart"), from));
        Predicate datePredicateTo = cb.and(cb.greaterThan(root.<Date>get("dateTimeEnd"), to));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicateFrom, datePredicateTo, deletedPredicate, roomPredicate);
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Searches for all Schedule records that are assigned to the given room and
     * the given date range is between start date and end date of the schedule record inclusive this dates
     *
     * @param room - Room to search by
     * @param from - start date of the given date range
     * @param to   - end date of the given range
     * @return List<Plan> List of existing Schedule records that are not marked as deleted
     */
    @Override
    public List<Schedule> findByRoomAndDatesInsideScheduleOrEqual(Rooms room, Date from, Date to) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
        Root<Schedule> root = cq.from(Schedule.class);
        Predicate roomPredicate = cb.and(cb.equal(root.<Rooms>get("room"), room));
        Predicate datePredicateFrom = cb.and(cb.lessThanOrEqualTo(root.<Date>get("dateTimeStart"), from));
        Predicate datePredicateTo = cb.and(cb.greaterThanOrEqualTo(root.<Date>get("dateTimeEnd"), to));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicateFrom, datePredicateTo, deletedPredicate, roomPredicate);
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Searches for all Schedule records that are assigned to the given room and
     * start date and end date of the schedule record is between the given date range exclusive this dates
     *
     * @param room - Room to search by
     * @param from - start date of the given date range
     * @param to   - end date of the given range
     * @return List<Plan> List of existing Schedule records that are not marked as deleted
     */
    @Override
    public List<Schedule> findByRoomAndDatesOutsideScheduleOrEqual(Rooms room, Date from, Date to) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
        Root<Schedule> root = cq.from(Schedule.class);
        Predicate roomPredicate = cb.and(cb.equal(root.<Rooms>get("room"), room));
        Predicate datePredicateFrom = cb.and(cb.greaterThan(root.<Date>get("dateTimeStart"), from));
        Predicate datePredicateTo = cb.and(cb.lessThan(root.<Date>get("dateTimeEnd"), to));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicateFrom, datePredicateTo, deletedPredicate, roomPredicate);
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Searches for all Schedule records that are assigned to the given room and
     * the given date is between start date and end date of the schedule record inclusive start date and exclusive end date
     *
     * @param room - Room to search by
     * @param date - start date of the given date range
     * @return Schedule existing Schedule record that are not marked as deleted
     */
    @Override
    public Schedule findByRoomAndDateInside(Rooms room, Date date) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
        Root<Schedule> root = cq.from(Schedule.class);
        Predicate roomPredicate = cb.and(cb.equal(root.<Rooms>get("room"), room));
        Predicate datePredicateFrom = cb.and(cb.lessThanOrEqualTo(root.<Date>get("dateTimeStart"), date));
        Predicate datePredicateTo = cb.and(cb.greaterThan(root.<Date>get("dateTimeEnd"), date));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicateFrom, datePredicateTo, deletedPredicate, roomPredicate);
        cq.distinct(true);
        if (getEntityManager().createQuery(cq).getResultList().isEmpty())
            return null;
        else
            return getEntityManager().createQuery(cq).getSingleResult();
    }

    /**
     * Searches for all Schedule records that have starting date between the given date range
     *
     * @param from - date to search from
     * @param to   - date to search to
     * @return List<Schedule> List of existing Schedule records that are not marked as deleted
     */
    @Override
    public List<Schedule> findByStartDateBetween(Date from, Date to) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
        Root<Schedule> root = cq.from(Schedule.class);
        Predicate datePredicate = cb.and(cb.between(root.<Date>get("dateTimeStart"), from, to));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicate, deletedPredicate);
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }
    /**
     * Searches for all Schedule records that have starting date between the given date range
     * and are payed(payment exists for them)
     *
     * @param from - date to search from
     * @param to   - date to search to
     * @return List<Schedule> List of existing Schedule records that are not marked as deleted
     */
    @Override
    public List<Schedule> findPayedByStartDateBetween(Date from, Date to){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
        Root<Schedule> root = cq.from(Schedule.class);
        Predicate paymentPredicate = cb.and(cb.isNotEmpty(root.<Collection<Payments>>get("payments")));
        Predicate datePredicate = cb.and(cb.between(root.<Date>get("dateTimeStart"), from, to));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicate, deletedPredicate, paymentPredicate);
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Searches for current Schedule records child
     *
     * @param schedule - record whose child to search
     * @return Schedule child record
     */
    @Override
    public Schedule findChildSchedule(Schedule schedule) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
        Root<Schedule> root = cq.from(Schedule.class);
        Predicate parentPredicate = cb.and(cb.equal(root.<Schedule>get("parentSchedule"), schedule));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(parentPredicate, deletedPredicate);
        cq.distinct(true);
        if (getEntityManager().createQuery(cq).getResultList().isEmpty())
            return null;
        else
            return getEntityManager().createQuery(cq).getSingleResult();
    }

    /**
     * Searches for all Schedule records that have starting date between the given date range
     * And with the given doctor
     *
     * @param employee Employee to search for
     * @param from
     * @param to
     * @return List<Schedule> List of existing Schedule records that are not marked as deleted
     */
    @Override
    public List<Schedule> findByEmployeeAndDateBetween(Users employee, Date from, Date to) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
        Root<Schedule> root = cq.from(Schedule.class);
        Predicate datePredicate = cb.and(cb.between(root.<Date>get("dateTimeStart"), from, to));
        Predicate doctorPredicate = cb.and(cb.equal(root.<Users>get("doctor"), employee));
        Predicate assistantPredicate = cb.and(cb.equal(root.<Users>get("assistant"), employee));
        Predicate doctorDirectedPredicate = cb.and(cb.equal(root.<Users>get("doctorDirected"), employee));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicate, deletedPredicate, cb.or(doctorPredicate, assistantPredicate, doctorDirectedPredicate));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Searches for all Schedule records that have starting date between the given date range
     * And with the given doctor, and are payed(payment exists for them)
     *
     * @param employee Employee to search for
     * @param from
     * @param to
     * @return List<Schedule> List of existing Schedule records that are not marked as deleted
     */
    @Override
    public List<Schedule> findPayedByEmployeeAndDateBetween(Users employee, Date from, Date to){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
        Root<Schedule> root = cq.from(Schedule.class);
        Predicate datePredicate = cb.and(cb.between(root.<Date>get("dateTimeStart"), from, to));
        Predicate doctorPredicate = cb.and(cb.equal(root.<Users>get("doctor"), employee));
        Predicate paymentPredicate = cb.and(cb.isNotEmpty(root.<Collection<Payments>>get("payments")));
        Predicate assistantPredicate = cb.and(cb.equal(root.<Users>get("assistant"), employee));
        Predicate doctorDirectedPredicate = cb.and(cb.equal(root.<Users>get("doctorDirected"), employee));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicate, deletedPredicate, paymentPredicate, cb.or(doctorPredicate, assistantPredicate, doctorDirectedPredicate));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }
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
    @Override
    public List<Schedule> findPayedByEmployeeAndMethodAndDateBetween(Users employee, Methods method, Date from, Date to){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
        Root<Schedule> root = cq.from(Schedule.class);
        Predicate datePredicate = cb.and(cb.between(root.<Date>get("dateTimeStart"), from, to));
        Predicate doctorPredicate = cb.and(cb.equal(root.<Users>get("doctor"), employee));
        Predicate paymentPredicate = cb.and(cb.isNotEmpty(root.<Collection<Payments>>get("payments")));
        Predicate methodPredicate = cb.and(cb.equal(root.<Users>get("method"), method));
        Predicate assistantPredicate = cb.and(cb.equal(root.<Users>get("assistant"), employee));
        Predicate doctorDirectedPredicate = cb.and(cb.equal(root.<Users>get("doctorDirected"), employee));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicate, deletedPredicate, paymentPredicate, methodPredicate, cb.or(doctorPredicate, assistantPredicate, doctorDirectedPredicate));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

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
    @Override
    public List<Schedule> findPayedByMethodAndDateBetween(Methods method, Date from, Date to){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
        Root<Schedule> root = cq.from(Schedule.class);
        Predicate datePredicate = cb.and(cb.between(root.<Date>get("dateTimeStart"), from, to));
        Predicate methodPredicate = cb.and(cb.equal(root.<Users>get("method"), method));
        Predicate paymentPredicate = cb.and(cb.isNotEmpty(root.<Collection<Payments>>get("payments")));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicate, deletedPredicate, paymentPredicate, methodPredicate);
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }
    /**
     * Searches for all Schedule records that have starting date between the given date range
     *
     * @param from
     * @param to
     * @return List<Schedule> List of existing Schedule records that are not marked as deleted
     */
    @Override
    public List<Schedule> findPayedByDateBetween(Date from, Date to){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
        Root<Schedule> root = cq.from(Schedule.class);
        Predicate datePredicate = cb.and(cb.between(root.<Date>get("dateTimeStart"), from, to));
        Predicate paymentPredicate = cb.and(cb.isNotEmpty(root.<Collection<Payments>>get("payments")));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicate, deletedPredicate, paymentPredicate);
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Searches for all Schedule records that are assigned to the given employee and
     * start date is between the given date range inclusive from and exclusive to
     * and are payed(payment exists for them)
     *
     * @param employee - employee to search by
     * @param from     - start date of the given date range
     * @param to       - end date of the given range
     * @return List<Plan> List of existing Schedule records that are not marked as deleted
     */
    @Override
    public List<Schedule> findPayedByEmployeeAndStartDateBetweenExclusiveTo(Users employee, Date from, Date to){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
        Root<Schedule> root = cq.from(Schedule.class);
        Predicate breakPredicate = cb.and(cb.equal(root.<Users>get("doctor"), usersFacade.find(USERS_BREAK_ID)));
        Predicate doctorPredicate = cb.and(cb.equal(root.<Users>get("doctor"), employee));
        Predicate assistantPredicate = cb.and(cb.equal(root.<Users>get("assistant"), employee));
        Predicate paymentPredicate = cb.and(cb.isNotEmpty(root.<Collection<Payments>>get("payments")));
        Predicate doctorDirectedPredicate = cb.and(cb.equal(root.<Users>get("doctorDirected"), employee));
        Predicate datePredicate = cb.and(cb.between(root.<Date>get("dateTimeStart"), from, to));
        Predicate datePredicate2 = cb.and(cb.notEqual(root.<Date>get("dateTimeStart"), to));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicate, datePredicate2, deletedPredicate,paymentPredicate, cb.or(doctorPredicate, assistantPredicate, doctorDirectedPredicate, breakPredicate));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }


    /**
     * Searches for all Schedule records that are assigned to the given employee and
     * start date is between the given date range inclusive from and exclusive to
     *
     * @param employee - employee to search by
     * @param from     - start date of the given date range
     * @param to       - end date of the given range
     * @return List<Plan> List of existing Schedule records that are not marked as deleted
     */
    @Override
    public List<Schedule> findByEmployeeAndStartDateBetweenExclusiveTo(Users employee, Date from, Date to) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
        Root<Schedule> root = cq.from(Schedule.class);
        Predicate breakPredicate = cb.and(cb.equal(root.<Users>get("doctor"), usersFacade.find(USERS_BREAK_ID)));
        Predicate doctorPredicate = cb.and(cb.equal(root.<Users>get("doctor"), employee));
        Predicate assistantPredicate = cb.and(cb.equal(root.<Users>get("assistant"), employee));
        Predicate doctorDirectedPredicate = cb.and(cb.equal(root.<Users>get("doctorDirected"), employee));
        Predicate datePredicate = cb.and(cb.between(root.<Date>get("dateTimeStart"), from, to));
        Predicate datePredicate2 = cb.and(cb.notEqual(root.<Date>get("dateTimeStart"), to));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicate, datePredicate2, deletedPredicate, cb.or(doctorPredicate, assistantPredicate, doctorDirectedPredicate, breakPredicate));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Searches for all Schedule records that are assigned to the given room and
     * end date is between the given date range exclusive from and to
     *
     * @param employee - employee to search by
     * @param from     - start date of the given date range
     * @param to       - end date of the given range
     * @return List<Plan> List of existing Schedule records that are not marked as deleted
     */
    @Override
    public List<Schedule> findByEmployeeAndEndDateBetweenExclusiveFrom(Users employee, Date from, Date to) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
        Root<Schedule> root = cq.from(Schedule.class);
        Predicate breakPredicate = cb.and(cb.equal(root.<Users>get("doctor"), usersFacade.find(USERS_BREAK_ID)));
        Predicate doctorPredicate = cb.and(cb.equal(root.<Users>get("doctor"), employee));
        Predicate assistantPredicate = cb.and(cb.equal(root.<Users>get("assistant"), employee));
        Predicate doctorDirectedPredicate = cb.and(cb.equal(root.<Users>get("doctorDirected"), employee));
        Predicate datePredicate = cb.and(cb.between(root.<Date>get("dateTimeEnd"), from, to));
        Predicate datePredicate2 = cb.and(cb.notEqual(root.<Date>get("dateTimeEnd"), from));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicate, datePredicate2, deletedPredicate, cb.or(doctorPredicate, assistantPredicate, doctorDirectedPredicate,breakPredicate));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

}
