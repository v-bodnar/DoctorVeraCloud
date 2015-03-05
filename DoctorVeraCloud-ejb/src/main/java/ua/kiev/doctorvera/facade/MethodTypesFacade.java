/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ua.kiev.doctorvera.entities.MethodTypes;
import ua.kiev.doctorvera.entities.Methods;

import java.util.List;

/**
 * Class for implementing main operations with User entity
 * @author Volodymyr Bodnar
 */
@Stateless
public class MethodTypesFacade extends AbstractFacade<MethodTypes> implements MethodTypesFacadeLocal {
		
    public MethodTypesFacade() {
        super(MethodTypes.class);
    }

    //@return List<T> List of Identified existing entities that are not marked as deleted with param
    //@param String shortName
    @Override
    public List<MethodTypes> findByShortName(String shortName) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<MethodTypes> cq = cb.createQuery(MethodTypes.class);
        Root<MethodTypes> root = cq.from(MethodTypes.class);

        cq.select(root).where(cb.equal(root.<String>get("shortName"), shortName),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        List<MethodTypes> resultList = getEntityManager().createQuery(cq).getResultList();

        return resultList;
    }
}
