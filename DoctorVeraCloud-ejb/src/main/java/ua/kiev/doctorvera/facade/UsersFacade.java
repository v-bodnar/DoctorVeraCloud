/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.*;
import ua.kiev.doctorvera.facadeLocal.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * Class for implementing main operations with User entity
 *
 * @author Volodymyr Bodnar
 */
@Stateless
public class UsersFacade extends AbstractFacade<Users> implements UsersFacadeLocal {

    //private final static Logger LOG = Logger.getLogger(UsersFacade.class.getName());

    @EJB
    private DoctorsHasMethodFacadeLocal doctorsHasMethodFacade;

    @EJB
    private MethodsFacadeLocal methodsFacade;

    @EJB
    private UsersHasUserTypesFacadeLocal usersHasUserTypesFacade;

    @EJB
    private UserTypesFacadeLocal userTypesFacade;

    public UsersFacade() {
        super(Users.class);
    }

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
    * Searches for all Users with the given User Type
    * @returns List<Users> List of users that matches search parameter
    * @param type - User Type to search by
    */
    @Override
    public List<Users> findByType(UserTypes type) {
        if (type != null && type.getId() != null) {
            Collection<UsersHasUserTypes> list = usersHasUserTypesFacade.findUsersByType(type);
            //List<UsersHasUserTypes> list = usersHasUserTypesFacade.findUsersByType(type);
            HashSet<Users> result = new HashSet<Users>();
            if (list != null)
                for (UsersHasUserTypes entry : list)
                    result.add(entry.getUser());
            return new ArrayList<Users>(result);
        } else
            return null;
    }

    /**
    * Searches for all Users with the given User Type
    * @returns List<Users> List of users that matches search parameter
    * @param typeName - User Type name to search by
    */
    @Override
    public List<Users> findByType(String typeName) {
        UserTypes type = userTypesFacade.findByName(typeName);
        if (type != null) {
            Collection<UsersHasUserTypes> list = usersHasUserTypesFacade.findUsersByType(type);
            //List<UsersHasUserTypes> list = usersHasUserTypesFacade.findUsersByType(type);
            HashSet<Users> result = new HashSet<Users>();
            if (list != null)
                for (UsersHasUserTypes entry : list)
                    result.add(entry.getUser());
            return new ArrayList<Users>(result);
        } else
            return null;
    }

    /**
    * Searches for all Users with the given User Type
    * @returns List<Users> List of users that matches search parameter
    * @param typeId - User Type id to search by
    */
    @Override
    public List<Users> findByType(Integer typeId) {
        UserTypes type = userTypesFacade.find(typeId);
        if (type != null) {
            Collection<UsersHasUserTypes> list = usersHasUserTypesFacade.findUsersByType(type);
            //List<UsersHasUserTypes> list = usersHasUserTypesFacade.findUsersByType(type);
            HashSet<Users> result = new HashSet<Users>();
            if (list != null)
                for (UsersHasUserTypes entry : list)
                    result.add(entry.getUser());
            return new ArrayList<Users>(result);
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
    public boolean addUserType(Users user, UserTypes type, Users userCreated) {
        if (user != null && type != null && userCreated != null) {
            //Find all entries with the same User and UserType
            List<UsersHasUserTypes> alredyExists = new ArrayList<UsersHasUserTypes>();

            for (UsersHasUserTypes entry : usersHasUserTypesFacade.findTypesByUser(user)) {
                if (entry.getUserType().equals(type)) alredyExists.add(entry);
            }

            //Create new entry
            if (alredyExists == null || alredyExists.size() == 0) {
                UsersHasUserTypes entry = new UsersHasUserTypes();
                entry.setDateCreated(new Date());
                entry.setUser(find(user));
                entry.setUserType(userTypesFacade.find(type));
                entry.setUserCreated(find(userCreated));

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
    public boolean removeUserType(Users user, UserTypes type) {
        type = userTypesFacade.find(type);
        if (user != null && type != null) {
            List<UsersHasUserTypes> alredyExists = usersHasUserTypesFacade.findTypesByUser(user);
            for (UsersHasUserTypes entry : alredyExists) {
                if (entry.getUserType().equals(type))
                    usersHasUserTypesFacade.removeFromDB(entry);
            }

            //Collection<UsersHasUserTypes> list = type.getUsersHasUserTypesCollection();
            //list.add(entry);
            //type.setUsersHasUserTypesCollection(list);
            return true;
        } else
            return false;
    }

    /**
    * Searches for all Users with the given method
    * @returns List<Users> List of users that matches search parameter
    * @param method - Method to search by
    */
    @Override
    public List<Users> findByMethod(Methods method) {
        if (method != null && method.getId() != null) {
            Collection<DoctorsHasMethod> list = doctorsHasMethodFacade.findDoctorsByMethod(method);
            HashSet<Users> result = new HashSet<Users>();
            if (list != null)
                for (DoctorsHasMethod entry : list)
                    result.add(entry.getDoctor());
            return new ArrayList<Users>(result);
        } else
            return null;
    }

    /**
    * Searches for all Users with the given Method Id
    * @returns List<Users> List of users that matches search parameter
    * @param methodId - Method Id to search by
    */
    @Override
    public List<Users> findByMethod(Integer methodId) {
        Methods method = methodsFacade.find(methodId);
        if (method != null && method.getId() != null) {
            Collection<DoctorsHasMethod> list = doctorsHasMethodFacade.findDoctorsByMethod(method);
            HashSet<Users> result = new HashSet<Users>();
            if (list != null)
                for (DoctorsHasMethod entry : list)
                    result.add(entry.getDoctor());
            return new ArrayList<Users>(result);
        } else
            return null;
    }

    /**
    * Adds record to the reference table for referencing given user and Method
    * @returns true - in the case operation was successful and false otherwise
    * @param doctor - User that has to be referenced
    * @param method - Method that has to be referenced
    * @param userCreated - User that initiated process
    */
    @Override
    public boolean addMethod(Users doctor, Methods method, Users userCreated) {
        if (doctor != null && method != null && userCreated != null) {
            //Find all entries with the same User and UserType
            List<DoctorsHasMethod> alredyExists = new ArrayList<DoctorsHasMethod>();

            for (DoctorsHasMethod entry : doctorsHasMethodFacade.findMethodsByDoctor(doctor)) {
                if (entry.getMethod().equals(method)) alredyExists.add(entry);
            }

            //Create new entry
            if (alredyExists == null || alredyExists.size() == 0) {
                DoctorsHasMethod entry = new DoctorsHasMethod();
                entry.setDateCreated(new Date());
                entry.setDoctor(find(doctor));
                entry.setMethod(methodsFacade.find(method));
                entry.setUserCreated(find(userCreated));

                doctorsHasMethodFacade.create(entry);
            }
            return true;
        } else
            return false;
    }

    /**
    * Permanently deletes record from the reference table for removing reference between given user and Method
    * @returns true - in the case operation was successful and false otherwise
    * @param user - User that has to be unreferenced
    * @param method - Method that has to be unreferenced
    */
    @Override
    public boolean removeMethod(Users doctor, Methods method) {
        method = methodsFacade.find(method);
        if (doctor != null && method != null) {
            List<DoctorsHasMethod> alredyExists = doctorsHasMethodFacade.findMethodsByDoctor(doctor);
            for (DoctorsHasMethod entry : alredyExists) {
                if (entry.getMethod().equals(method))
                    doctorsHasMethodFacade.removeFromDB(entry);
            }
            return true;
        } else
            return false;
    }

}