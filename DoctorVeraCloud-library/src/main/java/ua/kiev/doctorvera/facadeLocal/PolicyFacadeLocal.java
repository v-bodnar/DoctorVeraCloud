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
public interface PolicyFacadeLocal  extends CRUDFacade<Policy>{

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

}
