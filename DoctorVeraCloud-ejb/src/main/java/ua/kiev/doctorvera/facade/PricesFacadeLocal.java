/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import ua.kiev.doctorvera.entities.Methods;
import ua.kiev.doctorvera.entities.Prices;

/**
 * Interface for declaring main operations with Generic (Identified) entity
 * @author Volodymyr Bodnar
 */
@Local
public interface PricesFacadeLocal{
    //@return EntityManager got from container
    public EntityManager getEntityManager();
    
    //Creates new entity representation(record) in the persistent storage(Data Base) 
    //@param  entity  concrete NEW entity to write 
    public void create(Prices entity);

    //Updates existing entity representation(record) in the persistent storage(Data Base) 
    //@param  entity existing entity to be updated 
    public void edit(Prices entity);

    //Marks existing entity as deleted in the persistent storage(Data Base) 
    //@param  entity existing entity to be marked as deleted 
    public void remove(Prices entity);

    //@param id Unique object identifier
    //@return T Identified existing entity represented by unique identifier 
    public Prices find(Integer id);

    //@return List<T> List of all Identified existing entities that are not marked as deleted
    public List<Prices> findAll();

    //@return List<T> List of Identified existing entities that are not marked as deleted from range 
    //@param int[] range
    public List<Prices> findRange(int[] range);

    //@return int records count
    public int count();    
    
    public Prices findLastPrice(Methods method);
    
    public List<Prices> findByMethod(Methods method);
}
