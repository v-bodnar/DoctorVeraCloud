package ua.kiev.doctorvera.validators;

import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.resources.Message;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named(value = "validator")
@SessionScoped
public class Validator implements Serializable {
	
	
	@EJB
	private UsersFacadeLocal usersFacade;
	
	private static final String startLine = "";
	private static final String endLine = "\n";
	
	public static Boolean isNull(String string) {
		return (string == null || string.trim().length() == 0);
	}
	
	public static Boolean containsCyrillic(String string) {
		final Pattern WORD = Pattern.compile("[А-Яа-яїёіъьЬЪЁЇІ]",Pattern.CASE_INSENSITIVE);
		Matcher wordM = WORD.matcher(string);
		return wordM.find();
	}
	
	public static Boolean isCyrillic(String string) {
		return Pattern.matches("[А-Яа-яїёіъьЬЪЁЇІ]*", string);
	}
	
	public static Boolean containsLatin(String string) {
		final Pattern WORD = Pattern.compile("[A-Za-z]",Pattern.CASE_INSENSITIVE);
		Matcher wordM = WORD.matcher(string);
		return wordM.find();
	}
	
	public static Boolean isLatin(String string) {
		return Pattern.matches("[A-Za-z]*", string);
	}
	
	public static Boolean containsNumeric(String string) {
		final Pattern NUMBERS = Pattern.compile("\\d");
		Matcher wordM = NUMBERS.matcher(string);
		return wordM.find();
	}
	
	public static Boolean isNumeric(String string) {
		return Pattern.matches("\\d*", string);
	}
	
	public static Boolean containsPunct(String string) {
		final Pattern PUNCT = Pattern.compile("[\\.\\?\\,\\:\\;\\\\\"\\|\\/\\<\\>\\-\\_\\=\\'\\~\\`\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\{\\}]",Pattern.CASE_INSENSITIVE);
		Matcher wordM = PUNCT.matcher(string);
		return wordM.find();
	}
	
	public static Boolean isPunct(String string) {
		return Pattern.matches("[\\.\\?\\,\\:\\;\\\\\"\\|\\/\\<\\>\\-\\_\\=\\'\\~\\`\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\{\\}]*", string);
	}
	
	public static String checkNameCyr(String name) {
		String note = "";
		if (isNull(name))
			note += startLine + Message.getMessage("VALIDATOR_REQUIRED") + endLine;
		else if (!isCyrillic(name))
			note += startLine + Message.getMessage("VALIDATOR_CYRILLIC_ONLY") + endLine;
		
		return note;
	}
	
	public static String checkNameLat(String name) {
		String note = "";
		if (isNull(name))
			note += startLine + Message.getMessage("VALIDATOR_REQUIRED") + endLine;
		else if (!isCyrillic(name))
			note += startLine + Message.getMessage("VALIDATOR_CYRILLIC_ONLY") + endLine;
		
		return note;
	}
	
	public String checkUsername(String login, String incomingUserId) {
		String note = "";
		Users user = usersFacade.findByUsername(login);

		if (isNull(login))
			note += startLine + Message.getMessage("VALIDATOR_REQUIRED") + endLine;
		else{
			if (user != null && !(user.getId().toString()).equals(incomingUserId))
				note += startLine + Message.getMessage("VALIDATOR_LOGIN_IN_USE") + endLine;
			if (containsCyrillic(login))
				note += startLine + Message.getMessage("VALIDATOR_NOT_CYRILLIC") + endLine;
			if (containsPunct(login))
				note += startLine + Message.getMessage("VALIDATOR_NOT_PUNCT") + endLine;
		}
		return note;
	}
	
	public static String checkPassword(String password) {
		String note = "";
		if (isNull(password))
			note += startLine + Message.getMessage("VALIDATOR_REQUIRED") + endLine;
		else{
			if (password.length() < 6)
				note += startLine + Message.getMessage("VALIDATOR_PASSWORD_LESS_SIX") + endLine;
			if (!containsNumeric(password))
				note += startLine + Message.getMessage("VALIDATOR_NUMBERS") + endLine;
			if (!containsLatin(password))
				note += startLine + Message.getMessage("VALIDATOR_LATIN") + endLine;
			if (containsCyrillic(password))
				note += startLine + Message.getMessage("VALIDATOR_NOT_CYRILLIC") + endLine;
		}
		return note;
	}
	
	public static String checkEmail(String email) {
		String note = "";
		final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
				Pattern.CASE_INSENSITIVE);
		Matcher emailM = VALID_EMAIL_ADDRESS_REGEX.matcher(email);

		if (isNull(email))
			note += startLine + Message.getMessage("VALIDATOR_REQUIRED") + endLine;
		else if (!emailM.find())
			note += startLine + Message.getMessage("VALIDATOR_EMAIL") + endLine;
		return note;
	}
	
	
	public static String checkUkrainianPhone(String phone) {
		String note = "";
		if(!isNull(phone) || !phone.equals("+38")){
			if (phone.startsWith("+38") && phone.length()!=13)
				note += startLine + Message.getMessage("VALIDATOR_PHONE") + endLine;
			return note;
		} else
			return startLine + Message.getMessage("VALIDATOR_REQUIRED") + endLine;
	}

	public static String checkInternationalPhone(String phone) {
		String note = "";
		if(!isNull(phone)){
			final Pattern VALID_PHONE_REGEX = Pattern.compile("\\+[0-9]{11,15}", Pattern.CASE_INSENSITIVE);

			if (!VALID_PHONE_REGEX.matcher(phone).find())
				note += startLine + Message.getMessage("VALIDATOR_PHONE") + endLine;
			return note;
		} else
			return startLine + Message.getMessage("VALIDATOR_REQUIRED") + endLine;
	}
	
	public static String checkCyrText(String text) {
		String note = "";
		if (isNull(text))
			note += startLine + Message.getMessage("VALIDATOR_REQUIRED") + endLine;
		else if (!isCyrillic(text))
				note += startLine + Message.getMessage("VALIDATOR_CYRILLIC_ONLY") + endLine;
		
		return note;
	}
	
	public static String checkNull(String text) {
		String note = "";
		if (isNull(text))
			note += startLine + Message.getMessage("VALIDATOR_REQUIRED") + endLine;
		return note;
	}
	
	public static String checkNumeric(String text) {
		String note = "";
		if (isNull(text))
			note += startLine + Message.getMessage("VALIDATOR_REQUIRED") + endLine;
		else 
			if(!isNumeric(text)) 
				note += startLine + Message.getMessage("VALIDATOR_NUMBERS_ONLY") + endLine;
		
		return note;
	}
	
	public static String checkLiteral(String value) {
		String note = "";
		if (isNull(value))
			note += startLine + Message.getMessage("VALIDATOR_REQUIRED") + endLine;
		else if (containsNumeric(value))
			note += startLine + Message.getMessage("VALIDATOR_LITERAL_ONLY") + endLine;
		
		return note;
	}
	
	public static String checkLiteralOrNull(String value) {
		String note = "";
		if (isNull(value) || value.equals(""))
			return "";
		else if (containsNumeric(value))
			note += startLine + Message.getMessage("VALIDATOR_LITERAL_ONLY") + endLine;
		return note;
	}
	
	public static String checkNumericOrNull(String value) {
		String note = "";
		if (isNull(value) || value.equals(""))
			return "";
		else if (!isNumeric(value))
			note += startLine + Message.getMessage("VALIDATOR_NUMBERS_ONLY") + endLine;
		return note;
	}
	
}
