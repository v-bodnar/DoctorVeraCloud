/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facadeLocal;

import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import ua.kiev.doctorvera.entities.Address;

/**
 * Interface for declaring main operations with Generic (Identified) entity
 * @author Volodymyr Bodnar
 */
@Local
public interface AddressFacadeLocal{
    /**
    @return EntityManager got from container
    */
    EntityManager getEntityManager();

    /**
    Creates new entity representation(record) in the persistent storage(Data Base)
    @param  entity  concrete NEW entity to write
    */
    void create(Address entity);

    /**
    Updates existing entity representation(record) in the persistent storage(Data Base)
    @param  entity existing entity to be updated
    */
    void edit(Address entity);

    /**
    Marks existing entity as deleted in the persistent storage(Data Base)
    @param  entity existing entity to be marked as deleted
    */
    void remove(Address entity);

    /**
    @param id Unique object identifier
    @return T Identified existing entity represented by unique identifier
    */
    Address find(Integer id);

    /**
    @return List<T> List of all Identified existing entities that are not marked as deleted
    */
    List<Address> findAll();

    /**
    @return List<T> List of Identified existing entities that are not marked as deleted from range
    @param range
    */
    List<Address> findRange(int[] range);

    /**
    @return int records count
    */
    int count();
}
