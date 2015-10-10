/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facadeLocal;

import ua.kiev.doctorvera.entities.Policy;
import ua.kiev.doctorvera.entities.UserGroups;
import ua.kiev.doctorvera.entities.Users;

import javax.ejb.Local;
import javax.persistence.EntityManager;
import java.util.List;

/**
 *
 * @author Bodun
 */
@Local
public interface PolicyFacadeLocal {
    /**
       @return EntityManager got from container
       */
    EntityManager getEntityManager();

    /**
    Creates new entity representation(record) in the persistent storage(Data Base)
    @param  entity  concrete NEW entity to write
    */
    void create(Policy entity);

    /**
    Updates existing entity representation(record) in the persistent storage(Data Base)
    @param  entity existing entity to be updated
    */
    void edit(Policy entity);

    /**
    Marks existing entity as deleted in the persistent storage(Data Base)
    @param  entity existing entity to be marked as deleted
    */
    void remove(Policy entity);

    /**
    @param id Unique object identifier
    @return Policy Identified existing entity represented by unique identifier
    */
    Policy find(Integer id);

    /**
    @param policy Policy that has to be found
    @return Policy Identified existing entity represented by unique identifier
    */
    Policy find(Policy policy);

    /**
    @return List<Policy> List of all Identified existing entities that are not marked as deleted
    */
    List<Policy> findAll();

    /**
    @return List<Policy> List of Identified existing entities that are not marked as deleted from range
    @param range
    */
    List<Policy> findRange(int[] range);

    /**
    @return int records count
    */
    int count();

    /**
    <strong>Permanently</strong> removes entity from the persistent storage(Data Base)
    @param entity - Entity that has to be removed
    */
    void removeFromDB(Policy entity);

    /**
    * Searches for User Group by group name
    * @returns Policy entity that matches search parameter
    * @param id - String Id of the User Group
    */
    Policy findByStringId(String id);

    /**
    * Searches all Policies that contain given user
    * @returns List<Policy> entity that matches search parameter
    * @param user - user to search by
    */
    List<Policy> findByUser(Users user);

    /**
     * Adds record to the reference table for referencing given policy and User Group
     * @returns true - in the case operation was successful and false otherwise
     * @param group - User Group that has to be referenced
     * @param policy - Policy that has to be referenced
     * @param userCreated - User that initiated process
     */
    boolean addUserGroups(UserGroups group, Policy policy, Users userCreated);

    /**
     * Permanently deletes record from the reference table for removing reference between given Policy and User Group
     * @returns true - in the case operation was successful and false otherwise
     * @param policy - Policy that has to be unreferenced
     * @param group - User Group that has to be unreferenced
     */
    public boolean removeUser(UserGroups group, Policy policy);
}
