/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.data;

import java.util.List;
import model.Clients;
import model.CostCategories;
import model.Expenditures;
import model.Persons;
import model.Projects;
import model.Users;
import model.display.DisplayExpenditure;

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
            if(ds.getClientId() == 0)
            {
                Persons person = ds.getPerson();
                if(null != person)  
                {
                 em.getTransaction().begin();
                    em.persist(person);
                    em.getTransaction().commit();
                }
                client = ds.getClient();
                client.setPersonId(person);
                em.getTransaction().begin();
                    em.persist(client);
                    em.getTransaction().commit();
            }else
            {
               client = (Clients) em.createNamedQuery("Clients.findById")
                        .setParameter("id", ds.getClientId()).getSingleResult();
            }
            exp.setClientId(client);
            exp.setIssuerId(user);
            exp.setCostCategoryId(ccat);
            exp.setProjectId(projectId);
            em.getTransaction().begin();
                    em.persist(exp);
                    em.getTransaction().commit();
            return true;
        }catch(Exception ex){ex.printStackTrace();}
        return false;
    }
    
    
        
        public List<DisplayExpenditure> getAllExpenditures()
        {
             return em.createQuery("SELECT new model.display.DisplayExpenditure(a.costCategoryId.id,a.projectId.id,a.issuerId.id,a.clientId.businessName, a.projectId.name, a.costCategoryId.name, a.clientId.id,a.amount, a.date)FROM Expenditures a ORDER BY a.id desc").getResultList();
        }
    }
    
