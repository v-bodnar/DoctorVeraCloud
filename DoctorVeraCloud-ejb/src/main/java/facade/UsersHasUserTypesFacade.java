/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import ua.kiev.doctorvera.entities.UserTypes;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.entities.UsersHasUserTypes;
import ua.kiev.doctorvera.facadeLocal.UsersHasUserTypesFacadeLocal;

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
public class UsersHasUserTypesFacade extends AbstractFacade<UsersHasUserTypes> implements UsersHasUserTypesFacadeLocal {
	
    public UsersHasUserTypesFacade() {
        super(UsersHasUserTypes.class);
    }

    /**
    Searches for given user in the Reference table UsersHasUserTypes and returns all found records
    @param user - user to search for
    @return List<UsersHasUserTypes> List of UsersHasUserTypes records that match search parameter
     */
	@Override
	public List<UsersHasUserTypes> findTypesByUser(Users user) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UsersHasUserTypes> cq = cb.createQuery(UsersHasUserTypes.class);
        Root<UsersHasUserTypes> root = cq.from(UsersHasUserTypes.class);
        
        cq.select(root).where(cb.equal(root.<Users>get("user"), user),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
	}

    /**
    Searches for given User Type in the Reference table UsersHasUserTypes and returns all found records
    @param type - User Type to search for
    @return List<UsersHasUserTypes> List of UsersHasUserTypes records that match search parameter
     */
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
