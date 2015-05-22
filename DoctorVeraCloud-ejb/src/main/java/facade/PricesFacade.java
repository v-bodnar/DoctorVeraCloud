/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import ua.kiev.doctorvera.entities.Methods;
import ua.kiev.doctorvera.entities.Prices;
import ua.kiev.doctorvera.facadeLocal.PricesFacadeLocal;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/**
 * Class for implementing main operations with User entity
 *
 * @author Volodymyr Bodnar
 */
@Stateless
public class PricesFacade extends AbstractFacade<Prices> implements PricesFacadeLocal {

    public PricesFacade() {
        super(Prices.class);
    }

    /**
    Searches for actual (the last record before current date) price record with the given method
    @return Price record that is not marked as deleted
    @param method method to search for
    */
    @Override
    public Prices findLastPrice(Methods method) {
        if (method != null && method.getId() != null) {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Prices> cq = cb.createQuery(Prices.class);
            Root<Prices> root = cq.from(Prices.class);

            Predicate datePredicate = cb.lessThanOrEqualTo(root.<Date>get("dateTime"), new Date());
            Predicate methodPredicate = cb.equal(root.<String>get("method"), method);
            Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));

            cq.select(root).where(methodPredicate, deletedPredicate, datePredicate);
            cq.orderBy(cb.desc(root.<Date>get("dateTime")));
            cq.distinct(true);

            return getEntityManager().createQuery(cq).setMaxResults(1).getSingleResult();
        } else
            return null;
    }

    /**
    Searches for all Prices records with the given method
    @return List<Prices> List of Prices records that are not marked as deleted
    @param method method to search for
    */
    @Override
    public List<Prices> findByMethod(Methods method) {
        if (method != null && method.getId() != null) {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Prices> cq = cb.createQuery(Prices.class);
            Root<Prices> root = cq.from(Prices.class);
            Predicate methodPredicate = cb.equal(root.<String>get("method"), method);
            Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
            cq.select(root).where(methodPredicate, deletedPredicate);

            cq.distinct(true);
            List<Prices> resultList = getEntityManager().createQuery(cq).getResultList();

            return resultList;
        } else
            return null;
    }
}
