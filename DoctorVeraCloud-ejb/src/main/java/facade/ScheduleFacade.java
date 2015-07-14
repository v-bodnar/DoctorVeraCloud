/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.entities.Schedule;
import ua.kiev.doctorvera.facadeLocal.ScheduleFacadeLocal;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/**
 * Class for implementing main operations with User entity
 * @author Volodymyr Bodnar
 */
@Stateless
public class ScheduleFacade extends AbstractFacade<Schedule> implements ScheduleFacadeLocal {
		
    public ScheduleFacade() {
        super(Schedule.class);
    }

    /**
    Searches for all Schedule records that are assigned to the given room and
    start date is between the given date range inclusive from and exclusive to
    @return List<Plan> List of existing Schedule records that are not marked as deleted
    @param room - Room to search by
    @param from - start date of the given date range
    @param to - end date of the given range
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
    Searches for all Schedule records that are assigned to the given room and
    end date is between the given date range exclusive from and to
    @return List<Plan> List of existing Schedule records that are not marked as deleted
    @param room - Room to search by
    @param from - start date of the given date range
    @param to - end date of the given range
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
    Searches for all Schedule records that are assigned to the given room and
    end date is between the given date range inclusive to and exclusive from
    @return List<Plan> List of existing Schedule records that are not marked as deleted
    @param room - Room to search by
    @param from - start date of the given date range
    @param to - end date of the given range
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
    Searches for all Schedule records that are assigned to the given room and
    the given date range is between start date and end date of the schedule record inclusive this dates
    @return List<Plan> List of existing Schedule records that are not marked as deleted
    @param room - Room to search by
    @param from - start date of the given date range
    @param to - end date of the given range
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
    Searches for all Schedule records that are assigned to the given room and
    start date and end date of the schedule record is between the given date range exclusive this dates
    @return List<Plan> List of existing Schedule records that are not marked as deleted
    @param room - Room to search by
    @param from - start date of the given date range
    @param to - end date of the given range
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
    Searches for all Schedule records that are assigned to the given room and
    the given date is between start date and end date of the schedule record inclusive start date and exclusive end date
    @return List<Plan> List of existing Schedule records that are not marked as deleted
    @param room - Room to search by
    @param date - start date of the given date range
    */
    @Override
    public Schedule findByRoomAndDateInside(Rooms room, Date date){
	    CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
	    CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
	    Root<Schedule> root = cq.from(Schedule.class);
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
    Searches for all Schedule records that have starting date between the given date range
    @return List<Schedule> List of existing Schedule records that are not marked as deleted
    @param from - date to search from
    @param to - date to search to
    */
    @Override
    public List<Schedule> findByStartDateBetween(Date from, Date to){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
        Root<Schedule> root = cq.from(Schedule.class);
        Predicate datePredicate = cb.and(cb.between(root.<Date>get("dateTimeStart"), from, to));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicate, deletedPredicate);
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }
    

}