/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facadeLocal;

import ua.kiev.doctorvera.entities.Policy;
import ua.kiev.doctorvera.entities.PolicyHasUserGroups;
import ua.kiev.doctorvera.entities.UserGroups;

import javax.ejb.Local;
import javax.persistence.EntityManager;
import java.util.List;

/**
 *
 * @author Bodun
 */
@Local
public interface PolicyHasUserGroupsFacadeLocal {
    /**
    @return EntityManager got from container
    */
    EntityManager getEntityManager();

    /**
    Creates new entity representation(record) in the persistent storage(Data Base)
    @param  entity  concrete NEW entity to write
    */
    void create(PolicyHasUserGroups entity);

    /**
    Updates existing entity representation(record) in the persistent storage(Data Base)
    @param  entity existing entity to be updated
    */
    void edit(PolicyHasUserGroups entity);

    /**
    Marks existing entity as deleted in the persistent storage(Data Base)
    @param  entity existing entity to be marked as deleted
    */
    void remove(PolicyHasUserGroups entity);

    /**
    @param id Unique object identifier
    @return PolicyHasUserGroupsFacadeLocal Identified existing entity represented by unique identifier
    */
    PolicyHasUserGroups find(Integer id);

    /**
    @return List<PolicyHasUserGroupsFacadeLocal> List of all Identified existing entities that are not marked as deleted
    */
    List<PolicyHasUserGroups> findAll();

    /**
    @return List<PolicyHasUserGroupsFacadeLocal> List of Identified existing entities that are not marked as deleted from range
    @param range
    */
    List<PolicyHasUserGroups> findRange(int[] range);

    /**
    @return int records count
    */
    int count();

    /**
    <strong>Permanently</strong> removes entity from the persistent storage(Data Base)
    @param entity - Entity that has to be removed
    */
    void removeFromDB(PolicyHasUserGroups entity);

    /**
    Searches for given policy in the Reference table PolicyHasUserGroupsFacadeLocal and returns all found records
    @param policy - policy to search for
    @return List<PolicyHasUserGroupsFacadeLocal> List of PolicyHasUserGroupsFacadeLocal records that match search parameter
     */
    List<PolicyHasUserGroups> findPolicyByPolicy(Policy policy);

    /**
    Searches for given User Group in the Reference table PolicyHasUserGroupsFacadeLocal and returns all found records
    @param group - User Group to search for
    @return List<PolicyHasUserGroupsFacadeLocal> List of PolicyHasUserGroupsFacadeLocal records that match search parameter
     */
    List<PolicyHasUserGroups> findPolicyByGroup(UserGroups group);

    /**
     Searches for the Reference record in PolicyHasUserGroupsFacadeLocal and returns all found records
     @param group - User Group to search for
     @param policy - Policy to search for
     @return List<PolicyHasUserGroupsFacadeLocal> List of PolicyHasUserGroupsFacadeLocal records that match search parameter
     */
    List<PolicyHasUserGroups> findPolicyByGroupAndPolicy(UserGroups group, Policy policy);
    
}
