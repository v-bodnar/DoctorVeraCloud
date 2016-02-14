package ua.kiev.doctorvera.resources;

import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Abstract class that contains common methods needed for ResourceBundles in the system
 */
public abstract class LocalizedResource extends ListResourceBundle implements Serializable{

    private Locale locale;

    public LocalizedResource() {
        Locale defaultLocale;
        if(FacesContext.getCurrentInstance() != null){
            defaultLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        }else{
            defaultLocale = Locale.getDefault();
        }
        setParent(ResourceBundle.getBundle(getBundleName(), defaultLocale, new DBControl()));
    }

    public LocalizedResource(Locale locale) {
        setParent(ResourceBundle.getBundle(getBundleName(), locale, new DBControl()));
        this.locale = locale;
    }

    @Override
    protected Object[][] getContents() {
        return ((DBControl.ResourceService)parent).getContents();
    }

    abstract String getBundleName();

}
