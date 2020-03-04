/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.net.view_model;

import java.text.SimpleDateFormat;
import java.util.Date;
import model.Projects;
import model.Services;

/**
 *
 * @author E-book
 */
public class ServiceSyncItem extends Services implements SyncItem{
    private Integer projectId, projectOnlineEntryId, changeId, changeUserOnlineEntryId;
    private String change, changeTimeStamp;
    
    public ServiceSyncItem(Integer id, String name, String description, double amount,
            Integer OnlineEntryId, Integer fixedAmount, Integer projectId, Integer projectOnlineEntryId){
        setId(id);
        setName(name);
        setDescription(description);
        setAmount(amount);
        setOnlineEntryId(OnlineEntryId);
        setFixedAmount(fixedAmount);
        setProjectId(projectId);
        setProjectOnlineEntryId(projectOnlineEntryId);
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
    
    
    public void setProjectId(Integer projectId){
        this.projectId = projectId;
    }
    
    public Integer getProjectId(){
        return projectId;
    }
    
    
    public void setProjectOnlineEntryId(Integer projectOnlineEntryId){
        this.projectOnlineEntryId = projectOnlineEntryId;
    }

    public static Services map(ServiceSyncItem entry, Projects project){
        Services result = new Services();
        return map(result, entry, entry.getId(), project);
    }
    
    public static Services map(Services result, Services entry, int onlineEntryId, Projects project){
        result.setName(entry.getName());
        result.setDescription(entry.getDescription());
        result.setAmount(entry.getAmount());
        result.setFixedAmount(entry.getFixedAmount());
        result.setOnlineEntryId(onlineEntryId);
        result.setProject(project);
        return result;
    }
      
}
