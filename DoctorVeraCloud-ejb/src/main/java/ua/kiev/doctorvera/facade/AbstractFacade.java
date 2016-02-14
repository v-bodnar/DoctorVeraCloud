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
import ua.kiev.doctorvera.facadeLocal.CRUDFacade;
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
public abstract class AbstractFacade<T extends Identified<Integer>> implements CRUDFacade<T> {

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
    //private final String JOINS_AND_CONDITIONS = Config.getInstance().getString("JOINS_AND_CONDITIONS");

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
     * @param entity - any entity that has to be persisted
     */
    @Override
    public void create(T entity) {
        getEntityManager().persist(entity);
        LOG.info("Entity " + entity.getClass() + " id: " + entity.getId() + " persisted");
    }

    /**
     Creates new entities representation(record) in the persistent storage(Data Base)
     @param  entityList  list of concrete NEW entities to write
     */
    @Override
    public void create(List<T> entityList){
        for(T entity : entityList) {
            getEntityManager().persist(entity);
            LOG.info("Entity " + entity.getClass() + " id: " + entity.getId() + " persisted");
        }
    }

    /**
     * Updates existing record in the persistent storage that represents given entity
     * @param entity - any entity that has to be updated
     */
    @Override
    public T edit(T entity) {
        T ent = getEntityManager().merge(entity);
        LOG.info("Entity " + ent.getClass() + " id: " + ent.getId() + " updated");
        return ent;
    }

    /**
     * Updates existing record in the persistent storage that represents given entity
     * @param entityList - list of entities that has to be updated
     */
    @Override
    public List<T>  edit(List<T> entityList) {
        List<T> updatedEntityList = new LinkedList<>();
        for(T entity : entityList) {
            updatedEntityList.add(getEntityManager().merge(entity));
            LOG.info("Entity " + entity.getClass() + " id: " + entity.getId() + " updated");
        }
        return updatedEntityList;
    }

    /**
     * Marks existing record in the persistent storage that represents given entity as deleted
     * @param entity - any entity that has to be checked as deleted
     */
    @Override
    public T remove(T entity) {
        entity.setDeleted(true);
        entity.setDateCreated(new Date());
        T ent = getEntityManager().merge(entity);
        LOG.info("Entity " + ent.getClass() + " id: " + ent.getId() + " marked as removed");
        return ent;
    }

    /**
     Marks existing entity as deleted in the persistent storage(Data Base)
     @param  entityList list of existing entities to be marked as deleted
     */
    @Override
    public List<T> remove(List<T> entityList){
        List<T> updatedEntityList = new LinkedList<>();
        for(T entity : entityList) {
            entity.setDeleted(true);
            entity.setDateCreated(new Date());
            updatedEntityList.add(getEntityManager().merge(entity));
            LOG.info("Entity " + entity.getClass() + " id: " + entity.getId() + " updated");
        }
        return updatedEntityList;
    }

    /**
     * Permanently removes record from persistent storage
     * @param entity - entity that has to be removed
     */
    @Override
    public void removeFromDB(T entity) {
        getEntityManager().remove(entity);
    }

    /**
     * Permanently removes record from persistent storage
     * @param entityList - list of entities that has to be removed
     */
    @Override
    public void removeFromDB(List<T> entityList){
        for(T entity : entityList) {
            getEntityManager().remove(entity);
        }
    }

    /**
     * Searches for entity in the persistent storage with the given Id
     * @param id - unique identifier
     */
    @Override
    public T find(Integer id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Searches for the given entity in the persistent storage with the same Id
     * @param entity - entity to be found
     */
    @Override
    public T find(T entity) {
        return getEntityManager().find(entityClass, entity.getId());
    }

    /**
     * Searches for the given entity in the persistent storage with the same Id
     * @param entityList - list of entities to be found
     */
    @Override
    public List<T> find(List<T> entityList){
        List<T> foundEntityList = new LinkedList<>();
        for(T entity : entityList) {
            foundEntityList.add(getEntityManager().find(entityClass, entity.getId()));
        }
        return foundEntityList;
    }

    /**
     * Searches for the given entity in the persistent storage with the same Id
     * @param entitiesIdentifierList - list entities identifiers
     */
    @Override
    public List<T> find(Set<Integer> entitiesIdentifierList){
        List<T> foundEntityList = new LinkedList<>();
        for(Integer id : entitiesIdentifierList) {
            getEntityManager().find(entityClass, id);
        }
        return foundEntityList;
    }

    /**
     * Searches for all entities of the T type in the persistent storage
     */
    @Override
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
    @Override
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
    @Override
    public int count() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(entityClass);
        cq.select(cb.count(root)).where(cb.isFalse(root.<Boolean>get("deleted")));
        return getEntityManager().createQuery(cq).getSingleResult().intValue();
    }

    /**
     * Searches for all entities of the T type in the persistent storage and retrieves them with pagination,
     * filtered and sorted by the given params
     * @param firstResult number of entity in the result set to return from
     * @param maxResults amount of entities to return starting from firstResult
     * @param sortField name of field to sort by
     * @param sortOrder sort order
     * @param filters map with filters where key is an entity field to filter by and value is a value to search for
     *                key can contain "." which represents another entities field eg. user.userGroup.name
     * @return entities found by given criteria
     */
    @Override
    public List<T> findAll(Integer firstResult, Integer maxResults, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);

        Root<T> root = cq.from(entityClass);
        cq.distinct(true);

        Predicate conditions = cb.and(cb.isFalse(root.<Boolean>get("deleted")));

        //Creating filters
        for (Map.Entry<String, Object> filter : filters.entrySet()) {
            if (!filter.getValue().equals("")) {
                //try as string using like
                Path pathFilter;
                if (filter.getKey().contains(".")){ //for multiple depth attributes
                    conditions = cb.and(conditions, modifyFilter(cb, root, filter));
                }else{ // for single attribute
                    pathFilter = root.get(filter.getKey());
                    if (pathFilter != null) {
                        conditions = cb.and(conditions, createFilterCondition(cb, pathFilter, filter.getValue()));
                    }
                }
            }
        }

        cq.select(root).where(conditions);

        // This part of code is responsible for sorting and is duplicated in the next methods:
        // ua.kiev.doctorvera.facade.InitializerFacade.initializeLazyEntity(java.util.List<? extends ua.kiev.doctorvera.entities.Identified>, org.primefaces.model.SortOrder, java.lang.String)
        if (sortOrder != null && sortField != null && sortOrder.equals(SortOrder.ASCENDING)) {
            cq.orderBy(cb.asc(root.get(sortField)));
        } else if (sortOrder != null && sortField != null && sortOrder.equals(SortOrder.DESCENDING)) {
            cq.orderBy(cb.desc(root.get(sortField)));
        }

        List<T> resultList = getEntityManager().createQuery(cq).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
        return resultList;
    }

    /**
     * Searches for all entities of the T type in the persistent storage and returns number of entities
     * with applied pagination and filters
     * @param firstResult number of entity in the result set to return from
     * @param maxResults amount of entities to return starting from firstResult
     * @param filters map with filters where key is an entity field to filter by and value is a value to search for
     *                key can contain "." which represents another entities field eg. user.userGroup.name
     * @return entities found by given criteria
     */
    @Override
    public Integer count(Integer firstResult, Integer maxResults, Map<String, Object> filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<T> root = cq.from(entityClass);
        cq.distinct(true);

        Predicate conditions = cb.and(cb.isFalse(root.<Boolean>get("deleted")));

        //filter
        for (Map.Entry<String, Object> filter : filters.entrySet()) {
            if (!filter.getValue().equals("")) {
                //try as string using like
                Path pathFilter;
                if (filter.getKey().contains(".")){ //for multiple depth attributes
                    conditions = cb.and(conditions, modifyFilter(cb, root, filter));
                }else{ // for single attribute
                    pathFilter = root.get(filter.getKey());
                    if (pathFilter != null) {
                        conditions = cb.and(conditions, createFilterCondition(cb, pathFilter, filter.getValue()));
                    }
                }
            }
        }

        cq.select(cb.count(root)).where(conditions);

        return getEntityManager().createQuery(cq).getSingleResult().intValue();
    }

    /**
     * Helper method for creating filter SQL query
     * Creates Predicates for simple parsed filter value
     * @param cb criteria builder for entity we want to retrieve
     * @param path we filter by entities field represented by criteria Path
     * @param value value to search for
     * @return returns condition for filtering represented by criteria Predicate
     */
    private Predicate createFilterCondition(CriteriaBuilder cb, Path path, Object value){
        if(Integer.class.equals(path.getJavaType())){
            return cb.and(cb.equal(path, value));
        }else if(String.class.equals(path.getJavaType())){
            return cb.and(cb.like(path, "%" + value + "%"));
        }else if(Date.class.equals(path.getJavaType())){
            return cb.and(cb.equal(path, value));
        }if(Collection.class.isAssignableFrom(path.getJavaType())){
            return cb.and(cb.isMember(value, path));
        }if(path.getJavaType().isEnum()){
            return cb.and(cb.equal(path, value));
        }if(Identified.class.isAssignableFrom(path.getJavaType()) && Collection.class.isAssignableFrom(value.getClass())){
            return cb.and(path.in(value));
        }
        return null;
    }

    /**
     * Helper method for creating filter SQL query
     * Creates Predicates for complex filter value that contain "." in the key
     * It is based on tokens separated by . and currently only 2 levels depth is supported for String attributes
     * @param parentCb criteria builder for entity we want to retrieve
     * @param parentRoot criteria root for entity we want to retrieve
     * @param entry filter entry, key is field to filter by and value is value to search for
     * @return returns condition for filtering represented by criteria Predicate
     */
    private Predicate modifyFilter(CriteriaBuilder parentCb, Root<T> parentRoot, Map.Entry<String, Object> entry){
        List<String> tokens = Arrays.asList(entry.getKey().split("\\."));
        for(String token : tokens){
            int currentTokenIndex = tokens.indexOf(token);
            if(currentTokenIndex == tokens.size() -1){
                return null;
            }
            String nextToken = tokens.get(currentTokenIndex + 1);

            Path parentPath = parentRoot.get(token);

            //Creating subQuery to find all suitable values
            if (Identified.class.isAssignableFrom(parentPath.getJavaType()) && entry.getValue() instanceof String){
                CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
                CriteriaQuery cq = cb.createQuery(parentPath.getJavaType());

                Root root = cq.from(parentPath.getJavaType());
                cq.distinct(true);
                Path path = root.get(nextToken);
                if (!path.getJavaType().equals(String.class)) throw new RuntimeException("");
                Predicate stringPredicate = cb.and(cb.like(path, "%" + entry.getValue() + "%"));
                Predicate deleted = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
                cq.select(root).where(cb.and(deleted, stringPredicate));
                List resultList = getEntityManager().createQuery(cq).getResultList();

                return createFilterCondition(parentCb, parentPath, resultList);

            }else{
                throw new RuntimeException("At this moment only Identified and Strings are supported");
            }
        }

        throw new RuntimeException("There are no tokens in filter key");
    }

    /**
     * This method has to initialize all lazy members of the parsed entity
     * @param entity entity which fields have to be fetched
     * @return initialized entity
     */
    @Override
    public T initializeLazyEntity(T entity) {

        try {
            entity = (T) em.find(entity.getClass(),entity.getId());
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (field.getType().equals(Collection.class) || field.getType().equals(Set.class) || field.getType().equals(List.class)) {
                    field.setAccessible(true);
                    Collection collection = (Collection) field.get(entity);

                    collection.size();
                    field.set(entity, collection);
                    field.setAccessible(false);
                }
            }
            return entity;
        }  catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method has to initialize all lazy members of the parsed entity list, preserving order
     * @param entityList list of entities which fields have to be fetched
     * @param sortOrder order asc or desc
     * @param sortField field to sort by
     * @return list with initialized entities
     */
    @Override
    public List<T> initializeLazyEntity(List<T> entityList, SortOrder sortOrder, String sortField) {
        if (entityList.isEmpty()) return null;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        Class clazz = entityList.get(0).getClass();
        CriteriaQuery cq = cb.createQuery(clazz);
        Root root = cq.from(clazz);
//        Todo find resolution for org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags
//        List<String> manyToManyFields = new LinkedList<>();
//        for (Object attribute : ((RootImpl)root).getEntityType().getPluralAttributes()){
//            if(attribute instanceof PluralAttribute && (((PluralAttribute) attribute).getPersistentAttributeType() == Attribute.PersistentAttributeType.MANY_TO_MANY)){
//                //manyToManyFields.add(((PluralAttribute) attribute).getName());
//                root.fetch(((PluralAttribute) attribute).getName(),JoinType.INNER);
//            }
//        }

        cq.select(root).where(root.in(entityList));
        cq.distinct(true);

        // This part of code is responsible for sorting and is duplicated in the next methods:
        // ua.kiev.doctorvera.facade.AbstractFacade.count(java.lang.Integer, java.lang.Integer, java.lang.String, org.primefaces.model.SortOrder, java.util.Map<java.lang.String,java.lang.Object>)
        // ua.kiev.doctorvera.facade.AbstractFacade.findAll(java.lang.Integer, java.lang.Integer, java.lang.String, org.primefaces.model.SortOrder, java.util.Map<java.lang.String,java.lang.Object>)
        if (sortOrder != null && sortField != null && sortOrder.equals(SortOrder.ASCENDING)) {
            cq.orderBy(cb.asc(root.get(sortField)));
        } else if (sortOrder != null && sortField != null && sortOrder.equals(SortOrder.DESCENDING)) {
            cq.orderBy(cb.desc(root.get(sortField)));
        }

        List<T> result = new ArrayList<>(); //Todo find solution to initialize in one query
        for(T entity : (List<T>) em.createQuery(cq).getResultList()){
            result.add(initializeLazyEntity(entity));
        }
        return result;
    }

    /**
     * This method has to initialize all lazy members of the parsed entity list
     * @param entityList list of entities which fields have to be fetched
     * @return list with initialized entities
     */
    @Override
    public List<T> initializeLazyEntity(List<T> entityList) {
        List<T> result = initializeLazyEntity(entityList, null, null);
        if (result == null){
            return new ArrayList<T>();
        }else{
            return result;
        }
    }

}
