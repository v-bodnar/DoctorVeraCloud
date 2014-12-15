/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.resourses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {

	private static Config instance;
	private static final String path = "config.xml";
	private static Properties properties;
	private static InputStream url;
	private static File file;
	@SuppressWarnings("rawtypes")
	private Class clazz;
	private ClassLoader cl;

	private Config() {
		properties = new Properties();

		try {
			clazz = this.getClass();
			cl = clazz.getClassLoader();
			url = cl.getResourceAsStream(path);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		// file = new File();

	}

	public static enum Key {
		DATASOURCE_JNDI, COUNTRY, LANGUAGE, PROJECT_NAME
	}

	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
			createProperties();

			// Setting Default Locale
			Locale.setDefault(new Locale(properties.getProperty(Key.LANGUAGE.toString()), properties.getProperty(Key.COUNTRY.toString())));
		}
		return instance;
	}

	private static void createProperties() {
		try {
			properties.loadFromXML(url);
		} catch (IOException ex) {
			Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
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
