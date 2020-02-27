/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.net.view_model;

import model.CostCategories;

/**
 *
 * @author E-book
 */
public class CostCategorySyncItem extends CostCategories implements SyncItem{
    private String change, changeTimestamp;
    private Integer changeId, changeUserOnlineEntryId;
    
    public CostCategorySyncItem(Integer id, String name, String type, String description, Integer onlineEntryId){
        setId(id);
        setName(name);
        setType(type);
        setDescription(description);
        setOnlineEntryId(onlineEntryId);
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

    public static CostCategories map(CostCategorySyncItem entry){
        CostCategories result = new CostCategories();
        return map(result, entry, entry.getId());
    }
    
    public static CostCategories map(CostCategories result, CostCategories entry, int onlineEntryId){
        result.setName(entry.getName());
        result.setType(entry.getType());
        result.setDescription(entry.getDescription());
        result.setOnlineEntryId(onlineEntryId);
        return result;
    }
    
}
