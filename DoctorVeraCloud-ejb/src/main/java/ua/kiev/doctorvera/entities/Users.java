/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ua.kiev.doctorvera.utils.Service;

/**
 * Entity class Describes User
 * @author Volodymyr Bodnar
 */
@Entity
@Table(name = "Users")
@XmlRootElement
/*
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findByUserId", query = "SELECT u FROM Users u WHERE u.userId = :userId"),
    @NamedQuery(name = "Users.findByUsername", query = "SELECT u FROM Users u WHERE u.username = :username"),
    @NamedQuery(name = "Users.findByPassword", query = "SELECT u FROM Users u WHERE u.password = :password"),
    @NamedQuery(name = "Users.findByFirstName", query = "SELECT u FROM Users u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "Users.findByMiddleName", query = "SELECT u FROM Users u WHERE u.middleName = :middleName"),
    @NamedQuery(name = "Users.findByLastName", query = "SELECT u FROM Users u WHERE u.lastName = :lastName"),
    @NamedQuery(name = "Users.findByBirthDate", query = "SELECT u FROM Users u WHERE u.birthDate = :birthDate"),
    @NamedQuery(name = "Users.findByPhoneNumberHome", query = "SELECT u FROM Users u WHERE u.phoneNumberHome = :phoneNumberHome"),
    @NamedQuery(name = "Users.findByPhoneNumberMobile", query = "SELECT u FROM Users u WHERE u.phoneNumberMobile = :phoneNumberMobile"),
    @NamedQuery(name = "Users.findByDescription", query = "SELECT u FROM Users u WHERE u.description = :description"),
    @NamedQuery(name = "Users.findByUserCreated", query = "SELECT u FROM Users u WHERE u.userCreated = :userCreated"),
    @NamedQuery(name = "Users.findByDateCreated", query = "SELECT u FROM Users u WHERE u.dateCreated = :dateCreated"),
    @NamedQuery(name = "Users.findByDeleted", query = "SELECT u FROM Users u WHERE u.deleted = :deleted")})
*/
public class Users implements Serializable,Identified<Integer> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "UserId")
    private Integer userId;
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
    @Size(max = 150)
    @Column(name = "Description")
    private String description;
    //@JoinColumn(name = "Users", referencedColumnName = "UserId")
    //@ManyToOne
    @Column(name = "UserCreated")
    private Integer userCreatedId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DateCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Deleted")
    private boolean deleted;
    @Basic(optional = false)
    @Size(max = 150)
    @Column(name = "AvatarImage", columnDefinition="default default_male_avatar.png")
    private String avatarImage;
    @Basic(optional = false)
    @NotNull
    @Size(max = 10)
    @Column(name = "Color")
    private String color;
    //@JoinColumn(name = "Address", referencedColumnName = "AddressId")
    @Column(name = "Address")
    private Integer addressId;
    
    @OneToMany(mappedBy = "recipient")
    private Collection<Payments> paymentsCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor")
    private Collection<Plan> planCollection;
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<UsersHasUserTypes> usersHasUserTypesCollection;
    
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor")
    private Collection<DoctorsHasMethod> doctorsHasMethodCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patient")
    private Collection<Schedule> scheduleCollection;
    @OneToMany(mappedBy = "assistant")
    private Collection<Schedule> scheduleCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor")
    private Collection<Schedule> scheduleCollection2;
    @OneToMany(mappedBy = "doctorDirected")
    private Collection<Schedule> scheduleCollection3;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor")
    private Collection<Share> shareCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assistant")
    private Collection<Share> shareCollection1;
       
    public Users() {
    }

    public Users(Integer userId) {
        this.userId = userId;
    }

    public Users(Integer userId, String username, String password, String firstName, String lastName, Integer userCreatedId, Date dateCreated,Integer addressId, boolean deleted, String color,String avatarImage) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressId = addressId;
        this.userCreatedId = userCreatedId;
        this.dateCreated = dateCreated;
        this.deleted = deleted;
        this.color = color;
        this.avatarImage = avatarImage;
    }

    public String getAvatarImage() {
		if (avatarImage == null || avatarImage.equals("")) return "default_male_avatar.png";
		else return avatarImage;
	}

	public void setAvatarImage(String avatarImage) {
		this.avatarImage = avatarImage;
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
        this.password = Service.encrypt(password);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = Service.firstUpperCase(firstName);
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = Service.firstUpperCase(middleName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = Service.firstUpperCase(lastName);
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
        this.phoneNumberHome = Service.stripPhone(phoneNumberHome);
    }

    public String getPhoneNumberMobile() {
        return phoneNumberMobile;
    }

    public void setPhoneNumberMobile(String phoneNumberMobile) {
        this.phoneNumberMobile = Service.stripPhone(phoneNumberMobile);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public Users getUserCreated() {
        //TODO
    	return null;
    	/*
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, userCreatedId);
        } finally {
        	em.close();
        }
        */
    }
    public Integer getUserCreatedId() {
        return userCreatedId;
    }
    
    public void setUserCreatedId(Integer userCreatedId) {
        this.userCreatedId = userCreatedId;
    }
    
    @Override
    public void setUserCreated(Users userCreated) {
        this.userCreatedId = userCreated.getId();
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

    @XmlTransient
    public Collection<Payments> getPaymentsCollection1() {
        return paymentsCollection1;
    }

    public void setPaymentsCollection1(Collection<Payments> paymentsCollection1) {
        this.paymentsCollection1 = paymentsCollection1;
    }

    public Integer getAddressId() {
        return addressId;
    }
    
    public void setAddressId(Integer addressId) {
    	this.addressId = addressId;
    }
    
    public Address getAddress() {
        //TODO
    	return null;
    	/*
        EntityManager em = getEntityManager();
        try {
            return em.find(Address.class, address);
        } finally {
        	try{
        		em.close();
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        }
        */
    }

    public void setAddress(Address address) {
        this.addressId = address.getId();
    }

	@XmlTransient
    public Collection<Plan> getPlanCollection() {
        return planCollection;
    }

    public void setPlanCollection(Collection<Plan> planCollection) {
        this.planCollection = planCollection;
    }

    @XmlTransient
    public Collection<UsersHasUserTypes> getUsersHasUserTypesCollection() {
        return usersHasUserTypesCollection;
    }

    public void setUsersHasUserTypesCollection(Collection<UsersHasUserTypes> usersHasUserTypesCollection) {
        this.usersHasUserTypesCollection = usersHasUserTypesCollection;
    }

    @XmlTransient
    public Collection<DoctorsHasMethod> getDoctorsHasMethodCollection() {
        return doctorsHasMethodCollection;
    }

    public void setDoctorsHasMethodCollection(Collection<DoctorsHasMethod> doctorsHasMethodCollection) {
        this.doctorsHasMethodCollection = doctorsHasMethodCollection;
    }

    @XmlTransient
    public Collection<Schedule> getScheduleCollection() {
        return scheduleCollection;
    }

    public void setScheduleCollection(Collection<Schedule> scheduleCollection) {
        this.scheduleCollection = scheduleCollection;
    }

    @XmlTransient
    public Collection<Schedule> getScheduleCollection1() {
        return scheduleCollection1;
    }

    public void setScheduleCollection1(Collection<Schedule> scheduleCollection1) {
        this.scheduleCollection1 = scheduleCollection1;
    }

    @XmlTransient
    public Collection<Schedule> getScheduleCollection2() {
        return scheduleCollection2;
    }

    public void setScheduleCollection2(Collection<Schedule> scheduleCollection2) {
        this.scheduleCollection2 = scheduleCollection2;
    }

    @XmlTransient
    public Collection<Schedule> getScheduleCollection3() {
        return scheduleCollection3;
    }

    public void setScheduleCollection3(Collection<Schedule> scheduleCollection3) {
        this.scheduleCollection3 = scheduleCollection3;
    }

    @XmlTransient
    public Collection<Share> getShareCollection() {
        return shareCollection;
    }

    public void setShareCollection(Collection<Share> shareCollection) {
        this.shareCollection = shareCollection;
    }

    @XmlTransient
    public Collection<Share> getShareCollection1() {
        return shareCollection1;
    }

    public void setShareCollection1(Collection<Share> shareCollection1) {
        this.shareCollection1 = shareCollection1;
    }
    /*
    private EntityManager getEntityManager(){
    	final Logger LOG = Logger.getLogger(Users.class.getName());
    	long startTime = System.nanoTime();
    	LOG.info("Creating Entity Manager in Users");
    	EntityManager em = Persistence.createEntityManagerFactory("DoctorVera").createEntityManager();
    	long endTime = System.nanoTime();
    	long duration = (endTime - startTime); 
    	LOG.info("Entity Manager created in " + duration/1000000 + " millis");
    	return em;
    }
	*/
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addressId == null) ? 0 : addressId.hashCode());
		result = prime * result
				+ ((birthDate == null) ? 0 : birthDate.hashCode());
		result = prime * result
				+ ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((color == null) ? 0 : color.hashCode());
		result = prime * result
				+ ((avatarImage == null) ? 0 : avatarImage.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((middleName == null) ? 0 : middleName.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((phoneNumberHome == null) ? 0 : phoneNumberHome.hashCode());
		result = prime
				* result
				+ ((phoneNumberMobile == null) ? 0 : phoneNumberMobile
						.hashCode());
		result = prime * result + userCreatedId.hashCode();
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Users other = (Users) obj;
		if (addressId == null) {
			if (other.addressId != null)
				return false;
		} else if (!addressId.equals(other.addressId))
			return false;
		if (birthDate == null) {
			if (other.birthDate != null)
				return false;
		} else if (!birthDate.equals(other.birthDate))
			return false;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (deleted != other.deleted)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (avatarImage == null) {
			if (other.avatarImage != null)
				return false;
		} else if (!avatarImage.equals(other.avatarImage))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (middleName == null) {
			if (other.middleName != null)
				return false;
		} else if (!middleName.equals(other.middleName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (phoneNumberHome == null) {
			if (other.phoneNumberHome != null)
				return false;
		} else if (!phoneNumberHome.equals(other.phoneNumberHome))
			return false;
		if (phoneNumberMobile == null) {
			if (other.phoneNumberMobile != null)
				return false;
		} else if (!phoneNumberMobile.equals(other.phoneNumberMobile))
			return false;
		if (userCreatedId != other.userCreatedId)
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Users [userId=" + userId + ", username=" + username
				+ ", password=" + password + ", firstName=" + firstName
				+ ", middleName=" + middleName + ", lastName=" + lastName
				+ ", birthDate=" + birthDate + ", phoneNumberHome="
				+ phoneNumberHome + ", phoneNumberMobile=" + phoneNumberMobile
				+ ", description=" + description + ", userCreatedId="
				+ userCreatedId + ", dateCreated=" + dateCreated + ", deleted="
				+ deleted + ", addressId=" + addressId + ", color=" + color +", avatarImage=" + avatarImage + "]";
	}

    @Override
    public Integer getId() {
        return getUserId();
    }

    @Override
    public void setId(Integer id) {
        setUserId(id);
    }   
}
