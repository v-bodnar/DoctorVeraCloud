/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facadeLocal;

import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import ua.kiev.doctorvera.entities.DeliveryGroup;
import ua.kiev.doctorvera.entities.Policy;
import ua.kiev.doctorvera.entities.UserGroups;
import ua.kiev.doctorvera.entities.Users;

/**
 *
 * @author Bodun
 */
@Local
public interface UserGroupsFacadeLocal  extends CRUDFacade<UserGroups>{
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
     * Searches all UserGroups that contain given deliveryGroup
     * @param deliveryGroup
     * @return
     */
    List<UserGroups> findUserGroupsByDeliveryGroup(DeliveryGroup deliveryGroup);
}
