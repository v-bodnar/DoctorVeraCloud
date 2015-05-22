/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facadeLocal;

import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import ua.kiev.doctorvera.entities.UserTypes;
import ua.kiev.doctorvera.entities.Users;

/**
 *
 * @author Bodun
 */
@Local
public interface UserTypesFacadeLocal {
    /**
       @return EntityManager got from container
       */
    EntityManager getEntityManager();

    /**
    Creates new entity representation(record) in the persistent storage(Data Base)
    @param  entity  concrete NEW entity to write
    */
    void create(UserTypes entity);

    /**
    Updates existing entity representation(record) in the persistent storage(Data Base)
    @param  entity existing entity to be updated
    */
    void edit(UserTypes entity);

    /**
    Marks existing entity as deleted in the persistent storage(Data Base)
    @param  entity existing entity to be marked as deleted
    */
    void remove(UserTypes entity);

    /**
    @param id Unique object identifier
    @return UserTypes Identified existing entity represented by unique identifier
    */
    UserTypes find(Integer id);

    /**
    @param type User Type that has to be found
    @return UserTypes Identified existing entity represented by unique identifier
    */
    UserTypes find(UserTypes type);

    /**
    @return List<UserTypes> List of all Identified existing entities that are not marked as deleted
    */
    List<UserTypes> findAll();

    /**
    @return List<UserTypes> List of Identified existing entities that are not marked as deleted from range
    @param int[] range
    */
    List<UserTypes> findRange(int[] range);

    /**
    @return int records count
    */
    int count();

    /**
    <strong>Permanently</strong> removes entity from the persistent storage(Data Base)
    @param entity - Entity that has to be removed
    */
    void removeFromDB(UserTypes entity);

    /**
    * Searches for User Type by type name
    * @returns UserTypes entity that matches search parameter
    * @param typeName - name of the User Type
    */
    public UserTypes findByName(String typeName);

    /**
    * Searches all User Types that contain given user
    * @returns List<UserTypes> entity that matches search parameter
    * @param user - user to search by
    */
    public List<UserTypes> findByUser(Users user);

    /**
    * Adds record to the reference table for referencing given user and User Type
    * @returns true - in the case operation was successful and false otherwise
    * @param user - User that has to be referenced
    * @param type - User Type that has to be referenced
    * @param userCreated - User that initiated process
    */
    public boolean addUser(Users user, UserTypes type, Users userCreated);

    /**
    * Permanently deletes record from the reference table for removing reference between given user and User Type
    * @returns true - in the case operation was successful and false otherwise
    * @param user - User that has to be unreferenced
    * @param type - User Type that has to be unreferenced
    */
    public boolean removeUser(Users user, UserTypes type);
}
