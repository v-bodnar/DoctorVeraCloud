package facade;


import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.kiev.doctorvera.entities.Plan;
import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.PlanFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.RoomsFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by volodymyr.bodnar on 22.05.2015.
 */
public class PlanFacadeTest{

    private static EJBContainer container;
    private static PlanFacadeLocal planFacade;
    private static UsersFacadeLocal usersFacade;
    private static RoomsFacadeLocal roomsFacade;
    private static Rooms existingRoom;
    public static final String DOCTOR_LOGIN = "root";
    public static final Integer ROOM_ID_FOR_TEST = 1;
    public static final Date NOW = new Date();
    public static final Logger LOG = Logger.getLogger(PlanFacadeTest.class.getName());

    @BeforeClass
    public static void setUp() throws Exception {

        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
        properties.put("DoctorVera", "new://Resource?type=DataSource");
        properties.put("DoctorVera.JdbcDriver", "com.mysql.jdbc.Driver");
        properties.put("DoctorVera.JdbcUrl", "jdbc:mysql://localhost:3306/DrVera");
        properties.put("DoctorVera.hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("DoctorVera.UserName", "bodnar");
        properties.put("DoctorVera.Password", "6215891");

        container = EJBContainer.createEJBContainer(properties);
        try {
            planFacade = (PlanFacadeLocal) container.getContext().lookup("java:global/DoctorVeraCloud-ejb/PlanFacade");
            usersFacade = (UsersFacadeLocal) container.getContext().lookup("java:global/DoctorVeraCloud-ejb/UsersFacade");
            roomsFacade = (RoomsFacadeLocal) container.getContext().lookup("java:global/DoctorVeraCloud-ejb/RoomsFacade");
        } catch (NamingException ex) {
            LOG.warning(ex.getMessage());
        }
        populatePlanForTest();

    }

    @AfterClass
    public static  void tearDown() {
        LOG.info("Destroy EJB container");
        container.close();
    }

    private static void populatePlanForTest(){

        existingRoom = roomsFacade.find(ROOM_ID_FOR_TEST);

        if(planFacade.findByStartDateBetween(new GregorianCalendar(2015, 05, 27, 9, 0, 0).getTime(), new GregorianCalendar(2015, 05, 27, 11, 0, 0).getTime()).isEmpty()){
            //Creating plan record
            LOG.info("Creating Plan record for test");
            Plan planRecordForTest = new Plan();
            Users someUser = usersFacade.findByUsername(DOCTOR_LOGIN);
            planRecordForTest.setDateCreated(NOW);
            planRecordForTest.setDateTimeStart(new GregorianCalendar(2015, 05, 27, 10, 0, 0).getTime());
            planRecordForTest.setDateTimeEnd(new GregorianCalendar(2015, 05, 27, 12, 0, 0).getTime());
            planRecordForTest.setRoom(existingRoom);
            planRecordForTest.setDoctor(someUser);
            planRecordForTest.setUserCreated(someUser);
            planFacade.create(planRecordForTest);
        }else {
            LOG.info("Test Plan record is already created");
        }
    }

    @Test
    public void testFindByStartDateBetween() throws Exception {
        //StartDate of plan is equals to given startDate
        GregorianCalendar calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 0, 0);
        GregorianCalendar calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 12, 0, 0);
        Assert.assertFalse(planFacade.findByStartDateBetween(calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //StartDate of plan is between given dates
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 9, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertFalse(planFacade.findByStartDateBetween(calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //StartDate of plan is equals to given endDate
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 9, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 10, 0, 0);
        Assert.assertFalse(planFacade.findByStartDateBetween(calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //StartDate of plan is not between givenDates
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 13, 0, 0);
        Assert.assertTrue(planFacade.findByStartDateBetween(calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

    }

    @Test
    public void testFindByStartDate() throws Exception {
        //StartDate of plan is equals to given startDate
        GregorianCalendar calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 0, 0);
        Assert.assertNotNull(planFacade.findByStartDate(calendarTimeStart.getTime()));

        //StartDate of plan is not equals to given startDate
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 0, 1);
        Assert.assertNull(planFacade.findByStartDate(calendarTimeStart.getTime()));
    }

    @Test
    public void testFindByStartThisWeek() throws Exception {
        //StartDate of plan is equals to given startDate
        Assert.assertTrue(planFacade.findByStartThisWeek().isEmpty());
    }

    @Test
    public void testFindByRoom() throws Exception {
        Rooms existingRoom = roomsFacade.find(ROOM_ID_FOR_TEST);
        Assert.assertFalse(planFacade.findByRoom(existingRoom).isEmpty());
    }

    @Test
    public void testFindByRoomAndStartDateBetween() throws Exception {
        //StartDate of plan is equals to given startDate
        GregorianCalendar calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 0, 0);
        GregorianCalendar calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 12, 0, 0);
        Assert.assertFalse(planFacade.findByRoomAndStartDateBetween(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //StartDate of plan is between given dates
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 9, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertFalse(planFacade.findByRoomAndStartDateBetween(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //StartDate of plan is equals to given endDate
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 9, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 10, 0, 0);
        Assert.assertTrue(planFacade.findByRoomAndStartDateBetween(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //StartDate of plan is not between givenDates
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 13, 0, 0);
        Assert.assertTrue(planFacade.findByRoomAndStartDateBetween(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());
    }

    @Test
    public void testFindByRoomAndEndDateBetween() throws Exception {
        //EndDate of plan is equals to given startDate
        GregorianCalendar calendarTimeStart = new GregorianCalendar(2015, 05, 27, 12, 0, 0);
        GregorianCalendar calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 13, 0, 0);
        Assert.assertFalse(planFacade.findByRoomAndEndDateBetween(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //EndDate of plan is between given dates
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 9, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 13, 0, 0);
        Assert.assertFalse(planFacade.findByRoomAndEndDateBetween(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //EndDate of plan is equals to given endDate
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 9, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 12, 0, 0);
        Assert.assertFalse(planFacade.findByRoomAndEndDateBetween(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //EndDate of plan is not between givenDates
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertTrue(planFacade.findByRoomAndEndDateBetween(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());
    }

    @Test
    public void testFindByRoomAndDatesInsidePlan() throws Exception {

        //Both dates are in Plan range
        GregorianCalendar calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 30, 0);
        GregorianCalendar calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertFalse(planFacade.findByRoomAndDatesInsidePlan(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Start dates are equals and endDate is inside Plans date range
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertTrue(planFacade.findByRoomAndDatesInsidePlan(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Start date is less and endDate is inside Plans date range
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 9, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertTrue(planFacade.findByRoomAndDatesInsidePlan(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Start Date is inside Plans date range, and endDate equals to those of Plan record
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 12, 0, 0);
        Assert.assertTrue(planFacade.findByRoomAndDatesInsidePlan(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Start Date is inside Plans date range, and endDate greater than those of Plan record
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 13, 0, 0);
        Assert.assertTrue(planFacade.findByRoomAndDatesInsidePlan(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Date range is outside plans date range
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 9, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 13, 0, 0);
        Assert.assertTrue(planFacade.findByRoomAndDatesInsidePlan(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());
    }

    @Test
    public void testFindByRoomAndDatesInsidePlanOrEqual() throws Exception {
        //Both dates are in Plan range
        GregorianCalendar calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 30, 0);
        GregorianCalendar calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertFalse(planFacade.findByRoomAndDatesInsidePlanOrEqual(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Both dates are equals to Plan range
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 00, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 12, 0, 0);
        Assert.assertFalse(planFacade.findByRoomAndDatesInsidePlanOrEqual(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Start dates are equals and endDate is inside Plans date range
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertFalse(planFacade.findByRoomAndDatesInsidePlanOrEqual(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Start Date is inside Plans date range, and endDate equals to those of Plan record
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 12, 0, 0);
        Assert.assertFalse(planFacade.findByRoomAndDatesInsidePlanOrEqual(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Date range is inside
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertFalse(planFacade.findByRoomAndDatesInsidePlanOrEqual(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Date range is outside plans date range
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 9, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 13, 0, 0);
        Assert.assertTrue(planFacade.findByRoomAndDatesInsidePlanOrEqual(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());
    }

    @Test
    public void testFindByRoomAndDateInside() throws Exception {
        //Date is equals to startDate of Plan record
        GregorianCalendar calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 0, 0);
        Assert.assertNotNull(planFacade.findByRoomAndDateInside(existingRoom, calendarTimeStart.getTime()));

        //Date is inside the range of Plan record dates
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertNotNull(planFacade.findByRoomAndDateInside(existingRoom, calendarTimeStart.getTime()));

        //Date is equals to endDate of Plan record
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 12, 0, 0);
        Assert.assertNull(planFacade.findByRoomAndDateInside(existingRoom, calendarTimeStart.getTime()));

        //Date is outside the range of Plan record dates
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 13, 0, 0);
        Assert.assertNull(planFacade.findByRoomAndDateInside(existingRoom, calendarTimeStart.getTime()));

    }
}