package ua.kiev.doctorvera.resources;

import ua.kiev.doctorvera.facadeLocal.LocaleFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.MessageBundleFacadeLocal;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This class controls loading resources from DataBase basing on their type(baseName) and if needed locale
 */
public class DBControl extends ResourceBundle.Control {

    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException,
            IOException {

        if(baseName.equals("message")){
            return  new ResourceService(locale, baseName);
        }else if(baseName.equals("mapping")){
            return  new ResourceService(baseName);
        }else if(baseName.equals("config")){
            return  new ResourceService(baseName);
        }else if(baseName.equals("security_config")){
            return  new ResourceService(locale, baseName);
        }else
            return null;
    }

    /**
     * ResourceBundle filled with string values from data base
     */
    public class ResourceService extends ListResourceBundle implements Serializable {
        private Locale locale;
        private String bundleName;

        public ResourceService(Locale locale, String bundleName) {
            this.locale = locale;
            this.bundleName = bundleName;
        }

        public ResourceService(String bundleName) {
            this.bundleName = bundleName;
        }

        @Override
        protected Object[][] getContents() {

            Context ctx;
            LocaleFacadeLocal localeFacade = null;
            MessageBundleFacadeLocal messageBundleFacade = null;
            try {
                ctx = new InitialContext();
                localeFacade = (LocaleFacadeLocal) ctx.lookup("java:global/DoctorVeraCloud-ear-0.0.1-SNAPSHOT/DoctorVeraCloud-ejb-0.0.1-SNAPSHOT/LocaleFacade");
                messageBundleFacade = (MessageBundleFacadeLocal) ctx.lookup("java:global/DoctorVeraCloud-ear-0.0.1-SNAPSHOT/DoctorVeraCloud-ejb-0.0.1-SNAPSHOT/MessageBundleFacade");
            } catch (NamingException e) {
                e.printStackTrace();
            }

            if(locale == null || locale.getLanguage().isEmpty()){
                locale = new Locale("ru","Ru"); //Default locale
            }

            Map<String,String> resources;
            if (bundleName == null) bundleName = getBaseBundleName();
            if(bundleName.equals("message")){
                resources =  messageBundleFacade.findAllMessages(localeFacade.findLocale(locale));
            }else if(bundleName.equals("mapping")){
                resources =  messageBundleFacade.findAllMappingEntries();
            }else if(bundleName.equals("config")){
                resources =  messageBundleFacade.findAllConfigEntries();
            }else
                return null;

            Object[][] messages = new Object[resources.size()][2];
            int i = 0;
            for(String key : resources.keySet()){
                messages[i] = new Object[]{key, resources.get(key)};
                i++;
            }

            return messages;
        }
    }
}