package ua.kiev.doctorvera.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * Identified object interface
 */
public interface Identified<PK extends Serializable> {

    /** Returns unique object identifier */
    PK getId();
    
    /** Sets unique object identifier */
    void setId(PK id);
    
    /** Returns User that created object */
    Users getUserCreated();
    
    /** Sets User that created object */
    void setUserCreated(Users userCreated);
    
    /** Returns Date when the object was created  */
    Date getDateCreated();
    
    /** Sets Date when the object was created */
    void setDateCreated(Date datereated);
    
    /** Returns if the object is marked as deleted */	
    boolean getDeleted();
    
    /** Sets if the object is marked as deleted */
    void setDeleted(boolean deleted);
}
