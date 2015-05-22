/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import ua.kiev.doctorvera.entities.Address;
import ua.kiev.doctorvera.facadeLocal.AddressFacadeLocal;

import javax.ejb.Stateless;

/**
 * Class for implementing main operations with User entity
 *
 * @author Volodymyr Bodnar
 */
@Stateless
public class AddressFacade extends AbstractFacade<Address> implements AddressFacadeLocal {

    public AddressFacade() {
        super(Address.class);
    }
}
