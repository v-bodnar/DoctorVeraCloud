package ua.kiev.doctorvera.resources;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

public class Message extends ListResourceBundle implements Serializable{

	private static Message instance;
	private static final String BUNDLE_NAME = "/message";
	private static final ResourceBundle resource = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault(), new ExtendedControl());;
	private static Object[][] entriesArray;
	
	public Message(){
		super();
			setList();
	}

	public enum Messages {
		APPLICATION_TITLE,
		APPLICATION_SAVED,
		LOGIN_TITLE,
		LOGIN_ERROR_TITLE,
		LOGIN_ERROR,
		LOGIN_REGISTER,
		LOGIN_WELCOME_TITLE,
		LOGIN_WELCOME,
		LOGIN_LOGOUT_BUTTON,
		LOGIN_LOGIN_BUTTON,
		LOGIN_GOODBY_TITLE,
		LOGIN_GOODBY,
		LOGIN_USER_PROFILE,
		PAYMENTS_ADD_TITLE,
		PAYMENTS_TYPE,
		PAYMENTS_TYPE_INCOMING,
		PAYMENTS_TYPE_OUTOMING,
		PAYMENTS_TOTAL,
		PAYMENTS_DATE_TIME,
		PAYMENTS_CASHIER,
		PAYMENTS_ADD_BUTTON,
		PAYMENTS_ADD_PRINT_BUTTON,
		PAYMENTS_CREATED,
		SHOW_USERS_TITLE,
		SHOW_USERS_ACTIONS,
		ADD_USER_FORM_LEGEND_DETAILS,
		ADD_USER_FORM_LEGEND_ADDRESS,
		ADD_USER_BUTTON_ADD,
		UPDATE_USER_BUTTON_UPDATE,
		UPDATE_USER_TITLE,
		PROFILE_UPLOAD_AVATAR_BUTTON,
		PROFILE_CROP_AVATAR_TITLE,
		PROFILE_CROP_BUTTON,
		PROFILE_RELOAD_BUTTON,
		PROFILE_CROP_AVATAR_ERROR_TITLE,
		PROFILE_CROP_AVATAR_ERROR_MESSAGE,
		PROFILE_CROP_AVATAR_SUCCESS_TITLE,
		PROFILE_CROP_AVATAR_SUCCESS_MESSAGE,
		REGISTRATION_TITLE,
		USERS_DELETE_CONFIRM_TITLE,
		USERS_DELETE_CONFIRM_MESSAGE,
		USERS_DELETE_CONFIRM_YES,
		USERS_DELETE_CONFIRM_NO,
		USERS_DELETED,
		USER_TYPES_TITLE,
		USER_TYPES_ADD_BUTTON,
		USER_TYPES_NAME,
		USER_TYPES_ADD_DIALOG_TITLE,
		USER_TYPES_EDIT_DIALOG_TITLE,
		USER_TYPES_DELETE_BUTTON,
		USER_TYPES_EDIT_BUTTON,
		USER_TYPES_SAVE_BUTTON,
		USER_TYPES_DELETE_CONFIRM_TITLE,
		USER_TYPES_DELETE_CONFIRM_MESSAGE,
		USER_TYPES_DELETE_CONFIRM_YES,
		USER_TYPES_DELETE_CONFIRM_NO,
		USER_TYPES_DELETED,
		USER_TYPES_SAVED,
		USER_TYPES_EDITED,
		USER_TYPES_ADD_USERS,
		USER_TYPES_PICKER_TITLE,
		USER_TYPES_PICKER_SOURCE,
		USER_TYPES_ADD_SUCCESS_START,
		USER_TYPES_ADD_SUCCESS_END,
		USER_TYPES_REMOVE_SUCCESS_START,
		USER_TYPES_REMOVE_SUCCESS_END,
		ROOMS_TITLE,
		ROOMS_ADD_BUTTON,
		ROOMS_NAME,
		ROOMS_ADD_DIALOG_TITLE,
		ROOMS_DELETE_BUTTON,
		ROOMS_DELETE_CONFIRM_TITLE,
		ROOMS_DELETE_CONFIRM_MESSAGE,
		ROOMS_DELETE_CONFIRM_YES,
		ROOMS_DELETE_CONFIRM_NO,
		ROOMS_DELETED,
		ROOMS_SAVED,
		ROOMS_EDIT_CANCELED,
		ROOMS_EDITED,
		METHODS_TITLE,
		METHODS_ADD_BUTTON,
		METHODS_NAME,
		METHODS_ADD_DIALOG_TITLE,
		METHODS_DELETE_BUTTON,
		METHODS_DELETE_CONFIRM_TITLE,
		METHODS_DELETE_CONFIRM_MESSAGE,
		METHODS_DELETE_CONFIRM_YES,
		METHODS_DELETE_CONFIRM_NO,
		METHODS_DELETED,
		METHODS_SAVED,
		METHODS_EDIT_CANCELED,
		METHODS_EDITED,
		METHODS_PRICE_CREATED,
		METHODS_PRICE_CHANGE_BUTTON,
		METHODS_PICKER_TITLE,
		METHODS_PICKER_SOURCE,
		METHODS_ADD_SUCCESS_START,
		METHODS_ADD_SUCCESS_END,
		METHODS_REMOVE_SUCCESS_START,
		METHODS_REMOVE_SUCCESS_END,
		PLAN_TITLE,
		PLAN_ADD_BUTTON,
		PLAN_NAME,
		PLAN_ADD_DIALOG_TITLE,
		PLAN_DELETE_BUTTON,
		PLAN_DELETE_CONFIRM_TITLE,
		PLAN_DELETE_CONFIRM_MESSAGE,
		PLAN_DELETE_CONFIRM_YES,
		PLAN_DELETE_CONFIRM_NO,
		PLAN_DELETED,
		PLAN_SAVED,
		PLAN_EDIT_CANCELED,
		PLAN_EDITED,
		PLAN_VALIDATE_DATE,
		PLAN_VALIDATE_SCHEDULE,
		PLAN_VALIDATE_SCHEDULE_DELETE,
		PLAN_VALIDATE_SCHEDULE_UPDATE,
		SCHEDULE_TITLE,
		SCHEDULE_ADD_BUTTON,
		SCHEDULE_ADD_DIALOG_TITLE,
		SCHEDULE_EDIT_DIALOG_TITLE,
		SCHEDULE_DELETE_BUTTON,
		SCHEDULE_EDIT_BUTTON,
		SCHEDULE_SAVE_BUTTON,
		SCHEDULE_DELETE_CONFIRM_TITLE,
		SCHEDULE_DELETE_CONFIRM_MESSAGE,
		SCHEDULE_DELETE_CONFIRM_YES,
		SCHEDULE_DELETE_CONFIRM_NO,
		SCHEDULE_DELETED,
		SCHEDULE_SAVED,
		SCHEDULE_EDITED,
		SCHEDULE_CHOOSE,
		SCHEDULE_VALIDATE_DATE,
		SCHEDULE_VALIDATE_SCHEDULE_DELETE,
		SCHEDULE_VALIDATE_SCHEDULE_UPDATE,
        SCHEDULE_TIME_START,
        SCHEDULE_TIME_END,
        SCHEDULE_BREAK_TIME,
        SCHEDULE_MINUTES,
        SCHEDULE_METHOD,
        SCHEDULE_METHOD_AVAILABLE,
        SCHEDULE_METHOD_SELECTED,
        SCHEDULE_METHOD_ADD_SUCCESS_START,
        SCHEDULE_METHOD_ADD_SUCCESS_END,
        SCHEDULE_METHOD_REMOVE_SUCCESS_START,
        SCHEDULE_METHOD_REMOVE_SUCCESS_END,
        SCHEDULE_VALIDATE_NOT_IN_PLAN,
        SCHEDULE_VALIDATE_NOT_IN_PLAN_START,
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
		MENU_ITEM_TIME,
		MENU_ITEM_MAIN,
		MENU_ITEM_USER_TYPES,
		MENU_ITEM_SEND_SMS,
		MENU_HEADER_MANAGER,
		MENU_HEADER_FINANCE,
		MENU_HEADER_SCHEDULE,
		MENU_ITEM_PLAN_GENERAL,
		MENU_ITEM_SCHEDULE_GENERAL,
		ENTITY_ID,
		ENTITY_DATE_CREATED,
		ENTITY_USER_CREATED,
		ENTITY_DESCRIPTION,
		USERS_LAST_NAME,
		USERS_FIRST_NAME,
		USERS_MIDDLE_NAME,
		USERS_LOGIN,
		USERS_PASSWORD,
		USERS_BIRTH_DATE,
		USERS_PHONE_NUMBER_HOME,
		USERS_PHONE_NUMBER_MOBILE,
		USERS_ADDRESS,
		USERS_USER_GROUPS,
		USERS_PLACEHOLDER_LAST_NAME,
		USERS_PLACEHOLDER_FIRST_NAME,
		USERS_PLACEHOLDER_MIDDLE_NAME,
		USERS_PLACEHOLDER_LOGIN,
		USERS_PLACEHOLDER_PASSWORD,
		USERS_PLACEHOLDER_BIRTH_DATE,
		USERS_PLACEHOLDER_PHONE_NUMBER_HOME,
		USERS_PLACEHOLDER_PHONE_NUMBER_MOBILE,
		USERS_ADD_USERS,
		USERS_PICKER_TITLE,
		USERS_PICKER_SOURCE,
		USERS_ADD_SUCCESS_START,
		USERS_ADD_SUCCESS_END,
		USERS_REMOVE_SUCCESS_START,
		USERS_REMOVE_SUCCESS_END,
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
		ADDRESS_PLACEHOLDER_INDEX,
		VALIDATOR_ERROR_TITLE,
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
		VALIDATOR_PHONE,
		VALIDATOR_LITERAL_ONLY,
		VALIDATOR_SUCCESS_TITLE,
		VALIDATOR_METHOD_DUPLICATED_SHORT_NAME,
		VALIDATOR_METHOD_TIMEINMINUTES,
		VALIDATOR_PRICE_ZERO,
		VALIDATOR_PRICE_AFTER_NOW,
		INDEX_APPOINTMENTS_COUNT,
		INDEX_DAY_OF_MONTH,
		INDEX_MONTH_OF_YEAR,
		INDEX_MONTH_SALARY_CUR,
		INDEX_YEAR_SALARY_CUR,
		INDEX_MONTH_STATISTICS,
		INDEX_YEAR_STATISTICS,
		INDEX_TOTAL_APPOINTMENTS,
		INDEX_AVERAGE_APPOINTMENTS_PER_DAY,
		INDEX_AVERAGE_APPOINTMENTS_PER_MONTH,
		INDEX_BEST_DAY,
		INDEX_WORST_DAY,
		INDEX_BEST_MONTH,
		INDEX_WORST_MONTH,
		INDEX_TOTAL_MONTH_WORK_DAYS,
		INDEX_TOTAL_YEAR_WORK_DAYS,
		INDEX_MONTH_SALARY,
		INDEX_AVERAGE_DAY_SALARY,
		INDEX_AVERAGE_MONTH_SALARY,
		INDEX_YEAR_SALARY,
		CALENDAR_MONTH_1,
		CALENDAR_MONTH_2,
		CALENDAR_MONTH_3,
		CALENDAR_MONTH_4,
		CALENDAR_MONTH_5,
		CALENDAR_MONTH_6,
		CALENDAR_MONTH_7,
		CALENDAR_MONTH_8,
		CALENDAR_MONTH_9,
		CALENDAR_MONTH_10,
		CALENDAR_MONTH_11,
		CALENDAR_MONTH_12,
		SCHEDULE_TAB_PERSONAL,
		SCHEDULE_TAB_APPOINTMENT,
		SCHEDULE_TAB_DOCUMENTS,
		SCHEDULE_PRINT_FORM,
		SCHEDULE_PRINT_SECURE_AGREEMENT,
		SCHEDULE_PRINT_INCOME_FORM,
		APPLICATION_NOT_IMPLEMENTED,
		APPLICATION_SUCCESSFUL_TITLE,
		APPLICATION_ERROR_TITLE,
		MENU_ITEM_PERSONAL_SCHEDULE;


		@Override
		public String toString() {
			return Message.getString(this);
		}
	}

	public static Message getInstance() {
		if (instance == null) instance = new Message();
		return instance;
	}

	@SuppressWarnings("rawtypes")
	public static String getString(Enum key) {
		return (String) resource.getObject(key.name());
	}

	public static String getMessage(String key) {
		return (String) resource.getObject(key);
	}

	public ResourceBundle getResource() {
		return resource;
	}

	public Object[][] getContents() {
		return entriesArray;
	}
	
	private void setList(){
		Set<String> keys = resource.keySet();
		entriesArray = new Object[keys.size()][2];
		int i = 0;
		for(String key : keys){
		    entriesArray[i][0] = key;
		    entriesArray[i][1] = resource.getString(key);
			i++;
		}
	}

	public static void showMessage(Messages title, Messages message){
		final String successTitle = Message.getInstance().getString(title.name());
		final String successMessage = Message.getInstance().getString(message.name());
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage));
	}
	public static void showMessage(Messages title, String message){
		final String successTitle = Message.getInstance().getString(title.name());
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, message));
	}
	public static void showMessage(String title, Messages message){
		final String successMessage = Message.getInstance().getString(message.name());
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, title, successMessage));
	}
	public static void showMessage(String title, String message){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, title, message));
	}
	public static void showError(Messages title, Messages message){
		final String successTitle = Message.getInstance().getString(title.name());
		final String successMessage = Message.getInstance().getString(message.name());
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, successTitle, successMessage));
	}
	public static void showError(Messages title, String message){
		final String successTitle = Message.getInstance().getString(title.name());
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, successTitle, message));
	}
	public static void showError(String title, Messages message){
		final String successMessage = Message.getInstance().getString(message.name());
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, title, successMessage));
	}
	public static void showError(String title, String message){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, title, message));
	}
}
