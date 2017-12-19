/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.entities;

import org.apache.commons.io.IOUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import ua.kiev.doctorvera.utils.Utils;

import javax.faces.context.FacesContext;
import javax.persistence.*;
import javax.servlet.ServletContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * Entity class Describes User
 * @author Volodymyr Bodnar
 */
@Entity
@DynamicInsert
@Table(name = "Users")
@XmlRootElement
public class Users implements Serializable,Identified<Integer> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private Integer userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Foreigner")
    private Boolean foreigner;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "FirstName")
    private String firstName;
    @Size(max = 45)
    @Column(name = "MiddleName")
    private String middleName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "LastName")
    private String lastName;
    @Column(name = "BirthDate")
    @Temporal(TemporalType.DATE)
    private Date birthDate;
    @Size(max = 45)
    @Column(name = "PhoneNumberHome")
    private String phoneNumberHome;
    @Size(max = 45)
    @Column(name = "PhoneNumberMobile")
    private String phoneNumberMobile;
    @Size(max = 45)
    @Column(name = "Email")
    private String email;
    @Size(max = 150)
    @Column(name = "Description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DateCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Deleted")
    private boolean deleted;
    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    @Column(name = "AvatarImage")
    private byte[] avatarImage;
    @Basic(optional = false)
    @Size(max = 10)
    @Column(name = "Color", nullable=false, columnDefinition="varchar(150) default 'ffffff'")
    private String color;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "Address")
    private Address address;

    @ManyToOne
    @JoinColumn (name = "UserCreated")
    private Users userCreated;

    @ManyToOne
    @JoinColumn (name = "Locale")
    private Locale locale;

    @OrderColumn(name="UserGroup")
    @ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(
        name="UsersHasUserGroups",
        joinColumns={@JoinColumn(name="Users", referencedColumnName="UserId")},
        inverseJoinColumns={@JoinColumn(name="UserGroup", referencedColumnName="UserGroupId")})
    private Collection<UserGroups> userGroups;

    /**
     * If current user is Doctor he should have selected methods he can provide
     */
    @OrderColumn(name="Method")
    @ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(
            name="DoctorsHasMethod",
            joinColumns={@JoinColumn(name="Doctor", referencedColumnName="UserId")},
            inverseJoinColumns={@JoinColumn(name="Method", referencedColumnName="MethodId")})
    private Collection<Methods> methods;

    @ManyToMany(mappedBy="users")
    @OrderColumn(name="deliveryGroup")
    private Collection<DeliveryGroup> deliveryGroups;

    @ManyToMany(mappedBy="doctors")
    @OrderColumn(name="shareCollectionDoctors")
    private Collection<Share> shareCollectionDoctors;

    @ManyToMany(mappedBy="assistants")
    @OrderColumn(name="shareCollectionAssistants")
    private Collection<Share> shareCollectionAssistants;

    @Basic(optional = false)
    @NotNull
    @Column(name = "MessagingAccepted")
    private boolean messagingAccepted;

    public Users() {
    }

    public Users(Integer userId) {
        this.userId = userId;
    }

    public byte[] getAvatarImage() throws IOException {
        if (avatarImage != null && avatarImage.length != 0){
            return avatarImage;
        }else{
            final ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            final String DEFAULT_AVATAR_IMAGE_PATH = "/resources/images/avatar/default_male_avatar.png";
            return IOUtils.toByteArray(new FileInputStream(new File(servletContext.getRealPath("") + File.separator + DEFAULT_AVATAR_IMAGE_PATH)));
        }
    }

    public void setAvatarImage(byte[] avatarImage) throws IOException {
        if (avatarImage != null && avatarImage.length != 0){
            this.avatarImage = avatarImage;
        }else{
            final ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            final String DEFAULT_AVATAR_IMAGE_PATH = "/resources/images/avatar/default_male_avatar.png";
            this.avatarImage =  IOUtils.toByteArray(new FileInputStream(new File(servletContext.getRealPath("") + File.separator + DEFAULT_AVATAR_IMAGE_PATH)));
        }
    }

    public StreamedContent getAvatarImageStream() throws IOException {
        return new DefaultStreamedContent(new ByteArrayInputStream(getAvatarImage()));
    }

    public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = Utils.encrypt(password);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = Utils.firstUpperCase(firstName);
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = Utils.firstUpperCase(middleName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = Utils.firstUpperCase(lastName);
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumberHome() {
        return phoneNumberHome;
    }

    public void setPhoneNumberHome(String phoneNumberHome) {
        this.phoneNumberHome = Utils.stripPhone(phoneNumberHome);
    }

    public String getPhoneNumberMobile() {
        return phoneNumberMobile;
    }

    public void setPhoneNumberMobile(String phoneNumberMobile) {
        this.phoneNumberMobile = Utils.stripPhone(phoneNumberMobile);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getForeigner() {
        return foreigner;
    }

    public Boolean isForeigner() {
        return foreigner;
    }

    public void setForeigner(Boolean isForeigner) {
        this.foreigner = isForeigner;
    }

    public Boolean IsForeigner() {
        return foreigner;
    }

    public Collection<DeliveryGroup> getDeliveryGroups() {
        return deliveryGroups;
    }

    public void setDeliveryGroups(Collection<DeliveryGroup> deliveryGroups) {
        this.deliveryGroups = deliveryGroups;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getMessagingAccepted() {
        return messagingAccepted;
    }

    public Boolean isMessagingAccepted() {
        return messagingAccepted;
    }

    public void setMessagingAccepted(Boolean messagingAccepted) {
        this.messagingAccepted = messagingAccepted;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Collection<Share> getShareCollectionDoctors() {
        return shareCollectionDoctors;
    }

    public void setShareCollectionDoctors(Collection<Share> shareCollectionDoctors) {
        this.shareCollectionDoctors = shareCollectionDoctors;
    }

    public Collection<Share> getShareCollectionAssistants() {
        return shareCollectionAssistants;
    }

    public void setShareCollectionAssistants(Collection<Share> shareCollectionAssistants) {
        this.shareCollectionAssistants = shareCollectionAssistants;
    }

    @XmlTransient
    public Collection<UserGroups> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(Collection<UserGroups> userGroups) {
        this.userGroups = userGroups;
    }

    public Collection<Methods> getMethods() {
        return methods;
    }

    public void setMethods(Collection<Methods> methods) {
        this.methods = methods;
    }

    @Override
    public Integer getId() {
        return getUserId();
    }
    @Override
    public void setId(Integer id) {
        setUserId(id);
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
    public Date getDateCreated() {
        return dateCreated;
    }
    @Override
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    @Override
    public boolean getDeleted() {
        return deleted;
    }
    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Users users = (Users) o;

        if (deleted != users.deleted) return false;
        if (messagingAccepted != users.messagingAccepted) return false;
        if (userId != null ? !userId.equals(users.userId) : users.userId != null) return false;
        if (!foreigner.equals(users.foreigner)) return false;
        if (!username.equals(users.username)) return false;
        if (!password.equals(users.password)) return false;
        if (!firstName.equals(users.firstName)) return false;
        if (middleName != null ? !middleName.equals(users.middleName) : users.middleName != null) return false;
        if (lastName != null ? !lastName.equals(users.lastName) : users.lastName != null) return false;
        if (birthDate != null ? !birthDate.equals(users.birthDate) : users.birthDate != null) return false;
        if (phoneNumberHome != null ? !phoneNumberHome.equals(users.phoneNumberHome) : users.phoneNumberHome != null)
            return false;
        if (!phoneNumberMobile.equals(users.phoneNumberMobile)) return false;
        if (email != null ? !email.equals(users.email) : users.email != null) return false;
        if (description != null ? !description.equals(users.description) : users.description != null) return false;
        if (dateCreated != null ? !dateCreated.equals(users.dateCreated) : users.dateCreated != null) return false;
        if (!Arrays.equals(avatarImage, users.avatarImage)) return false;
        return color != null ? color.equals(users.color) : users.color == null;

    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + foreigner.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        result = 31 * result + (phoneNumberHome != null ? phoneNumberHome.hashCode() : 0);
        result = 31 * result + phoneNumberMobile.hashCode();
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (dateCreated != null ? dateCreated.hashCode() : 0);
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + Arrays.hashCode(avatarImage);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (messagingAccepted? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
