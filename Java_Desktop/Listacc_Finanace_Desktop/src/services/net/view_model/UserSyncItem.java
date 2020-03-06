/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.net.view_model;

import model.Departments;
import model.Persons;
import model.Users;

/**
 *
 * @author E-book
 */
public class UserSyncItem extends Users implements SyncItem{
    private Integer departmentId, departmentOnlineEntryId, personId;
    private String change, changeTimeStamp;
    private Integer changeId, changeUserOnlineEntryId;
    
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
    
    public Integer getDepartmentId(){
        return departmentId;
    }
    
    public Integer getPersonId(){
        return personId;
    }

    public static Users map(UserSyncItem entry, Departments department, Persons person){
        Users result = new Users();
        return map(result, entry, entry.getId(), department, person);
    }
    
    public static Users map(Users result, Users entry, int onlineEntryId, Departments department, Persons person){
        result.setUserName(entry.getUserName());
        result.setNormalizedUserName(entry.getNormalizedUserName());
        result.setEmail(entry.getEmail());
        result.setNormalizedEmail(entry.getNormalizedEmail());
        result.setEmailConfirmed(entry.getEmailConfirmed());
        result.setPasswordHash(entry.getPasswordHash());
        result.setSecurityStamp(entry.getSecurityStamp());
        result.setConcurrencyStamp(entry.getConcurrencyStamp());
        result.setPhoneNumber(entry.getPhoneNumber());
        result.setPhoneNumberConfirmed(entry.getPhoneNumberConfirmed());
        result.setTwoFactorEnabled(entry.getTwoFactorEnabled());
        result.setLockoutEnd(entry.getLockoutEnd());
        result.setLockoutEnabled(entry.getLockoutEnabled());
        result.setAccessFailedCount(entry.getAccessFailedCount());
        result.setPhone(entry.getPhone());
        result.setAddress(entry.getAddress());
        result.setDiscriminator(entry.getDiscriminator());
        result.setDiscriminator("User");
        result.setSalt(entry.getSalt());
        result.setOnlineEntryId(onlineEntryId);
        result.setDepartment(department);
        result.setPerson(person);
        result.setStatus(entry.getStatus());
        return result;
    }
    
}
