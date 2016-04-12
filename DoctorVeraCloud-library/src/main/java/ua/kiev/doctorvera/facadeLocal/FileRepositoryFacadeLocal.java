package ua.kiev.doctorvera.facadeLocal;

import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import ua.kiev.doctorvera.entities.FileRepository;
import ua.kiev.doctorvera.entities.Users;

import javax.ejb.Local;
import java.io.File;
import java.io.IOException;

/**
 * @author Volodymyr Bodnar
 * @date 23.01.2015.
 */
@Local
public interface FileRepositoryFacadeLocal extends CRUDFacade<FileRepository> {

    /**
     * Method searches for file with the given name in the repository
     * and returns temp file ready for download
     * @param fileName file name to search for
     * @return temp file ready for download
     * @throws IOException
     */
    File getFile(String fileName) throws IOException;

    /**
     * Method searches for file with the given id in the repository
     * and returns temp file ready for download
     * @param id id to search for
     * @return temp file ready for download
     * @throws IOException
     */
    File getFile(Integer id) throws IOException;

    /**
     * Method searches for file with the given id in the repository
     * and returns temp file ready for download
     * @param id id to search for
     * @return temp file ready for download
     * @throws IOException
     */
    UploadedFile getUploadedFile(Integer id) throws IOException;

    /**
     * Method searches for file with the given name in the repository
     * and returns temp file ready for download
     * @param fileName file name to search for
     * @return temp file ready for download
     * @throws IOException
     */
    UploadedFile getUploadedFile(String fileName) throws IOException;

    /**
     * Saves parsed file to File Repository in DB
     * @param file file that has to be saved
     * @param currentUser user that saves file
     */
    Integer saveFile(File file, Users currentUser);

    /**
     * Saves parsed file to File Repository in DB
     * @param fileContent byteArray that has to be saved
     * @param currentUser user that saves file
     * @param fileName name of saved file(with extention)
     */
    Integer saveFile(byte[] fileContent, String fileName, Users currentUser);

    /**
     * Method returns sql file that is halt by system in order to restore database
     * @return sql file
     * @throws IOException throws exception if file has not been not found or is not readable
     */
    StreamedContent getExistingDataBaseDump() throws IOException;
}
