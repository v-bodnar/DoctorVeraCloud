/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facadeLocal;

import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import ua.kiev.doctorvera.entities.UserGroups;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.entities.UsersHasUserGroups;

/**
 *
 * @author Bodun
 */
@Local
public interface UsersHasUserGroupsFacadeLocal {
    /**
    @return EntityManager got from container
    */
    EntityManager getEntityManager();

    /**
    Creates new entity representation(record) in the persistent storage(Data Base)
    @param  entity  concrete NEW entity to write
    */
    void create(UsersHasUserGroups entity);

    /**
    Updates existing entity representation(record) in the persistent storage(Data Base)
    @param  entity existing entity to be updated
    */
    void edit(UsersHasUserGroups entity);

    /**
    Marks existing entity as deleted in the persistent storage(Data Base)
    @param  entity existing entity to be marked as deleted
    */
    void remove(UsersHasUserGroups entity);

    /**
    @param id Unique object identifier
    @return UsersHasUserGroups Identified existing entity represented by unique identifier
    */
    UsersHasUserGroups find(Integer id);

    /**
    @return List<UsersHasUserGroups> List of all Identified existing entities that are not marked as deleted
    */
    List<UsersHasUserGroups> findAll();

    /**
    @return List<UsersHasUserGroups> List of Identified existing entities that are not marked as deleted from range
    @param range
    */
    List<UsersHasUserGroups> findRange(int[] range);

    /**
    @return int records count
    */
    int count();

    /**
    <strong>Permanently</strong> removes entity from the persistent storage(Data Base)
    @param entity - Entity that has to be removed
    */
    void removeFromDB(UsersHasUserGroups entity);

    /**
    Searches for given user in the Reference table UsersHasUserGroups and returns all found records
    @param user - user to search for
    @return List<UsersHasUserGroups> List of UsersHasUserGroups records that match search parameter
     */
    List<UsersHasUserGroups> findGroupsByUser(Users user);

    /**
    Searches for given User Group in the Reference table UsersHasUserGroups and returns all found records
    @param group - User Group to search for
    @return List<UsersHasUserGroups> List of UsersHasUserGroups records that match search parameter
     */
    List<UsersHasUserGroups> findUsersByGroup(UserGroups group);
    
}
