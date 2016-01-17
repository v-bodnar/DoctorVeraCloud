/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facadeLocal;

import ua.kiev.doctorvera.entities.Address;

import javax.ejb.Local;

/**
 * Interface for declaring main operations with Generic (Identified) entity
 * Here will be declared operations specific to current entity
 * @author Volodymyr Bodnar
 */
@Local
public interface AddressFacadeLocal extends CRUDFacade<Address>{

}
