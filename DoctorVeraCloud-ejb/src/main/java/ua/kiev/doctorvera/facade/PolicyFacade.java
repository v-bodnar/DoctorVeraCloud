/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.*;
import ua.kiev.doctorvera.facadeLocal.PolicyFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UserGroupsFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 *
 * @author Bodun
 */
@Stateless
public class PolicyFacade extends AbstractFacade<Policy> implements PolicyFacadeLocal {
	@EJB
    private UserGroupsFacadeLocal userGroupsFacade;

	@EJB
	private UsersFacadeLocal usersFacade;

    public PolicyFacade() {
        super(Policy.class);
    }

	/**
	* Searches for Policy by type name
	* @returns Policy entity that matches search parameter
	* @param stringId - stringId of the Policy
	*/
	@Override
    public Policy findByStringId(String stringId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Policy> cq = cb.createQuery(Policy.class);
        Root<Policy> root = cq.from(Policy.class);
        cq.select(root).where(cb.equal(root.<String>get("stringId"), stringId),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        List<Policy> resultList = getEntityManager().createQuery(cq).getResultList();
        if (resultList.size() == 1) return resultList.get(0);
        else return null;
    }

	/**
	* Searches all Policy that contain given user
	* @returns List<Policy> entity that matches search parameter
	* @param user - user to search by
	*/
	@Override
	public List<Policy> findByUser(Users user) {
		Set<Policy> result = new HashSet<>();
		for (UserGroups group : userGroupsFacade.initializeLazyEntity(userGroupsFacade.findByUser(user))) {
			result.addAll(group.getPolicies());
		}
		return new ArrayList<>(result);
	}

}
