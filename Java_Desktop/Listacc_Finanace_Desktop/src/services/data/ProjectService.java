/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.data;

import java.util.List;
import javax.persistence.NoResultException;
import model.Departments;
import model.Projects;

import model.display.DisplayProject;

/**
 *
 * @author Agozie
 */
public class ProjectService extends DataService {
    
    public List<DisplayProject> getAllProjects()
    {
        return em.createQuery("SELECT new model.display.DisplayProject(a.id, a.name,a.description, a.departmentId.name, a.departmentId)FROM Projects a").getResultList();
    }
    
    public boolean projectNameExists(String name)
    {
        
        List<Projects> department = (List<Projects>) em.createQuery("SELECT q FROM Projects q  where upper(q.name)=:name")
            .setParameter("name",name.toUpperCase()).getResultList();
        
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
            project.setDepartmentId(dept);
            return createProject(project);
        }catch(NoResultException ex){
            return false;
        }catch(Exception exc){
            exc.printStackTrace();
        }
        return  false;   
    }
}
