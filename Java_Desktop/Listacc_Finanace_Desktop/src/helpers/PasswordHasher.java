/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author E-book
 */
public class PasswordHasher {
    public static String create(String password, String salt) throws RuntimeException {
        // StringBuilder pwBuilder = new StringBuilder(salt);
        // pwBuilder.append(password);
        // String rawPass = pwBuilder.toString();
        int iterations = 10000;
        char[] chars = password.toCharArray();
        byte[] saltBytes = salt.getBytes(StandardCharsets.UTF_8);
        byte[] saltByte2 = salt.getBytes();
        
        try {
            PBEKeySpec spec = new PBEKeySpec(chars, saltBytes, iterations, 256);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            byte[] result = skf.generateSecret(spec).getEncoded();

            String hash = Base64.getEncoder().encodeToString(result);
            return hash;
       } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException( e );
       }
    }
    
    public static boolean validate(String value, String salt, String hash) {
        try{
            return create(value, salt).equals(hash);
        }
        catch(RuntimeException e){
            return false;
        }
    }
}
