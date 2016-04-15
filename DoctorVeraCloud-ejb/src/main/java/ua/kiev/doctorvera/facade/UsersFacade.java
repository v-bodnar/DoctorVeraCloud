/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import org.primefaces.model.SortOrder;
import ua.kiev.doctorvera.entities.*;
import ua.kiev.doctorvera.facadeLocal.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import ua.kiev.doctorvera.resources.Config;

/**
 * Class for implementing main operations with User entity
 *
 * @author Volodymyr Bodnar
 */
@Stateless
public class UsersFacade extends AbstractFacade<Users> implements UsersFacadeLocal {
    @EJB
    private MethodsFacadeLocal methodsFacade;

    @EJB
    private UserGroupsFacadeLocal userGroupsFacade;

    public UsersFacade() {
        super(Users.class);
    }

    private final Integer DOCTOR_GROUP_ID = Integer.parseInt(Config.getMessage("DOCTORS_USER_GROUP_ID"));

    private final Integer PATIENT_GROUP_ID = Integer.parseInt(Config.getMessage("PATIENTS_USER_GROUP_ID"));

    private final Integer ASSISTANT_GROUP_ID = Integer.parseInt(Config.getMessage("ASSISTANTS_USER_GROUP_ID"));
    /**
    * Searches for User by his username(unique value)
    * @returns Users entity that matches search parameter
    * @param username - Username of the user
    */
    @Override
    public Users findByUsername(String username) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);

        cq.select(root).where(cb.equal(root.<String>get("username"), username), cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        List<Users> resultList = getEntityManager().createQuery(cq).getResultList();
        if (resultList.size() == 1) return resultList.get(0);
        else return null;
    }

    /**
    * Searches for User by his credentials username and password
    * @returns Users entity that matches search parameter
    * @param username - Users username
    * @param username - Users password
    */
    @Override
    public Users findByCred(String username, String password) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);
        cq.select(root).where(cb.equal(root.<String>get("password"), password), cb.equal(root.<String>get("username"), username), cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        List<Users> resultList = getEntityManager().createQuery(cq).getResultList();
        if (resultList.size() == 1) return resultList.get(0);
        else return null;
    }

    /**
    * Searches for User by his credentials First Name
    * @returns Users entity that matches search parameter
    * @param firstName - Users First Name
    */
    @Override
    public List<Users> findByFirstName(String firstName) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);
        cq.select(root).where(cb.equal(root.<String>get("firstName"), firstName), cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
    * Searches for all Users with the given by his Middle Name
    * @returns List<Users> List of users that matches search parameter
    * @param middleName - Users middleName
    */
    @Override
    public List<Users> findByMiddleName(String middleName) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);
        cq.select(root).where(cb.equal(root.<String>get("middleName"), middleName), cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
    * Searches for all Users with the given Last Name
    * @returns List<Users> List of users that matches search parameter
    * @param lastName - Users lastName
    */
    @Override
    public List<Users> findByLastName(String lastName) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);
        cq.select(root).where(cb.equal(root.<String>get("lastName"), lastName), cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
    * Searches for all Users with the birth date between the given dates
    * @returns List<Users> List of users that matches search parameter
    * @param from - start date of the range
    * @param to - end date of the range
    */
    @Override
    public List<Users> findByBirthDateBetween(Date from, Date to) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);
        cq.select(root).where(cb.between(root.<Date>get("birthDate"), from, to), cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
    * Searches for all Users with the given Home Phone Number
    * @returns List<Users> List of users that matches search parameter
    * @param phoneNumberHome - phone number to search by
    */
    @Override
    public List<Users> findByPhoneNumberHome(String phoneNumberHome) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);
        cq.select(root).where(cb.equal(root.<String>get("phoneNumberHome"), phoneNumberHome), cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
    * Searches for all Users with the given Mobile Phone Number
    * @returns List<Users> List of users that matches search parameter
    * @param phoneNumberMobile - phone number to search by
    */
    @Override
    public List<Users> findByPhoneNumberMobile(String phoneNumberMobile) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);
        Predicate phonePredicate = cb.and(cb.equal(root.<Users>get("phoneNumberMobile"), phoneNumberMobile));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(phonePredicate, deletedPredicate);
        cq.distinct(true);
        List<Users> result = getEntityManager().createQuery(cq).getResultList();
        return result;
    }

    /**
    * Searches for all Users with the given description
    * @returns List<Users> List of users that matches search parameter
    * @param description - part or full description
    */
    @Override
    public List<Users> findByDescription(String description) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);
        cq.select(root).where(cb.like(root.<String>get("description"), description), cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
        //ToDo Test this!!!!
    }

    /**
    * Searches for all Users with the given User Group
    * @returns List<Users> List of users that matches search parameter
    * @param group - User Group to search by
    */
    @Override
    public List<Users> findByGroup(UserGroups group) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);

        Root root = cq.from(Users.class);
        cq.distinct(true);

        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        Predicate userGroupPredicate = cb.and(cb.isMember(group, root.get("userGroups")));
        cq.select(root).where(deletedPredicate, userGroupPredicate);

        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
    * Searches for all Users with the given User Group
    * @returns List<Users> List of users that matches search parameter
    * @param groupName - User Group name to search by
    */
    @Override
    public List<Users> findByGroup(String groupName) {
        UserGroups group = userGroupsFacade.findByName(groupName);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);

        Root root = cq.from(Users.class);
        cq.distinct(true);

        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        Predicate userGroupPredicate = cb.and(cb.isMember(group, root.get("userGroups")));
        cq.select(root).where(deletedPredicate, userGroupPredicate);

        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
    * Searches for all Users with the given User Group
    * @returns List<Users> List of users that matches search parameter
    * @param groupId - User Group id to search by
    */
    @Override
    public List<Users> findByGroup(Integer groupId) {
        UserGroups group = userGroupsFacade.find(groupId);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);

        Root root = cq.from(Users.class);
        cq.distinct(true);

        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        Predicate userGroupPredicate = cb.and(cb.isMember(group, root.get("userGroups")));
        cq.select(root).where(deletedPredicate, userGroupPredicate);

        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
    * Searches for all Users with the given method
    * @returns List<Users> List of users that matches search parameter
    * @param method - Method to search by
    */
    @Override
    public List<Users> findByMethod(Methods method) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);

        Root root = cq.from(Users.class);
        cq.distinct(true);

        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        Predicate methodsPredicate = cb.and(cb.isMember(method, root.get("methods")));
        cq.select(root).where(deletedPredicate, methodsPredicate);

        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
    * Searches for all Users with the given Method Id
    * @returns List<Users> List of users that matches search parameter
    * @param methodId - Method Id to search by
    */
    @Override
    public List<Users> findByMethod(Integer methodId) {
        Methods method = methodsFacade.find(methodId);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);

        Root root = cq.from(Users.class);
        cq.distinct(true);

        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        Predicate methodsPredicate = cb.and(cb.isMember(method, root.get("methods")));
        cq.select(root).where(deletedPredicate, methodsPredicate);

        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Checks if given user is in the group Doctors
     * @param user - User that has to be checked
     */
    @Override
    public boolean isDoctor(Users user){
       return initializeLazyEntity(user).getUserGroups().contains(userGroupsFacade.find(DOCTOR_GROUP_ID));
    }

    /**
     * Checks if given user is in the group Assistants
     * @param user - User that has to be checked
     */
    @Override
    public boolean isAssistant(Users user){
        return initializeLazyEntity(user).getUserGroups().contains(userGroupsFacade.find(ASSISTANT_GROUP_ID));
    }

    /**
     * Checks if given user is in the group Patients
     * @param user - User that has to be checked
     */
    @Override
    public boolean isPatient(Users user){
        return initializeLazyEntity(user).getUserGroups().contains(userGroupsFacade.find(PATIENT_GROUP_ID));
    }

    /**
     * Searches all Users that contain given DeliveryGroup
     * @param deliveryGroup given DeliveryGroup
     * @return all found Users
     */
    @Override
    public List<Users> findUsersByDeliveryGroup(DeliveryGroup deliveryGroup){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);

        Root root = cq.from(Users.class);
        cq.distinct(true);

        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        Predicate deliveryGroupPredicate = cb.and(cb.isMember(deliveryGroup, root.get("deliveryGroups")));
        cq.select(root).where(deletedPredicate, deliveryGroupPredicate);

        return getEntityManager().createQuery(cq).getResultList();
    }

}
