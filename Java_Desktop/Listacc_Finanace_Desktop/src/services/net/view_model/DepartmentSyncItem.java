/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.net.view_model;

import model.Departments;

/**
 *
 * @author E-book
 */
public class DepartmentSyncItem extends Departments implements SyncItem{
    private String change, changeTimestamp;
    private Integer changeId, changeUserOnlineEntryId;
    
    public DepartmentSyncItem(Integer Id, Integer OnlineEntryId, String Name){
        setId(Id);
        setOnlineEntryId(OnlineEntryId);
        setName(Name);
    }

    public DepartmentSyncItem(int Id, int OnlineEntryId, String Name, String Change){
        this(Id, OnlineEntryId, Name);
        this.change = Change;
    }
    
    @Override
    public Integer getChangeId(){
        return changeId;
    }
    
    @Override
    public void setChange(String change){
        this.change = change;
    }
    
    @Override
    public void setChangeTimestamp(String timestamp){
        this.changeTimestamp = timestamp;
    }
    
    @Override
    public void setChangeUserOnlineEntryId(Integer userId){
        this.changeUserOnlineEntryId = userId;
    }
    
    public static Departments map(DepartmentSyncItem entry){
        Departments result = new Departments();
        return map(result, entry, entry.getId());
    }
    
    public static Departments map(Departments result, Departments entry, int onlineEntryId){
        result.setName(entry.getName());
        result.setOnlineEntryId(onlineEntryId);
        return result;
    }
}
