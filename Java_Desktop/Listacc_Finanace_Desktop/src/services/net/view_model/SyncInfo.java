/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.net.view_model;

import helpers.FileHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author E-book
 */
public class SyncInfo {
    private String LastUpdateTimestamp, Token;
    private int LastChangeId;
    
    private final static String FILEPATH = "sc_info/SyncInfo.txt";
    private final static String CONTENT_SEPARATOR = "::::###&%&###::::";
    
    public String getLastUpdateTimestamp(){
        return LastUpdateTimestamp;
    }
    
    public void setLastChangeId(int LastChangeId){
        this.LastChangeId = LastChangeId;
    }
    
    public int getLastChangeId(){
        return LastChangeId;
    }
    
    public String getToken(){
        return Token;
    }
    
    public static SyncInfo getLastSyncInfo(){
        try{
            SyncInfo info = new SyncInfo();
            File file = new File(FILEPATH);
            
            // check if file exists
            if(file.exists() && file.isFile()) // if file exists
            {
                // read file
                FileHelper fileHelper = new FileHelper();
                String content = fileHelper.decryptFileToString(FILEPATH);
        
                // set values to object
                String[] values = content.split(CONTENT_SEPARATOR);
                info.Token = values[0];
                info.LastChangeId = Integer.parseInt(values[1]);
                info.LastUpdateTimestamp = values[2];
            }
            else {// if file does not exist
                // create directory
                if(!file.getParentFile().exists()) file.mkdirs();
                
                // create new file
                if(file.createNewFile()){
                    // set default values
                    info.Token = "";
                    info.LastChangeId = 0;
                    info.LastUpdateTimestamp = "0";
                    
                    // save file
                    save(info);
                }
            }
            
            return info;
        }
        catch(FileNotFoundException e){
            e.getMessage();
        }
        catch(IOException e){
            e.getMessage();
        }
        
        return null;
    }
    
    public static void saveToken(String token){
        // modify object
        SyncInfo currentData = getLastSyncInfo();
        currentData.Token = token.startsWith("\"") && token.endsWith("\"") ? 
                token.substring(1, token.length() - 1) : token;
        
        // save changes
        save(currentData);
    }
    
    public static void saveLastChangeId(int lastChangeId){
        // modify object
        SyncInfo currentData = getLastSyncInfo();
        currentData.LastChangeId = lastChangeId;
        
        // save changes
        save(currentData);
    }
    
    public static void saveLastUpdateTimestamp(String lastUpdateTimestamp){
        // modify object
        SyncInfo currentData = getLastSyncInfo();
        currentData.LastUpdateTimestamp = lastUpdateTimestamp;
        
        // save changes
        save(currentData);
    }
    
    private static void save(SyncInfo info) {
        // set string content
        String content = String.format("%s%s%d%s%s",info.getToken(), CONTENT_SEPARATOR, info.getLastChangeId(),
                CONTENT_SEPARATOR, info.getLastUpdateTimestamp());

        // save encrypted into file
        FileHelper fileHelper = new FileHelper();
        fileHelper.encryptToFile(content, FILEPATH);
    }
}
