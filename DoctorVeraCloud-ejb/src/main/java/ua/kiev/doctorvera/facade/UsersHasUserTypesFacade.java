/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ua.kiev.doctorvera.entities.UserTypes;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.entities.UsersHasUserTypes;

/**
 *
 * @author Bodun
 */
@Stateless
public class UsersHasUserTypesFacade extends AbstractFacade<UsersHasUserTypes> implements UsersHasUserTypesFacadeLocal {
	
    public UsersHasUserTypesFacade() {
        super(UsersHasUserTypes.class);
    }

	@Override
	public List<UsersHasUserTypes> findTypesByUser(Users user) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UsersHasUserTypes> cq = cb.createQuery(UsersHasUserTypes.class);
        Root<UsersHasUserTypes> root = cq.from(UsersHasUserTypes.class);
        
        cq.select(root).where(cb.equal(root.<Users>get("user"), user),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
	}

	@Override
	public List<UsersHasUserTypes> findUsersByType(UserTypes type) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UsersHasUserTypes> cq = cb.createQuery(UsersHasUserTypes.class);
        Root<UsersHasUserTypes> root = cq.from(UsersHasUserTypes.class);
        
        cq.select(root).where(cb.equal(root.<Users>get("userType"), type),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
	}
    
}
