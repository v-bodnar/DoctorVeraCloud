/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facadeLocal;

import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import ua.kiev.doctorvera.entities.Rooms;

/**
 * Interface for declaring main operations with Generic (Identified) entity
 * @author Volodymyr Bodnar
 */
@Local
public interface RoomsFacadeLocal extends CRUDFacade<Rooms>{
}
