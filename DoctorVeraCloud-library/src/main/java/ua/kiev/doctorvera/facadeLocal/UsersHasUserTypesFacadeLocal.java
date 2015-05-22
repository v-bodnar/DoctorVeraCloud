/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facadeLocal;

import java.io.IOException;
import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import ua.kiev.doctorvera.entities.UserTypes;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.entities.UsersHasUserTypes;

/**
 *
 * @author Bodun
 */
@Local
public interface UsersHasUserTypesFacadeLocal {
    /**
    @return EntityManager got from container
    */
    EntityManager getEntityManager();

    /**
    Creates new entity representation(record) in the persistent storage(Data Base)
    @param  entity  concrete NEW entity to write
    */
    void create(UsersHasUserTypes entity);

    /**
    Updates existing entity representation(record) in the persistent storage(Data Base)
    @param  entity existing entity to be updated
    */
    void edit(UsersHasUserTypes entity);

    /**
    Marks existing entity as deleted in the persistent storage(Data Base)
    @param  entity existing entity to be marked as deleted
    */
    void remove(UsersHasUserTypes entity);

    /**
    @param id Unique object identifier
    @return UsersHasUserTypes Identified existing entity represented by unique identifier
    */
    UsersHasUserTypes find(Integer id);

    /**
    @return List<UsersHasUserTypes> List of all Identified existing entities that are not marked as deleted
    */
    List<UsersHasUserTypes> findAll();

    /**
    @return List<UsersHasUserTypes> List of Identified existing entities that are not marked as deleted from range
    @param range
    */
    List<UsersHasUserTypes> findRange(int[] range);

    /**
    @return int records count
    */
    int count();

    /**
    <strong>Permanently</strong> removes entity from the persistent storage(Data Base)
    @param entity - Entity that has to be removed
    */
    void removeFromDB(UsersHasUserTypes entity);

    /**
    Searches for given user in the Reference table UsersHasUserTypes and returns all found records
    @param user - user to search for
    @return List<UsersHasUserTypes> List of UsersHasUserTypes records that match search parameter
     */
    List<UsersHasUserTypes> findTypesByUser(Users user);

    /**
    Searches for given User Type in the Reference table UsersHasUserTypes and returns all found records
    @param type - User Type to search for
    @return List<UsersHasUserTypes> List of UsersHasUserTypes records that match search parameter
     */
    List<UsersHasUserTypes> findUsersByType(UserTypes type);
    
}
