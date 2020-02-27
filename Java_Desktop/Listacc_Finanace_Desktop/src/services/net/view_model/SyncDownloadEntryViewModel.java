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
public class SyncDownloadEntryViewModel {
    private DepartmentSyncItem dept;
    // private PersonSyncItem  person;
    private UserSyncItem user;
    private ClientSyncItem client;
    private ProjectSyncItem project;
    private CostCategorySyncItem costCategory;
    private ExpenditureSyncItem expenditure;
    private ServiceSyncItem service;
    private IncomeSyncItem income;
    private String table;
    
    // Departmnet
    public void setDepartment(DepartmentSyncItem dept){
        this.dept = dept;
    }
    
    public DepartmentSyncItem getDepartment(){
        return dept;
    }
    
    
    // User
    public UserSyncItem getUser(){
        return user;
    }
    
    
    // Client
    public void setClient(ClientSyncItem client){
        this.client = client;
    }
    
    public ClientSyncItem getClient(){
        return client;
    }
    
    
    // Project
    public void setProject(ProjectSyncItem project){
        this.project = project;
    }
    
    public ProjectSyncItem getProject(){
        return project;
    }
    
    
    // Cost Category
    public void setCostCategory(CostCategorySyncItem costCategory){
        this.costCategory = costCategory;
    }
    
    public CostCategorySyncItem getCostCategory(){
        return costCategory;
    }
    
    
    // Expenditure
    public void setExpenditure(ExpenditureSyncItem expenditure){
        this.expenditure = expenditure;
    }
    
    public ExpenditureSyncItem getExpenditure(){
        return expenditure;
    }
    
    
    // Service
    public void setService(ServiceSyncItem service){
        this.service = service;
    }
    
    public ServiceSyncItem getService(){
        return service;
    }
    
    
    // Income
    public void setIncome(IncomeSyncItem income){
        this.income = income;
    }
    
    public IncomeSyncItem getIncome(){
        return income;
    }
    
    
    // Table
    public void setTable(String table){
        this.table = table;
    }
    
    public String getTable(){
        return table;
    }
}
