package ua.kiev.doctorvera.utils;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.xwpf.usermodel.*;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import ua.kiev.doctorvera.entities.FileRepository;
import ua.kiev.doctorvera.entities.Payments;
import ua.kiev.doctorvera.entities.Schedule;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.PricesFacadeLocal;
import ua.kiev.doctorvera.resources.Message;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import javax.xml.registry.infomodel.User;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by volodymyr.bodnar on 2/27/2016.
 */
@Named
@RequestScoped
public class TemplateProcessor {

    Users currentUser;
    Users user;
    Schedule schedule;
    Payments payment;

    @EJB
    private PricesFacadeLocal pricesFacade;

    private SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");

    public StreamedContent processTemplate(FileRepository fileRepository) throws IOException {

        if (fileRepository.getExtension() == FileRepository.Extension.DOCX) {
            return processDocXTemplate(fileRepository);
        } else if (fileRepository.getExtension() == FileRepository.Extension.DOC) {
            return processDocTemplate(fileRepository);
        } else {
            throw new IOException("Wrong file format, should be doc or docx");
        }
    }

    public StreamedContent processDocTemplate(FileRepository fileRepository) throws IOException {
        InputStream is = new ByteArrayInputStream(fileRepository.getFile());
        HWPFDocument doc = new HWPFDocument(is);
        Range r1 = doc.getRange();

        for (int i = 0; i < r1.numSections(); ++i) {
            Section s = r1.getSection(i);
            for (int x = 0; x < s.numParagraphs(); x++) {
                Paragraph p = s.getParagraph(x);
                String text = p.text();
                text = replaceUsingUsersData(text);
                text = replaceUsingCommonData(text);
                text = replaceUsingScheduleData(text);
                text = replaceUsingPaymentData(text);
                for (int z = 0; z < p.numCharacterRuns(); z++) {
                    CharacterRun run = p.getCharacterRun(z);
                    if (z == 0) {
                        run.replaceText(text, false);
                    } else {
                        run.delete();
                    }
                }
            }
        }
        File tempFile = File.createTempFile(fileRepository.getFileName(), "." + fileRepository.getExtension().name().toLowerCase());
        OutputStream writer = new FileOutputStream(tempFile);
        doc.write(writer);
        writer.close();
        InputStream tempIs = new FileInputStream(tempFile);
        return new DefaultStreamedContent(tempIs, fileRepository.getMimeType(), fileRepository.getFileNameWithExtention());

    }

    public StreamedContent processDocXTemplate(FileRepository fileRepository) throws IOException {
        InputStream is = new ByteArrayInputStream(fileRepository.getFile());
        XWPFDocument doc = new XWPFDocument(is);
        for (XWPFParagraph p : doc.getParagraphs()) {
            processRuns(p);
        }
        for (XWPFTable tbl : doc.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        processRuns(p);
                    }
                }
            }
        }
        File tempFile = File.createTempFile(fileRepository.getFileName(), "." + fileRepository.getExtension().name().toLowerCase());
        OutputStream writer = new FileOutputStream(tempFile);
        doc.write(writer);
        is.close();
        InputStream tempIs = new FileInputStream(tempFile);
        return new DefaultStreamedContent(tempIs, fileRepository.getMimeType(), fileRepository.getFileNameWithExtention());
    }

    private void processRuns(XWPFParagraph p) {
        List<XWPFRun> runs = p.getRuns();
        if (runs != null && !runs.isEmpty()) {
            String text = p.getText();
            text = replaceUsingUsersData(text);
            text = replaceUsingCommonData(text);
            text = replaceUsingPaymentData(text);
            if (schedule != null)
                text = replaceUsingScheduleData(text);
            for (int i = runs.size() - 1; i > 0; i--) {
                p.removeRun(i);
            }
            XWPFRun run = runs.get(0);
            run.setText(text, 0);
        }
    }

    public String replaceUsingUsersData(String text) {
        if (text == null || user == null){
            text = text.replaceAll("\\$usersFirstName|\\$usersLastName|\\$usersMiddleName|\\$usersMiddleName, " +
                    "\\$usersId|\\$usersBirthDate|\\$usersMobilePhoneNumber|\\$usersHomePhoneNumber|\\$usersEmail|\\$usersLogin", "");
            return text;
        }
        if (text.contains("$usersFirstName"))
            text = text.replaceAll("\\$usersFirstName", user.getFirstName() == null ? "" : user.getFirstName());
        if (text.contains("$usersLastName"))
            text = text.replaceAll("\\$usersLastName", user.getLastName() == null ? "" : user.getLastName());
        if (text.contains("$usersMiddleName"))
            text = text.replaceAll("\\$usersMiddleName", user.getMiddleName() == null ? "" : user.getMiddleName());
        if (text.contains("$usersId"))
            text = text.replaceAll("\\$usersId", user.getId() == null ? "" : "" + user.getId());
        if (text.contains("$usersBirthDate"))
            text = text.replaceAll("\\$usersBirthDate", user.getBirthDate() == null ? "" : formatter.format(user.getBirthDate()));
        if (text.contains("$usersMobilePhoneNumber"))
            text = text.replaceAll("\\$usersMobilePhoneNumber", user.getPhoneNumberMobile() == null ? "" : user.getPhoneNumberMobile());
        if (text.contains("$usersHomePhoneNumber"))
            text = text.replaceAll("\\$usersHomePhoneNumber", user.getPhoneNumberHome() == null ? "" : user.getPhoneNumberHome());
        if (text.contains("$usersEmail"))
            text = text.replaceAll("\\$usersEmail", user.getEmail() == null ? "" : user.getEmail());
        if (text.contains("$usersLogin"))
            text = text.replaceAll("\\$usersLogin", user.getUsername() == null ? "" : user.getUsername());
        return text;
    }

    public String replaceUsingCommonData(String text) {
        if (text == null || currentUser == null){
            text = text.replaceAll("\\$currentDate|\\$currentUsersFirstName|\\$currentUsersLastName", "");
            return text;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
        if (text.contains("$currentDate"))
            text = text.replaceAll("\\$currentDate", formatter.format(new Date()));
        if (text.contains("$currentUsersFirstName") && currentUser != null)
            text = text.replaceAll("\\$currentUsersFirstName", currentUser.getFirstName());
        if (text.contains("$currentUsersLastName") && currentUser != null)
            text = text.replaceAll("\\$currentUsersLastName", currentUser.getLastName());
        if (text.contains("currentUsersMiddleName") && currentUser != null)
            text = text.replaceAll("\\$currentUsersMiddleName", currentUser.getMiddleName());
        return text;
    }

    public String replaceUsingScheduleData(String text) {
        if (text == null || schedule == null) {
            text = text.replaceAll("\\$scheduleId|\\$scheduleMethod", "");
            return text;
        }
        if (text.contains("$scheduleId"))
            text = text.replaceAll("\\$scheduleId", schedule.getId() == null ? "" : "" + schedule.getId());
        if (text.contains("$scheduleMethod"))
            text = text.replaceAll("\\$scheduleMethod", schedule.getMethod() == null ? "" : "" + schedule.getMethod().getShortName());
        Users patient = schedule.getPatient();

        if (text.contains("$patientFirstName"))
            text = text.replaceAll("\\$patientFirstName", patient.getFirstName() == null ? "" : patient.getFirstName());
        if (text.contains("$patientLastName"))
            text = text.replaceAll("\\$patientLastName", patient.getLastName() == null ? "" : patient.getLastName());
        if (text.contains("$patientMiddleName"))
            text = text.replaceAll("\\$patientMiddleName", patient.getMiddleName() == null ? "" : patient.getMiddleName());
        if (text.contains("$patientId"))
            text = text.replaceAll("\\$patientId", patient.getId() == null ? "" : "" + patient.getId());
        if (text.contains("$patientBirthDate"))
            text = text.replaceAll("\\$patientBirthDate", patient.getBirthDate() == null ? "" : formatter.format(patient.getBirthDate()));
        if (text.contains("$patientMobilePhoneNumber"))
            text = text.replaceAll("\\$patientMobilePhoneNumber", patient.getPhoneNumberMobile() == null ? "" : patient.getPhoneNumberMobile());
        if (text.contains("$patientHomePhoneNumber"))
            text = text.replaceAll("\\$patientHomePhoneNumber", patient.getPhoneNumberHome() == null ? "" : patient.getPhoneNumberHome());
        if (text.contains("$patientEmail"))
            text = text.replaceAll("\\$patientEmail", patient.getEmail() == null ? "" : patient.getEmail());
        if (text.contains("$patientLogin"))
            text = text.replaceAll("\\$patientLogin", patient.getUsername() == null ? "" : patient.getUsername());
        return text;
    }

    public String replaceUsingPaymentData(String text) {
        if (text == null || payment == null){
            text = text.replaceAll("\\$paymentId|\\$paymentTotal|\\$paymentDescription", "");
            return text;
        }
        if (text.contains("$paymentId"))
            text = text.replaceAll("\\$paymentId", payment.getId() == null ? "" : "" + payment.getId());
        if (text.contains("$paymentTotal"))
            text = text.replaceAll("\\$paymentTotal", payment.getTotal() + " " + Message.getMessage("APPLICATION_CURRENCY"));
        if (text.contains("$paymentDescription"))
            text = text.replaceAll("\\$paymentDescription", payment.getDescription());

        return text;
    }

    public String processMessageText(String text, Schedule schedule){
        if(schedule.getDateTimeStart() != null)
            text = text.replaceAll("\\$appointmentStartDate", "" + new SimpleDateFormat("yyyy-MM-dd").format(schedule.getDateTimeStart()));
        if(schedule.getDateTimeStart() != null)
            text = text.replaceAll("\\$appointmentStartTime", "" + new SimpleDateFormat("HH:mm").format(schedule.getDateTimeStart()));
        if(schedule.getMethod() != null && schedule.getMethod().getShortName() != null)
            text = text.replaceAll("\\$appointmentMethodName", schedule.getMethod().getShortName());
        if(schedule.getMethod() != null && schedule.getMethod() != null && pricesFacade.findLastPrice(schedule.getMethod())!=null)
            text = text.replaceAll("\\$appointmentMethodPrice", "" + pricesFacade.findLastPrice(schedule.getMethod()).getTotal());
        if(schedule.getDoctor() != null)
            text = text.replaceAll("\\$doctorsFirstName", schedule.getDoctor().getFirstName());
        if(schedule.getDoctor()  != null)
            text = text.replaceAll("\\$doctorsLastName", schedule.getDoctor().getLastName());
        return text;
    }

    public Users getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Payments getPayment() {
        return payment;
    }

    public void setPayment(Payments payment) {
        this.payment = payment;
    }
}
