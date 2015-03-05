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

/**
 * Interface for declaring main operations with Generic (Identified) entity
 * @author Volodymyr Bodnar
 */
@Local
public interface MethodTypesFacadeLocal{
    //@return EntityManager got from container
    public EntityManager getEntityManager();
    
    //Creates new entity representation(record) in the persistent storage(Data Base) 
    //@param  entity  concrete NEW entity to write 
    void create(MethodTypes entity);

    //Updates existing entity representation(record) in the persistent storage(Data Base) 
    //@param  entity existing entity to be updated 
    void edit(MethodTypes entity);

    //Marks existing entity as deleted in the persistent storage(Data Base) 
    //@param  entity existing entity to be marked as deleted 
    void remove(MethodTypes entity);

    //@param id Unique object identifier
    //@return T Identified existing entity represented by unique identifier 
    MethodTypes find(Integer id);

    //@return List<T> List of all Identified existing entities that are not marked as deleted
    List<MethodTypes> findAll();

    //@return List<T> List of Identified existing entities that are not marked as deleted from range 
    //@param int[] range
    List<MethodTypes> findRange(int[] range);

    //@return List<T> List of Identified existing entities that are not marked as deleted with param
    //@param String shortName
    List<MethodTypes> findByShortName(String shortName);

    //@return int records count
    int count();    
}
