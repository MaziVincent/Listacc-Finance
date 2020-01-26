/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.data;

import java.util.List;
import model.Departments;
import model.Users;
import model.display.DisplayUser;

/**
 *
 * @author Agozie
 */
public class UserService extends DataService {
    public List<DisplayUser> getAllUsers()
    {
        
         return em.createQuery("SELECT new model.display.DisplayUser(a.id, a.departmentId.id, a.personId.id, a.personId.lastName, a.personId.firstName, a.phoneNumber, a.email,a.discriminator, a.departmentId.name) FROM Users a").getResultList();
    }
    
    public Users getUserById(int id)
    {
        try{
             Users user = (Users) em.createNamedQuery("Users.findById")
            .setParameter("id", id).getSingleResult();
             return user;
        }catch(Exception ex){return null;}
    }
}