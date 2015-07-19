/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.MethodTypes;
import ua.kiev.doctorvera.entities.Methods;
import ua.kiev.doctorvera.facadeLocal.MethodsFacadeLocal;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Class for implementing main operations with User entity
 * @author Volodymyr Bodnar
 */
@Stateless
public class MethodsFacade extends AbstractFacade<Methods> implements MethodsFacadeLocal {
		
    public MethodsFacade() {
        super(Methods.class);
    }

    /**
    * Searches for method with the given short Name
    * @param shortName - short Name of the method
    * @return Method that corresponds given parameters
    * */
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

    /**
    * Searches for all methods with the given Method Type
    * @param type - Method Type of the method
    * @return List of methods Method that corresponds given parameters
    * */
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
