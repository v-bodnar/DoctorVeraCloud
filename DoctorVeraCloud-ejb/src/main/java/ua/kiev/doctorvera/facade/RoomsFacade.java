/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import javax.ejb.Stateless;

import ua.kiev.doctorvera.entities.Rooms;

/**
 * Class for implementing main operations with User entity
 * @author Volodymyr Bodnar
 */
@Stateless
public class RoomsFacade extends AbstractFacade<Rooms> implements RoomsFacadeLocal {
		
    public RoomsFacade() {
        super(Rooms.class);
    }

}
