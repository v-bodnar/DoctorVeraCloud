/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.entities.Schedule;

/**
 * Class for implementing main operations with User entity
 * @author Volodymyr Bodnar
 */
@Stateless
public class ScheduleFacade extends AbstractFacade<Schedule> implements ScheduleFacadeLocal {
		
    public ScheduleFacade() {
        super(Schedule.class);
    }

    @Override
    public List<Schedule> findByRoomAndStartDate(Rooms room, Date from, Date to) {
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
    
    @Override
    public List<Schedule> findByRoomAndEndDate(Rooms room, Date from, Date to) {
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
    
    @Override
    public List<Schedule> findByRoomAndDatesInside(Rooms room, Date from, Date to) {
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
    
    @Override
    public List<Schedule> findByRoomAndDatesInsideOrEqual(Rooms room, Date from, Date to) {
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
    
    @Override
    public List<Schedule> findByRoomAndDatesOutsideOrEqual(Rooms room, Date from, Date to) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
        Root<Schedule> root = cq.from(Schedule.class);
        Predicate roomPredicate = cb.and(cb.equal(root.<Rooms>get("room"), room));
        Predicate datePredicateFrom = cb.and(cb.greaterThanOrEqualTo(root.<Date>get("dateTimeStart"), from));
        Predicate datePredicateTo = cb.and(cb.lessThanOrEqualTo(root.<Date>get("dateTimeEnd"), to));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicateFrom, datePredicateTo, deletedPredicate, roomPredicate);
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList(); 
    }
    
    @Override
    public Schedule findByRoomAndDateInside(Rooms room, Date date){
	    CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
	    CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
	    Root<Schedule> root = cq.from(Schedule.class);
	    Predicate roomPredicate = cb.and(cb.equal(root.<Rooms>get("room"), room));
	    Predicate datePredicateFrom = cb.and(cb.greaterThanOrEqualTo(root.<Date>get("dateTimeStart"), date));
	    Predicate datePredicateTo = cb.and(cb.lessThan(root.<Date>get("dateTimeEnd"), date));
	    Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
	    cq.select(root).where(datePredicateFrom, datePredicateTo, deletedPredicate, roomPredicate);
	    cq.distinct(true);
	    return getEntityManager().createQuery(cq).getSingleResult(); 
	}
    
    
    

}
