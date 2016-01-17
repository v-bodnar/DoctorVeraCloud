package ua.kiev.doctorvera.facadeLocal;

import org.primefaces.model.SortOrder;
import ua.kiev.doctorvera.entities.Identified;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by volodymyr.bodnar on 1/9/2016.
 */
public interface CRUDFacade<T extends Identified<Integer>> {

    /**
     @return EntityManager got from container
     */
    EntityManager getEntityManager();

    /**
     Creates new entity representation(record) in the persistent storage(Data Base)
     @param  entity  concrete NEW entity to write
     */
    void create(T entity);

    /**
     Creates new entities representation(record) in the persistent storage(Data Base)
     @param  entityList  list of concrete NEW entities to write
     */
    void create(List<T> entityList);

    /**
     Updates existing entity representation(record) in the persistent storage(Data Base)
     @param  entity existing entity to be updated
     */
    T edit(T entity);

    /**
     * Updates existing record in the persistent storage that represents given entity
     * @param entityList - list of entities that has to be updated
     */
    List<T>  edit(List<T> entityList);

    /**
     Marks existing entity as deleted in the persistent storage(Data Base)
     @param  entity existing entity to be marked as deleted
     */
    T remove(T entity);

    /**
     Marks existing entity as deleted in the persistent storage(Data Base)
     @param  entityList list of existing entities to be marked as deleted
     */
    List<T> remove(List<T> entityList);

    /**
     * Permanently removes record from persistent storage
     * @param entity - entity that has to be removed
     */
    void removeFromDB(T entity);

    /**
     * Permanently removes record from persistent storage
     * @param entityList - list of entities that has to be removed
     */
    void removeFromDB(List<T> entityList);

    /**
     * Searches for entity in the persistent storage with the given Id
     * @param id - unique identifier
     */
    T find(Integer id);

    /**
     * Searches for the given entity in the persistent storage with the same Id
     * @param entity - entity to be found
     */
    T find(T entity);

    /**
     * Searches for the given entity in the persistent storage with the same Id
     * @param entityList - list of entities to be found
     */
    List<T> find(List<T> entityList);

    /**
     * Searches for the given entity in the persistent storage with the same Id
     * @param entitiesIdentifierList - list entities identifiers
     */
    List<T> find(Set<Integer> entitiesIdentifierList);

    /**
     * Searches for all entities of the T type in the persistent storage
     */
    List<T> findAll();

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
    List<T> findAll(Integer firstResult, Integer maxResults, String sortField, SortOrder sortOrder, Map<String, Object> filters);

    /**
     * Searches for all entities of the T type in the persistent storage in the given range
     */
    List<T> findRange(int[] range);

    /**
     * Counts the number of all entities the T type in the persistent storage
     */
    int count();

    /**
     * Searches for all entities of the T type in the persistent storage and returns number of entities
     * with applied pagination and filters
     * @param firstResult number of entity in the result set to return from
     * @param maxResults amount of entities to return starting from firstResult
     * @param filters map with filters where key is an entity field to filter by and value is a value to search for
     *                key can contain "." which represents another entities field eg. user.userGroup.name
     * @return entities found by given criteria
     */
    Integer count(Integer firstResult, Integer maxResults, Map<String, Object> filters);

    /**
     * This method has to initialize all lazy members of the parsed entity
     * @param entity entity which fields have to be fetched
     * @return initialized entity
     */
    T initializeLazyEntity(T entity);

    /**
     * This method has to initialize all lazy members of the parsed entity list
     * @param entityList list of entities which fields have to be fetched
     * @return list with initialized entities
     */
    List<T> initializeLazyEntity(List<T> entityList);

    /**
     * This method has to initialize all lazy members of the parsed entity list, preserving order
     * @param entityList list of entities which fields have to be fetched
     * @param sortOrder order asc or desc
     * @param sortField field to sort by
     * @return list with initialized entities
     */
    List<T> initializeLazyEntity(List<T> entityList, SortOrder sortOrder, String sortField);

}
