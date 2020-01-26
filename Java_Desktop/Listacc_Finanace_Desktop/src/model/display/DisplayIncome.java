/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.display;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import model.Clients;
import model.Incomes;
import model.Persons;
import services.data.ClientService;

/**
 *
 * @author Agozie
 */
public class DisplayIncome extends Incomes{
    String serviceName;
    String clientName;
    String clientFirstName;
    int serviceIdnum;
    Clients  client;
    String displayPaymentType;
    String dateString;

    public String getDateString() {
        String date =   getDate();
        long dateLong = Long.parseLong(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateLong);
        DateFormat df = new SimpleDateFormat("dd-EEE, MM, yyyy");
        return df.format(calendar.getTime());
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }
    

    public String getDisplayPaymentType() {
        return displayPaymentType;
    }

    public void setDisplayPaymentType(String paymentType) {
        this.displayPaymentType = paymentType;
    }

    public Clients getClient() {
        return client;
    }

    public void setClient(Clients client) {
        this.client = client;
    }

    public Persons getPerson() {
        return person;
    }

    public void setPerson(Persons person) {
        this.person = person;
    }
    Persons person;

    public int getServiceIdnum() {
        return serviceIdnum;
    }

    public void setServiceIdnum(int serviceIdnum) {
        this.serviceIdnum = serviceIdnum;
    }

    public String getClientFirstName() {
        return clientFirstName;
    }

    public void setClientFirstName(String clientFirstName) {
        this.clientFirstName = clientFirstName;
    }

    public String getClientLastName() {
        return clientLastName;
    }

    public void setClientLastName(String clientLastName) {
        this.clientLastName = clientLastName;
    }
    String clientLastName;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    
    public DisplayIncome(){
        super();
    }
    int clientNumId;

    public int getClientNumId() {
        return clientNumId;
    }

    public void setClientNumId(int clientNumId) {
        this.clientNumId = clientNumId;
    }
    public DisplayIncome(String serviceName, String clientName, int clientId,   Integer id, String date, double amountReceived, double discount, String paymentType, double amountReceivable, String dateDue) {
        super(id, date, amountReceived, discount, amountReceivable, dateDue);
        this.serviceName = serviceName;
        this.displayPaymentType = paymentType;
        if (null == clientName || clientName.isEmpty())
        {
            Clients clien = new ClientService().getClientById(clientId);
            setClientId(clien);
            if(null != clien.getPersonId())
             this.clientName = clien.getPersonId().getLastName() + " " + clien.getPersonId().getFirstName();
            else 
                this.clientName = clien.getBusinessName();
           this.clientNumId = clien.getId();
        }
        else 
            this.clientName = clientName;
            
    }
    
            
}