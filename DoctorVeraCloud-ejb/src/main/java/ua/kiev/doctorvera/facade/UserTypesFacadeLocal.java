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

/**
 *
 * @author Bodun
 */
@Local
public interface UserTypesFacadeLocal {
    //@return EntityManager got from container
    public EntityManager getEntityManager();
    
    //Creates new entity representation(record) in the persistent storage(Data Base) 
    //@param  entity  concrete NEW entity to write 
    void create(UserTypes entity);

    //Updates existing entity representation(record) in the persistent storage(Data Base) 
    //@param  entity existing entity to be updated 
    void edit(UserTypes entity);

    //Marks existing entity as deleted in the persistent storage(Data Base) 
    //@param  entity existing entity to be marked as deleted 
    void remove(UserTypes entity);

    //@param id Unique object identifier
    //@return T Identified existing entity represented by unique identifier 
    UserTypes find(Integer id);

    //@return List<T> List of all Identified existing entities that are not marked as deleted
    List<UserTypes> findAll();

    //@return List<T> List of Identified existing entities that are not marked as deleted from range 
    //@param int[] range
    List<UserTypes> findRange(int[] range);

    //@return int records count
    int count();   
    
    public UserTypes findByName(String typeName);
}
