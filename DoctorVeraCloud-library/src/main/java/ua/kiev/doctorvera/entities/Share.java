/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity class Describes User
 * @author Volodymyr Bodnar
 */
@Entity
@Table(name = "Share")
@XmlRootElement
public class Share implements Serializable,Identified<Integer> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ShareId")
    private Integer shareId;
    @Column(name = "SalaryDoctor")
    private Float salaryDoctor;
    @Column(name = "SalaryAssistant")
    private Float salaryAssistant;
    @Max(value=100)  @Min(value=0)
    @Column(name = "PercentageDoctor")
    private Float percentageDoctor;
    @Max(value=100)  @Min(value=0)
    @Column(name = "PercentageAssistant")
    private Float percentageAssistant;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DateCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Deleted")
    private boolean deleted;
    @OrderColumn(name="Share")
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name = "ShareHasMethods", joinColumns={@JoinColumn(name="Share", referencedColumnName = "ShareId")},
                                         inverseJoinColumns={@JoinColumn(name="Method", referencedColumnName="MethodId")})
    private Collection<Methods> methods;
    @OrderColumn(name="Share")
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name = "ShareHasDoctors", joinColumns={@JoinColumn(name="Share", referencedColumnName = "ShareId")},
                                    inverseJoinColumns={@JoinColumn(name="Doctor", referencedColumnName="UserId")})
    private Collection<Users> doctors;
    @OrderColumn(name="Share")
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name = "ShareHasAssistants", joinColumns={@JoinColumn(name="Share", referencedColumnName = "ShareId")},
                                    inverseJoinColumns={@JoinColumn(name="Assistant", referencedColumnName="UserId")})
    private Collection<Users> assistants;
    @JoinColumn(name = "UserCreated", referencedColumnName = "UserId")
    @ManyToOne(optional = false)
    private Users userCreated;

    public Share() {
    }

    public Share(Integer shareId) {
        this.shareId = shareId;
    }

    public Share(Integer shareId, Date date, Date dateCreated, boolean deleted) {
        this.shareId = shareId;
        this.date = date;
        this.dateCreated = dateCreated;
        this.deleted = deleted;
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
    public Users getUserCreated() {
        return userCreated;
    }
    @Override
    public void setUserCreated(Users userCreated) {
        this.userCreated = userCreated;
    }

    @Override
    public Integer getId() {
        return getShareId();
    }

    @Override
    public void setId(Integer id) {
        setShareId(id);
    }

    public Integer getShareId() {
        return shareId;
    }

    public void setShareId(Integer shareId) {
        this.shareId = shareId;
    }

    public Float getSalaryDoctor() {
        return salaryDoctor;
    }

    public void setSalaryDoctor(Float salaryDoctor) {
        this.salaryDoctor = salaryDoctor;
    }

    public Float getSalaryAssistant() {
        return salaryAssistant;
    }

    public void setSalaryAssistant(Float salaryAssistant) {
        this.salaryAssistant = salaryAssistant;
    }

    public Float getPercentageDoctor() {
        return percentageDoctor;
    }

    public void setPercentageDoctor(Float persentageDoctor) {
        this.percentageDoctor = persentageDoctor;
    }

    public Float getPercentageAssistant() {
        return percentageAssistant;
    }

    public void setPercentageAssistant(Float percentageAssistant) {
        this.percentageAssistant = percentageAssistant;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Collection<Methods> getMethods() {
        return methods;
    }

    public void setMethods(Collection<Methods> methods) {
        this.methods = methods;
    }

    public Collection<Users> getDoctors() {
        return doctors;
    }

    public void setDoctors(Collection<Users> doctors) {
        this.doctors = doctors;
    }

    public Collection<Users> getAssistants() {
        return assistants;
    }

    public void setAssistants(Collection<Users> assistants) {
        this.assistants = assistants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Share share = (Share) o;

        if (deleted != share.deleted) return false;
        if (shareId != null ? !shareId.equals(share.shareId) : share.shareId != null) return false;
        if (salaryDoctor != null ? !salaryDoctor.equals(share.salaryDoctor) : share.salaryDoctor != null) return false;
        if (salaryAssistant != null ? !salaryAssistant.equals(share.salaryAssistant) : share.salaryAssistant != null)
            return false;
        if (percentageDoctor != null ? !percentageDoctor.equals(share.percentageDoctor) : share.percentageDoctor != null)
            return false;
        if (percentageAssistant != null ? !percentageAssistant.equals(share.percentageAssistant) : share.percentageAssistant != null)
            return false;
        if (date != null ? !date.equals(share.date) : share.date != null) return false;
        if (!dateCreated.equals(share.dateCreated)) return false;
        return userCreated.equals(share.userCreated);

    }

    @Override
    public int hashCode() {
        int result = shareId != null ? shareId.hashCode() : 0;
        result = 31 * result + (salaryDoctor != null ? salaryDoctor.hashCode() : 0);
        result = 31 * result + (salaryAssistant != null ? salaryAssistant.hashCode() : 0);
        result = 31 * result + (percentageDoctor != null ? percentageDoctor.hashCode() : 0);
        result = 31 * result + (percentageAssistant != null ? percentageAssistant.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + dateCreated.hashCode();
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + userCreated.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Share{" +
                "shareId=" + shareId +
                ", dataTime=" + date +
                '}';
    }
}
