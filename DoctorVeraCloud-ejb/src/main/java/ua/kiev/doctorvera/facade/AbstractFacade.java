/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ua.kiev.doctorvera.entities.Identified;

/**
 * Class for implementing main operations with Generic (Identified) entity
 * @author Volodymyr Bodnar
 */
public abstract class AbstractFacade<T extends Identified<Integer>> {
	//Entity manager got by JNDI from container
	@PersistenceContext(unitName = "DoctorVera")
	private EntityManager em;
	
	//Actual Class of the T.class
	private Class<T> entityClass;

	//Just setting actual Class of T.class to entityClass variable
	//@param entityClass actual Class of T.class
    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    //@return Entity manager
    public EntityManager getEntityManager(){
    	 return em;
    };

	public void create(T entity) {
        getEntityManager().persist(entity);
    }
    
    public void edit(T  entity) {
        getEntityManager().merge(entity);
    }
    
    public void remove(T  entity) {
    	entity.setDeleted(true);
        getEntityManager().merge(entity);
    }

    public T find(Integer id) {
        return getEntityManager().find(entityClass, id);
    }
    
    public T find(T id) {
        return getEntityManager().find(entityClass, id.getId());
    }

    public List<T> findAll() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root).where(cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        List<T> resultList = getEntityManager().createQuery(cq).getResultList();
        return resultList;
    }

    @SuppressWarnings("unchecked")
	public List<T> findRange(int[] range) {
    	CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root).where(cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
    	CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root).where(cb.isFalse(root.<Boolean>get("deleted")));
        List<T> resultList = getEntityManager().createQuery(cq).getResultList();
        return resultList.size();
    }    
}
