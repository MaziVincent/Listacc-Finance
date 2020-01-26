/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.data;

import java.util.List;
import model.Departments;
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
}
