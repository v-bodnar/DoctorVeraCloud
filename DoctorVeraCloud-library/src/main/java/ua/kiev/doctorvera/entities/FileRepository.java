package ua.kiev.doctorvera.entities;

import javax.mail.internet.MimeMessage;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Entity class Describes File persisted to database
 * @author Volodymyr Bodnar
 * @date 23.02.2016
 */

@Entity
@Table(name = "FileRepository")
@XmlRootElement
public class FileRepository implements Serializable, Identified<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FileRepositoryId")
    private Integer fileRepositoryId;

    @Basic(optional = false)
    @NotNull
    @Column(name = "FileName")
    private String fileName;

    @Basic(optional = false)
    @NotNull
    @Column(name = "Extension")
    @Enumerated(EnumType.ORDINAL)
    private Extension extension;

    @Lob
    @NotNull
    @Column(name = "File")
    private byte[] file;

    @Basic(optional = false)
    @NotNull
    @Column(name = "Size")
    private Long size;

    @Basic(optional = false)
    @Column(name = "DateCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Basic(optional = false)
    @Column(name = "Deleted")
    private boolean deleted;
    @JoinColumn(name = "UserCreated", referencedColumnName = "UserId")
    @ManyToOne(optional = false,fetch= FetchType.LAZY)
    private Users userCreated;

    public enum Extension {
        DOC,
        DOCX,
        XLS,
        XLSX,
        PDF,
        TXT,
        JPG,
        PNG,
        XML
    }

    public enum MimeType {
        DOC("application/msword"),
        DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        XLS("application/vnd.ms-excel"),
        XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet "),
        PDF("application/pdf"),
        TXT("text/plain"),
        JPG("image/jpeg"),
        PNG("image/png"),
        XML("application/xml");
        MimeType(String mimeType){
            this.mimeType = mimeType;
        }
        private String mimeType;

        public String getMimeType() {
            return mimeType;
        }

        public void setMimeType(String mimeType) {
            this.mimeType = mimeType;
        }
    }

    public Integer getFileRepositoryId() {
        return fileRepositoryId;
    }

    public void setFileRepositoryId(Integer fileRepositoryId) {
        this.fileRepositoryId = fileRepositoryId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileNameWithExtention() {
        return fileName + "." + extension.name().toLowerCase();
    }

    public String getMimeType(){
        return MimeType.valueOf(extension.name()).getMimeType();
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Extension getExtension() {
        return extension;
    }

    public void setExtension(Extension extension) {
        this.extension = extension;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public Date getDateCreated() {
        return dateCreated;
    }

    @Override
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public Users getUserCreated() {
        return userCreated;
    }

    @Override
    public void setUserCreated(Users userCreated) {
        this.userCreated = userCreated;
    }

    @Override
    public Integer getId() {
        return getFileRepositoryId();
    }

    @Override
    public void setId(Integer id) {
        setFileRepositoryId(id);
    }

    @Override
    public boolean getDeleted() {
        return deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileRepository that = (FileRepository) o;

        if (deleted != that.deleted) return false;
        if (fileRepositoryId != null ? !fileRepositoryId.equals(that.fileRepositoryId) : that.fileRepositoryId != null)
            return false;
        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) return false;
        if (extension != that.extension) return false;
        if (size != null ? !size.equals(that.size) : that.size != null) return false;
        if (!dateCreated.equals(that.dateCreated)) return false;
        return userCreated.equals(that.userCreated);

    }

    @Override
    public int hashCode() {
        int result = fileRepositoryId != null ? fileRepositoryId.hashCode() : 0;
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (extension != null ? extension.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + dateCreated.hashCode();
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + userCreated.hashCode();
        return result;
    }
}
