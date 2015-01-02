/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import ua.kiev.doctorvera.entities.Plan;
import ua.kiev.doctorvera.entities.UserTypes;
import ua.kiev.doctorvera.entities.Users;

/**
 * Interface for declaring main operations with Generic (Identified) entity
 * @author Volodymyr Bodnar
 */
@Local
public interface UsersFacadeLocal{
    //@return EntityManager got from container
    public EntityManager getEntityManager();
    
    //Creates new entity representation(record) in the persistent storage(Data Base) 
    //@param  entity  concrete NEW entity to write 
    void create(Users entity);

    //Updates existing entity representation(record) in the persistent storage(Data Base) 
    //@param  entity existing entity to be updated 
    void edit(Users entity);

    //Marks existing entity as deleted in the persistent storage(Data Base) 
    //@param  entity existing entity to be marked as deleted 
    void remove(Users entity);

    //@param id Unique object identifier
    //@return T Identified existing entity represented by unique identifier 
    Users find(Integer id);

    //@return List<T> List of all Identified existing entities that are not marked as deleted
    List<Users> findAll();

    //@return List<T> List of Identified existing entities that are not marked as deleted from range 
    //@param int[] range
    List<Users> findRange(int[] range);

    //@return int records count
    int count();    
	
    public Users findByUsername(String username);
	
    public Users findByCred(String username, String password);

    public List<Users> findByFirstName(String firstName);

    public List<Users> findByMiddleName(String middleName);

    public List<Users> findByLastName(String lastName);

    public List<Users> findByBirthDateBetween(Date from, Date to);

    public List<Users> findByPhoneNumberHome(String phoneNumberHome);

    public List<Users> findByPhoneNumberMobile(String phoneNumberMobile);

    public List<Users> findByDescription(String description);
    
    public List<Users> findByType(UserTypes type);
    
    public List<Users> findByType(String type);
    
    public List<Users> findByType(Integer typeId);
    

    
}
