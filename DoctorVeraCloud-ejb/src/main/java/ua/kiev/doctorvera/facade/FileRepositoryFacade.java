package ua.kiev.doctorvera.facade;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.model.DefaultUploadedFile;
import org.primefaces.model.UploadedFile;
import ua.kiev.doctorvera.entities.FileRepository;
import ua.kiev.doctorvera.entities.Locale;
import ua.kiev.doctorvera.entities.MessageBundle;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.FileRepositoryFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.MessageBundleFacadeLocal;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.*;
import java.util.*;

/**
 * Class for implementing main operations with Locale entity
 *
 * @author Volodymyr Bodnar
 * @date 23.01.2016
 */
@Stateless
public class FileRepositoryFacade extends AbstractFacade<FileRepository> implements FileRepositoryFacadeLocal {
    public FileRepositoryFacade() {
        super(FileRepository.class);
    }

    @Override
    public File getFile(String fileName) throws IOException {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<FileRepository> cq = cb.createQuery(FileRepository.class);

        Root root = cq.from(FileRepository.class);
        cq.distinct(true);
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        Predicate  fileNamePredicate = cb.and(cb.equal(root.get("fileName"), fileName));
        cq.select(root).where(deletedPredicate, fileNamePredicate);

        FileRepository file = getEntityManager().createQuery(cq).getSingleResult();

        File tempFile = File.createTempFile(file.getFileName(), "." + file.getExtension().name().toLowerCase());

        FileOutputStream  fileInputStream = new FileOutputStream(tempFile);
        fileInputStream.write(file.getFile());
        fileInputStream.close();

        return  tempFile;
    }

    @Override
    public File getFile(Integer id) throws IOException{
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<FileRepository> cq = cb.createQuery(FileRepository.class);

        Root root = cq.from(FileRepository.class);
        cq.distinct(true);
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        Predicate  fileNamePredicate = cb.and(cb.equal(root.get("fileRepositoryId"), id));
        cq.select(root).where(deletedPredicate, fileNamePredicate);

        FileRepository file = getEntityManager().createQuery(cq).getSingleResult();

        File tempFile = File.createTempFile(file.getFileName(), "." + file.getExtension().name().toLowerCase());

        FileOutputStream  fileInputStream = new FileOutputStream(tempFile);
        fileInputStream.write(file.getFile());
        fileInputStream.close();

        return  tempFile;
    }

    @Override
    public UploadedFile getUploadedFile(Integer id) throws IOException{
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<FileRepository> cq = cb.createQuery(FileRepository.class);

        Root root = cq.from(FileRepository.class);
        cq.distinct(true);
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        Predicate  fileNamePredicate = cb.and(cb.equal(root.get("fileRepositoryId"), id));
        cq.select(root).where(deletedPredicate, fileNamePredicate);

        FileRepository file = getEntityManager().createQuery(cq).getSingleResult();

        File tempFile = File.createTempFile(file.getFileName(), "." + file.getExtension().name().toLowerCase());

        FileOutputStream  fileInputStream = new FileOutputStream(tempFile);
        fileInputStream.write(file.getFile());
        fileInputStream.close();
        FileItem item = new DiskFileItem(file.getFileName(), file.getMimeType(), false, file.getFileNameWithExtention(), file.getSize().intValue(), tempFile);
        return  new DefaultUploadedFile(item);
    }

    @Override
    public UploadedFile getUploadedFile(String fileName) throws IOException {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<FileRepository> cq = cb.createQuery(FileRepository.class);

        Root root = cq.from(FileRepository.class);
        cq.distinct(true);
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        Predicate  fileNamePredicate = cb.and(cb.equal(root.get("fileName"), fileName));
        cq.select(root).where(deletedPredicate, fileNamePredicate);

        FileRepository file = getEntityManager().createQuery(cq).getSingleResult();

        File tempFile = File.createTempFile(file.getFileName(), "." + file.getExtension().name().toLowerCase());

        FileOutputStream  fileInputStream = new FileOutputStream(tempFile);
        fileInputStream.write(file.getFile());
        fileInputStream.close();

        FileItem item = new DiskFileItem(file.getFileName(), file.getMimeType(), false, file.getFileNameWithExtention(), file.getSize().intValue(), tempFile);
        return  new DefaultUploadedFile(item);
    }

    @Override
    public Integer saveFile(File file, Users currentUser) {
        FileRepository newFile = new FileRepository();
        newFile.setFileName(FilenameUtils.removeExtension(file.getName()));
        newFile.setExtension(getExtention(file.getName()));
        newFile.setUserCreated(currentUser);
        newFile.setDateCreated(new Date());
        newFile.setSize(file.length());
        create(newFile);
        return  newFile.getId();
    }

    @Override
    public Integer saveFile(byte[] fileContent,String fileName, Users currentUser) {
        FileRepository newFile = new FileRepository();
        newFile.setFileName(FilenameUtils.removeExtension(fileName));
        newFile.setExtension(getExtention(fileName));
        newFile.setUserCreated(currentUser);
        newFile.setDateCreated(new Date());
        newFile.setSize((long)fileContent.length);
        newFile.setFile(fileContent);
        create(newFile);
        return  newFile.getId();
    }

    private FileRepository.Extension getExtention(String fileName){
            switch(FilenameUtils.getExtension(fileName).toLowerCase()){
                case ("doc"): return FileRepository.Extension.DOC;
                case ("docx"): return FileRepository.Extension.DOCX;
                case ("xls"): return FileRepository.Extension.XLS;
                case ("xlsx"): return FileRepository.Extension.XLSX;
                case ("pdf"): return FileRepository.Extension.PDF;
                case ("txt"): return FileRepository.Extension.TXT;
                case ("jpg"): return FileRepository.Extension.JPG;
                case ("png"): return FileRepository.Extension.PNG;
                case ("xml"): return FileRepository.Extension.XML;
                default: throw new RuntimeException("Unknown file format");
            }
    }
}
