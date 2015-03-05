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

import ua.kiev.doctorvera.entities.Plan;
import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.utils.DateUtils;

/**
 *
 * @author Bodun
 */
@Stateless
public class PlanFacade extends AbstractFacade<Plan> implements PlanFacadeLocal {
	
    public PlanFacade() {
        super(Plan.class);
    }
    
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
    
    @Override
    public List<Plan> findByStartThisWeek() {
    	return findByStartDateBetween(DateUtils.getWeekStart(), DateUtils.getWeekEnd());
    }
    
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
    
    @Override
    public List<Plan> findByRoomAndStartDate(Rooms room, Date from, Date to) {
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
    
    @Override
    public List<Plan> findByRoomAndEndDate(Rooms room, Date from, Date to) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Plan> cq = cb.createQuery(Plan.class);
        Root<Plan> root = cq.from(Plan.class);
        Predicate roomPredicate = cb.and(cb.equal(root.<Rooms>get("room"), room));
        Predicate datePredicate = cb.and(cb.between(root.<Date>get("dateTimeEnd"), from, to));
        Predicate datePredicate2 = cb.and(cb.notEqual(root.<Date>get("dateTimeEnd"), from));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicate, datePredicate2, deletedPredicate, roomPredicate);
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList(); 
    }
    
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
    
    @Override
    public Plan findByRoomAndDateInside(Rooms room, Date from) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Plan> cq = cb.createQuery(Plan.class);
        Root<Plan> root = cq.from(Plan.class);
        Predicate roomPredicate = cb.and(cb.equal(root.<Rooms>get("room"), room));
        Predicate datePredicateFrom = cb.and(cb.lessThanOrEqualTo(root.<Date>get("dateTimeStart"), from));
        Predicate datePredicateTo = cb.and(cb.lessThan(root.<Date>get("dateTimeEnd"), from));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(datePredicateFrom, datePredicateTo, deletedPredicate, roomPredicate);
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getSingleResult(); 
    }
    
}
