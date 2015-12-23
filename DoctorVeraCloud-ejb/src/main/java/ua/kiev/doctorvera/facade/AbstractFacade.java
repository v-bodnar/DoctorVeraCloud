/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import org.primefaces.model.SortOrder;
import ua.kiev.doctorvera.entities.Identified;
import ua.kiev.doctorvera.entities.UserGroups;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.resources.Config;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Logger;

/**
 * Class for implementing main operations with Generic (Identified) entity
 *
 * @author Volodymyr Bodnar
 */
public abstract class AbstractFacade<T extends Identified<Integer>> {

    private final static Logger LOG = Logger.getLogger(AbstractFacade.class.getName());

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
     * This field identifies key of the map with table joins and column conditions inside filters map
     */
    private final String JOINS_AND_CONDITIONS = Config.getInstance().getProperty("JOINS_AND_CONDITIONS");

    /**
     * Just setting actual Class of T.class to entityClass variable
     *
     * @param entityClass actual Class of T.class
     */
    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected AbstractFacade() {
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
        LOG.info("Entity " + entity.getClass() + " id: " + entity.getId() + " persisted");
    }

    /**
     * Updates existing record in the persistent storage that represents given entity
     *
     * @param entity - any entity that has to be updated
     */
    public void edit(T entity) {
        getEntityManager().merge(entity);
        LOG.info("Entity " + entity.getClass() + " id: " + entity.getId() + " updated");
    }

    /**
     * Marks existing record in the persistent storage that represents given entity as deleted
     *
     * @param entity - any entity that has to be checked as deleted
     */
    public void remove(T entity) {
        entity.setDeleted(true);
        getEntityManager().merge(entity);
        LOG.info("Entity " + entity.getClass() + " id: " + entity.getId() + " marked as removed");
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
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(entityClass);
        cq.select(cb.count(root)).where(cb.isFalse(root.<Boolean>get("deleted")));
        return getEntityManager().createQuery(cq).getSingleResult().intValue();
    }

    /**
     * Permanently removes record from persistant storage
     *
     * @param entity - entity that has to be removed
     */
    public void removeFromDB(T entity) {
        getEntityManager().remove(entity);
    }

    /**
     * Searches for all entities of the T type in the persistent storage and retrieves them with pagination
     */
    public List<T> findAll(Integer firstResult, Integer maxResults, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);

        Root<T> root = cq.from(entityClass);
        cq.distinct(true);

        Predicate conditions = cb.and(cb.isFalse(root.<Boolean>get("deleted")));

        //filter
        for (Map.Entry<String, Object> filter : filters.entrySet()) {
            if (!filter.getValue().equals("")) {
                //try as string using like
                Path pathFilter = root.get(filter.getKey());
                if (pathFilter != null) {
                    conditions = cb.and(conditions, createFilterCondition(cb, pathFilter, filter.getValue()));
                }
            }
        }

        cq.select(root).where(conditions);

        if (sortOrder != null && sortField != null && sortOrder.equals(SortOrder.ASCENDING)) {
            cq.orderBy(cb.asc(root.get(sortField)));
        } else if (sortOrder != null && sortField != null && sortOrder.equals(SortOrder.DESCENDING)) {
            cq.orderBy(cb.desc(root.get(sortField)));
        }

        List<T> resultList = getEntityManager().createQuery(cq).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
        return resultList;
    }

    /**
     * Searches for all entities of the T type in the persistent storage and retrieves them with pagination applies filters and counts selected rows
     * @return number of found records
     */
    public Integer count(Integer firstResult, Integer maxResults, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<T> root = cq.from(entityClass);
        cq.distinct(true);

        Predicate conditions = cb.and(cb.isFalse(root.<Boolean>get("deleted")));

        //filter
        for (Map.Entry<String, Object> filter : filters.entrySet()) {
            if (!filter.getValue().equals("")) {
                //try as string using like
                Path pathFilter = root.get(filter.getKey());
                if (pathFilter != null) {
                    conditions = cb.and(conditions, createFilterCondition(cb, pathFilter, filter.getValue()));
                }
            }
        }

        cq.select(cb.count(root)).where(conditions);

        if (sortOrder != null && sortField != null && sortOrder.equals(SortOrder.ASCENDING)) {
            cq.orderBy(cb.asc(root.get(sortField)));
        } else if (sortOrder != null && sortField != null && sortOrder.equals(SortOrder.DESCENDING)) {
            cq.orderBy(cb.desc(root.get(sortField)));
        }

        return getEntityManager().createQuery(cq).getSingleResult().intValue();
    }

    /**
     * Creates Predicates for parsed filter value
     * @return predicate for current filter
     */
    private Predicate createFilterCondition(CriteriaBuilder cb, Path path, Object value){
        if(Integer.class.equals(path.getJavaType())){
            return cb.and(cb.equal(path, value));
        }else if(String.class.equals(path.getJavaType())){
            return cb.and(cb.like(path, "%" + value + "%"));
        }else if(Date.class.equals(path.getJavaType())){
            return cb.and(cb.equal(path, value));
        }if(Collection.class.equals(path.getJavaType())){
            return cb.and(cb.isMember(value, path));
        }if(path.getJavaType().isEnum()){
            return cb.and(cb.equal(path, value));
        }
        return null;
    }

}
