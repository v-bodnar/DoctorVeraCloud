package ua.kiev.doctorvera.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * Entity class Describes language Locale in the system
 * @author Volodymyr Bodnar
 * @date 23.01.2016
 */
@Entity
@Table(name = "Locale")
@XmlRootElement
public class Locale implements Serializable, Identified<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LocaleId")
    private Integer localeId;
    @Column(name = "LanguageCode")
    private String languageCode;
    @Column(name = "CountryCode")
    private String countryCode;
    @Column(name = "Language")
    private String language;
    @Column(name = "LanguageNative")
    private String languageNative;
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
    @OneToMany(mappedBy = "locale")
    private Collection<MessageBundle> messages;
    @OneToMany(mappedBy = "locale")
    private Collection<Users> users;


    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguageNative() {
        return languageNative;
    }

    public void setLanguageNative(String languageNative) {
        this.languageNative = languageNative;
    }

    public Integer getLocaleId() {
        return localeId;
    }

    public void setLocaleId(Integer localeId) {
        this.localeId = localeId;
    }

    public Collection<Users> getUsers() {
        return users;
    }

    public void setUsers(Collection<Users> users) {
        this.users = users;
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
        return getLocaleId();
    }

    @Override
    public void setId(Integer id) {
        setLocaleId(id);
    }

    @Override
    public boolean getDeleted() {
        return deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Locale locale = (Locale) o;

        if (deleted != locale.deleted) return false;
        if (localeId != null ? !localeId.equals(locale.localeId) : locale.localeId != null) return false;
        if (languageCode != null ? !languageCode.equals(locale.languageCode) : locale.languageCode != null)
            return false;
        if (countryCode != null ? !countryCode.equals(locale.countryCode) : locale.countryCode != null) return false;
        if (language != null ? !language.equals(locale.language) : locale.language != null) return false;
        if (languageNative != null ? !languageNative.equals(locale.languageNative) : locale.languageNative != null)
            return false;
        if (!dateCreated.equals(locale.dateCreated)) return false;
        return userCreated.equals(locale.userCreated);

    }

    @Override
    public int hashCode() {
        int result = localeId != null ? localeId.hashCode() : 0;
        result = 31 * result + (languageCode != null ? languageCode.hashCode() : 0);
        result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (languageNative != null ? languageNative.hashCode() : 0);
        result = 31 * result + dateCreated.hashCode();
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + userCreated.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Locale{" +
                "languageCode='" + languageCode + '\'' +
                ", countryCode='" + countryCode + '\'' +
                '}';
    }
}
