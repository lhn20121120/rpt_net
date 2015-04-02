
package com.cbrc.auth.util;

import java.security.MessageDigest;

public class Encoder {

    private static MessageDigest digest = null;
    private static boolean isInited = false;

    private Encoder() {
    }

    /**
     * Êý¾Ý¼ÓÃÜ
     * @param input
     * @return
     */
    public static synchronized String getMD5_Base64(String input) {

        if (isInited == false) {
            isInited = true;
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (Exception ex) {
               ex.printStackTrace();
            }
        }
        if (digest == null) return input;

        try {
            digest.update(input.getBytes("GBK"));
        } catch (java.io.UnsupportedEncodingException ex) {
           ex.printStackTrace();
        }
        byte[] rawData = digest.digest();
        byte[] encoded = Base64.encode(rawData);
        String retValue = new String(encoded);
        return retValue;
    }  
    public static void main(String[] args){
    	System.out.println(Encoder.getMD5_Base64("123456"));
    }

}