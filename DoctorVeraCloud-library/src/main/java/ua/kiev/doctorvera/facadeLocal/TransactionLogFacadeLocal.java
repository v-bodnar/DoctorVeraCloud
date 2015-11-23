package ua.kiev.doctorvera.facadeLocal;

import ua.kiev.doctorvera.entities.TransactionLog;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Interface for declaring main operations with TransactionLog entity
 * @author Volodymyr Bodnar
 * @date 22.11.2015.
 */
public interface TransactionLogFacadeLocal {
    /**
     @return EntityManager got from container
     */
    EntityManager getEntityManager();

    /**
     Creates new entity representation(record) in the persistent storage(Data Base)
     @param  entity  concrete NEW entity to write
     */
    void create(TransactionLog entity);

    /**
     Updates existing entity representation(record) in the persistent storage(Data Base)
     @param  entity existing entity to be updated
     */
    void edit(TransactionLog entity);

    /**
     Marks existing entity as deleted in the persistent storage(Data Base)
     @param  entity existing entity to be marked as deleted
     */
    void remove(TransactionLog entity);

    /**
     @param id Unique object identifier
     @return T Identified existing entity represented by unique identifier
     */
    TransactionLog find(Integer id);

    /**
     @return List<T> List of all Identified existing entities that are not marked as deleted
     */
    List<TransactionLog> findAll();

    /**
     @return List<T> List of Identified existing entities that are not marked as deleted from range
     @param range
     */
    List<TransactionLog> findRange(int[] range);

    /**
     @return int records count
     */
    int count();
}
