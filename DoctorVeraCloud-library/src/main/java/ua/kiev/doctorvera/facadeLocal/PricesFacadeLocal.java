/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facadeLocal;

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
public interface PricesFacadeLocal extends CRUDFacade<Prices>{
    /**
    Searches for actual (the last record before current date) price record with the given method
    @return Price record that is not marked as deleted
    @param method method to search for
    */
    Prices findLastPrice(Methods method);

    /**
    Searches for all Prices records with the given method
    @return List<Prices> List of Prices records that are not marked as deleted
    @param method method to search for
    */
    List<Prices> findByMethod(Methods method);
}
