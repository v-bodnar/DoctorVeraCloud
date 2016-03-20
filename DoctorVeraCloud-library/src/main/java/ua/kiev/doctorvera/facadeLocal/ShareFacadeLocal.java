package ua.kiev.doctorvera.facadeLocal;

import ua.kiev.doctorvera.entities.Payments;
import ua.kiev.doctorvera.entities.Schedule;
import ua.kiev.doctorvera.entities.Share;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;

/**
 * Created by volodymyr.bodnar on 3/2/2016.
 */
@Local
public interface ShareFacadeLocal  extends CRUDFacade<Share> {

    Map<Schedule, Map<String, Float>> findFinancialDataOnScheduleList(List<Schedule> scheduleList) throws ShareNotFoundException;

    enum Part{
        DOCTOR,
        ASSISTANT,
        DOCTOR_DIRECTED,
        CENTER,
        COST,
        PAID;
    }

    class ShareNotFoundException extends Exception{
        public ShareNotFoundException(){super();}
        public ShareNotFoundException(String message){super(message);}
        public ShareNotFoundException(String message, Throwable cause){super(message,cause);}
    }
}
