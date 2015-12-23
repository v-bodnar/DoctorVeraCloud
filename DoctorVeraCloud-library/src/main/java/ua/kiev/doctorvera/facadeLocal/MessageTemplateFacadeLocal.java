package ua.kiev.doctorvera.facadeLocal;

import org.primefaces.model.SortOrder;
import ua.kiev.doctorvera.entities.MessageTemplate;
import ua.kiev.doctorvera.entities.Users;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

/**
 * Interface for declaring main operations with MessageTemplate entity
 * @author Volodymyr Bodnar
 * @date 22.11.2015.
 */
public interface MessageTemplateFacadeLocal {
    /**
     @return EntityManager got from container
     */
    EntityManager getEntityManager();

    /**
     Creates new entity representation(record) in the persistent storage(Data Base)
     @param  entity  concrete NEW entity to write
     */
    void create(MessageTemplate entity);

    /**
     Updates existing entity representation(record) in the persistent storage(Data Base)
     @param  entity existing entity to be updated
     */
    void edit(MessageTemplate entity);

    /**
     Marks existing entity as deleted in the persistent storage(Data Base)
     @param  entity existing entity to be marked as deleted
     */
    void remove(MessageTemplate entity);

    /**
     @param id Unique object identifier
     @return T Identified existing entity represented by unique identifier
     */
    MessageTemplate find(Integer id);

    /**
     @param entity
     @return T Identified existing entity represented by unique identifier
     */
    MessageTemplate find(MessageTemplate entity);

    /**
     @return List<T> List of all Identified existing entities that are not marked as deleted
     */
    List<MessageTemplate> findAll();

    /**
     @return List<T> List of Identified existing entities that are not marked as deleted from range
     @param range
     */
    List<MessageTemplate> findRange(int[] range);

    /**
     @return int records count
     */
    int count();

    /**
     @return List<MessageTemplate> List of all MessageTemplate existing entities that are not marked as deleted
     and have corresponding type value
     @param type search for templates with this type
     */
    List<MessageTemplate> findByType(MessageTemplate.Type type);

    /**
     * Searches for all entities of the T type in the persistent storage and retrieves them with pagination
     */
    List<MessageTemplate> findAll(Integer firstResult, Integer maxResults, String sortField, SortOrder sortOrder, Map<String, Object> filters);

    Integer count(Integer firstResult, Integer maxResults, String sortField, SortOrder sortOrder, Map<String, Object> filters);

}
