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
public interface PaymentsFacadeLocal  extends CRUDFacade<Payments>{

    /**
     * Searches for payment for the given Schedule record
     *
     * @param schedule record to search by
     * @return payment for the given Schedule record
     */
    Payments findBySchedule(Schedule schedule);
}
