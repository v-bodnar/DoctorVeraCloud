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

/**
 *
 * @author Bodun
 */
@Stateless
public class UserTypesFacade extends AbstractFacade<UserTypes> implements UserTypesFacadeLocal {
	
    public UserTypesFacade() {
        super(UserTypes.class);
    }
    
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
    
}
