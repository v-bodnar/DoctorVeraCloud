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
import ua.kiev.doctorvera.entities.Rooms;

/**
 *
 * @author Bodun
 */
@Local
public interface PlanFacadeLocal {
    //@return EntityManager got from container
    public EntityManager getEntityManager();
    
    //Creates new entity representation(record) in the persistent storage(Data Base) 
    //@param  entity  concrete NEW entity to write 
    void create(Plan entity);

    //Updates existing entity representation(record) in the persistent storage(Data Base) 
    //@param  entity existing entity to be updated 
    void edit(Plan entity);

    //Marks existing entity as deleted in the persistent storage(Data Base) 
    //@param  entity existing entity to be marked as deleted 
    void remove(Plan entity);

    //@param id Unique object identifier
    //@return T Identified existing entity represented by unique identifier 
    Plan find(Integer id);

    //@return List<T> List of all Identified existing entities that are not marked as deleted
    List<Plan> findAll();

    //@return List<T> List of Identified existing entities that are not marked as deleted from range 
    //@param int[] range
    List<Plan> findRange(int[] range);

    //@return int records count
    int count();   
    public List<Plan> findByStartDateBetween(Date from, Date to);
    
    //@return Plan with startDate equals to @param date param
    //@param date Date startDate
    public Plan findByStartDate(Date date);
    
    //@return List<Plan> All Plan records that have start date this week
    public List<Plan> findByStartThisWeek();
    
    //@return List<Plan> List of Plan records with room equals to room param
    //@param room Rooms selected room
    public List<Plan> findByRoom(Rooms room);
    
    //@return List<Plan> List of Plan records with room equals to room param, and start date between from and to
    //Inclusive from and exclusive to
    //@param room Rooms selected room
    //@param from Date start Date
    //@param to Date end Date
    public List<Plan> findByRoomAndStartDate(Rooms room, Date from, Date to);
    
    //@return List<Plan> List of Plan records with room equals to room param, and end date between from and to
    //Exclusive from and inclusive to
    //@param room Rooms selected room
    //@param from Date start Date
    //@param to Date end Date
    public List<Plan> findByRoomAndEndDate(Rooms room, Date from, Date to);
    
    //@return List<Plan> List of Plan records with room equals to room param, 
    //and selected interval is inside of any recorded plans interval
    //Exclusive from and to
    //@param room Rooms selected room
    //@param from Date start Date
    //@param to Date end Date
    public List<Plan> findByRoomAndDatesInside(Rooms room, Date from, Date to);
}
