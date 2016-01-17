/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.*;
import ua.kiev.doctorvera.facadeLocal.UserGroupsFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 *
 * @author Bodun
 */
@Stateless
public class UserGroupsFacade extends AbstractFacade<UserGroups> implements UserGroupsFacadeLocal {

	@EJB
    private UsersFacadeLocal usersFacade;
	
    public UserGroupsFacade() {
        super(UserGroups.class);
    }

	/**
	* Searches for User Group by group name
	* @returns UserGroups entity that matches search parameter
	* @param groupName - name of the User Group
	*/
	@Override
    public UserGroups findByName(String groupName) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UserGroups> cq = cb.createQuery(UserGroups.class);
        Root<UserGroups> root = cq.from(UserGroups.class);
        cq.select(root).where(cb.equal(root.<String>get("name"), groupName),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        List<UserGroups> resultList = getEntityManager().createQuery(cq).getResultList();
        if (resultList.size() == 1) return resultList.get(0);
        else return null;
    }

	/**
	* Searches all User Groups that contain given user
	* @returns List<UserGroups> entity that matches search parameter
	* @param user - user to search by
	*/
	@Override
    public List<UserGroups> findByUser(Users user) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<UserGroups> cq = cb.createQuery(UserGroups.class);
		Root<UserGroups> root = cq.from(UserGroups.class);
		Predicate usersPredicate = cb.isMember(user, root.<List<Users>>get("users"));
		Predicate deletedPredicate = cb.isFalse(root.<Boolean>get("deleted"));
		cq.select(root).where(usersPredicate,deletedPredicate);
		cq.distinct(true);
		return getEntityManager().createQuery(cq).getResultList();
    }

	/**
	 * Searches all UserGroups that contain given deliveryGroup
	 * @param deliveryGroup
	 * @return
	 */
	public List<UserGroups> findUserGroupsByDeliveryGroup(DeliveryGroup deliveryGroup){

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<UserGroups> cq = cb.createQuery(UserGroups.class);

		Root root = cq.from(UserGroups.class);
		cq.distinct(true);

		Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
		Predicate deliveryGroupPredicate = cb.and(cb.isMember(deliveryGroup, root.get("deliveryGroups")));
		cq.select(root).where(deletedPredicate, deliveryGroupPredicate);

		return getEntityManager().createQuery(cq).getResultList();
	}
}
