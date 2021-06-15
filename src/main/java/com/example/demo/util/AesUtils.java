package com.example.demo.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesUtils {

    private static byte[] key = "uT9JPn2Ye2dNbCy0".getBytes();

    public static void main(String[] args) throws Exception {
        String phone = "123456";
        byte[] encrypt = encrypt(phone.getBytes(), key);
        System.out.println(Base64.encodeBase64String(encrypt));
        String decrypt = decrypt(encrypt, key);
        System.out.println(decrypt);
    }

    public static byte[] encrypt(byte[] text, byte[] key) throws Exception {
        byte[] crypt = null;
        SecretKey aesKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, new IvParameterSpec(key));
        crypt = cipher.doFinal(text);
        return crypt;
    }

    public static String decrypt(byte[] text, byte[] key) throws Exception {
        SecretKey aesKey = new SecretKeySpec(key, "AES");
        final Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(key));
        return new String(cipher.doFinal(text));
    }

}
