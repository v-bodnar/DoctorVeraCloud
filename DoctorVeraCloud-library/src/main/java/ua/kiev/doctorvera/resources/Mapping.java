package ua.kiev.doctorvera.resources;

import java.util.ResourceBundle;



public class Mapping extends LocalizedResource {

	private static final String BUNDLE_NAME = "mapping";

	// We do not have locale for mapping strings so we have only one instance of
	// ResourceBundle and can use it for all locales
	private static Mapping instance;
	public Mapping(){
		super();
		instance = this;
	}

	@Override
	String getBundleName() {
		return BUNDLE_NAME;
	}

	public static ResourceBundle getInstance() {
		if (instance == null) return new Mapping();
		else
			return instance;
	}

	/**
	 * Method for retrieving paths
	 * @param key to search for
	 * @return message correspondent to given key
	 */
	public static String getMessage(String key) {
		if(getInstance() == null) return "";
		return getInstance().getString(key);
	}
}
