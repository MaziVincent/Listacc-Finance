/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.net.view_model;

import java.text.SimpleDateFormat;
import java.util.Date;
import model.Clients;
import model.Persons;

/**
 *
 * @author E-book
 */
public class ClientSyncItem extends Clients implements SyncItem{
    private Integer personOnlineEntryId, changeId, changeUserOnlineEntryId;
    private String change, changeTimeStamp;

    public ClientSyncItem(Integer Id, String BusinessName, String Phone, String Email,
            String Address, String UId, String UId2, Double AmountReceivable, Integer OnlineEntryId, 
            String FirstName, String LastName, String Gender, String DateOfBirth, String Change){
        setId(Id);
        setOnlineEntryId(OnlineEntryId);
        setBusinessName(BusinessName);
        setPhone(Phone);
        setEmail(Email);
        setAddress(Address);
        setUId(UId);
        setUId2(UId2);
        setAmountReceivable(AmountReceivable);
        
        Persons p = new Persons();
        p.setFirstName(FirstName);
        p.setLastName(LastName);
        p.setGender(Gender);
        p.setDateOfBirth(DateOfBirth);
        setPerson(p);
        
        this.change = Change;
    }
    
    public ClientSyncItem(Integer Id, String BusinessName, String Phone, String Email,
            String Address, String UId, String UId2, Double AmountReceivable, Integer OnlineEntryId, 
            Integer PersonId, String FirstName, String LastName, String Gender, String DateOfBirth,
            Integer PersonOnlineEntryId){
        setId(Id);
        setOnlineEntryId(OnlineEntryId);
        setBusinessName(BusinessName);
        setPhone(Phone);
        setEmail(Email);
        setAddress(Address);
        setUId(UId);
        setUId2(UId2);
        setAmountReceivable(AmountReceivable);
        
        Persons p = new Persons();
        p.setId(PersonId);
        p.setFirstName(FirstName);
        p.setLastName(LastName);
        p.setGender(Gender);
        p.setDateOfBirth(DateOfBirth);
        p.setOnlineEntryId(PersonOnlineEntryId);
        setPerson(p);
    }
    
    public ClientSyncItem(Integer Id, String BusinessName, String Phone, String Email,
            String Address, String UId, String UId2, Double AmountReceivable, Integer OnlineEntryId, 
            Integer PersonId){
        setId(Id);
        setOnlineEntryId(OnlineEntryId);
        setBusinessName(BusinessName);
        setPhone(Phone);
        setEmail(Email);
        setAddress(Address);
        setUId(UId);
        setUId2(UId2);
        setAmountReceivable(AmountReceivable);
        setPersonId(PersonId);
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
        Date d = new Date(Long.parseLong(timestamp));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        this.changeTimeStamp = sdf.format(d);
    }
    
    @Override
    public void setChangeUserOnlineEntryId(Integer userId){
        this.changeUserOnlineEntryId = userId;
    }

    public static Clients map(ClientSyncItem entry, Persons person){
        Clients result = new Clients();
        return map(result, entry, entry.getId(), person);
    }
    
    public static Clients map(Clients result, Clients entry, int onlineEntryId, Persons person){
        result.setBusinessName(entry.getBusinessName());
        result.setPhone(entry.getPhone());
        result.setEmail(entry.getEmail());
        result.setAddress(entry.getAddress());
        result.setUId(entry.getUId());
        result.setUId2(entry.getUId2());
        result.setAmountReceivable(entry.getAmountReceivable());
        result.setOnlineEntryId(onlineEntryId);
        result.setPerson(person);
        return result;
    }
    
}
