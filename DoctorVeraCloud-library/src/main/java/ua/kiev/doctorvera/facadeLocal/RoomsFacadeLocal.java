/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facadeLocal;

import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import ua.kiev.doctorvera.entities.Rooms;

/**
 * Interface for declaring main operations with Generic (Identified) entity
 * @author Volodymyr Bodnar
 */
@Local
public interface RoomsFacadeLocal{
    /**
   @return EntityManager got from container
   */
    EntityManager getEntityManager();

    /**
    Creates new entity representation(record) in the persistent storage(Data Base)
    @param  entity  concrete NEW entity to write
    */
    void create(Rooms entity);

    /**
    Updates existing entity representation(record) in the persistent storage(Data Base)
    @param  entity existing entity to be updated
    */
    void edit(Rooms entity);

    /**
    Marks existing entity as deleted in the persistent storage(Data Base)
    @param  entity existing entity to be marked as deleted
    */
    void remove(Rooms entity);

    /**
    @param id Unique object identifier
    @return Rooms Identified existing entity represented by unique identifier
    */
    Rooms find(Integer id);

    /**
    @return List<Rooms> List of all Identified existing entities that are not marked as deleted
    */
    List<Rooms> findAll();

    /**
    @return List<Rooms> List of Identified existing entities that are not marked as deleted from range
    @param  range
    */
    List<Rooms> findRange(int[] range);

    /**
    @return int records count
    */
    int count();

    /**
    <strong>Permanently</strong> removes entity from the persistent storage(Data Base)
    @param entity - Entity that has to be removed
    */
    public void removeFromDB(Rooms entity);
}
