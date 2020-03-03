/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import model.Changes;
import model.Clients;
import model.Incomes;
import model.Persons;
import model.Services;
import model.display.DisplayIncome;
import services.net.view_model.IncomeSyncItem;
import services.net.view_model.UploadResponseViewModel;

/**
 *
 * @author Agozie
 */
public class IncomeService extends DataService {
//    (String serviceName, String clientName, int clientId,   Integer id, String date, double amountReceived, double discount, String paymentType, double amountReceivable, String dateDue) {
    public List<DisplayIncome> getAllIncomes()
    { 
        return em.createQuery("SELECT new model.display.DisplayIncome(a.service.name,a.client.businessName,a.client.id,a.id, a.date,a.unit,a.amountReceived, a.discount, a.paymentType, a.amountReceivable, a.dateDue)FROM Incomes a ORDER BY a.id desc").getResultList();
    }
      
    public List<DisplayIncome> getAllReceivableIncomes()
    { 
        try{
            Query q =  em.createNativeQuery("SELECT s.name,c.businessName,c.id,a.id, a.date,a.unit,a.amountReceived, a.discount, a.paymentType, a.amountReceivable, a.dateDue, \n" +
                                    "Count(e.id) FROM Incomes a LEFT JOIN Incomes e ON a.id = e.incomeId\n" +
                                    "LEFT JOIN Clients c ON a.ClientId = c.id\n" +
                                    "LEFT JOIN Services s ON a.ServiceId = s.id\n" +
                                    "Where a.amountReceivable > 0");
           
            @SuppressWarnings("unchecked")
            List<Object[]> list = q.getResultList();
            
            if(null != list.get(0)[2] )
            return map(DisplayIncome.class, list);
            else return new LinkedList<>() ;
        }
        catch(Exception xc){
            xc.printStackTrace();
            return null;
        }
    }
    
    public IncomeSyncItem getIncomeById(int id)
    {
        try{
            Query q = em.createQuery("SELECT new services.net.view_model.IncomeSyncItem(c.id, c.type, c.date, c.unit, c.amountReceived, c.discount, c.paymentType, c.amountReceivable, c.dateDue, c.onlineEntryId, c.client.id, c.incomeId, c.client.onlineEntryId, c.service.id, c.service.onlineEntryId, c.user.id, c.user.onlineEntryId) "
                    + "FROM Incomes c  WHERE c.id = :id");
            q.setParameter("id", id);
            IncomeSyncItem a = (IncomeSyncItem)q.getSingleResult();
            return a;
        }catch(NoResultException ex){
            return null;
        }catch(Exception exc){
            exc.printStackTrace();
        }
        return null;
    }
    
    public IncomeSyncItem getIncomeByChange(Changes ch)
    {
        try{
            Query q = em.createQuery("SELECT new services.net.view_model.IncomeSyncItem(c.id, c.type, c.date, c.unit, c.amountReceived, c.discount, c.paymentType, c.amountReceivable, c.dateDue, c.onlineEntryId, c.client.id, c.incomeId, c.client.onlineEntryId, c.service.id, c.service.onlineEntryId, c.user.id, c.user.onlineEntryId) "
                    + "FROM Incomes c  WHERE c.id = :id");
            q.setParameter("id", ch.getEntryId());
            IncomeSyncItem a = (IncomeSyncItem)q.getSingleResult();
            a.setChange(ch.getChanges());
            a.setChange(ch.getChanges());
            a.setChangeTimestamp(ch.getTimeStamp());
            a.setChangeUserOnlineEntryId(ch.getUser() != null ? ch.getUser().getOnlineEntryId(): null);
            return a;
        }catch(NoResultException ex){
            return null;
        }catch(Exception exc){
            exc.printStackTrace();
        }
        return null;
    }

    public boolean createIncome(DisplayIncome display)
    {
        try{
            Incomes income = new Incomes();
            Persons incomePerson = display.getPerson();
            Clients incomeClient = display.getClient();;
            if(incomeClient.getId() == 0){
                if(null != incomePerson  )
                {
                    em.getTransaction().begin();
                    em.persist(incomePerson);
                    em.getTransaction().commit();
                    
                    // insert chage
                    // new ChangeService().insertCreateChange(incomePerson);
                }

                incomeClient.setPerson(incomePerson);
                em.getTransaction().begin();
                em.persist(incomeClient);
                em.getTransaction().commit();
                
                // insert chage
                new ChangeService().insertCreateChange(incomeClient);
            }
            income.setClient(incomeClient);
            income.setAmountReceivable(display.getAmountReceivable());
            income.setAmountReceived(display.getAmountReceived());
            income.setDiscount(display.getDiscount());
            income.setType(display.getType());
            income.setDate(display.getDate());
            income.setPaymentType(display.getPaymentType());
            income.setDateDue(display.getDateDue());
            Services serv = (Services) em.createNamedQuery("Services.findById")
               .setParameter("id", display.getServiceIdnum()).getSingleResult();
            em.getTransaction().begin();
            income.setService(serv);
            income.setUser(display.getUser());
            em.getTransaction().commit();
            
            if(display.getParentIncomeId() > 0) {
                Incomes pIncome = (Incomes) em.createNamedQuery("Incomes.findById")
                   .setParameter("id", display.getParentIncomeId()).getSingleResult();
                income.setIncomeId(pIncome.getId());
                pIncome.setAmountReceivable(pIncome.getAmountReceivable() - income.getAmountReceived());
                em.getTransaction().begin();
                em.persist(pIncome);
                em.getTransaction().commit();
               
            }
            em.getTransaction().begin();
            em.persist(income);
            em.getTransaction().commit();
            
            // insert chage
            new ChangeService().insertCreateChange(income);
            
            // return
            return true;
        }
        catch(Exception xx){
            xx.printStackTrace();
            return false;
        }
         
     }
    
    public boolean updateEntryAsSynced(UploadResponseViewModel entry)
    {
        try{
            em.getTransaction().begin();
            Query q = em.createNamedQuery("Incomes.findById");
            q.setParameter("id", entry.getId());
            Incomes a = (Incomes)q.getSingleResult();
                
            a.setOnlineEntryId(entry.getOnlineEntryId()); // true
            em.merge(a);
            em.getTransaction().commit();
        
            return true;
        }
        catch(Exception exc){
            return false;
        }
    }
     
    public static <T> List<T> map(Class<T> type, List<Object[]> records){
        List<T> result = new LinkedList<>();
        for(Object[] record : records){
           result.add(map(type, record));
        }
        return result;
    }
     
    public static <T> T map(Class<T> type, Object[] tuple){
        List<Class<?>> tupleTypes = new ArrayList<>();
        for(Object field : tuple){
            if (null != field)
           tupleTypes.add(field.getClass());
            else
               tupleTypes.add(String.class);
        }
        try {
           Constructor<T> ctor = type.getConstructor(tupleTypes.toArray(new Class<?>[tuple.length]));
           return ctor.newInstance(tuple);
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }
    
    public void close(){
        em.close();
    }
}
