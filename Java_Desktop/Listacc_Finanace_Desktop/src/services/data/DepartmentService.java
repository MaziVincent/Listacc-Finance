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
    
    public boolean createDepartment(Departments department){
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
    
    public List<Departments> getAllDepartments()
    {
         return em.createQuery("SELECT a FROM Departments a", Departments.class).getResultList();
    }
}
