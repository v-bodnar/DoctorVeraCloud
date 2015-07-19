package ua.kiev.doctorvera.facade;

import org.junit.*;
import ua.kiev.doctorvera.entities.*;
import ua.kiev.doctorvera.facadeLocal.*;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Logger;


/**
 * Created by volodymyr.bodnar on 05.07.2015.
 */
@Ignore
public class ScheduleFacadeTest{

    private static EJBContainer container;
    private static PlanFacadeLocal planFacade;
    private static ScheduleFacadeLocal scheduleFacade;
    private static UsersFacadeLocal usersFacade;
    private static RoomsFacadeLocal roomsFacade;
    private static MethodsFacadeLocal methodsFacade;
    private static MethodTypesFacadeLocal methodTypesFacade;
    private static Rooms existingRoom;
    private static Users someUser;
    private static Methods someMethod;
    private static MethodTypes someMethodType;
    public static final String DOCTOR_LOGIN = "root";
    public static final Integer ROOM_ID_FOR_TEST = 1;
    public static final Integer METHOD_ID_FOR_TEST = 1;
    public static final Integer METHOD_TYPE_ID_FOR_TEST = 1;
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
            scheduleFacade = (ScheduleFacadeLocal) container.getContext().lookup("java:global/DoctorVeraCloud-ejb/ScheduleFacade");
            methodsFacade = (MethodsFacadeLocal) container.getContext().lookup("java:global/DoctorVeraCloud-ejb/MethodsFacade");
            methodTypesFacade = (MethodTypesFacadeLocal) container.getContext().lookup("java:global/DoctorVeraCloud-ejb/MethodTypesFacade");
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
        someUser = usersFacade.findByUsername(DOCTOR_LOGIN);

        if(planFacade.findByStartDateBetween(new GregorianCalendar(2015, 05, 27, 9, 0, 0).getTime(), new GregorianCalendar(2015, 05, 27, 11, 0, 0).getTime()).isEmpty()){
            //Creating plan record
            LOG.info("Creating Plan record for test");
            Plan planRecordForTest = new Plan();
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

        someMethodType = methodTypesFacade.find(METHOD_TYPE_ID_FOR_TEST);
        if(someMethodType == null){
            //Creating Method Type for test
            LOG.info("Creating Method Type for test");
            someMethodType = new MethodTypes();
            someMethodType.setDateCreated(NOW);
            someMethodType.setUserCreated(someUser);
            someMethodType.setShortName("УЗД");
            someMethodType.setFullName("УЗД");
            methodTypesFacade.create(someMethodType);
        } else{

            LOG.info("Method Type for test has been found");
        }

        someMethod = methodsFacade.find(METHOD_TYPE_ID_FOR_TEST);
        if(someMethod == null){
            //Creating Method for test
            LOG.info("Creating Method for test");
            someMethod = new Methods();
            someMethod.setDateCreated(NOW);
            someMethod.setUserCreated(someUser);
            someMethod.setMethodType(someMethodType);
            someMethod.setShortName("УЗИ ОБП");
            someMethod.setFullName("УЗИ ОБП");
            someMethod.setTimeInMinutes(30);
            methodsFacade.create(someMethod);
        } else{
            LOG.info("Method for test has been found");
        }

        if(scheduleFacade.findByStartDateBetween(new GregorianCalendar(2015, 05, 27, 9, 0, 0).getTime(), new GregorianCalendar(2015, 05, 27, 11, 0, 0).getTime()).isEmpty()){
            //Creating Schedule record
            LOG.info("Creating Schedule record for test");
            Schedule scheduleRecordForTest = new Schedule();
            scheduleRecordForTest.setDateCreated(NOW);
            scheduleRecordForTest.setDateTimeStart(new GregorianCalendar(2015, 05, 27, 10, 0, 0).getTime());
            scheduleRecordForTest.setDateTimeEnd(new GregorianCalendar(2015, 05, 27, 12, 0, 0).getTime());
            scheduleRecordForTest.setRoom(existingRoom);
            scheduleRecordForTest.setDoctor(someUser);
            scheduleRecordForTest.setUserCreated(someUser);
            scheduleRecordForTest.setMethod(someMethod);
            scheduleRecordForTest.setPatient(someUser);
            scheduleFacade.create(scheduleRecordForTest);
        }else{
            LOG.info("Test Schedule record is already created");
        }
    }
    @Test
    public void testFindByRoomAndStartDateBetweenExclusiveTo() throws Exception {
        //StartDate of plan is equals to given startDate
        GregorianCalendar calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 0, 0);
        GregorianCalendar calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 12, 0, 0);
        Assert.assertFalse(scheduleFacade.findByRoomAndStartDateBetweenExclusiveTo(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //StartDate of schedule is between given dates
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 9, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertFalse(scheduleFacade.findByRoomAndStartDateBetweenExclusiveTo(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //StartDate of schedule is equals to given endDate
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 9, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 10, 0, 0);
        Assert.assertTrue(scheduleFacade.findByRoomAndStartDateBetweenExclusiveTo(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //StartDate of schedule is not between givenDates
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 13, 0, 0);
        Assert.assertTrue(scheduleFacade.findByRoomAndStartDateBetweenExclusiveTo(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());
    }

    @Test
    public void testFindByRoomAndEndDateBetweenExclusiveFrom() throws Exception {
        //EndDate of schedule is equals to given startDate
        GregorianCalendar calendarTimeStart = new GregorianCalendar(2015, 05, 27, 12, 0, 0);
        GregorianCalendar calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 13, 0, 0);
        Assert.assertTrue(scheduleFacade.findByRoomAndEndDateBetweenExclusiveFrom(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //EndDate of schedule is between given dates
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 9, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 13, 0, 0);
        Assert.assertFalse(scheduleFacade.findByRoomAndEndDateBetweenExclusiveFrom(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //EndDate of schedule is equals to given endDate
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 9, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 12, 0, 0);
        Assert.assertTrue(scheduleFacade.findByRoomAndEndDateBetweenExclusiveFrom(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //EndDate of schedule is not between givenDates
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertTrue(scheduleFacade.findByRoomAndEndDateBetweenExclusiveFrom(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());
    }

    @Test
    public void testFindByRoomAndDatesInside() throws Exception {
        //Both dates are in schedule range
        GregorianCalendar calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 30, 0);
        GregorianCalendar calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertFalse(scheduleFacade.findByRoomAndDatesInsideSchedule(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Start dates are equals and endDate is inside schedule date range
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertTrue(scheduleFacade.findByRoomAndDatesInsideSchedule(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Start date is less and endDate is inside schedule date range
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 9, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertTrue(scheduleFacade.findByRoomAndDatesInsideSchedule(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Start Date is inside Plans date range, and endDate equals to those of schedule record
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 12, 0, 0);
        Assert.assertTrue(scheduleFacade.findByRoomAndDatesInsideSchedule(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Start Date is inside Plans date range, and endDate greater than those of schedule record
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 13, 0, 0);
        Assert.assertTrue(scheduleFacade.findByRoomAndDatesInsideSchedule(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Date range is outside schedule date range
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 9, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 13, 0, 0);
        Assert.assertTrue(scheduleFacade.findByRoomAndDatesInsideSchedule(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());
    }

    @Test
    public void testFindByRoomAndDatesInsideOrEqual() throws Exception {
        //Both dates are in schedule range
        GregorianCalendar calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 30, 0);
        GregorianCalendar calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertFalse(scheduleFacade.findByRoomAndDatesInsideScheduleOrEqual(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Both dates are equals to schedule range
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 30, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 12, 0, 0);
        Assert.assertFalse(scheduleFacade.findByRoomAndDatesInsideScheduleOrEqual(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Start dates are equals and endDate is inside schedule date range
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertFalse(scheduleFacade.findByRoomAndDatesInsideScheduleOrEqual(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Start Date is inside Plans date range, and endDate equals to those of schedule record
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 12, 0, 0);
        Assert.assertFalse(scheduleFacade.findByRoomAndDatesInsideScheduleOrEqual(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Date range is inside
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertFalse(scheduleFacade.findByRoomAndDatesInsideScheduleOrEqual(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Date range is outside schedule date range
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 9, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 13, 0, 0);
        Assert.assertTrue(scheduleFacade.findByRoomAndDatesInsideScheduleOrEqual(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());
    }

    @Test
    public void testFindByRoomAndDatesOutsideOrEqual() throws Exception {
        //Both dates are in schedule range
        GregorianCalendar calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 30, 0);
        GregorianCalendar calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertTrue(scheduleFacade.findByRoomAndDatesOutsideScheduleOrEqual(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Both dates are equals to schedule range
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 00, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 12, 0, 0);
        Assert.assertTrue(scheduleFacade.findByRoomAndDatesOutsideScheduleOrEqual(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Start dates are equals and endDate is inside schedule date range
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertTrue(scheduleFacade.findByRoomAndDatesOutsideScheduleOrEqual(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Start Date is inside Plans date range, and endDate equals to those of schedule record
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 12, 0, 0);
        Assert.assertTrue(scheduleFacade.findByRoomAndDatesOutsideScheduleOrEqual(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Date range is inside
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertTrue(scheduleFacade.findByRoomAndDatesOutsideScheduleOrEqual(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //Date range is outside schedule date range
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 9, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 13, 0, 0);
        Assert.assertFalse(scheduleFacade.findByRoomAndDatesOutsideScheduleOrEqual(existingRoom, calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());
    }

    @Test
    public void testFindByRoomAndDateInside() throws Exception {
        //Date is equals to startDate of schedule record
        GregorianCalendar calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 0, 0);
        Assert.assertNotNull(scheduleFacade.findByRoomAndDateInside(existingRoom, calendarTimeStart.getTime()));

        //Date is inside the range of schedule record dates
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertNotNull(scheduleFacade.findByRoomAndDateInside(existingRoom, calendarTimeStart.getTime()));

        //Date is equals to endDate of schedule record
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 12, 0, 0);
        Assert.assertNull(scheduleFacade.findByRoomAndDateInside(existingRoom, calendarTimeStart.getTime()));

        //Date is outside the range of schedule record dates
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 13, 0, 0);
        Assert.assertNull(scheduleFacade.findByRoomAndDateInside(existingRoom, calendarTimeStart.getTime()));
    }

    @Test
    public void testFindByStartDateBetween() throws Exception {
        //StartDate of schedule is equals to given startDate
        GregorianCalendar calendarTimeStart = new GregorianCalendar(2015, 05, 27, 10, 0, 0);
        GregorianCalendar calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 12, 0, 0);
        Assert.assertFalse(scheduleFacade.findByStartDateBetween(calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //StartDate of schedule is between given dates
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 9, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        Assert.assertFalse(scheduleFacade.findByStartDateBetween(calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //StartDate of schedule is equals to given endDate
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 9, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 10, 0, 0);
        Assert.assertFalse(scheduleFacade.findByStartDateBetween(calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());

        //StartDate of schedule is not between givenDates
        calendarTimeStart = new GregorianCalendar(2015, 05, 27, 11, 0, 0);
        calendarTimeEnd = new GregorianCalendar(2015, 05, 27, 13, 0, 0);
        Assert.assertTrue(scheduleFacade.findByStartDateBetween(calendarTimeStart.getTime(), calendarTimeEnd.getTime()).isEmpty());
    }
}