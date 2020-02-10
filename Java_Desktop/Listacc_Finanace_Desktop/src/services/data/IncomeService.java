/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.data;

import java.util.List;
import model.Clients;
import model.Incomes;
import model.Persons;
import model.Services;
import model.display.DisplayIncome;

/**
 *
 * @author Agozie
 */
public class IncomeService extends DataService {
//    (String serviceName, String clientName, int clientId,   Integer id, String date, double amountReceived, double discount, String paymentType, double amountReceivable, String dateDue) {
    public List<DisplayIncome> getAllIncomes()
    {
        return em.createQuery("SELECT new model.display.DisplayIncome(a.serviceId.name,a.clientId.businessName,a.clientId.id,a.id, a.date,a.amountReceived, a.discount, a.paymentType, a.amountReceivable, a.dateDue)FROM Incomes a ORDER BY a.id desc").getResultList();
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
                    new ChangeService().insertCreateChange(incomePerson);
                }

                incomeClient.setPersonId(incomePerson);
                em.getTransaction().begin();
                em.persist(incomeClient);
                em.getTransaction().commit();
                
                // insert chage
                new ChangeService().insertCreateChange(incomeClient);
            }
            income.setClientId(incomeClient);
            income.setAmountReceivable(display.getAmountReceivable());
            income.setAmountReceived(display.getAmountReceived());
            income.setDiscount(display.getDiscount());
            income.setType(display.getType());
            income.setDate(display.getDate());
            income.setPaymentType(display.getPaymentType());
            income.setDateDue(display.getDateDue());
            Services serv = (Services) em.createNamedQuery("Services.findById")
               .setParameter("id", display.getServiceIdnum()).getSingleResult();
            income.setServiceId(serv);
            income.setProjectId(serv.getProjectId());
            income.setUserId(display.getUserId());
            em.getTransaction().begin();
            em.persist(income);
            em.getTransaction().commit();
            
            // insert chage
            new ChangeService().insertCreateChange(income);
            
            // return
            return true;
        }
        catch(Exception xx){xx.printStackTrace();
            return false;
        }
         
     }
}
