/**
 * DoctorVera Copyright (c) ${year} by DoctorVera, Inc. All Rights Reserved.
 * Proprietary and Confidential - This source code is not for redistribution
 */
package ua.kiev.doctorvera.utils;

import java.util.Random;

/**
 * Provides helper methods to generate random char array using given parameters 
 *
 * @author Volodymyr Bodnar
 */
public class RandomPasswordGenerator {

    private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUM = "0123456789";
    private static final String SPL_CHARS = "!@#$%^&*_=+-/";

    /**
     * Method generates random password using specified paremeters
     * @param minLen Minimum length of generated password
     * @param maxLen Maximum length of generated password
     * @param noOfCAPSAlpha Number of Upper Case symbols in the generated password
     * @param noOfDigits Number of Digits in the generated password
     * @param noOfSplChars Number of Special characters in the generated password
     */
    public static char[] generatePswd(int minLen, int maxLen, int noOfCAPSAlpha,
            int noOfDigits, int noOfSplChars) {
        //1.	Checks if  minLen > maxLen
        if (minLen > maxLen) {
            throw new IllegalArgumentException("Min. Length > Max. Length!");
        }
        //2.	Checks that number of symbols that must occur in the password is not more than password length
        if ((noOfCAPSAlpha + noOfDigits + noOfSplChars) > minLen) {
            throw new IllegalArgumentException("Min. Length should be atleast sum of (CAPS, DIGITS, SPL CHARS) Length!");
        }
        
        Random rnd = new Random();
        int len = rnd.nextInt(maxLen - minLen + 1) + minLen;
        char[] pswd = new char[len];
        int index = 0;
        for (int i = 0; i < noOfCAPSAlpha; i++) {
            index = getNextIndex(rnd, len, pswd);
            pswd[index] = ALPHA_CAPS.charAt(rnd.nextInt(ALPHA_CAPS.length()));
        }
        for (int i = 0; i < noOfDigits; i++) {
            index = getNextIndex(rnd, len, pswd);
            pswd[index] = NUM.charAt(rnd.nextInt(NUM.length()));
        }
        for (int i = 0; i < noOfSplChars; i++) {
            index = getNextIndex(rnd, len, pswd);
            pswd[index] = SPL_CHARS.charAt(rnd.nextInt(SPL_CHARS.length()));
        }
        for (int i = 0; i < len; i++) {
            if (pswd[i] == 0) {
                pswd[i] = ALPHA.charAt(rnd.nextInt(ALPHA.length()));
            }
        }
        return pswd;
    }

    private static int getNextIndex(Random rnd, int len, char[] pswd) {
        int index = rnd.nextInt(len);
        while (pswd[index = rnd.nextInt(len)] != 0);
        return index;
    }
}
