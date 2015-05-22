/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import ua.kiev.doctorvera.entities.UserTypes;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.entities.UsersHasUserTypes;
import ua.kiev.doctorvera.facadeLocal.UserTypesFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersHasUserTypesFacadeLocal;

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
public class UserTypesFacade extends AbstractFacade<UserTypes> implements UserTypesFacadeLocal {
	
	@EJB
    private UsersHasUserTypesFacadeLocal usersHasUserTypesFacade;
	
	@EJB
    private UsersFacadeLocal usersFacade;
	
    public UserTypesFacade() {
        super(UserTypes.class);
    }

	/**
	* Searches for User Type by type name
	* @returns UserTypes entity that matches search parameter
	* @param typeName - name of the User Type
	*/
	@Override
    public UserTypes findByName(String typeName) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UserTypes> cq = cb.createQuery(UserTypes.class);
        Root<UserTypes> root = cq.from(UserTypes.class);
        cq.select(root).where(cb.equal(root.<String>get("name"), typeName),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        List<UserTypes> resultList = getEntityManager().createQuery(cq).getResultList();
        if (resultList.size() == 1) return resultList.get(0);
        else return null;
    }

	/**
	* Searches all User Types that contain given user
	* @returns List<UserTypes> entity that matches search parameter
	* @param user - user to search by
	*/
	@Override
    public List<UserTypes> findByUser(Users user) {
    	if(user!=null){
	    	Collection<UsersHasUserTypes> list = usersHasUserTypesFacade.findTypesByUser(user);
	    	HashSet<UserTypes> result = new HashSet<UserTypes>();
	    	if (list!=null)
	    		for(UsersHasUserTypes entry : list) 
	    			result.add(entry.getUserType());
	    	return new ArrayList<UserTypes>(result);
    	} else
    		return null;
    }

	/**
	* Adds record to the reference table for referencing given user and User Type
	* @returns true - in the case operation was successful and false otherwise
	* @param user - User that has to be referenced
	* @param type - User Type that has to be referenced
	* @param userCreated - User that initiated process
	*/
    @Override
    public boolean addUser(Users user, UserTypes type, Users userCreated){
    	if(user != null && type!=null && userCreated != null){
    		//Find all entries with the same User and UserType
    		List<UsersHasUserTypes> alredyExists = new ArrayList<UsersHasUserTypes>();
    		
    		for(UsersHasUserTypes entry : usersHasUserTypesFacade.findUsersByType(type)){
    			if(entry.getUser().equals(user)) alredyExists.add(entry);
    		}
    		
    		//Create new entry
    		if(alredyExists == null || alredyExists.size() == 0){
        		UsersHasUserTypes entry = new UsersHasUserTypes();
        		entry.setDateCreated(new Date());
        		entry.setUser(usersFacade.find(user));
        		entry.setUserType(find(type));
        		entry.setUserCreated(usersFacade.find(userCreated));
        		
        		usersHasUserTypesFacade.create(entry);
    		}	
	    	return true;
    	} else
    		return false;
    }

	/**
	* Permanently deletes record from the reference table for removing reference between given user and User Type
	* @returns true - in the case operation was successful and false otherwise
	* @param user - User that has to be unreferenced
	* @param type - User Type that has to be unreferenced
	*/
    @Override
    public boolean removeUser(Users user, UserTypes type){
    	type = find(type);
    	if(user != null && type!=null){
    		List<UsersHasUserTypes> alredyExists = usersHasUserTypesFacade.findUsersByType(type);
    		for(UsersHasUserTypes entry : alredyExists){
    			if(entry.getUser().equals(user)) 
    				usersHasUserTypesFacade.removeFromDB(entry);
    		}
	    	return true;
    	} else
    		return false;
    }
    
}
