package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.DeliveryGroup;
import ua.kiev.doctorvera.entities.UserGroups;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.DeliveryGroupFacadeLocal;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

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

    /**
     * Searches all DeliveryGroups that contain given user
     * @param user given user
     * @return all found DeliveryGroups
     */
    public List<DeliveryGroup> findDeliveryGroupByUser(Users user){

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<DeliveryGroup> cq = cb.createQuery(DeliveryGroup.class);

        Root root = cq.from(DeliveryGroup.class);
        cq.distinct(true);

        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        Predicate deliveryGroupPredicate = cb.and(cb.isMember(user, root.get("users")));
        cq.select(root).where(deletedPredicate, deliveryGroupPredicate);

        List<DeliveryGroup> resultList = getEntityManager().createQuery(cq).getResultList();
        return resultList;
    }

    /**
     * Searches all DeliveryGroups that contain given userGroup
     * @param userGroup given userGroup
     * @return all found DeliveryGroups
     */
    public List<DeliveryGroup> findDeliveryGroupByUserGroup(UserGroups userGroup){

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<DeliveryGroup> cq = cb.createQuery(DeliveryGroup.class);

        Root root = cq.from(DeliveryGroup.class);
        cq.distinct(true);

        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        Predicate deliveryGroupPredicate = cb.and(cb.isMember(userGroup, root.get("userGroups")));
        cq.select(root).where(deletedPredicate, deliveryGroupPredicate);

        List<DeliveryGroup> resultList = getEntityManager().createQuery(cq).getResultList();
        return resultList;
    }

}
