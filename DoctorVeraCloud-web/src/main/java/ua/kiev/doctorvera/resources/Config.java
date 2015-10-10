/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.resources;

import java.io.*;
import java.net.URL;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

public class Config extends ListResourceBundle implements Serializable {

    private static Config instance;
    private static final String path = "/config.xml";
    private static Properties properties = new Properties();
    private static URL url;
    private static File file;
    Object[][] entriesArray;

    public Config() {
        super();
        url = this.getClass().getResource(path);
        file = new File(url.getPath());
        setList();
    }

    @Override
    public Object[][] getContents() {
        return entriesArray;
    }

    private void setList() {
        Set<String> keys = properties.stringPropertyNames();
        entriesArray = new Object[keys.size()][2];
        int i = 0;
        for (String key : keys) {
            entriesArray[i][0] = key;
            entriesArray[i][1] = properties.getProperty(key);
            i++;
        }
    }

    public static enum Key {
        DATASOURCE_JNDI,
        COUNTRY,
        LANGUAGE,
        PROJECT_NAME,
        SUPER_ADMIN_ID,
        SUPER_ADMIN_USER_TYPE_ID
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
            createProperties();

            //Setting Default Locale
            Locale.setDefault(new Locale(properties.getProperty(Key.LANGUAGE.toString()),
                    properties.getProperty(Key.COUNTRY.toString())));
        }
        return instance;
    }

    private static void createProperties() {

        try (FileInputStream fileInput = new FileInputStream(file)) {
            properties.loadFromXML(fileInput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(Key key) {
        return properties.getProperty(key.toString());
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static Boolean setProperty(String key, String value) {
        try {
            FileOutputStream fileOutput = new FileOutputStream(file);
            properties.setProperty(key, value);
            properties.storeToXML(fileOutput, "Configuration file", "UTF-8");
            fileOutput.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
