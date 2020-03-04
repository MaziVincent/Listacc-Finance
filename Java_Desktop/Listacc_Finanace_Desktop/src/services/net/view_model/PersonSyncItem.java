/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.net.view_model;

import model.Persons;

/**
 *
 * @author E-book
 */
public class PersonSyncItem extends Persons implements SyncItem{
    private String change, changeTimeStamp;
    private Integer changeId, changeUserOnlineEntryId;
    
    public PersonSyncItem(){}
    
    public PersonSyncItem(int Id, String firstName, String lastName, String gender, String dateOfBirth){
        setId(Id);
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setDateOfBirth(dateOfBirth);
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
        this.changeTimeStamp = timestamp;
    }
    
    @Override
    public void setChangeUserOnlineEntryId(Integer userId){
        this.changeUserOnlineEntryId = userId;
    }

    public static Persons map(PersonSyncItem entry){
        Persons result = new Persons();
        return map(result, entry, entry.getId());
    }
    
    public static Persons map(Persons result, Persons entry, int onlineEntryId){
        result.setFirstName(entry.getFirstName());
        result.setLastName(entry.getLastName());
        result.setGender(entry.getGender());
        result.setDateOfBirth(entry.getDateOfBirth());
        result.setOnlineEntryId(onlineEntryId);
        return result;
    }
}
