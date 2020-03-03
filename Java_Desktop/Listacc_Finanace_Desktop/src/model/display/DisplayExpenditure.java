/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.display;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import model.Clients;
import model.Persons;
import services.data.ClientService;

/**
 *
 * @author Agozie
 */
public class DisplayExpenditure {
    int costCatId,prjId,userId, clientId, id ;

    public DisplayExpenditure(int id, double amount, String description, String lastName, String firstName, String phone, String email, String address, String gender, Long date, String businessName, String projectName, String CostCategoryName) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.lastName = lastName;
        this.firstName = firstName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.gender = gender;
        this.date = date;
        this.businessName = businessName;
        this.projectName = projectName;
        this.costCategoryName = CostCategoryName;
    }
    double amount;
    Persons person;
    Clients client;
    String dateString;

    public String getDateString() {
       
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        DateFormat df = new SimpleDateFormat("dd-EEE, MM, yyyy");
        return df.format(calendar.getTime());
    }

    public Persons getPerson() {
        return person;
    }

    public void setPerson(Persons person) {
        this.person = person;
    }

    public Clients getClient() {
        return client;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public void setClient(Clients client) {
        this.client = client;
    }
    String description, lastName, firstName, phone, email, address, gender, businessName; 
    String projectName, costCategoryName;
    Long date;
    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCostCategoryName() {
        return costCategoryName;
    }

    public void setCostCategoryName(String CostCategoryName) {
        this.costCategoryName = CostCategoryName;
    }

    public DisplayExpenditure(int costCatId, int prjId, int userId, int clientId, double amount, String description) {
        this.costCatId = costCatId;
        this.prjId = prjId;
        this.userId = userId;
        this.clientId = clientId;
        this.amount = amount;
        this.description = description;
    }

    public DisplayExpenditure(int costCatId, int prjId, int userId, String businessName, int clientId, double amount, Long date) {
        this.costCatId = costCatId;
        this.prjId = prjId;
        this.userId = userId;
        this.clientId = clientId;
        this.amount = amount;
        this.businessName = businessName;
        
        if (null == businessName || businessName.isEmpty())
        {
            Clients clien = new ClientService().getClientById(clientId);
            
            if(null != clien.getPerson())
                {
                    this.lastName = clien.getPerson().getLastName();
                    this.firstName =clien.getPerson().getFirstName();
                }
        }
            
        
       this.date = date;
    }
    
    public DisplayExpenditure(int costCatId, int prjId, int userId, String businessName, String projectName,String costCategoryName, int clientId, double amount, Long date) {
        this.costCatId = costCatId;
        this.prjId = prjId;
        this.userId = userId;
        this.clientId = clientId;
        this.amount = amount;
        this.businessName = businessName;
        this.projectName =projectName;
        this.costCategoryName = costCategoryName;
        
        if (null == businessName || businessName.isEmpty())
        {
            Clients clien = new ClientService().getClientById(clientId);
            
            if(null != clien.getPerson())
                {
                    this.lastName = clien.getPerson().getLastName();
                    this.firstName =clien.getPerson().getFirstName();
                    this.businessName = this.firstName + " "+ this.lastName;
                }
            
        }
        else 
            this.businessName = businessName;
            
        
       this.date = date;
    }

    public int getCostCatId() {
        return costCatId;
    }

    public void setCostCatId(int costCatId) {
        this.costCatId = costCatId;
    }

    public int getPrjId() {
        return prjId;
    }

    public void setPrjId(int prjId) {
        this.prjId = prjId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    
    
    
}
