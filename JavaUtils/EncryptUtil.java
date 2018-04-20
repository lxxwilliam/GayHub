package com.calabar.commons.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class EncryptUtil {
    
    public EncryptUtil() {
    }
    
    private static final String ENCRYPT_TYPE_MD2 = "MD2";
    
    private static final String ENCRYPT_TYPE_MD5 = "MD5";
    
    private static final String ENCRYPT_TYPE_SHA1 = "SHA-1";
    
    private static final String ENCRYPT_TYPE_SHA256 = "SHA-256";
    
    private static final String ENCRYPT_TYPE_MD384 = "SHA-384";
    
    private static final String ENCRYPT_TYPE_MD512 = "SHA-512";
    
    public static String getMD2EncryptString(String text) {
        return getEncryptString(text, ENCRYPT_TYPE_MD2);
    }
    
    public static String getMD5EncryptString(String text) {
        return getEncryptString(text, ENCRYPT_TYPE_MD5);
    }
    
    public static String getSHA1EncryptString(String text) {
        return getEncryptString(text, ENCRYPT_TYPE_SHA1);
    }
    
    public static String getSHA256EncryptString(String text) {
        return getEncryptString(text, ENCRYPT_TYPE_SHA256);
    }
    
    public static String getSHA384EncryptString(String text) {
        return getEncryptString(text, ENCRYPT_TYPE_MD384);
    }
    
    public static String getSHA512EncryptString(String text) {
        return getEncryptString(text, ENCRYPT_TYPE_MD512);
    }
    
    private static String getEncryptString(String text, String type) {
        
        try {
            MessageDigest md = MessageDigest.getInstance(type);
            md.update(text.getBytes());
            byte[] b = md.digest();
            StringBuilder output = new StringBuilder(32);
            for (int i = 0; i < b.length; i++) {
                String temp = Integer.toHexString(b[i] & 0xff);
                if (temp.length() < 2) {
                    output.append("0"); // 不足两位，补0
                }
                output.append(temp);
            }
            return output.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void main(String[] args) {
        String encrypedText = getSHA256EncryptString("1");
        System.out.println(encrypedText);
    }
    
}
