/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facadeLocal;

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
    /**
    @return EntityManager got from container
    */
    EntityManager getEntityManager();

    /**
    Creates new entity representation(record) in the persistent storage(Data Base)
    @param  entity  concrete NEW entity to write
    */
    void create(Methods entity);

    /**
    Updates existing entity representation(record) in the persistent storage(Data Base)
    @param  entity existing entity to be updated
    */
    void edit(Methods entity);

    /**
    Marks existing entity as deleted in the persistent storage(Data Base)
    @param  entity existing entity to be marked as deleted
    */
    void remove(Methods entity);

    /**
    @param id Unique object identifier
    @return Methods Identified existing entity represented by unique identifier
    */
    Methods find(Integer id);

    /**
    * Searches for the given method in the persistent storage with the same Id
    * @param method - Method that has to be found
    * @return Methods Identified existing entity represented by unique identifier
    */
    Methods find(Methods method);

    /**
    @return List<Methods> List of all Identified existing entities that are not marked as deleted
    */
    List<Methods> findAll();

    /**
    @return List<Methods> List of Identified existing entities that are not marked as deleted from range
    @param range
    */
    List<Methods> findRange(int[] range);

    /**
    @return int records count
    */
    int count();

    /**
    <strong>Permanently</strong> removes entity from the persistent storage(Data Base)
    @param entity - Entity that has to be removed
    */
    void removeFromDB(Methods entity);

    /**
    * Searches for method with the given short Name
    * @param shortName - short Name of the method
    * @return Method that corresponds given parameters
    * */
    Methods findByShortName(String shortName);

    /**
    * Searches for all methods with the given Method Type
    * @param type - Method Type of the method
    * @return List of methods Method that corresponds given parameters
    * */
    List<Methods> findByType(MethodTypes type);
}
