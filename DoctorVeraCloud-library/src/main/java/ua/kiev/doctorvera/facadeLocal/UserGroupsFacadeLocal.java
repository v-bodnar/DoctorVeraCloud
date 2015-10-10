/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facadeLocal;

import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import ua.kiev.doctorvera.entities.Policy;
import ua.kiev.doctorvera.entities.UserGroups;
import ua.kiev.doctorvera.entities.Users;

/**
 *
 * @author Bodun
 */
@Local
public interface UserGroupsFacadeLocal {
    /**
       @return EntityManager got from container
       */
    EntityManager getEntityManager();

    /**
    Creates new entity representation(record) in the persistent storage(Data Base)
    @param  entity  concrete NEW entity to write
    */
    void create(UserGroups entity);

    /**
    Updates existing entity representation(record) in the persistent storage(Data Base)
    @param  entity existing entity to be updated
    */
    void edit(UserGroups entity);

    /**
    Marks existing entity as deleted in the persistent storage(Data Base)
    @param  entity existing entity to be marked as deleted
    */
    void remove(UserGroups entity);

    /**
    @param id Unique object identifier
    @return UserGroups Identified existing entity represented by unique identifier
    */
    UserGroups find(Integer id);

    /**
    @param group User Group that has to be found
    @return UserGroups Identified existing entity represented by unique identifier
    */
    UserGroups find(UserGroups group);

    /**
    @return List<UserGroups> List of all Identified existing entities that are not marked as deleted
    */
    List<UserGroups> findAll();

    /**
    @return List<UserGroups> List of Identified existing entities that are not marked as deleted from range
    @param range
    */
    List<UserGroups> findRange(int[] range);

    /**
    @return int records count
    */
    int count();

    /**
    <strong>Permanently</strong> removes entity from the persistent storage(Data Base)
    @param entity - Entity that has to be removed
    */
    void removeFromDB(UserGroups entity);

    /**
    * Searches for User Group by group name
    * @returns UserGroups entity that matches search parameter
    * @param groupName - name of the User Group
    */
    UserGroups findByName(String groupName);

    /**
    * Searches all User Groups that contain given user
    * @returns List<UserGroups> entity that matches search parameter
    * @param user - user to search by
    */
    List<UserGroups> findByUser(Users user);

    /**
    * Adds record to the reference table for referencing given user and User Group
    * @returns true - in the case operation was successful and false otherwise
    * @param user - User that has to be referenced
    * @param group - User Group that has to be referenced
    * @param userCreated - User that initiated process
    */
    boolean addUser(Users user, UserGroups group, Users userCreated);

    /**
    * Permanently deletes record from the reference table for removing reference between given user and User Group
    * @returns true - in the case operation was successful and false otherwise
    * @param user - User that has to be unreferenced
    * @param group - User Group that has to be unreferenced
    */
    boolean removeUser(Users user, UserGroups group);

    /**
     * Searches for all policies by given User Group
     * @returns UserGroups entity that matches search parameter
     * @param group - name of the User Group
     */
    List<Policy> findPoliciesByGroup(UserGroups group);

    /**
     * Searches for all policies by given User
     * @returns UserGroups entity that matches search parameter
     * @param user - user to search by
     */
    List<Policy> findPoliciesByUser(Users user);
}
