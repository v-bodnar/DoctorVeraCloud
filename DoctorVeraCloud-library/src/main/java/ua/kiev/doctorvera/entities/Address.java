/**
 * This document is a part of the source code and related artifacts
 * for DoctorVeraCloud, Medical CRM System
 * for medical institutions
 *
 * http://www.cloud.doctorvera.kiev.ua/info
 *
 * Copyright © 2015 Doctor Vera {All rights reserved}
 *
 * DoctorVeraCloud project can not be copied and/or distributed without the express
 * permission of General Manager of Doctor Vera trade mark
 */
package ua.kiev.doctorvera.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entity class Describes User
 * @author Volodymyr Bodnar
 * @date 2015.09.28
 */
@Entity
@Table(name = "Address")
@XmlRootElement
public class Address implements Serializable, Identified<Integer> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AddressId")
    private Integer addressId;
    @Column(name = "Country")
    private String country;
    @Column(name = "Region")
    private String region;
    @Column(name = "City")
    private String city;
    @Column(name = "Address")
    private String address;
    @Column(name = "PostIndex")
    private Integer postIndex;
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
    @OneToMany(mappedBy = "address",fetch= FetchType.LAZY)
    private Collection<Users> usersCollection;

    public Address() {
    }

    public Address(Integer addressId) {
        this.addressId = addressId;
    }

    public Address(Integer addressId, Date dateCreated, boolean deleted) {
        this.addressId = addressId;
        this.dateCreated = dateCreated;
        this.deleted = deleted;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPostIndex() {
        return postIndex;
    }

    public void setPostIndex(Integer postIndex) {
        this.postIndex = postIndex;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Users getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(Users userCreated) {
        this.userCreated = userCreated;
    }
    @XmlTransient
    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }
	@Override
	public Integer getId() {
		return getAddressId();
	}

	@Override
	public void setId(Integer id) {
		setAddressId(id);
	}

	@Override
	public String toString() {
		return "Address [addressId=" + addressId + ", country=" + country
				+ ", region=" + region + ", city=" + city + ", address="
				+ address + ", index=" + postIndex + ", dateCreated=" + dateCreated
				+ ", deleted=" + deleted + ", userCreated=" + userCreated
				+  "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result
				+ ((addressId == null) ? 0 : addressId.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result
				+ ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + ((postIndex == null) ? 0 : postIndex.hashCode());
		result = prime * result + ((region == null) ? 0 : region.hashCode());
		result = prime * result
				+ ((userCreated == null) ? 0 : userCreated.hashCode());
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
		Address other = (Address) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (addressId == null) {
			if (other.addressId != null)
				return false;
		} else if (!addressId.equals(other.addressId))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (deleted != other.deleted)
			return false;
		if (postIndex == null) {
			if (other.postIndex != null)
				return false;
		} else if (!postIndex.equals(other.postIndex))
			return false;
		if (region == null) {
			if (other.region != null)
				return false;
		} else if (!region.equals(other.region))
			return false;
		if (userCreated == null) {
			if (other.userCreated != null)
				return false;
		} else if (!userCreated.equals(other.userCreated))
			return false;
		return true;
	}
	
	
    
}
