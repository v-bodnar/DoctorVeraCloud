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

import ua.kiev.doctorvera.entities.DoctorsHasMethod;
import ua.kiev.doctorvera.entities.Methods;
import ua.kiev.doctorvera.entities.Users;

/**
 *
 * @author Bodun
 */
@Stateless
public class DoctorsHasMethodFacade extends AbstractFacade<DoctorsHasMethod> implements DoctorsHasMethodFacadeLocal {
	
    public DoctorsHasMethodFacade() {
        super(DoctorsHasMethod.class);
    }

	@Override
	public List<DoctorsHasMethod> findMethodsByDoctor(Users user) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<DoctorsHasMethod> cq = cb.createQuery(DoctorsHasMethod.class);
        Root<DoctorsHasMethod> root = cq.from(DoctorsHasMethod.class);
        
        cq.select(root).where(cb.equal(root.<Users>get("doctor"), user),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
	}

	@Override
	public List<DoctorsHasMethod> findDoctorsByMethod(Methods method) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<DoctorsHasMethod> cq = cb.createQuery(DoctorsHasMethod.class);
        Root<DoctorsHasMethod> root = cq.from(DoctorsHasMethod.class);
        
        cq.select(root).where(cb.equal(root.<Users>get("method"), method),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
	}
}
