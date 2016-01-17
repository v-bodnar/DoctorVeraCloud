/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facadeLocal;

import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import ua.kiev.doctorvera.entities.MethodTypes;
import ua.kiev.doctorvera.entities.Methods;

/**
 * Interface for declaring main operations with Generic (Identified) entity
 * @author Volodymyr Bodnar
 */
@Local
public interface MethodsFacadeLocal extends CRUDFacade<Methods>{

    /**
    * Searches for method with the given short Name
    * @param shortName - short Name of the method
    * @return Method that corresponds given parameters
    * */
    Methods findByShortName(String shortName);

    /**
    * Searches for all methods with the given Method Type
    * @param type - Method Type of the method
    * @return List of methods Method that corresponds given parameters
    * */
    List<Methods> findByType(MethodTypes type);
}
