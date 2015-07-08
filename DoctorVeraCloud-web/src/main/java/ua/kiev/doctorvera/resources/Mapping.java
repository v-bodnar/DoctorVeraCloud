package ua.kiev.doctorvera.resources;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

public class Mapping extends ListResourceBundle{
	private static Mapping instance;
	private ResourceBundle resource;
	private static final String BUNDLE_NAME = "/mapping";
	private static Object[][] entriesArray;
	
	public Mapping(){
		super();
		resource = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault(), new ExtendedControl());
		setList();
	}
/*
    public static enum Page { 
    	MAIN_PAGE, 
    	LOGIN_PAGE,
    	SHOW_USERS_PAGE,
    	ADD_USER_PAGE,
    	EDIT_USER_PAGE,
    	SHOW_USER_TYPES_PAGE,
    	USER_PROFILE_PAGE,
    	USER_USERS_PAGE,
    	AVATAR_CROP_DIALOG,
    	REGISTER_PAGE,
    	PLAN_PAGE,
    	USERS_PAGE,
    	ROOMS_PAGE,
    	SEND_SMS_PAGE,
    	USER_TYPES_PAGE,
    	USER_ADD_PAGE,
    	METHODS_PAGE,
    	PAYMENTS_PAGE,
    	SCHEDULE_PAGE,
    	SCHEDULE_GENERAL_PAGE,
    	PLAN_GENERAL_PAGE
    }
    
    public static enum Path { 
    	APPLICATION_IMAGES_PATH,
    	APPLICATION_AVATAR_IMAGES_PATH,
    	APPLICATION_ROOT_PATH,
    	APPLICATION_AVATAR_DEFAULT
    }

    public static enum UserTypes{
    	DOCTORS_TYPE_ID,
    	PATIENTS_TYPE_ID,
    	ASSISTENTS_TYPE_ID
    }
    
    public static enum Methods{
    	METHOD_BREAK_ID
    }

    public static enum Users{
    	USERS_BREAK_ID
    }
      */
	public static Mapping getInstance() {
		if (instance == null) {
			instance = new Mapping();
		}
		return instance;
	}
/*
	@SuppressWarnings("rawtypes")
	public String getProperty(Enum key) {
		return (String) resource.getObject(key.toString());
	}
	
	public String getProperty(String key) {
		return (String) resource.getObject(key);
	}
	*/
	public ResourceBundle getResource() {
		return resource;
	}

	public Object[][] getContents() {
		return entriesArray;
	}
	
	private void setList(){
		Set<String> keys = getResource().keySet();
		entriesArray = new Object[keys.size()][2];
		int i = 0;
		for(String key : keys){
		    entriesArray[i][0] = key;
		    entriesArray[i][1] = getString(key);
		    i++;
		}
	}
}
