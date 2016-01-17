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

/**
 * Interface for declaring main operations with Generic (Identified) entity
 * @author Volodymyr Bodnar
 */
@Local
public interface MethodTypesFacadeLocal extends CRUDFacade<MethodTypes>{

    /**
    Searches for all MethodTypes by the given name
    @return List<MethodTypes> List of existing MethodTypes entities that are not marked as deleted
    @param shortName - short name to search for
    */
    List<MethodTypes> findByShortName(String shortName);
}
