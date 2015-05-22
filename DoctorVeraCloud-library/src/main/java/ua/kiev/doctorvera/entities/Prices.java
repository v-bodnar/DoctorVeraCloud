/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity class Describes User
 * @author Volodymyr Bodnar
 */
@Entity
@Table(name = "Prices")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Prices.findAll", query = "SELECT p FROM Prices p"),
    @NamedQuery(name = "Prices.findByPriceId", query = "SELECT p FROM Prices p WHERE p.priceId = :priceId"),
    @NamedQuery(name = "Prices.findByTotal", query = "SELECT p FROM Prices p WHERE p.total = :total"),
    @NamedQuery(name = "Prices.findByDateTime", query = "SELECT p FROM Prices p WHERE p.dateTime = :dateTime"),
    @NamedQuery(name = "Prices.findByDateCreated", query = "SELECT p FROM Prices p WHERE p.dateCreated = :dateCreated"),
    @NamedQuery(name = "Prices.findByDeleted", query = "SELECT p FROM Prices p WHERE p.deleted = :deleted")})
public class Prices implements Serializable,Identified<Integer> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PriceId")
    private Integer priceId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Total")
    private float total;
    @Column(name = "DateTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DateCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Deleted")
    private boolean deleted;
    @JoinColumn(name = "Method", referencedColumnName = "MethodId")
    @ManyToOne(optional = false)
    private Methods method;
    @JoinColumn(name = "UserCreated", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users userCreated;

    public Prices() {
    }

    public Prices(Integer priceId) {
        this.priceId = priceId;
    }

    public Prices(Integer priceId, float total, Date dateCreated, boolean deleted) {
        this.priceId = priceId;
        this.total = total;
        this.dateCreated = dateCreated;
        this.deleted = deleted;
    }

    public Integer getPriceId() {
        return priceId;
    }

    public void setPriceId(Integer priceId) {
        this.priceId = priceId;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
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

    public Methods getMethod() {
        return method;
    }

    public void setMethod(Methods method) {
        this.method = method;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result
				+ ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((priceId == null) ? 0 : priceId.hashCode());
		result = prime * result + Float.floatToIntBits(total);
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
		Prices other = (Prices) obj;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (deleted != other.deleted)
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		if (priceId == null) {
			if (other.priceId != null)
				return false;
		} else if (!priceId.equals(other.priceId))
			return false;
		if (Float.floatToIntBits(total) != Float.floatToIntBits(other.total))
			return false;
		if (userCreated == null) {
			if (other.userCreated != null)
				return false;
		} else if (!userCreated.equals(other.userCreated))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Prices [priceId=" + priceId + ", total=" + total
				+ ", dateTime=" + dateTime + ", dateCreated=" + dateCreated
				+ ", deleted=" + deleted + ", method=" + method
				+ ", userCreated=" + userCreated + "]";
	}

    @Override
    public Integer getId() {
        return getPriceId();
    }

    @Override
    public void setId(Integer id) {
        setPriceId(id);
    }

    
    
}
