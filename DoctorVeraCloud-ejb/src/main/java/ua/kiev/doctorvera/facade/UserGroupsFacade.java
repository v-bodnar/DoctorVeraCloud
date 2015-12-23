/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.*;
import ua.kiev.doctorvera.facadeLocal.PolicyHasUserGroupsFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UserGroupsFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersHasUserGroupsFacadeLocal;

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
    private UsersHasUserGroupsFacadeLocal usersHasUserGroupsFacade;

	@EJB
	private PolicyHasUserGroupsFacadeLocal policyHasUserGroupsFacadeLocal;

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
    	if(user!=null){
	    	Collection<UsersHasUserGroups> list = usersHasUserGroupsFacade.findGroupsByUser(user);
	    	HashSet<UserGroups> result = new HashSet<UserGroups>();
	    	if (list!=null)
	    		for(UsersHasUserGroups entry : list)
	    			result.add(entry.getUserGroup());
	    	return new ArrayList<UserGroups>(result);
    	} else
    		return new ArrayList<>();
    }

	/**
	* Adds record to the reference table for referencing given user and User Group
	* @returns true - in the case operation was successful and false otherwise
	* @param user - User that has to be referenced
	* @param group - User Group that has to be referenced
	* @param userCreated - User that initiated process
	*/
    @Override
    public boolean addUser(Users user, UserGroups group, Users userCreated){
    	if(user != null && group!=null && userCreated != null){
    		//Find all entries with the same User and UserGroup
    		List<UsersHasUserGroups> alredyExists = new ArrayList<UsersHasUserGroups>();
    		
    		for(UsersHasUserGroups entry : usersHasUserGroupsFacade.findUsersByGroup(group)){
    			if(entry.getUser().equals(user)) alredyExists.add(entry);
    		}
    		
    		//Create new entry
    		if(alredyExists == null || alredyExists.size() == 0){
        		UsersHasUserGroups entry = new UsersHasUserGroups();
        		entry.setDateCreated(new Date());
        		entry.setUser(usersFacade.find(user));
        		entry.setUserGroup(find(group));
        		entry.setUserCreated(usersFacade.find(userCreated));
        		
        		usersHasUserGroupsFacade.create(entry);
    		}	
	    	return true;
    	} else
    		return false;
    }

	/**
	* Permanently deletes record from the reference table for removing reference between given user and User Group
	* @returns true - in the case operation was successful and false otherwise
	* @param user - User that has to be unreferenced
	* @param group - User Group that has to be unreferenced
	*/
    @Override
    public boolean removeUser(Users user, UserGroups group){
    	group = find(group);
    	if(user != null && group!=null){
    		List<UsersHasUserGroups> alredyExists = usersHasUserGroupsFacade.findUsersByGroup(group);
    		for(UsersHasUserGroups entry : alredyExists){
    			if(entry.getUser().equals(user)) 
    				usersHasUserGroupsFacade.removeFromDB(entry);
    		}
	    	return true;
    	} else
    		return false;
    }

	/**
	 * Searches for all policies by given User Group
	 * @returns UserGroups entity that matches search parameter
	 * @param group - User Group to search by
	 */
	@Override
	public List<Policy> findPoliciesByGroup(UserGroups group) {
		List<Policy> result = new ArrayList<>();
		for(PolicyHasUserGroups entry : policyHasUserGroupsFacadeLocal.findPolicyByGroup(group)){
			result.add(entry.getPolicy());
		}
		return result;
	}

	/**
	 * Searches for all policies by given User
	 * @returns UserGroups entity that matches search parameter
	 * @param user - user to search by
	 */
	@Override
	public List<Policy> findPoliciesByUser(Users user) {
		List<Policy> result = new ArrayList<>();
		for(UserGroups userGroup : findByUser(user)){
			result.addAll(findPoliciesByGroup(userGroup));
		}
		return result;
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
