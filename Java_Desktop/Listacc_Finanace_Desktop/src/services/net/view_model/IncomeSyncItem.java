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
import model.Incomes;
import model.Services;
import model.Users;
import services.data.IncomeService;

/**
 *
 * @author E-book
 */
public class IncomeSyncItem implements SyncItem{
    private Integer id, unit, onlineEntryId;
    private String type, date, paymentType, dateDue;
    private double amountReceived, discount, amountReceivable;
    private String change, changeTimeStamp;
    private Integer changeId, changeUserOnlineEntryId, clientId, serviceId, userId, incomeId;
    private Integer clientOnlineEntryId, incomeOnlineEntryId, serviceOnlineEntryId, userOnlineEntryId;
    
    transient static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    
    public IncomeSyncItem(Integer id, String type, Long date, Integer unit, double amountReceived,
            double discount, String paymentType, double amountReceivable, Long dateDue, Integer onlineEntryId,
            Integer clientId, Integer clientOnlineEntryId, Integer incomeId, Integer serviceId, 
            Integer serviceOnlineEntryId, Integer userId, Integer userOnlineEntryId){
        setId(id);
        setType(type);
        setDate(date);
        setUnit(unit);
        setAmountReceived(amountReceived);
        setDiscount(discount);
        setPaymentType(paymentType);
        setAmountReceivable(amountReceivable);
        setDateDue(dateDue);
        setOnlineEntryId(onlineEntryId);
        setClientId(clientId);
        setClientOnlineEntryId(clientOnlineEntryId);
        setIncomeId(incomeId);
        setServiceId(serviceId);
        setServiceOnlineEntryId(serviceOnlineEntryId);
        setUserId(userId);
        setUserOnlineEntryId(userOnlineEntryId);
        
        if(incomeId != null){
            // get parent income info
            IncomeService iService = new IncomeService();
            IncomeSyncItem parentIncome = iService.getIncomeById(incomeId);
            setIncomeOnlineEntryId(parentIncome.getOnlineEntryId());
            iService.close();
        }
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
    
    public void setIncomeId(Integer incomeId){
        this.incomeId = incomeId;
    }
    
    public Integer getIncomeId(){
        return incomeId;
    }
    
    public void setIncomeOnlineEntryId(Integer incomeOnlineEntryId){
        this.incomeOnlineEntryId = incomeOnlineEntryId;
    }
    
    public void setServiceId(Integer serviceId){
        this.serviceId = serviceId;
    }
    
    public Integer getServiceId(){
        return serviceId;
    }
    
    public void setServiceOnlineEntryId(Integer serviceOnlineEntryId){
        this.serviceOnlineEntryId = serviceOnlineEntryId;
    }
    
    public void setUserId(Integer userId){
        this.userId = userId;
    }
    
    public Integer getUserId(){
        return userId;
    }
    
    public void setUserOnlineEntryId(Integer userOnlineEntryId){
        this.userOnlineEntryId = userOnlineEntryId;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getId(){
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getType(){
        return type;
    }

    public void setDate(Long date) {
        Date d = new Date(date);
        this.date = sdf.format(d);
    }
    
    public Long getLongDate(){
        try{
            Date d = sdf.parse(this.date);
            return d.getTime();
        }
        catch(ParseException ex){
            return null;
        }
    }
    
    public void setUnit(int unit) {
        this.unit = unit;
    }
    
    public Integer getUnit(){
        return unit;
    }

    public void setAmountReceived(double amountReceived) {
        this.amountReceived = amountReceived;
    }
    
    public Double getAmountReceived(){
        return amountReceived;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
    
    public Double getDiscount(){
        return discount;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
    
    public String getPaymentType(){
        return paymentType;
    }

    public void setAmountReceivable(double amountReceivable) {
        this.amountReceivable = amountReceivable;
    }
    
    public Double getAmountReceivable(){
        return amountReceivable;
    }

    public void setDateDue(Long dateDue) {
        Date d = new Date(dateDue);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        this.dateDue = sdf.format(d);
    }
    
    public Long getLongDateDue(){
        try{
            Date d = sdf.parse(dateDue);
            return d.getTime();
        }
        catch(ParseException ex){
            return null;
        }
    }

    public Integer getOnlineEntryId() {
        return this.onlineEntryId;
    }

    public void setOnlineEntryId(Integer onlineEntryId) {
        this.onlineEntryId = onlineEntryId;
    }
    
    public static Incomes map(IncomeSyncItem entry, Services service, Clients client, Users user, Incomes income){
        Incomes result = new Incomes();
        return map(result, entry, entry.getId(), service, client, user, income);
    }
    
    public static Incomes map(Incomes result, IncomeSyncItem entry, int onlineEntryId, Services service, 
            Clients client, Users user, Incomes income){
        result.setType(entry.getType());
        result.setDate(entry.getLongDate());
        result.setAmountReceived(entry.getAmountReceived());
        result.setDiscount(entry.getDiscount());
        result.setPaymentType(entry.getPaymentType());
        result.setAmountReceivable(entry.getAmountReceivable());
        result.setDateDue(entry.getLongDateDue());
        result.setUnit(entry.getUnit());
        result.setOnlineEntryId(onlineEntryId);
        result.setService(service);
        result.setClient(client);
        result.setUser(user);
        result.setIncome(income);
        return result;
    }
}
