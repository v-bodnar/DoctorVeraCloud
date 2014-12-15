/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.web.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

public class Config {

    private static Config instance;
        private static final String path = "/config.xml";
	private static Properties properties;
	private static URL url;
        private static File file;

        
	private Config(){
		properties = new Properties();
                url = this.getClass().getResource(path);
                file = new File(url.getPath());
                
	}
	
	public static enum Key {
		DATASOURCE_JNDI, COUNTRY, LANGUAGE, PROJECT_NAME
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
			e. printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getProperty(Key key) {
		return properties.getProperty(key.toString());
	}

	public Boolean setProperty(String key, String value) {
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
