/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.net.view_model;

import java.text.SimpleDateFormat;
import java.util.Date;
import model.Incomes;
import services.data.IncomeService;

/**
 *
 * @author E-book
 */
public class IncomeSyncItem implements SyncItem{
    private Integer id, unit, onlineEntryId, incomeId;
    private String type, date, paymentType, dateDue;
    private double amountReceived, discount, amountReceivable;
    private String change, changeTimeStamp;
    private Integer changeId, changeUserOnlineEntryId, clientId, serviceId, userId;
    private Integer clientOnlineEntryId, incomeOnlineEntryId, serviceOnlineEntryId, userOnlineEntryId;
    
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
    
    public void setClientOnlineEntryId(Integer clientOnlineEntryId){
        this.clientOnlineEntryId = clientOnlineEntryId;
    }
    
    public void setIncomeOnlineEntryId(Integer incomeOnlineEntryId){
        this.incomeOnlineEntryId = incomeOnlineEntryId;
    }
    
    public void setServiceId(Integer serviceId){
        this.serviceId = serviceId;
    }
    
    public void setServiceOnlineEntryId(Integer serviceOnlineEntryId){
        this.serviceOnlineEntryId = serviceOnlineEntryId;
    }
    
    public void setUserId(Integer userId){
        this.userId = userId;
    }
    
    public void setUserOnlineEntryId(Integer userOnlineEntryId){
        this.userOnlineEntryId = userOnlineEntryId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(Long date) {
        Date d = new Date(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        this.date = sdf.format(d);
    }
    
    public void setUnit(int unit) {
        this.unit = unit;
    }

    public void setAmountReceived(double amountReceived) {
        this.amountReceived = amountReceived;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setAmountReceivable(double amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public void setDateDue(Long dateDue) {
        Date d = new Date(dateDue);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        this.dateDue = sdf.format(d);
    }

    public Integer getOnlineEntryId() {
        return this.onlineEntryId;
    }

    public void setOnlineEntryId(Integer onlineEntryId) {
        this.onlineEntryId = onlineEntryId;
    }

    public void setIncomeId(Integer incomeId) {
        this.incomeId = incomeId;
    }
}
