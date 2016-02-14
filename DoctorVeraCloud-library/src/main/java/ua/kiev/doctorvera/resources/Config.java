/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.resources;

import java.util.ResourceBundle;

public class Config extends LocalizedResource {
    private static final String BUNDLE_NAME = "config";

    // We do not have locale for config constants so we have only one instance of
    // ResourceBundle and can use it for all locales
    private static Config instance;
    public Config(){
        super();
        instance = this;
    }

    @Override
    String getBundleName() {
        return BUNDLE_NAME;
    }

    public static ResourceBundle getInstance() {
        if (instance == null) return new Config();
        else
            return instance;
    }

    public static String getMessage(String key) {
        if(getInstance() == null) return "";
        return getInstance().getString(key);
    }
}
