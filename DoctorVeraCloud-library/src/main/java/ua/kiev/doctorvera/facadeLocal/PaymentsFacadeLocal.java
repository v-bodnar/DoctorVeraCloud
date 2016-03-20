/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facadeLocal;

import ua.kiev.doctorvera.entities.Payments;
import ua.kiev.doctorvera.entities.Schedule;

import javax.ejb.Local;
import java.util.List;

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
    List<Payments> findBySchedule(Schedule schedule);
}
