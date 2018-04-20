package org.ParkSys.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UtilsMD5 {

    public static String EncoderByMd5(String str) throws NoSuchAlgorithmException {
        String encoded = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes());
            encoded = new BigInteger(1, md5.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encoded;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        UtilsMD5 md5 = new UtilsMD5();
        System.out.println(md5.EncoderByMd5("1"));
    }

}
