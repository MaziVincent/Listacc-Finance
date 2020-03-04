/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.net.view_model;

import java.text.SimpleDateFormat;
import java.util.Date;
import model.Departments;
import model.Projects;

/**
 *
 * @author E-book
 */
public class ProjectSyncItem extends Projects implements SyncItem{
    private Integer departmentId, departmentOnlineEntryId, changeId, changeUserOnlineEntryId;
    private String change, changeTimeStamp;
    
    public ProjectSyncItem(Integer Id, String Name, String Description, 
            Integer DepartmentId, Integer DepartmentOnlineEntryId, Integer OnlineEntryId){
        setId(Id);
        setOnlineEntryId(OnlineEntryId);
        setName(Name);
        setDescription(Description);
        setDepartmentId(DepartmentId);
        setDepartmentOnlineEntryId(DepartmentOnlineEntryId);
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
    
    
    public void setDepartmentId(Integer departmentId){
        this.departmentId = departmentId;
    }
    
    public Integer getDepartmentId(){
        return departmentId;
    }
    
    
    public void setDepartmentOnlineEntryId(Integer departmentOnlineEntryId){
        this.departmentOnlineEntryId = departmentOnlineEntryId;
    }

    public static Projects map(ProjectSyncItem entry, Departments department){
        Projects result = new Projects();
        return map(result, entry, entry.getId(), department);
    }
    
    public static Projects map(Projects result, Projects entry, int onlineEntryId, Departments department){
        result.setName(entry.getName());
        result.setDescription(entry.getDescription());
        result.setOnlineEntryId(onlineEntryId);
        result.setDepartment(department);
        return result;
    }
    
    
}
