/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facadeLocal;

import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import ua.kiev.doctorvera.entities.Payments;
import ua.kiev.doctorvera.entities.Schedule;

/**
 * Interface for declaring main operations with Generic (Identified) entity
 *
 * @author Volodymyr Bodnar
 */
@Local
public interface PaymentsFacadeLocal {
    /**
     * @return EntityManager got from container
     */
    EntityManager getEntityManager();

    /**
     * Creates new entity representation(record) in the persistent storage(Data Base)
     *
     * @param entity concrete NEW entity to write
     */
    void create(Payments entity);

    /**
     * Updates existing entity representation(record) in the persistent storage(Data Base)
     *
     * @param entity existing entity to be updated
     */
    void edit(Payments entity);

    /**
     * Marks existing entity as deleted in the persistent storage(Data Base)
     *
     * @param entity existing entity to be marked as deleted
     */
    void remove(Payments entity);

    /**
     * @param id Unique object identifier
     * @return Payments Identified existing entity represented by unique identifier
     */
    Payments find(Integer id);

    /**
     * @return List<Payments> List of all Identified existing entities that are not marked as deleted
     */
    List<Payments> findAll();

    /**
     * @param range
     * @return List<Payments> List of Identified existing entities that are not marked as deleted from range
     */
    List<Payments> findRange(int[] range);

    /**
     * @return int records count
     */
    int count();

    /**
     * <strong>Permanently</strong> removes entity from the persistent storage(Data Base)
     *
     * @param entity - Entity that has to be removed
     */
    void removeFromDB(Payments entity);

    /**
     * Searches for payment for the given Schedule record
     *
     * @param schedule record to search by
     * @return payment for the given Schedule record
     */
    Payments findBySchedule(Schedule schedule);
}
