package ua.kiev.doctorvera.facadeLocal;

import ua.kiev.doctorvera.entities.MessageTemplate;

import javax.persistence.EntityManager;
import java.util.List;

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
}
