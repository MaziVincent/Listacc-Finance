/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.data;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import model.Changes;
import model.Departments;
import services.net.view_model.DepartmentUploadItem;
import services.net.view_model.OnlineEntryMapping;
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
    
    public List<DepartmentUploadItem> getUnpushedChanges(List<Changes> departmentChanges){    
        try{
            List<Integer> ids = new ArrayList<>();
            for(Changes c: departmentChanges){
                ids.add(c.getEntryId());
            }
            Query query = em.createQuery( "SELECT new services.net.view_model.DepartmentUploadItem(s.id, s.onlineEntryId, s.name)"
                    + " FROM Departments s WHERE s.id IN :ids" );
            query.setParameter("ids", ids);
            List<DepartmentUploadItem> changeList = (List<DepartmentUploadItem>)query.getResultList();

            return changeList;
        }
        catch(Exception exc){exc.getMessage();}
        return null;
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
    
    public void close(){
        em.close();
    }
}
