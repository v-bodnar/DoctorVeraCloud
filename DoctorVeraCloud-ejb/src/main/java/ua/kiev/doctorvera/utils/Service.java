package ua.kiev.doctorvera.utils;

import java.security.NoSuchAlgorithmException;

public class Service {
	
	  public static String encrypt(String x){
		    java.security.MessageDigest d = null;
		    try {
				d = java.security.MessageDigest.getInstance("SHA-1");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    d.reset();
		    d.update(x.getBytes());
		    
		    return byteArrayToHexString(d.digest());
		  }
	  
	  private static String byteArrayToHexString(byte[] b) {
		  String result = "";
		  for (int i=0; i < b.length; i++) {
		    result +=
		          Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		  }
		  return result;
		}
	  
	  public static String firstUpperCase(String word){
			if(word == null || word.isEmpty()) return "";
			return word.substring(0, 1).toUpperCase() + word.substring(1);
		}
	  
	  public static String stripPhone(String phone){
			if(phone == null || phone.isEmpty() || phone.equals("(___)___-__-__")) 
				return "";
			phone = "+38" + phone.replaceAll("\\D*", "");
			return phone;
	 }
}
