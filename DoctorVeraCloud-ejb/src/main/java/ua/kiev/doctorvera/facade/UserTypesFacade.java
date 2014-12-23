/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import javax.ejb.Stateless;

import ua.kiev.doctorvera.entities.UserTypes;

/**
 *
 * @author Bodun
 */
@Stateless
public class UserTypesFacade extends AbstractFacade<UserTypes> implements UserTypesFacadeLocal {
	
    public UserTypesFacade() {
        super(UserTypes.class);
    }
    
}
