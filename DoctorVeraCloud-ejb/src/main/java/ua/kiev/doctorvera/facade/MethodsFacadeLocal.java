/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import ua.kiev.doctorvera.entities.MethodTypes;
import ua.kiev.doctorvera.entities.Methods;

/**
 * Interface for declaring main operations with Generic (Identified) entity
 * @author Volodymyr Bodnar
 */
@Local
public interface MethodsFacadeLocal{
    //@return EntityManager got from container
    public EntityManager getEntityManager();
    
    //Creates new entity representation(record) in the persistent storage(Data Base) 
    //@param  entity  concrete NEW entity to write 
    void create(Methods entity);

    //Updates existing entity representation(record) in the persistent storage(Data Base) 
    //@param  entity existing entity to be updated 
    void edit(Methods entity);

    //Marks existing entity as deleted in the persistent storage(Data Base) 
    //@param  entity existing entity to be marked as deleted 
    void remove(Methods entity);

    //@param id Unique object identifier
    //@return T Identified existing entity represented by unique identifier 
    Methods find(Integer id);
    
    //@param userType Unique object identifier
    //@return T Identified existing entity represented by unique identifier 
    Methods find(Methods method);

    //@return List<T> List of all Identified existing entities that are not marked as deleted
    List<Methods> findAll();

    //@return List<T> List of Identified existing entities that are not marked as deleted from range 
    //@param int[] range
    List<Methods> findRange(int[] range);
    
    public Methods findByShortName(String shortName);
    public List<Methods> findByType(MethodTypes type);

    //@return int records count
    int count();    
}
