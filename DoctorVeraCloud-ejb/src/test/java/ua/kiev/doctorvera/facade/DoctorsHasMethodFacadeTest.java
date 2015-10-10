package ua.kiev.doctorvera.facade;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import ua.kiev.doctorvera.entities.DoctorsHasMethod;
import ua.kiev.doctorvera.entities.Methods;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.MethodsFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * Created by volodymyr.bodnar on 02.05.2015.
 */
@Ignore
public class DoctorsHasMethodFacadeTest {
    private static EJBContainer container;
    //private static UserGroupsFacadeLocal userTypesFacade;
    private static DoctorsHasMethodFacade doctorsHasMethodFacade;
    private static MethodsFacadeLocal methodsFacade;
    private static UsersFacadeLocal usersFacade;
    public static final String DOCTOR_LOGIN = "jeda";
    public static final Integer METHOD_ID = 1;

    @BeforeClass
    public static void setUp() {
        //Creating EJB container and searching for facade instances
        System.out.println("Create EJB container");
        container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        try {
            methodsFacade = (MethodsFacadeLocal) container.getContext().lookup("java:global/classes/MethodsFacade");
            usersFacade = (UsersFacadeLocal) container.getContext().lookup("java:global/classes/UsersFacade");
        } catch (NamingException ex) {
            Logger.getLogger(DoctorsHasMethodFacadeTest.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    @AfterClass
    public static  void tearDown() {
        //Destroying EJB container
        System.out.println("Destroy EJB container");
        container.close();
    }

    @Test
    public void testFindMethodsByDoctor() throws Exception {
        System.out.println("Testing method: findMethodsByDoctor");

        System.out.println("Find doctor...");
        Users doctor = usersFacade.findByUsername(DOCTOR_LOGIN);
        assertNotNull(doctor);

        System.out.println("Find relations...");
        List<DoctorsHasMethod> relations = doctorsHasMethodFacade.findMethodsByDoctor(doctor);
        assertNotNull(relations);
        assertTrue(relations.size()>0);

    }

    @Test
    public void testFindDoctorsByMethod() throws Exception {
        System.out.println("Testing method: findMethodsByDoctor");

        System.out.println("Find method...");
        Methods method = methodsFacade.find(METHOD_ID);
        assertNotNull(method);

        System.out.println("Find relations...");
        List<DoctorsHasMethod> relations = doctorsHasMethodFacade.findDoctorsByMethod(method);
        assertNotNull(relations);
        assertTrue(relations.size()>0);
    }
}