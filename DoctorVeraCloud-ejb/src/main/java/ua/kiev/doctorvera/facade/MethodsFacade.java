/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ua.kiev.doctorvera.entities.MethodTypes;
import ua.kiev.doctorvera.entities.Methods;

/**
 * Class for implementing main operations with User entity
 * @author Volodymyr Bodnar
 */
@Stateless
public class MethodsFacade extends AbstractFacade<Methods> implements MethodsFacadeLocal {
		
    public MethodsFacade() {
        super(Methods.class);
    }
    
    @Override
    public Methods findByShortName(String shortName){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Methods> cq = cb.createQuery(Methods.class);
        Root<Methods> root = cq.from(Methods.class);
        
        cq.select(root).where(cb.equal(root.<String>get("shortName"), shortName),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        List<Methods> resultList = getEntityManager().createQuery(cq).getResultList();
        
        if (resultList.size() == 1) return resultList.get(0);
        else return null;
    }
    
    @Override
    public List<Methods> findByType(MethodTypes type){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Methods> cq = cb.createQuery(Methods.class);
        Root<Methods> root = cq.from(Methods.class);
        
        cq.select(root).where(cb.equal(root.<String>get("methodType"), type),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        List<Methods> resultList = getEntityManager().createQuery(cq).getResultList();
        
        return resultList;
    }

}
