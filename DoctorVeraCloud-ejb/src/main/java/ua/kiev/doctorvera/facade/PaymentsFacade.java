/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.Payments;
import ua.kiev.doctorvera.entities.Schedule;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.PaymentsFacadeLocal;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/**
 * Class for implementing main operations with Payments entity
 * @author Volodymyr Bodnar
 */
@Stateless
public class PaymentsFacade extends AbstractFacade<Payments> implements PaymentsFacadeLocal {
		
    public PaymentsFacade() {
        super(Payments.class);
    }

    /**
     * Searches for payment for the given Schedule record
     * @param schedule record to search by
     * @return payment for the given Schedule record
     */
    @Override
    public Payments findBySchedule(Schedule schedule){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Payments> cq = cb.createQuery(Payments.class);
        Root<Payments> root = cq.from(Payments.class);
        Predicate schedulePredicate = cb.and(cb.equal(root.<Schedule>get("schedule"), schedule));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(schedulePredicate, deletedPredicate);
        cq.distinct(true);
        if(getEntityManager().createQuery(cq).getResultList().isEmpty())
            return null;
        else
            return getEntityManager().createQuery(cq).getSingleResult();
    }

}
