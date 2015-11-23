package ua.kiev.doctorvera.facadeLocal;

import ua.kiev.doctorvera.entities.MessageLog;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Interface for declaring main operations with MessageLog entity
 * @author Volodymyr Bodnar
 * @date 22.11.2015.
 */
public interface MessageLogFacadeLocal {
    /**
     @return EntityManager got from container
     */
    EntityManager getEntityManager();

    /**
     Creates new entity representation(record) in the persistent storage(Data Base)
     @param  entity  concrete NEW entity to write
     */
    void create(MessageLog entity);

    /**
     Updates existing entity representation(record) in the persistent storage(Data Base)
     @param  entity existing entity to be updated
     */
    void edit(MessageLog entity);

    /**
     Marks existing entity as deleted in the persistent storage(Data Base)
     @param  entity existing entity to be marked as deleted
     */
    void remove(MessageLog entity);

    /**
     @param id Unique object identifier
     @return T Identified existing entity represented by unique identifier
     */
    MessageLog find(Integer id);

    /**
     @return List<T> List of all Identified existing entities that are not marked as deleted
     */
    List<MessageLog> findAll();

    /**
     @return List<T> List of Identified existing entities that are not marked as deleted from range
     @param range
     */
    List<MessageLog> findRange(int[] range);

    /**
     @return int records count
     */
    int count();
}
