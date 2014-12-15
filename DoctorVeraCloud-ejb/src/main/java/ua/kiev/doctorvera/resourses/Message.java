package ua.kiev.doctorvera.resourses;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

public class Message extends ListResourceBundle{

	private static Message instance;
	private ResourceBundle resource;
	private static final String BUNDLE_NAME = "ua.kiev.doctorvera.resourses.message";
	private static Object[][] entriesArray;
	
	public Message(){
		super();
		try{
		resource = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault(), new ExtendedControl());
		setList();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static enum Messages {
		SERVLET_EXCEPTION, 
		IO_EXCEPTION, 
		COMMAND_MISSING,
		HEADER_WELCOME,
		APPLICATION_TITLE,
		LOGOUT,
		ERROR_BACK_TO_MAIN
		
	}
	public static enum Login {
		LOGIN_TITLE,
		LOGIN_ERROR
	}
	
	public static enum ShowUsers{
		SHOW_USERS_TITLE,
		SHOW_USERS_ACTIONS
	}
	public static enum AddUser{
		ADD_USER_FORM_LEGEND_DETAILS,
		ADD_USER_FORM_LEGEND_ADDRESS,
		ADD_USER_BUTTON_ADD
	}
	public static enum UpdateUsers{
		UPDATE_USER_BUTTON_UPDATE,
		UPDATE_USER_TITLE
	}
	
	public static enum Menu{
		MENU_HEADER,
		ADMIN_BLOCK_HEADER,
		MANAGER_BLOCK_HEADER,
		FINANCIAL_BLOCK_HEADER,
		STATISTICS_BLOCK_HEADER,
		MENU_ITEM_USERS,
		MENU_ITEM_ADD_USER,
		MENU_ITEM_USER_GROUPS,
		MENU_ITEM_ACCESS_RIGHTS,
		MENU_ITEM_SETTINGS,
		MENU_ITEM_PLAN,
		MENU_ITEM_ADD_PLAN,
		MENU_ITEM_SCHEDULE,
		MENU_ITEM_ADD_SCHEDULE,
		MENU_ITEM_METHODS,
		MENU_ITEM_METHOD_TYPES,
		MENU_ITEM_ROOMS,
		MENU_ITEM_CASH,
		MENU_ITEM_PAYMENTS,
		MENU_ITEM_CREATE_PAYMENT,
		MENU_ITEM_SALARY,
		MENU_ITEM_APPOINTMENTS,
		MENU_ITEM_FINANCE,
		MENU_ITEM_TIME
	}

	public static enum Entity{
		ENTITY_ID,
		ENTITY_DATE_CREATED,
		ENTITY_USER_CREATED,
		ENTITY_DESCRIPTION
	}

	public static enum Users{
		USERS_LAST_NAME,
		USERS_FIRST_NAME,
		USERS_MIDDLE_NAME,
		USERS_LOGIN,
		USERS_PASSWORD,
		USERS_BIRTH_DATE,
		USERS_PHONE_NUMBER_HOME,
		USERS_PHONE_NUMBER_MOBILE,
		USERS_ADDRESS,
		USERS_USER_TYPE,
		USERS_PLACEHOLDER_LAST_NAME,
		USERS_PLACEHOLDER_FIRST_NAME,
		USERS_PLACEHOLDER_MIDDLE_NAME,
		USERS_PLACEHOLDER_LOGIN,
		USERS_PLACEHOLDER_PASSWORD,
		USERS_PLACEHOLDER_BIRTH_DATE,
		USERS_PLACEHOLDER_PHONE_NUMBER_HOME,
		USERS_PLACEHOLDER_PHONE_NUMBER_MOBILE,
	}
	
	public static enum Address{
		ADDRESS_COUNTRY,
		ADDRESS_REGION,
		ADDRESS_CITY,
		ADDRESS_ADDRESS,
		ADDRESS_STREET,
		ADDRESS_INDEX,
		ADDRESS_PLACEHOLDER_COUNTRY,
		ADDRESS_PLACEHOLDER_REGION,
		ADDRESS_PLACEHOLDER_CITY,
		ADDRESS_PLACEHOLDER_ADDRESS,
		ADDRESS_PLACEHOLDER_STREET,
		ADDRESS_PLACEHOLDER_INDEX
		
	}
	public static enum Validator{
		VALIDATOR_REQUIRED,
		VALIDATOR_CYRILLIC_ONLY,
		VALIDATOR_NOT_CYRILLIC,
		VALIDATOR_LATIN,
		VALIDATOR_LATIN_ONLY,
		VALIDATOR_NOT_LATIN,
		VALIDATOR_NUMBERS,
		VALIDATOR_NUMBERS_ONLY,
		VALIDATOR_NOT_NUMBERS,
		VALIDATOR_PUNCT,
		VALIDATOR_NOT_PUNCT,
		VALIDATOR_PASSWORD_LESS_SIX,
		VALIDATOR_LOGIN_IN_USE,
		VALIDATOR_EMAIL,
		VALIDATOR_PHONE
	}
	
	public static Message getInstance() {
		if (instance == null) instance = new Message();
		return instance;
	}
	
		
	@SuppressWarnings("rawtypes")
	public String getMessage(Enum key) {
		return (String) resource.getObject(key.toString());
	}

	public String getMessage(String key) {
		return (String) resource.getObject(key);
	}
	
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
		    entriesArray[i][1] = getMessage(key);
		    i++;
		}
	}
	
	
}
