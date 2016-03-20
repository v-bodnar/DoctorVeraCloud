package ua.kiev.doctorvera.resources;

import org.primefaces.context.RequestContext;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.*;

/**
 * Class implements localized resource bundle for messages in the system
 */
public class Message extends LocalizedResource {
	private static final String BUNDLE_NAME = "message";

	public Message(){
		super();
	}

	public Message(Locale locale){
		super(locale);
	}

	@Override
	String getBundleName() {
		return BUNDLE_NAME;
	}

	/**
	 * Method for retrieving messages simply
	 * @param key to search for
	 * @return message correspondent to given key
     */
	public static String getMessage(String key) {
		return  getBundle(BUNDLE_NAME, FacesContext.getCurrentInstance().getViewRoot().getLocale()).getString(key);
	}

	/**
	 * Static utility method to help showing JSF messages in UI
	 * @param title message title
	 * @param message message text
	 */
	public static void showMessage(String title, String message){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, title, message));
	}

	/**
	 * Static utility method to help showing JSF messages in UI
	 * @param title message title
	 * @param message message text
	 */
	public static void showError(String title, String message){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, title, message));
	}

	public static void showErrorInDialog(String message) {
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, Message.getMessage("APPLICATION_ERROR_TITLE"), message);
		RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
	}
}
