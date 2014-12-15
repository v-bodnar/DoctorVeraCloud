package ua.kiev.doctorvera.facade;

import java.util.List;

import javax.persistence.EntityManager;

import ua.kiev.doctorvera.entities.Identified;

/**
 * Interface for declaring main CRUD operations with Generic(Identified) entity
 * @author Volodymyr Bodnar
 */
public interface GenericFacade<T extends Identified<Integer>> {
	
    //@return EntityManager got from container
    public EntityManager getEntityManager();
    
    //Creates new entity representation(record) in the persistent storage(Data Base) 
    //@param  entity  concrete NEW entity to write 
    void create(T entity);

    //Updates existing entity representation(record) in the persistent storage(Data Base) 
    //@param  entity existing entity to be updated 
    void edit(T entity);

    //Marks existing entity as deleted in the persistent storage(Data Base) 
    //@param  entity existing entity to be marked as deleted 
    void remove(T entity);

    //@param id Unique object identifier
    //@return T Identified existing entity represented by unique identifier 
    T find(Integer id);

    //@return List<T> List of all Identified existing entities that are not marked as deleted
    List<T> findAll();

    //@return List<T> List of Identified existing entities that are not marked as deleted from range 
    //@param int[] range
    List<T> findRange(int[] range);

    //@return int records count
    int count();
}
