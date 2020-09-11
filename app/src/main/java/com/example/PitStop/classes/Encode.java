package com.example.PitStop.classes;

import android.util.Log;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Encode {
    public static String salt="%gmA4";

    static SecretKeyFactory factory;
    static byte[] hash;

    public static String hashPassword(String password){
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(),salt.getBytes(),65536,128);
        try {
            factory= SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            hash = factory.generateSecret(keySpec).getEncoded();
            Log.i("Main", "onCreate: "+hash.toString());
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return new String(hash);
    }
}
