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

import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.entities.Schedule;

/**
 * Interface for declaring main operations with Generic (Identified) entity
 * @author Volodymyr Bodnar
 */
@Local
public interface ScheduleFacadeLocal{
    //@return EntityManager got from container
    public EntityManager getEntityManager();
    
    //Creates new entity representation(record) in the persistent storage(Data Base) 
    //@param  entity  concrete NEW entity to write 
    public void create(Schedule entity);

    //Updates existing entity representation(record) in the persistent storage(Data Base) 
    //@param  entity existing entity to be updated 
    public void edit(Schedule entity);

    //Marks existing entity as deleted in the persistent storage(Data Base) 
    //@param  entity existing entity to be marked as deleted 
    public void remove(Schedule entity);

    //@param id Unique object identifier
    //@return T Identified existing entity represented by unique identifier 
    public Schedule find(Integer id);

    //@return List<T> List of all Identified existing entities that are not marked as deleted
    public List<Schedule> findAll();

    //@return List<T> List of Identified existing entities that are not marked as deleted from range 
    //@param int[] range
    public List<Schedule> findRange(int[] range);
    
    //@return List<Schedule> List of Schedule records with room equals to room param, and start date between from and to
    //Inclusive from and exclusive to
    //@param room Rooms selected room
    //@param from Date start Date
    //@param to Date end Date
    public List<Schedule> findByRoomAndStartDate(Rooms room, Date from, Date to);
    
    //@return List<Schedule> List of Schedule records with room equals to room param, and end date between from and to
    //Exclusive from and inclusive to
    //@param room Rooms selected room
    //@param from Date start Date
    //@param to Date end Date
    public List<Schedule> findByRoomAndEndDate(Rooms room, Date from, Date to);
    
    //@return List<Schedule> List of Schedule records with room equals to room param, 
    //and selected interval is inside of any recorded Schedule interval
    //Exclusive from and to
    //@param room Rooms selected room
    //@param from Date start Date
    //@param to Date end Date
    public List<Schedule> findByRoomAndDatesInside(Rooms room, Date from, Date to);
    
    //@return List<Schedule> List of Schedule records with room equals to room param, 
    //and selected interval is inside of any recorded Schedule interval
    //Inclusive from and to
    //@param room Rooms selected room
    //@param from Date start Date
    //@param to Date end Date
    public List<Schedule> findByRoomAndDatesInsideOrEqual(Rooms room, Date from, Date to);
    
    //@return List<Schedule> List of Schedule records with room equals to room param, 
    //and selected interval is outside of any recorded Schedule interval
    //Inclusive from and to
    //@param room Rooms selected room
    //@param from Date start Date
    //@param to Date end Date
    public List<Schedule> findByRoomAndDatesOutsideOrEqual(Rooms room, Date from, Date to);
    
    //@return Schedule record with room equals to room param, 
    //and date param is inside of any recorded Schedule interval
    //Inclusive from and exclusive to
    //@param room Rooms selected room
    //@param date Date date to search inside schedule
    public Schedule findByRoomAndDateInside(Rooms room, Date date);

    //@return int records count
    public int count();    
}
