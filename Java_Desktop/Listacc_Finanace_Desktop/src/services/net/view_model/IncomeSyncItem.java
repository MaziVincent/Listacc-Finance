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
public class IncomeSyncItem extends Incomes implements SyncItem{
    private String change, changeTimeStamp;
    private Integer changeId, changeUserOnlineEntryId, clientId, serviceId, userId;
    private Integer clientOnlineEntryId, incomeOnlineEntryId, serviceOnlineEntryId, userOnlineEntryId;
    
    public IncomeSyncItem(Integer id, String type, String date, Integer unit, double amountReceived,
            double discount, String paymentType, double amountReceivable, String dateDue, Integer onlineEntryId,
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

    @Override
    public void setDate(String date) {
        Date d = new Date(Long.parseLong(date));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        super.setDate(sdf.format(d));
    }

    @Override
    public void setDateDue(String dateDue) {
        Date d = new Date(Long.parseLong(dateDue));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        super.setDateDue(sdf.format(d));
    }
}
