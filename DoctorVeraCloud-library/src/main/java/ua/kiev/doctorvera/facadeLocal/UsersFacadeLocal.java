/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facadeLocal;

import java.lang.reflect.Field;
import java.util.*;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import org.primefaces.model.SortOrder;
import ua.kiev.doctorvera.entities.DeliveryGroup;
import ua.kiev.doctorvera.entities.Methods;
import ua.kiev.doctorvera.entities.UserGroups;
import ua.kiev.doctorvera.entities.Users;

/**
 * Interface for declaring main operations with Generic (Identified) entity
 * @author Volodymyr Bodnar
 */
@Local
public interface UsersFacadeLocal extends CRUDFacade<Users>{

    /**
    * Searches for User by his username(unique value)
    * @returns Users entity that matches search parameter
    * @param username - Username of the user
    */
    Users findByUsername(String username);

    /**
    * Searches for User by his credentials username and password
    * @returns Users entity that matches search parameter
    * @param username - Users username
    * @param username - Users password
    */
    Users findByCred(String username, String password);

    /**
    * Searches for User by his credentials First Name
    * @returns Users entity that matches search parameter
    * @param firstName - Users First Name
    */
    List<Users> findByFirstName(String firstName);

    /**
    * Searches for all Users with the given by his Middle Name
    * @returns List<Users> List of users that matches search parameter
    * @param middleName - Users middleName
    */
    List<Users> findByMiddleName(String middleName);

    /**
    * Searches for all Users with the given Last Name
    * @returns List<Users> List of users that matches search parameter
    * @param lastName - Users lastName
    */
    List<Users> findByLastName(String lastName);

    /**
    * Searches for all Users with the birth date between the given dates
    * @returns List<Users> List of users that matches search parameter
    * @param from - start date of the range
    * @param to - end date of the range
    */
    List<Users> findByBirthDateBetween(Date from, Date to);

    /**
    * Searches for all Users with the given Home Phone Number
    * @returns List<Users> List of users that matches search parameter
    * @param phoneNumberHome - phone number to search by
    */
    List<Users> findByPhoneNumberHome(String phoneNumberHome);

    /**
    * Searches for all Users with the given Mobile Phone Number
    * @returns List<Users> List of users that matches search parameter
    * @param phoneNumberMobile - phone number to search by
    */
    List<Users> findByPhoneNumberMobile(String phoneNumberMobile);

    /**
    * Searches for all Users with the given description
    * @returns List<Users> List of users that matches search parameter
    * @param description - part or full description
    */
    List<Users> findByDescription(String description);

    /**
    * Searches for all Users with the given User Group
    * @returns List<Users> List of users that matches search parameter
    * @param group - User Group to search by
    */
    List<Users> findByGroup(UserGroups group);

    /**
    * Searches for all Users with the given User Group
    * @returns List<Users> List of users that matches search parameter
    * @param typeName - User Group name to search by
    */
    List<Users> findByGroup(String typeName);

    /**
    * Searches for all Users with the given User Group
    * @returns List<Users> List of users that matches search parameter
    * @param typeId - User Group id to search by
    */
    List<Users> findByGroup(Integer typeId);

    /**
    * Searches for all Users with the given method
    * @returns List<Users> List of users that matches search parameter
    * @param method - Method to search by
    */
    List<Users> findByMethod(Methods method);

    /**
    * Searches for all Users with the given Method Id
    * @returns List<Users> List of users that matches search parameter
    * @param methodId - Method Id to search by
    */
    List<Users> findByMethod(Integer methodId);

    /**
     * Checks if given user is in the group Doctors
     * @param user - User that has to be checked
     */
    boolean isDoctor(Users user);

    /**
     * Checks if given user is in the group Patients
     * @param user - User that has to be checked
     */
    boolean isPatient(Users user);

    /**
     * Searches all Users that contain given DeliveryGroup
     * @param deliveryGroup given DeliveryGroup
     * @return all found Users
     */
    List<Users> findUsersByDeliveryGroup(DeliveryGroup deliveryGroup);

}
