/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.net.view_model;

import model.Expenditures;

/**
 *
 * @author E-book
 */
public class ExpenditureSyncItem extends Expenditures implements SyncItem{
    private String change, changeTimestamp;
    private Integer changeId, changeUserOnlineEntryId, clientId, costCategoryId, projectId, issuerId;
    private Integer clientOnlineEntryId, costCategoryOnlineEntryId, projectOnlineEntryId, issuerOnlineEntryId;
    
    public ExpenditureSyncItem(Integer id, String date, String description, double amount,
            Integer onlineEntryId, Integer clientId, Integer clientOnlineEntryId, Integer costCategoryId, 
            Integer costCategoryOnlineEntryId, Integer projectId, Integer projectOnlineEntryId,
            Integer issuerId, Integer issuerOnlineEntryId){
        setId(id);
        setDate(date);
        setDescription(description);
        setAmount(amount);
        setOnlineEntryId(onlineEntryId);
        setClientId(clientId);
        setClientOnlineEntryId(clientOnlineEntryId);
        setCostCategoryId(costCategoryId);
        setCostCategoryOnlineEntryId(costCategoryOnlineEntryId);
        setProjectId(projectId);
        setProjectOnlineEntryId(projectOnlineEntryId);
        setIssuerId(issuerId);
        setIssuerOnlineEntryId(issuerOnlineEntryId);
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
    
    public void setClientId(Integer clientId){
        this.clientId = clientId;
    }
    
    public void setClientOnlineEntryId(Integer clientOnlineEntryId){
        this.clientOnlineEntryId = clientOnlineEntryId;
    }
    
    public void setCostCategoryId(Integer costCategoryId){
        this.costCategoryId = costCategoryId;
    }
    
    public void setCostCategoryOnlineEntryId(Integer costCategoryOnlineEntryId){
        this.costCategoryOnlineEntryId = costCategoryOnlineEntryId;
    }
    
    public void setProjectId(Integer projectId){
        this.projectId = projectId;
    }
    
    public void setProjectOnlineEntryId(Integer projectOnlineEntryId){
        this.projectOnlineEntryId = projectOnlineEntryId;
    }
    
    public void setIssuerId(Integer issuerId){
        this.issuerId = issuerId;
    }
    
    public void setIssuerOnlineEntryId(Integer issuerOnlineEntryId){
        this.issuerOnlineEntryId = issuerOnlineEntryId;
    }
}
