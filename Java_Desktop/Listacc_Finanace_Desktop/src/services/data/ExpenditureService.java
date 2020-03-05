/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.data;

import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import model.Changes;
import model.Clients;
import model.CostCategories;
import model.Expenditures;
import model.Persons;
import model.Projects;
import model.Users;
import model.display.DisplayExpenditure;
import services.net.view_model.ExpenditureSyncItem;
import services.net.view_model.UploadResponseViewModel;

/**
 *
 * @author Agozie
 */
public class ExpenditureService extends DataService {
    public boolean createExpenditure(DisplayExpenditure ds){
        try{
            Expenditures exp = new Expenditures();
            exp.setAmount(ds.getAmount());
            exp.setDate(ds.getDate());
            exp.setDescription(ds.getDescription());
            Clients client;
            Users user  = (Users) em.createNamedQuery("Users.findById")
                .setParameter("id", ds.getUserId()).getSingleResult(); 
            CostCategories ccat  = (CostCategories) em.createNamedQuery("CostCategories.findById")
                .setParameter("id", ds.getCostCatId()).getSingleResult(); 
            Projects projectId  = (Projects) em.createNamedQuery("Projects.findById")
                .setParameter("id", ds.getPrjId()).getSingleResult(); 
            if(ds.getClient() == null || ds.getClientId() == 0)
            {
                Persons person = ds.getPerson();
                if(null != person)  
                {
                    em.getTransaction().begin();
                    em.persist(person);
                    em.getTransaction().commit();
                    
                    // insert chage
                    // new ChangeService().insertCreateChange(person);
                }
                client = ds.getClient();
                client.setPerson(person);
                em.getTransaction().begin();
                em.persist(client);
                em.getTransaction().commit();
                
                // insert chage
                new ChangeService().insertCreateChange(client);
            }
            else {
                client = (Clients) em.createNamedQuery("Clients.findById")
                    .setParameter("id", ds.getClient().getId()).getSingleResult();
            }
            exp.setClient(client);
            exp.setIssuer(user);
            exp.setCostCategory(ccat);
            exp.setProject(projectId);
            em.getTransaction().begin();
            em.persist(exp);
            em.getTransaction().commit();
            
            // insert chage
            new ChangeService().insertCreateChange(exp);
            
            // return
            return true;
        }catch(Exception ex){ex.printStackTrace();}
        return false;
    }
       
    public List<DisplayExpenditure> getAllExpenditures()
    {
        return em.createQuery("SELECT new model.display.DisplayExpenditure(a.costCategory.id,a.project.id,a.issuer.id,a.client.businessName, a.project.name, a.costCategory.name, a.client.id,a.amount, a.date)FROM Expenditures a ORDER BY a.id desc").getResultList();
    }
    
    public ExpenditureSyncItem getExpenditureByChange(Changes ch)
    {
        try{
            Query q = em.createQuery("SELECT new services.net.view_model.ExpenditureSyncItem(c.id, c.date, c.description, c.amount, c.onlineEntryId, c.client.id, c.client.onlineEntryId, c.costCategory.id, c.costCategory.onlineEntryId, c.project.id, c.project.onlineEntryId, c.issuer.id, c.issuer.onlineEntryId) "
                    + "FROM Expenditures c WHERE c.id = :id");
            q.setParameter("id", ch.getEntryId());
            ExpenditureSyncItem a = (ExpenditureSyncItem)q.getSingleResult();
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
    
    public boolean updateEntryAsSynced(UploadResponseViewModel entry)
    {
        try{
            em.getTransaction().begin();
            Query q = em.createNamedQuery("Expenditures.findById");
            q.setParameter("id", entry.getId());
            Expenditures a = (Expenditures)q.getSingleResult();
                
            a.setOnlineEntryId(entry.getOnlineEntryId()); // true
            em.merge(a);
            em.getTransaction().commit();
        
            return true;
        }
        catch(Exception exc){
            return false;
        }
    }
    
    public void close(){
        em.close();
    }
}
    
