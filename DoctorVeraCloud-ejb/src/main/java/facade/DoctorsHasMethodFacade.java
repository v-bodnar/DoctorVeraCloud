/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import ua.kiev.doctorvera.entities.DoctorsHasMethod;
import ua.kiev.doctorvera.entities.Methods;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.DoctorsHasMethodFacadeLocal;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author Bodun
 */
@Stateless
public class DoctorsHasMethodFacade extends AbstractFacade<DoctorsHasMethod> implements DoctorsHasMethodFacadeLocal {

    public DoctorsHasMethodFacade() {
        super(DoctorsHasMethod.class);
    }

    /**
     * Searches for given user in the Reference table DoctorsHasMethod and returns all found records
     *
     * @param user - user to search for
     * @return List of DoctorsHasMethod records that match search parameter
     */
    @Override
    public List<DoctorsHasMethod> findMethodsByDoctor(Users user) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<DoctorsHasMethod> cq = cb.createQuery(DoctorsHasMethod.class);
        Root<DoctorsHasMethod> root = cq.from(DoctorsHasMethod.class);

        cq.select(root).where(cb.equal(root.<Users>get("doctor"), user), cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Searches for given method in the Reference table DoctorsHasMethod and returns all found records
     *
     * @param method - method to search for
     * @return List of DoctorsHasMethod records that match search parameter
     */
    @Override
    public List<DoctorsHasMethod> findDoctorsByMethod(Methods method) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<DoctorsHasMethod> cq = cb.createQuery(DoctorsHasMethod.class);
        Root<DoctorsHasMethod> root = cq.from(DoctorsHasMethod.class);

        cq.select(root).where(cb.equal(root.<Users>get("method"), method), cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }
}
