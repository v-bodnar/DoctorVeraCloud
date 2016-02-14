package ua.kiev.doctorvera.facadeLocal;

import ua.kiev.doctorvera.entities.Locale;

import java.util.List;

/**
 * Interface for declaring main operations with Locale entity
 * Here will be declared operations specific to current entity
 * @author Volodymyr Bodnar
 * @date 23.01.2015.
 */
public interface LocaleFacadeLocal extends CRUDFacade<Locale> {
    /**
     * Converts java.util.Locale to entity for persistent storage
     * @param locale locale that has to be converted
     * @return entity that represents locale
     */
    Locale findLocale(java.util.Locale locale);

    /**
     * Searches for all available locales in the system
     * @param nullObject need id to separate from method that returns entity
     * @return java.util.Locale that can be used by standard localization mechanism
     */
    List<java.util.Locale> findAll(Object nullObject);

    /**
     * Searches locale by given country and language code ex. "ru_RU"
     * @param codes code to search for ex. "ru_RU"
     * @return java.util.Locale that can be used by standard localization mechanism
     */
    java.util.Locale findByCode(String codes);
}
