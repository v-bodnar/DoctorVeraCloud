package ua.kiev.doctorvera.facadeLocal;

import ua.kiev.doctorvera.entities.DeliveryGroup;
import ua.kiev.doctorvera.entities.UserGroups;
import ua.kiev.doctorvera.entities.Users;

import javax.persistence.EntityManager;
import javax.transaction.*;
import java.util.List;

/**
 * Interface for declaring main operations with DeliveryGroup entity
 * Here will be declared operations specific to current entity
 * @author Volodymyr Bodnar
 * @date 22.11.2015.
 */
public interface DeliveryGroupFacadeLocal extends CRUDFacade<DeliveryGroup>{

    /**
     * Searches all DeliveryGroups that contain given userGroup
     * @param userGroup given userGroup
     * @return all found DeliveryGroups
     */
    List<DeliveryGroup> findDeliveryGroupByUserGroup(UserGroups userGroup);

    /**
     * Searches all DeliveryGroups that contain given user
     * @param user given user
     * @return all found DeliveryGroups
     */
    List<DeliveryGroup> findDeliveryGroupByUser(Users user);
}
