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
import ua.kiev.doctorvera.entities.Schedule;
import ua.kiev.doctorvera.entities.Users;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by volodymyr.bodnar on 2/27/2016.
 */
public class TemplateProcessUtil {

    public static StreamedContent processTemplate(FileRepository fileRepository, Users user, Users currentUser, Schedule schedule) throws IOException {

        if (fileRepository.getExtension() == FileRepository.Extension.DOCX) {
            return processDocXTemplate(fileRepository, user, currentUser, schedule);
        } else if (fileRepository.getExtension() == FileRepository.Extension.DOC) {
            return processDocTemplate(fileRepository, user, currentUser, schedule);
        } else {
            throw new IOException("Wrong file format, should be doc or docx");
        }
    }

    public static StreamedContent processDocTemplate(FileRepository fileRepository, Users user, Users currentUser, Schedule schedule) throws IOException {
        InputStream is = new ByteArrayInputStream(fileRepository.getFile());
        HWPFDocument doc = new HWPFDocument(is);
        Range r1 = doc.getRange();

        for (int i = 0; i < r1.numSections(); ++i) {
            Section s = r1.getSection(i);
            for (int x = 0; x < s.numParagraphs(); x++) {
                Paragraph p = s.getParagraph(x);
                String text = p.text();
                text = replaceUsingUsersData(text, user);
                text = replaceUsingCommonData(text, currentUser);
                text = replaceUsingScheduleData(text, schedule);
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

    public static StreamedContent processDocXTemplate(FileRepository fileRepository, Users user, Users currentUser, Schedule schedule) throws IOException {
        InputStream is = new ByteArrayInputStream(fileRepository.getFile());
        XWPFDocument doc = new XWPFDocument(is);
        for (XWPFParagraph p : doc.getParagraphs()) {
            processRuns(p, user, currentUser, schedule);
        }
        for (XWPFTable tbl : doc.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        processRuns(p, user, currentUser, schedule);
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

    private static void processRuns(XWPFParagraph p, Users user, Users currentUser, Schedule schedule) {
        List<XWPFRun> runs = p.getRuns();
        if (runs != null && !runs.isEmpty()) {
            String text = p.getText();
            text = replaceUsingUsersData(text, user);
            text = replaceUsingCommonData(text, currentUser);
            if (schedule != null)
                text = replaceUsingScheduleData(text, schedule);
            for (int i = runs.size() - 1; i > 0; i--) {
                p.removeRun(i);
            }
            XWPFRun run = runs.get(0);
            run.setText(text, 0);
        }
    }

    public static String replaceUsingUsersData(String text, Users user) {
        if (text == null) return null;
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
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

    public static String replaceUsingCommonData(String text, Users currentUser) {
        if (text == null) return null;
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
        if (text.contains("$currentDate"))
            text = text.replaceAll("\\$currentDate", formatter.format(new Date()));
        if (text.contains("$currentUser") && currentUser != null)
            text = text.replaceAll("\\$currentUser", currentUser.getFirstName() + " " + currentUser.getLastName());
        return text;
    }

    public static String replaceUsingScheduleData(String text, Schedule schedule) {
        if (text == null) return null;
        if (text.contains("$scheduleId"))
            text = text.replaceAll("\\$scheduleId", schedule.getId() == null ? "" : "" + schedule.getId());
        if (text.contains("$scheduleMethods"))
            text = text.replaceAll("\\$scheduleMethods", schedule.getMethod() == null ? "" : "" + schedule.getMethod().getShortName());
        return text;
    }
}
