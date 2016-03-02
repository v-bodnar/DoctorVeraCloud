package ua.kiev.doctorvera.views;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.ApplicationResourceBundle;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.LocaleFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@Named
@SessionScoped
public class HeaderView implements Serializable {

    @EJB
    private LocaleFacadeLocal localeFacade;

    @EJB
    private UsersFacadeLocal usersFacade;

    @Inject
    private SessionParams sessionParams;

    private Users authorizedUser;

    private List<Locale> allAvailableLocales;

    private Locale selectedLocale;


    @PostConstruct
    public void init(){
        authorizedUser = sessionParams.getAuthorizedUser();
        selectedLocale = sessionParams.getCurrentLocale();
        allAvailableLocales = localeFacade.findAll(null);
        allAvailableLocales.remove(selectedLocale);
    }

    public void onLanguageSelect(AjaxBehaviorEvent e){
        FacesContext.getCurrentInstance().getViewRoot().setLocale(selectedLocale);
        ResourceBundle.clearCache(Thread.currentThread().getContextClassLoader());
        sessionParams.setCurrentLocale(selectedLocale);

        ApplicationResourceBundle applicationBundle = ApplicationAssociate.getCurrentInstance().getResourceBundles().get("MESSAGE");
        try {
            Field field = applicationBundle.getClass().getDeclaredField("resources");
            field.setAccessible(true);
            Map<Locale, ResourceBundle> resources = (Map<Locale, ResourceBundle>) field.get(applicationBundle);
            resources.clear();
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }

        authorizedUser.setLocale(localeFacade.findLocale(selectedLocale));
        usersFacade.edit(authorizedUser);
        init();
    }
    public List<Locale> getAllAvailableLocales() {
        return allAvailableLocales;
    }

    public void setAllAvailableLocales(List<Locale> allAvailableLocales) {
        this.allAvailableLocales = allAvailableLocales;
    }

    public Locale getSelectedLocale() {
        return selectedLocale;
    }

    public void setSelectedLocale(Locale selectedLocale) {
        this.selectedLocale = selectedLocale;
    }
}
