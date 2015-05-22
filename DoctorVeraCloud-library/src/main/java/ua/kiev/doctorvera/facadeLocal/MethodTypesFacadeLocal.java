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

/**
 * Interface for declaring main operations with Generic (Identified) entity
 * @author Volodymyr Bodnar
 */
@Local
public interface MethodTypesFacadeLocal{
    /**
    @return EntityManager got from container
    */
    EntityManager getEntityManager();

    /**
    Creates new entity representation(record) in the persistent storage(Data Base)
    @param  entity  concrete NEW entity to write
    */
    void create(MethodTypes entity);

    /**
    Updates existing entity representation(record) in the persistent storage(Data Base)
    @param  entity existing entity to be updated
    */
    void edit(MethodTypes entity);

    /**
    Marks existing entity as deleted in the persistent storage(Data Base)
    @param  entity existing entity to be marked as deleted
    */
    void remove(MethodTypes entity);

    /**
    @param id Unique object identifier
    @return MethodTypes Identified existing entity represented by unique identifier
    */
    MethodTypes find(Integer id);

    /**
    @return List<MethodTypes> List of all Identified existing entities that are not marked as deleted
    */
    List<MethodTypes> findAll();

    /**
    @return List<MethodTypes> List of Identified existing entities that are not marked as deleted from range
    @param range
    */
    List<MethodTypes> findRange(int[] range);

    /**
    @return int records count
    */
    int count();

    /**
    <strong>Permanently</strong> removes entity from the persistent storage(Data Base)
    @param entity - Entity that has to be removed
    */
    public void removeFromDB(MethodTypes entity);

    /**
    Searches for all MethodTypes by the given name
    @return List<MethodTypes> List of existing MethodTypes entities that are not marked as deleted
    @param shortName - short name to search for
    */
    List<MethodTypes> findByShortName(String shortName);
}
