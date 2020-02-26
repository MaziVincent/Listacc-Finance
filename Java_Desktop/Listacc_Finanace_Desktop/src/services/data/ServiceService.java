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
import model.Projects;
import model.Services;
import model.display.DisplayService;
import services.net.view_model.ServiceSyncItem;
import services.net.view_model.UploadResponseViewModel;

/**
 *
 * @author Agozie
 */
public class ServiceService extends DataService{
    
    public List<DisplayService> getAllServices()
    {
        return em.createQuery("SELECT new model.display.DisplayService(a.id, a.name, a.description, a.amount, a.project.name, a.project.id, a.fixedAmount)FROM Services a").getResultList();
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

    public boolean createService( String name, double amount,String description, int projectId, boolean fixedAmount ){
        try{
            Services service = new Services(0,amount, fixedAmount?1:0);
            service.setDescription(description);
            service.setName(name);
            
            Projects project = (Projects) em.createNamedQuery("Projects.findById")
            .setParameter("id", projectId).getSingleResult();
            service.setProject(project);
            
            return createService(service);
        }catch(NoResultException ex){
            return false;
        }catch(Exception exc){
            exc.printStackTrace();
        }
        
        return  false;   
     }
    
    public Services getServiceByOnlineEntryId(int onlineEntryId)
    {
        try{
            Query q = em.createNamedQuery("Services.findByOnlineEntryId");
            q.setParameter("onlineEntryId", onlineEntryId);
            Services a = (Services)q.getSingleResult();
            
            return a;
        }catch(NoResultException ex){
            return null;
        }catch(Exception exc){
            exc.printStackTrace();
        }
        return null;
    }
    
    public ServiceSyncItem getServiceByChange(Changes ch)
    {
        try{
            Query q = em.createQuery("SELECT new services.net.view_model.ServiceSyncItem(c.id, c.name, c.description, c.amount, c.onlineEntryId, c.fixedAmount, c.project.id, c.project.onlineEntryId) "
                    + "FROM Services c WHERE c.id = :id");
            q.setParameter("id", ch.getEntryId());
            ServiceSyncItem a = (ServiceSyncItem)q.getSingleResult();
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
    
    public boolean addDownloadedEntry(ServiceSyncItem item)
    {
        try{
            em.getTransaction().begin();
            
            Services existingRecord = getServiceByOnlineEntryId(item.getId());
            Projects project = new ProjectService().getProjectByOnlineEntryId(item.getProjectId());
            if(existingRecord == null){
                Services newRecord = ServiceSyncItem.map(item, project);
                em.persist(newRecord);
            }
            else{
                existingRecord = ServiceSyncItem.map(existingRecord, item, item.getId(), project);
                em.merge(existingRecord);
            }
            
            em.getTransaction().commit();
        
            return true;
        }catch(Exception exc){
            return false;
        }
    }
    
    public boolean updateEntryAsSynced(UploadResponseViewModel entry)
    {
        try{
            em.getTransaction().begin();
            Query q = em.createNamedQuery("Services.findById");
            q.setParameter("id", entry.getId());
            Services a = (Services)q.getSingleResult();
                
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
