package ua.kiev.doctorvera.facadeLocal;

import ua.kiev.doctorvera.entities.Locale;
import ua.kiev.doctorvera.entities.MessageBundle;

import java.util.List;
import java.util.Map;

/**
 * Interface for declaring main operations with MessageBundle entity
 * Here will be declared operations specific to current entity
 * @author Volodymyr Bodnar
 * @date 23.01.2015.
 */
public interface MessageBundleFacadeLocal extends CRUDFacade<MessageBundle> {

    /**
     * This method retrieves map with string messages for given locale from persistent storage
     * @param locale given locale
     * @return map with string messages for given locale
     */
    Map<String, String> findAllMessages(Locale locale);

    /**
     * This method returns map with string constants from persistent storage
     * @return map with string constants
     */
    Map<String, String> findAllConfigEntries();

    /**
     * This method returns map with string constants from persistent storage
     * @return map with string constants
     */
    Map<String, String> findAllMappingEntries();

    /**
     * This method retrieves entity of MessageBundle given locale and key
     * that contains string value from persistent storage
     * @param key key to search for
     * @param locale with the given locale
     * @return
     */
    MessageBundle findMessageByLocaleAndKey(String key, Locale locale);

    /**
     * This method retrieves list of MessageBundle entities by given locale
     * that contains keys and corresponding string values
     * @param locale with the given locale
     * @return
     */
    List<MessageBundle> findAllMessagesByLocale(Locale locale);

    /**
     * This method retrieves list of MessageBundle entities by given type
     * that contains keys and corresponding string values
     * @param type to search for
     * @return
     */
    List<MessageBundle> findAllMessagesByType(MessageBundle.Type type);
}
