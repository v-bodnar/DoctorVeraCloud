package ua.kiev.doctorvera.utils;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Utils {

    private static List<Character> abcCyr = Arrays.asList(' ','а','б','в','г','д','е','ё', 'ж','з','и','й','к','л','м','н','о','п','р','с','т','у','ф','х', 'ц','ч', 'ш','щ','ъ','ы','ь','э', 'ю','я','А','Б','В','Г','Д','Е','Ё', 'Ж','З','И','Й','К','Л','М','Н','О','П','Р','С','Т','У','Ф','Х', 'Ц', 'Ч','Ш', 'Щ','Ъ','Ы','Б','Э','Ю','Я','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
    private static String[] abcLat = {" ","a","b","v","g","d","e","e","zh","z","i","y","k","l","m","n","o","p","r","s","t","u","f","h","ts","ch","sh","sch", "","i", "","e","ju","ja","A","B","V","G","D","E","E","Zh","Z","I","Y","K","L","M","N","O","P","R","S","T","U","F","H","Ts","Ch","Sh","Sch", "","I", "","E","Ju","Ja","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    public static String encrypt(String x) {
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
        for (int i = 0; i < b.length; i++) {
            result +=
                    Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public static String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) return "";
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    //This method is used to set value to Users entity!!!
    public static String stripPhone(String phone) {
        if (phone == null || phone.isEmpty() || phone.equals("(___)___-__-__"))
            return "";
        phone = "+" + phone.replaceAll("\\D*", "");
        phone = phone.trim();
        return phone;
    }

    public static String generateUsername(String firstName, String lastName) {
        if(firstName.length() >= 5) {
            return transliterate(lastName.substring(0, 4)) + transliterate(firstName.substring(0, 1)) + new Random().nextInt(9);
        }else{
            return transliterate(lastName) + transliterate(firstName.substring(0, 1)) + new Random().nextInt(9);
        }
    }

    public static String transliterate(String message){
        if(abcCyr.contains(message.charAt(0))) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < message.length(); i++) {
                for (int x = 0; x < abcCyr.size(); x++)
                    if (message.charAt(i) == abcCyr.get(x)) {
                        builder.append(abcLat[x]);
                    }
            }
            return builder.toString();
        }else {
            return message;
        }
    }



}
