/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.net.view_model;

/**
 *
 * @author E-book
 */
public class UploadResponseViewModel {
    private int entryId;
    private int onlineEntryId;
    private String table;
    
    public int getId() {
        return entryId;
    }

    public int getOnlineEntryId() {
        return onlineEntryId;
    }
    
    public String getTable() {
        return table;
    }
}
