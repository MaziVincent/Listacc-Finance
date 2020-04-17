/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.net.view_model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.Clients;
import model.CostCategories;
import model.Expenditures;
import model.Projects;
import model.Users;

/**
 *
 * @author E-book
 */
public class ExpenditureSyncItem implements SyncItem{
    private Integer id, onlineEntryId;
    private String date, description;
    private double amount;
    private String change, changeTimeStamp;
    private Integer changeId, changeUserOnlineEntryId, clientId, costCategoryId, projectId, issuerId;
    private Integer clientOnlineEntryId, costCategoryOnlineEntryId, projectOnlineEntryId, issuerOnlineEntryId;
    
    transient static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    
    public ExpenditureSyncItem(Integer id, Long date, String description, double amount,
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
        Date d = new Date(Long.parseLong(timestamp));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        this.changeTimeStamp = sdf.format(d);
    }
    
    @Override
    public void setChangeUserOnlineEntryId(Integer userId){
        this.changeUserOnlineEntryId = userId;
    }
    
    public void setClientId(Integer clientId){
        this.clientId = clientId;
    }
    
    public Integer getClientId(){
        return clientId;
    }
    
    public void setClientOnlineEntryId(Integer clientOnlineEntryId){
        this.clientOnlineEntryId = clientOnlineEntryId;
    }
    
    public void setCostCategoryId(Integer costCategoryId){
        this.costCategoryId = costCategoryId;
    }
    
    public Integer getCostCategoryId(){
        return costCategoryId;
    }
    
    public void setCostCategoryOnlineEntryId(Integer costCategoryOnlineEntryId){
        this.costCategoryOnlineEntryId = costCategoryOnlineEntryId;
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
    
    public void setIssuerId(Integer issuerId){
        this.issuerId = issuerId;
    }
    
    public Integer getIssuerId(){
        return issuerId;
    }
    
    public void setIssuerOnlineEntryId(Integer issuerOnlineEntryId){
        this.issuerOnlineEntryId = issuerOnlineEntryId;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getId(){
        return id;
    }

    public void setDate(Long date) {
        Date d = new Date(date);
        this.date = sdf.format(d);
    }
    
    public Long getLongDate(){
        try{
            Date d = sdf.parse(date);
            return d.getTime();
        }
        catch(ParseException ex){
            return null;
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDescription(){
        return description;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public Double getAmount(){
        return amount;
    }

    public void setOnlineEntryId(Integer onlineEntryId) {
        this.onlineEntryId = onlineEntryId;
    }
    
    public static Expenditures map(ExpenditureSyncItem entry, Projects project, Clients client, 
            CostCategories costCategory, Users issuer){
        Expenditures result = new Expenditures();
        return map(result, entry, entry.getId(), project, client, costCategory, issuer);
    }
    
    public static Expenditures map(Expenditures result, ExpenditureSyncItem entry, int onlineEntryId, Projects project, 
            Clients client, CostCategories costCategory, Users issuer){
        result.setDate(entry.getLongDate());
        result.setDescription(entry.getDescription());
        result.setAmount(entry.getAmount());
        result.setOnlineEntryId(onlineEntryId);
        result.setProject(project);
        result.setClient(client);
        result.setCostCategory(costCategory);
        result.setIssuer(issuer);
        return result;
    }
}
