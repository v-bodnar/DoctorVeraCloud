/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import ua.kiev.doctorvera.entities.DoctorsHasMethod;
import ua.kiev.doctorvera.entities.Methods;
import ua.kiev.doctorvera.entities.Users;

/**
 *
 * @author Bodun
 */
@Local
public interface DoctorsHasMethodFacadeLocal {
    //@return EntityManager got from container
    public EntityManager getEntityManager();
    
    //Creates new entity representation(record) in the persistent storage(Data Base) 
    //@param  entity  concrete NEW entity to write 
    void create(DoctorsHasMethod entity);

    //Updates existing entity representation(record) in the persistent storage(Data Base) 
    //@param  entity existing entity to be updated 
    void edit(DoctorsHasMethod entity);

    //Marks existing entity as deleted in the persistent storage(Data Base) 
    //@param  entity existing entity to be marked as deleted 
    void remove(DoctorsHasMethod entity);

    //@param id Unique object identifier
    //@return T Identified existing entity represented by unique identifier 
    DoctorsHasMethod find(Integer id);

    //@return List<T> List of all Identified existing entities that are not marked as deleted
    List<DoctorsHasMethod> findAll();

    //@return List<T> List of Identified existing entities that are not marked as deleted from range 
    //@param int[] range
    List<DoctorsHasMethod> findRange(int[] range);

    //@return int records count
    int count();   
    
    public void removeFromDB(DoctorsHasMethod entity);
    public List<DoctorsHasMethod> findMethodsByDoctor(Users user);
    public List<DoctorsHasMethod> findDoctorsByMethod(Methods method);
    
    
    
}
