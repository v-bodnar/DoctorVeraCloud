/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.*;
import ua.kiev.doctorvera.entities.PolicyHasUserGroups;
import ua.kiev.doctorvera.facadeLocal.PolicyFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.PolicyHasUserGroupsFacadeLocal;
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
    private PolicyHasUserGroupsFacadeLocal policyHasUserGroupsFacade;

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
		ArrayList<Policy> result = new ArrayList<>();
		for (UserGroups group : userGroupsFacade.findByUser(user)) {
			for (PolicyHasUserGroups entry : policyHasUserGroupsFacade.findPolicyByGroup(group)) {
				result.add(entry.getPolicy());
			}
		}
		return result;
	}

	/**
	* Adds record to the reference table for referencing given Policy and User Group
	* @returns true - in the case operation was successful and false otherwise
	* @param group - User Group that has to be referenced
	* @param policy - Policy that has to be referenced
	* @param userCreated - User that initiated process
	*/
    @Override
    public boolean addUserGroups(UserGroups group, Policy policy, Users userCreated){
    	if(policy != null && group!=null && userCreated != null){
    		//Find all entries with the same Policy and UserType
    		List<PolicyHasUserGroups> alreadyExists = policyHasUserGroupsFacade.findPolicyByGroupAndPolicy(group, policy);

    		//Create new entry
    		if(alreadyExists.size() == 0){
        		PolicyHasUserGroups entry = new PolicyHasUserGroups();
        		entry.setDateCreated(new Date());
        		entry.setUserGroup(userGroupsFacade.find(group));
        		entry.setPolicy(find(policy));
        		entry.setUserCreated(usersFacade.find(userCreated));

				policyHasUserGroupsFacade.create(entry);
    		}
	    	return true;
    	} else
    		return false;
    }

	/**
	* Permanently deletes record from the reference table for removing reference between given Policy and User Group
	* @returns true - in the case operation was successful and false otherwise
	* @param policy - Policy that has to be unreferenced
	* @param group - User Group that has to be unreferenced
	*/
    @Override
    public boolean removeUser(UserGroups group, Policy policy){
    	if(policy != null && group!=null){
			List<PolicyHasUserGroups> alreadyExists = policyHasUserGroupsFacade.findPolicyByGroupAndPolicy(group, policy);
			for(PolicyHasUserGroups policyHasUserGroups : alreadyExists){
				policyHasUserGroupsFacade.remove(policyHasUserGroups);
			}
	    	return true;
    	} else
    		return false;
    }

}
