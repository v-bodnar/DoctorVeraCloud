/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.UserGroups;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.entities.UsersHasUserGroups;
import ua.kiev.doctorvera.facadeLocal.UsersHasUserGroupsFacadeLocal;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 *
 * @author Bodun
 */
@Stateless
public class UsersHasUserGroupsFacade extends AbstractFacade<UsersHasUserGroups> implements UsersHasUserGroupsFacadeLocal {
	
    public UsersHasUserGroupsFacade() {
        super(UsersHasUserGroups.class);
    }

    /**
    Searches for given user in the Reference table UsersHasUserGroups and returns all found records
    @param user - user to search for
    @return List<UsersHasUserGroups> List of UsersHasUserGroups records that match search parameter
     */
	@Override
	public List<UsersHasUserGroups> findGroupsByUser(Users user) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UsersHasUserGroups> cq = cb.createQuery(UsersHasUserGroups.class);
        Root<UsersHasUserGroups> root = cq.from(UsersHasUserGroups.class);
        
        cq.select(root).where(cb.equal(root.<Users>get("user"), user),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
	}

    /**
    Searches for given User Group in the Reference table UsersHasUserGroups and returns all found records
    @param group - User Group to search for
    @return List<UsersHasUserGroups> List of UsersHasUserGroups records that match search parameter
     */
	@Override
	public List<UsersHasUserGroups> findUsersByGroup(UserGroups group) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UsersHasUserGroups> cq = cb.createQuery(UsersHasUserGroups.class);
        Root<UsersHasUserGroups> root = cq.from(UsersHasUserGroups.class);
        
        cq.select(root).where(cb.equal(root.<Users>get("userGroup"), group),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
	}
	

    
}
