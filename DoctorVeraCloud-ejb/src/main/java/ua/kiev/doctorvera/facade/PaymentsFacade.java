/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.Payments;
import ua.kiev.doctorvera.facadeLocal.PaymentsFacadeLocal;

import javax.ejb.Stateless;

/**
 * Class for implementing main operations with Payments entity
 * @author Volodymyr Bodnar
 */
@Stateless
public class PaymentsFacade extends AbstractFacade<Payments> implements PaymentsFacadeLocal {
		
    public PaymentsFacade() {
        super(Payments.class);
    }

}
