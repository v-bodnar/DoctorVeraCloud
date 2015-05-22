/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facadeLocal;

import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import ua.kiev.doctorvera.entities.Methods;
import ua.kiev.doctorvera.entities.Prices;

/**
 * Interface for declaring main operations with Generic (Identified) entity
 * @author Volodymyr Bodnar
 */
@Local
public interface PricesFacadeLocal{
    /**
   @return EntityManager got from container
   */
    EntityManager getEntityManager();

    /**
    Creates new entity representation(record) in the persistent storage(Data Base)
    @param  entity  concrete NEW entity to write
    */
    void create(Prices entity);

    /**
    Updates existing entity representation(record) in the persistent storage(Data Base)
    @param  entity existing entity to be updated
    */
    void edit(Prices entity);

    /**
    Marks existing entity as deleted in the persistent storage(Data Base)
    @param  entity existing entity to be marked as deleted
    */
    void remove(Prices entity);

    /**
    @param id Unique object identifier
    @return Prices Identified existing entity represented by unique identifier
    */
    Prices find(Integer id);

    /**
    @return List<Prices> List of all Identified existing entities that are not marked as deleted
    */
    List<Prices> findAll();

    /**
    @return List<Prices> List of Identified existing entities that are not marked as deleted from range
    @param range
    */
    List<Prices> findRange(int[] range);

    /**
    @return int records count
    */
    int count();

    /**
    <strong>Permanently</strong> removes entity from the persistent storage(Data Base)
    @param entity - Entity that has to be removed
    */
    void removeFromDB(Prices entity);

    /**
    Searches for actual (the last record before current date) price record with the given method
    @return Price record that is not marked as deleted
    @param method method to search for
    */
    Prices findLastPrice(Methods method);

    /**
    Searches for all Prices records with the given method
    @return List<Prices> List of Prices records that are not marked as deleted
    @param method method to search for
    */
    List<Prices> findByMethod(Methods method);
}
