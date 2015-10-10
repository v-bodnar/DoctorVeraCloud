/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facadeLocal;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import ua.kiev.doctorvera.entities.Methods;
import ua.kiev.doctorvera.entities.UserGroups;
import ua.kiev.doctorvera.entities.Users;

/**
 * Interface for declaring main operations with Generic (Identified) entity
 * @author Volodymyr Bodnar
 */
@Local
public interface UsersFacadeLocal{
    /**
    @return EntityManager got from container
    */
    EntityManager getEntityManager();

    /**
    Creates new entity representation(record) in the persistent storage(Data Base)
    @param  entity  concrete NEW entity to write
    */
    void create(Users entity);

    /**
    Updates existing entity representation(record) in the persistent storage(Data Base)
    @param  entity existing entity to be updated
    */
    void edit(Users entity);

    /**
    Marks existing entity as deleted in the persistent storage(Data Base)
    @param  entity existing entity to be marked as deleted
    */
    void remove(Users entity);

    /**
    @param id Unique object identifier
    @return Users Identified existing entity represented by unique identifier
    */
    Users find(Integer id);

    /**
    Searches for user that matches given one id
    @param user - User to search for
    @return Users Identified existing entity represented by unique identifier
    */
    Users find(Users user);

    /**
    @return List<Users> List of all Identified existing entities that are not marked as deleted
    */
    List<Users> findAll();

    /**
    @return List<Users> List of Identified existing entities that are not marked as deleted from range
    @param range
    */
    List<Users> findRange(int[] range);

    /**
    @return int records count
    */
    int count();

    /**
    <strong>Permanently</strong> removes entity from the persistent storage(Data Base)
    @param entity - Entity that has to be removed
    */
    void removeFromDB(Users entity);

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
    * Adds record to the reference table for referencing given user and User Group
    * @returns true - in the case operation was successful and false otherwise
    * @param user - User that has to be referenced
    * @param group - User Group that has to be referenced
    * @param userCreated - User that initiated process
    */
    boolean addUserGroup(Users user, UserGroups group, Users userCreated);

    /**
    * Permanently deletes record from the reference table for removing reference between given user and User Group
    * @returns true - in the case operation was successful and false otherwise
    * @param user - User that has to be unreferenced
    * @param group - User Group that has to be unreferenced
    */
    boolean removeUserGroup(Users user, UserGroups group);

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
    * Adds record to the reference table for referencing given user and Method
    * @returns true - in the case operation was successful and false otherwise
    * @param doctor - User that has to be referenced
    * @param method - Method that has to be referenced
    * @param userCreated - User that initiated process
    */
    boolean addMethod(Users doctor, Methods method, Users userCreated);

    /**
     * Permanently deletes record from the reference table for removing reference between given user and Method
     * @returns true - in the case operation was successful and false otherwise
     * @param user - User that has to be unreferenced
     * @param method - Method that has to be unreferenced
     */
    boolean removeMethod(Users user, Methods method);

    
}
