package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.Payments;
import ua.kiev.doctorvera.entities.Schedule;
import ua.kiev.doctorvera.entities.Share;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.PricesFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.ScheduleFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.ShareFacadeLocal;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * Created by volodymyr.bodnar on 3/2/2016.
 */
@Stateless
public class ShareFacade extends AbstractFacade<Share> implements ShareFacadeLocal {

    public ShareFacade() {
        super(Share.class);
    }

    @EJB
    PricesFacadeLocal pricesFacade;

    @EJB
    ScheduleFacadeLocal scheduleFacade;

    @Override
    public Map<Schedule, Map<String, Float>> findFinancialDataOnScheduleList(List<Schedule> scheduleList) throws ShareNotFoundException {
        scheduleList = scheduleFacade.initializeLazyEntity(scheduleList);
        Map<Schedule, Map<String, Float>> result = new LinkedHashMap<>();
        //Todo optimize query
        for(Schedule schedule : scheduleList){
            Map<String, Float> data = new HashMap<>();
            Float cost = pricesFacade.findPrice(schedule.getMethod(), schedule.getDateTimeStart()).getTotal();
            Float payed = calculatePayed(schedule);
            Float doctor = calculateDoctorsSalary(schedule, findByDoctorsAndAssistantsAndSchedule(schedule));
            Float doctorDirected = calculateDoctorsSalary(schedule, findByDoctorDirectedAndSchedule(schedule));
            Float assistant = calculateAssistantSalary(schedule, findByDoctorsAndAssistantsAndSchedule(schedule));
            Float center = payed - doctor - doctorDirected - assistant;

            data.put(Part.DOCTOR.name(), doctor);
            data.put(Part.ASSISTANT.name(), assistant);
            data.put(Part.DOCTOR_DIRECTED.name(), doctorDirected);
            data.put(Part.CENTER.name(), center);
            data.put(Part.PAID.name(), payed);
            data.put(Part.COST.name(), cost);
            result.put(schedule, data);
        }

        return result;
    }

    private Share findByDoctorsAndAssistantsAndSchedule(Schedule schedule) throws ShareNotFoundException {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Share> cq = cb.createQuery(Share.class);
        Root<Share> root = cq.from(Share.class);
        Predicate assistantPredicate;
        if(schedule.getAssistant() == null){
            assistantPredicate = cb.and(cb.isEmpty(root.<Collection<Users>>get("assistants")));
        }else{
            assistantPredicate = cb.and(cb.isMember(schedule.getAssistant(), root.<Collection<Users>>get("assistants")));
        }
        Predicate datePredicate = cb.and(cb.lessThan(root.<Date>get("date"), schedule.getDateTimeStart()));
        Predicate doctorPredicate = cb.and(cb.isMember(schedule.getDoctor(), root.<Collection<Users>>get("doctors")));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(doctorPredicate,assistantPredicate, datePredicate, deletedPredicate);
        cq.distinct(true);
        cq.orderBy(cb.desc(root.get("date")));
        try {
            return getEntityManager().createQuery(cq).setMaxResults(1).getSingleResult();
        }catch (NoResultException e){
            //throw new ShareNotFoundException("", e);
            return null;
        }
    }

    private Share findByDoctorDirectedAndSchedule(Schedule schedule) throws ShareNotFoundException {
        if(schedule.getDoctorDirected() == null)
                return null;
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Share> cq = cb.createQuery(Share.class);
        Root<Share> root = cq.from(Share.class);
        Predicate datePredicate = cb.and(cb.lessThan(root.<Date>get("date"), schedule.getDateTimeStart()));
        Predicate doctorDirectedPredicate = cb.and(cb.isMember(schedule.getDoctorDirected(), root.<Collection<Users>>get("doctorDirected")));
        Predicate assistantPredicate = cb.and(cb.isEmpty(root.<Collection<Users>>get("assistants")));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(doctorDirectedPredicate, assistantPredicate, datePredicate, deletedPredicate);
        cq.distinct(true);
        cq.orderBy(cb.desc(root.get("date")));
        try {
            return getEntityManager().createQuery(cq).setMaxResults(1).getSingleResult();
        }catch (NoResultException e){
            //throw new ShareNotFoundException("", e);
            return null;
        }

    }

    public float calculateDoctorsSalary(Schedule schedule, Share share){
        if(share != null && share.getPercentageDoctor() != null){
            return calculatePercents(schedule.getPayments(),share.getPercentageDoctor());
        }else if(share != null && share.getSalaryDoctor() != null){
            return share.getSalaryDoctor();
        }else{
            return 0;
        }
    }

    private float calculateAssistantSalary(Schedule schedule, Share share){
        if(share != null && share.getPercentageAssistant() != null){
            return calculatePercents(schedule.getPayments(),share.getPercentageAssistant());
        }else if(share != null && share.getSalaryAssistant() != null){
            return share.getSalaryAssistant();
        }else{
            return 0;
        }
    }

    private float calculatePayed(Schedule schedule){
        float sum = 0;
        for(Payments payment : schedule.getPayments()){
            sum += payment.getTotal();
        }
        return sum;
    }

    private float calculatePercents(Collection<Payments> paymentsList, Float percents){
        float sum = 0;
        for(Payments payment : paymentsList){
            sum += payment.getTotal() * percents/100f;
        }
        return sum;
    }

}
