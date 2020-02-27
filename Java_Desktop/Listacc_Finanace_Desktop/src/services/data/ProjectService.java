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
import model.Projects;

import model.display.DisplayProject;
import services.net.view_model.ProjectSyncItem;
import services.net.view_model.UploadResponseViewModel;

/**
 *
 * @author Agozie
 */
public class ProjectService extends DataService {
    
    public List<DisplayProject> getAllProjects()
    {
        return em.createQuery("SELECT new model.display.DisplayProject(a.id, a.name,a.description, a.department.name, a.department)FROM Projects a").getResultList();
    }
    
    public boolean projectNameExists(String name)
    {
        
        List<Projects> department = (List<Projects>) em.createQuery("SELECT q FROM Projects q  where upper(q.name)=:name")
            .setParameter("name",name.toUpperCase()).getResultList();
        
        return department.size() > 0;
    }
    
    public boolean projectNameExists(String name, int id)
    {
        
        List<Projects> department = (List<Projects>) em.createQuery("SELECT q FROM Projects q  where upper(q.name)=:name AND q.id !="+id)
            .setParameter("name",name.toUpperCase())
            //.setParameter(id, id)
                .getResultList();
        
        return department.size() > 0;
    }
    
    private boolean createProject(Projects project){
        try{
            em.getTransaction().begin();
            em.persist(project);
            em.getTransaction().commit();
            em.close();
            
            // insert chage
            new ChangeService().insertCreateChange(project);
        
            return true;
        }catch(Exception exc){
            return false;
        }
    }
    
    public boolean createProject( String name, String description, int departmentId ){
        try{
            Projects project = new Projects(0,name);
            project.setDescription(description);
            Departments dept = (Departments) em.createNamedQuery("Departments.findById")
            .setParameter("id", departmentId).getSingleResult();
            project.setDepartment(dept);
            return createProject(project);
        }catch(NoResultException ex){
            return false;
        }catch(Exception exc){
            exc.printStackTrace();
        }
        return  false;   
    }
    
    public Projects getProjectByOnlineEntryId(int onlineEntryId)
    {
        try{
            Query q = em.createNamedQuery("Projects.findByOnlineEntryId");
            q.setParameter("onlineEntryId", onlineEntryId);
            Projects a = (Projects)q.getSingleResult();
      
            return a;
        }catch(NoResultException ex){
            return null;
        }catch(Exception exc){
            exc.printStackTrace();
        }
        return null;
    }
    
    public ProjectSyncItem getProjectByChange(Changes ch)
    {
        try{
            Query q = em.createQuery("SELECT new services.net.view_model.ProjectSyncItem(c.id, c.name, c.description, c.department.id, c.department.onlineEntryId, c.onlineEntryId) "
                    + "FROM Projects c WHERE c.id = :id");
            q.setParameter("id", ch.getEntryId());
            ProjectSyncItem a = (ProjectSyncItem)q.getSingleResult();
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
    
    public boolean addDownloadedEntry(ProjectSyncItem item)
    {
        try{
            em.getTransaction().begin();
            
            Projects existingRecord = getProjectByOnlineEntryId(item.getId());
            Departments department = new DepartmentService().getDepartmentByOnlineEntryId(item.getDepartmentId());
            if(existingRecord == null){
                Projects newRecord = ProjectSyncItem.map(item, department);
                em.persist(newRecord);
            }
            else{
                existingRecord = ProjectSyncItem.map(existingRecord, item, item.getId(), department);
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
            Query q = em.createNamedQuery("Projects.findById");
            q.setParameter("id", entry.getId());
            Projects a = (Projects)q.getSingleResult();
                
            a.setOnlineEntryId(entry.getOnlineEntryId()); // true
            em.merge(a);
            em.getTransaction().commit();
        
            return true;
        }
        catch(Exception exc){
            return false;
        }
    }
    
    public boolean updateProject(int id, String name, String description, int departmentId){
        try{
            em.getTransaction().begin();
            Projects project = (Projects) em.createNamedQuery("Projects.findById").setParameter("id", id).getSingleResult();
            Departments dept = (Departments) em.createNamedQuery("Departments.findById").setParameter("id", departmentId).getSingleResult();
            project.setName(name);
            project.setDescription(description);
            project.setDepartment(dept);
            em.persist(project);
            em.getTransaction().commit();
            em.close();
            new ChangeService().insertUpdateChange(project);
            return true;
        }catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
    }
    
    public void close(){
        em.close();
    }
}
