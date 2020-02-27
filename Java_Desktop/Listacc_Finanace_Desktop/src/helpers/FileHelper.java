/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author E-book
 */
public class FileHelper {
    private SecretKeySpec sks = null;
    private Cipher c;
    private byte[] encodedBytes;
    
    public FileHelper(){
        generateKey("listacc_finance_");
        try {
            c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, sks);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
            System.out.println("AES decryption error");
        }
    }
    
    private void generateKey(String seed)
    {
        try {
            byte[] key = seed.getBytes("UTF-8");
            sks = new SecretKeySpec(key, "AES");
            
            /*KeySpec spec = new PBEKeySpec("password".toCharArray(), salt, 65536, 256); // AES-256
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] key = f.generateSecret(spec).getEncoded();
            sks = new SecretKeySpec(key, "AES");*/
        }
        catch (UnsupportedEncodingException e) {
            System.out.println("AES secret key spec error");
        }
    }
    
    public boolean encryptToFile(String content, String filePath)
    {
        try {
            FileOutputStream out = new FileOutputStream(filePath);
        
            c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, sks);
            encodedBytes = c.doFinal(content.getBytes());
            
            //ByteArrayOutputStream outStream = new ByteArrayOutputStream(out);
            out.write(encodedBytes);
            out.flush();
            out.close();
            
            return true;
            
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            // System.out.println("AES encryption error");
            return false;
        }
    }
    
    public String decryptFileToString(String filePath)
    {
        byte[] bFile;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            bFile = new byte[fis.available()];
            int result = fis.read(bFile);
            fis.close();

            c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, sks);
            String content = new String(c.doFinal(bFile));
            return content;
        }
        catch (IOException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException exc)
        {
            exc.getMessage();
        }
        return null;
    }
    
}
