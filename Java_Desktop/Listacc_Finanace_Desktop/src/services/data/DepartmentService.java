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
import model.Departments;
import services.net.view_model.DepartmentSyncItem;
import services.net.view_model.OnlineEntryMapping;
import services.net.view_model.UploadResponseViewModel;
/**
 *
 * @author Agozie
 */ 
public class DepartmentService extends DataService {
    
    private boolean createDepartment(Departments department){
        try{
            em.getTransaction().begin();
            em.persist(department);
            em.getTransaction().commit();
            em.close();
               
            // insert chage
            new ChangeService().insertCreateChange(department);
            
            // return
            return true;
        }catch(Exception exc){
            return false;
        }
    }
    
    public boolean createDepartment(String name){
        Departments department = new Departments(0,name);
        return createDepartment(department);
    }
    
    public List<Departments> getAllDepartments()
    {
        return em.createQuery("SELECT a FROM Departments a", Departments.class).getResultList();
    }
    
    public boolean departmentNameExists(String name)
    {
        
        List<Departments> department = (List<Departments>) em.createQuery("SELECT q FROM Departments q  where upper(q.name)=:name")
            .setParameter("name",name.toUpperCase()).getResultList();
        
        return department.size() > 0;
    }
    
    public boolean updateNewDepartments(List<OnlineEntryMapping> mapping)
    {
        try{
            em.getTransaction().begin();
            Query q = em.createNamedQuery("Departments.findById");
            for(OnlineEntryMapping entry: mapping){
                // get entry
                q.setParameter("id", entry.getId());
                List<Departments> a = (List<Departments>)q.getResultList();
                
                // modify entry
                if(a.size() > 0 ){
                    a.get(0).setOnlineEntryId(entry.getOnlineEntryId());
                    // a.get(0).setPushed(PushedStatus.True.getValue()); // true
                    em.merge(a.get(0));
                }
            }
            
            em.getTransaction().commit();
        
            return true;
        }
        catch(Exception exc){
            return false;
        }
    }
    
    public boolean updateEntryAsSynced(UploadResponseViewModel entry)
    {
        try{
            em.getTransaction().begin();
            Query q = em.createNamedQuery("Departments.findById");
            q.setParameter("id", entry.getId());
            Departments a = (Departments)q.getSingleResult();
                
            a.setOnlineEntryId(entry.getOnlineEntryId()); // true
            em.merge(a);
            em.getTransaction().commit();
        
            return true;
        }
        catch(Exception exc){
            return false;
        }
    }
    
    public Departments getDepartmentByOnlineEntryId(int onlineEntryId)
    {
        try{
            Query q = em.createNamedQuery("Departments.findByOnlineEntryId");
            q.setParameter("onlineEntryId", onlineEntryId);
            Departments a = (Departments)q.getSingleResult();
      
            return a;
        }catch(NoResultException ex){
            return null;
        }catch(Exception exc){
            exc.printStackTrace();
        }
        return null;
    }
    
    public DepartmentSyncItem getDepartmentByChange(Changes ch)
    {
        try{
            Query q = em.createQuery("SELECT new services.net.view_model.DepartmentSyncItem(c.id, c.onlineEntryId, c.name) "
                    + "FROM Departments c WHERE c.id = :id");
            q.setParameter("id", ch.getEntryId());
            DepartmentSyncItem a = (DepartmentSyncItem)q.getSingleResult();
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
    
    public boolean addDownloadedEntry(DepartmentSyncItem item)
    {
        try{
            em.getTransaction().begin();
            
            Departments existingRecord = getDepartmentByOnlineEntryId(item.getId());
            if(existingRecord == null){
                Departments newRecord = DepartmentSyncItem.map(item);
                em.persist(newRecord);
            }
            else{
                existingRecord = DepartmentSyncItem.map(existingRecord, item, item.getId());
                em.merge(existingRecord);
            }
            
            em.getTransaction().commit();
        
            return true;
        }catch(Exception exc){
            exc.printStackTrace();
            exc.getMessage();
            return false;
        }
    }
    
    public void close(){
        em.close();
    }
}
