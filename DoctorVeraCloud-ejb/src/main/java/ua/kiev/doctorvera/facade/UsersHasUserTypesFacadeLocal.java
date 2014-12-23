/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

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
    //@return EntityManager got from container
    public EntityManager getEntityManager();
    
    //Creates new entity representation(record) in the persistent storage(Data Base) 
    //@param  entity  concrete NEW entity to write 
    void create(UsersHasUserTypes entity);

    //Updates existing entity representation(record) in the persistent storage(Data Base) 
    //@param  entity existing entity to be updated 
    void edit(UsersHasUserTypes entity);

    //Marks existing entity as deleted in the persistent storage(Data Base) 
    //@param  entity existing entity to be marked as deleted 
    void remove(UsersHasUserTypes entity);

    //@param id Unique object identifier
    //@return T Identified existing entity represented by unique identifier 
    UsersHasUserTypes find(Integer id);

    //@return List<T> List of all Identified existing entities that are not marked as deleted
    List<UsersHasUserTypes> findAll();

    //@return List<T> List of Identified existing entities that are not marked as deleted from range 
    //@param int[] range
    List<UsersHasUserTypes> findRange(int[] range);

    //@return int records count
    int count();   
    
    public List<UsersHasUserTypes> findTypesByUser(Users user);
    public List<UsersHasUserTypes> findUsersByType(UserTypes type);
    
}
