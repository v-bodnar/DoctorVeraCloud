/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ua.kiev.doctorvera.entities.UserTypes;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.entities.UsersHasUserTypes;

/**
 * Class for implementing main operations with User entity
 * @author Volodymyr Bodnar
 */
@Stateless
public class UsersFacade extends AbstractFacade<Users> implements UsersFacadeLocal {
		
	//@EJB
    //private UsersHasUserTypesFacade usersHasUserTypesFacade;
	
	@EJB
    private UserTypesFacadeLocal userTypesFacade;
	
    public UsersFacade() {
        super(Users.class);
    }
	
	@Override
    public Users findByUsername(String username) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);
        
        cq.select(root).where(cb.equal(root.<String>get("username"), username),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        List<Users> resultList = getEntityManager().createQuery(cq).getResultList();
        if (resultList.size() == 1) return resultList.get(0);
        else return null;
    }
	
	@Override
    public Users findByCred(String username, String password) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);
        cq.select(root).where(cb.equal(root.<String>get("password"), password),cb.equal(root.<String>get("username"), username),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        List<Users> resultList = getEntityManager().createQuery(cq).getResultList();
        if (resultList.size() == 1) return resultList.get(0);
        else return null;
    }

    @Override
    public List<Users> findByFirstName(String firstName) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);
        cq.select(cq.from(Users.class)).where(cb.equal(root.<String>get("FirstName"), firstName),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList(); 
    }

    @Override
    public List<Users> findByMiddleName(String middleName) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);
        cq.select(cq.from(Users.class)).where(cb.equal(root.<String>get("MiddleName"), middleName),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList(); 
    }

    @Override
    public List<Users> findByLastName(String lastName) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);
        cq.select(cq.from(Users.class)).where(cb.equal(root.<String>get("LastName"), lastName),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList(); 
    }

    @Override
    public List<Users> findByBirthDateBetween(Date from, Date to) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);
        cq.select(cq.from(Users.class)).where(cb.between(root.<Date>get("BirthDate"), from, to), cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList(); 
    }

    @Override
    public List<Users> findByPhoneNumberHome(String phoneNumberHome) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);
        cq.select(cq.from(Users.class)).where(cb.equal(root.<String>get("PhoneNumberHome"), phoneNumberHome),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList(); 
    }

    @Override
    public List<Users> findByPhoneNumberMobile(String phoneNumberMobile) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);
        cq.select(cq.from(Users.class)).where(cb.equal(root.<String>get("PhoneNumberMobile"), phoneNumberMobile),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList(); 
    }

    @Override
    public List<Users> findByDescription(String description) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);
        cq.select(cq.from(Users.class)).where(cb.equal(root.<String>get("Description"), description),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList(); 
    }

    @Override
    public List<Users> findByType(UserTypes type){
    	Collection<UsersHasUserTypes> list = type.getUsersHasUserTypesCollection();
    	//List<UsersHasUserTypes> list = usersHasUserTypesFacade.findUsersByType(type);
    	HashSet<Users> result = new HashSet<Users>();
    	if (list!=null)
    		for(UsersHasUserTypes entry : list) 
    			result.add(entry.getUser());
    	return new ArrayList<Users>(result);
    }
    
    @Override
    public List<Users> findByType(String typeName){
    	UserTypes type = userTypesFacade.findByName(typeName);
    	if(type!=null){
	    	Collection<UsersHasUserTypes> list = type.getUsersHasUserTypesCollection();
	    	//List<UsersHasUserTypes> list = usersHasUserTypesFacade.findUsersByType(type);
	    	HashSet<Users> result = new HashSet<Users>();
	    	if (list!=null)
	    		for(UsersHasUserTypes entry : list) 
	    			result.add(entry.getUser());
	    	return new ArrayList<Users>(result);
    	} else
    		return null;
    }

    @Override
    public List<Users> findByType(Integer typeId){
    	UserTypes type = userTypesFacade.find(typeId);
    	if(type!=null){
	    	Collection<UsersHasUserTypes> list = type.getUsersHasUserTypesCollection();
	    	//List<UsersHasUserTypes> list = usersHasUserTypesFacade.findUsersByType(type);
	    	HashSet<Users> result = new HashSet<Users>();
	    	if (list!=null)
	    		for(UsersHasUserTypes entry : list) 
	    			result.add(entry.getUser());
	    	return new ArrayList<Users>(result);
    	} else
    		return null;
    }

}
