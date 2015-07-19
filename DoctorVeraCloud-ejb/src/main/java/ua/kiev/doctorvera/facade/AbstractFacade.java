/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.Identified;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Class for implementing main operations with Generic (Identified) entity
 *
 * @author Volodymyr Bodnar
 */
public abstract class AbstractFacade<T extends Identified<Integer>> {
    /*
     * Entity manager got by JNDI from container
     */
    @PersistenceContext(unitName = "DoctorVera")
    private EntityManager em;

    /**
     * Actual Class of the T.class
     */
    private Class<T> entityClass;

    /**
     * Just setting actual Class of T.class to entityClass variable
     *
     * @param entityClass actual Class of T.class
     */
    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * @return Entity manager
     */
    public EntityManager getEntityManager() {
        return em;
    }

    /**
     * Creates new record in the persistent storage that represents given entity
     *
     * @param entity - any entity that has to be persisted
     */
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    /**
     * Updates existing record in the persistent storage that represents given entity
     *
     * @param entity - any entity that has to be updated
     */
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    /**
     * Marks existing record in the persistent storage that represents given entity as deleted
     *
     * @param entity - any entity that has to be checked as deleted
     */
    public void remove(T entity) {
        entity.setDeleted(true);
        getEntityManager().merge(entity);
    }

    /**
     * Searches for entity in the persistent storage with the given Id
     *
     * @param id - unique identifier
     */
    public T find(Integer id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Searches for the given entity in the persistent storage with the same Id
     *
     * @param entity - entity to be found
     */
    public T find(T entity) {
        return getEntityManager().find(entityClass, entity.getId());
    }

    /**
     * Searches for all entities of the T type in the persistent storage
     */
    public List<T> findAll() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root).where(cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        List<T> resultList = getEntityManager().createQuery(cq).getResultList();
        return resultList;
    }

    /**
     * Searches for all entities of the T type in the persistent storage in the given range
     */
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

    /**
     * Counts the number of all entities the T type in the persistent storage
     */
    public int count() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root).where(cb.isFalse(root.<Boolean>get("deleted")));
        List<T> resultList = getEntityManager().createQuery(cq).getResultList();
        return resultList.size();
    }

    /**
     * Permanently removes record from persistant storage
     *
     * @param entity - entity that has to be removed
     */
    public void removeFromDB(T entity) {
        getEntityManager().remove(entity);
    }
}
