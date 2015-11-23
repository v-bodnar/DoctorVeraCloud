package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.DeliveryGroup;
import ua.kiev.doctorvera.facadeLocal.DeliveryGroupFacadeLocal;

import javax.ejb.Stateless;
/**
 * Class for implementing main operations with DeliveryGroup entity
 *
 * @author Volodymyr Bodnar
 * @date 23.11.2015
 */
@Stateless
public class DeliveryGroupFacade extends AbstractFacade<DeliveryGroup> implements DeliveryGroupFacadeLocal {

    public DeliveryGroupFacade() {
        super(DeliveryGroup.class);
    }
}
