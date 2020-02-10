/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.data;

import java.util.List;
import javax.persistence.NoResultException;
import model.Projects;
import model.Services;
import model.display.DisplayService;

/**
 *
 * @author Agozie
 */
public class ServiceService extends DataService{
    
    public List<DisplayService> getAllServices()
    {
<<<<<<< HEAD
        return em.createQuery("SELECT new model.display.DisplayService(a.id, a.name, a.description, a.amount, a.projectId.name, a.projectId.id)FROM Services a").getResultList();
=======
         return em.createQuery("SELECT new model.display.DisplayService(a.id, a.name, a.description, a.amount, a.projectId.name, a.projectId.id, a.fixedAmount)FROM Services a").getResultList();
>>>>>>> 23a1292ba37f3f716adde80a08c8e5ed6b57e078
    }
    
    public boolean serviceNameExists(String name)
    {
        
        List<Services> department = (List<Services>) em.createQuery("SELECT q FROM Services q  where upper(q.name)=:name")
            .setParameter("name",name.toUpperCase()).getResultList();
        
        return department.size() > 0;
    }
    
    private boolean createService(Services service){
       try{
            em.getTransaction().begin();
            em.persist(service);
            em.getTransaction().commit();
            em.close();
            
            // insert chage
            new ChangeService().insertCreateChange(service);
        
            return true;
        }catch(Exception exc){
            return false;
        }
    }
<<<<<<< HEAD
     
    public boolean createService( String name, double amount,String description, int projectId ){
        try{
            Services service = new Services(0,amount);
            service.setDescription(description);
            service.setName(name);
            
=======
     public boolean createService( String name, double amount,String description, int projectId, boolean fixedAmount ){
         try{
             Services service = new Services(0,amount, fixedAmount?1:0);
                service.setDescription(description);
                service.setName(name);
>>>>>>> 23a1292ba37f3f716adde80a08c8e5ed6b57e078
            Projects project = (Projects) em.createNamedQuery("Projects.findById")
            .setParameter("id", projectId).getSingleResult();
            service.setProjectId(project);
            
            return createService(service);
        }catch(NoResultException ex){
            return false;
        }catch(Exception exc){
            exc.printStackTrace();
        }
        
        return  false;   
     }
}
