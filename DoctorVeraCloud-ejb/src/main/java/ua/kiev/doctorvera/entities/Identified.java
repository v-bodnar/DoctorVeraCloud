package ua.kiev.doctorvera.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * Identified object interface
 */
public interface Identified<PK extends Serializable> {

    /** Returns unique object identifier */
    public PK getId();
    
    /** Sets unique object identifier */
    public void setId(PK id);
    
    /** Returns User that created object */
    public Users getUserCreated();
    
    /** Sets User that created object */
    public void setUserCreated(Users userCreated);
    
    /** Returns Date when the object was created  */
    public Date getDateCreated();
    
    /** Sets Date when the object was created */
    public void setDateCreated(Date datereated);
    
    /** Returns if the object is marked as deleted */	
    public boolean getDeleted();
    
    /** Sets if the object is marked as deleted */
    public void setDeleted(boolean deleted);
}
