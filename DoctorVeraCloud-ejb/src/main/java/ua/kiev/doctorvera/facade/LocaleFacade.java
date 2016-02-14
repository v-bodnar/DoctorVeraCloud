package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.Locale;
import ua.kiev.doctorvera.facadeLocal.LocaleFacadeLocal;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for implementing main operations with Locale entity
 *
 * @author Volodymyr Bodnar
 * @date 23.01.2016
 */
@Stateless
public class LocaleFacade extends AbstractFacade<Locale> implements LocaleFacadeLocal {
    public LocaleFacade() {
        super(Locale.class);
    }

    /**
     * Converts java.util.Locale to entity for persistent storage
     * @param locale locale that has to be converted
     * @return entity that represents locale
     */
    @Override
    public Locale findLocale(java.util.Locale locale) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Locale> cq = cb.createQuery(Locale.class);

        Root root = cq.from(Locale.class);
        cq.distinct(true);

        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        Predicate languagePredicate = cb.and(cb.equal( root.get("languageCode"), locale.getLanguage()));
        //Predicate countryPredicate = cb.and(cb.equal( root.get("countryCode"), locale.getCountry()));
        cq.select(root).where(deletedPredicate, languagePredicate);

        return getEntityManager().createQuery(cq).getSingleResult();
    }

    /**
     * Searches for all available locales in the system
     * @param nullObject need id to separate from method that returns entity
     * @return java.util.Locale that can be used by standard localization mechanism
     */
    @Override
    public List<java.util.Locale> findAll(Object nullObject) {
        List<java.util.Locale> locales = new LinkedList<>();
        for(Locale locale : findAll()){
            locales.add(new java.util.Locale(locale.getLanguageCode(), locale.getCountryCode()));
        }
        return  locales;
    }

    /**
     * Searches locale by given country and language code ex. "ru_RU"
     * @param codes code to search for ex. "ru_RU"
     * @return java.util.Locale that can be used by standard localization mechanism
     */
    @Override
    public java.util.Locale findByCode(String codes) {
        String[] codesArray = codes.split("_");
        if(codesArray.length == 1){
            return  new java.util.Locale(codesArray[0]);
        }else if(codesArray.length == 2){
            return  new java.util.Locale(codesArray[0], codesArray[1]);
        } else
            return null;

    }
}
