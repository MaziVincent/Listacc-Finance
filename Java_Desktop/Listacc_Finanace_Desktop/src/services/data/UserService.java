/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.data;

import helpers.DateHelper;
import java.util.Date;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import model.Departments;
import model.Persons;
import model.Users;
import model.display.DisplayUser;
import services.net.view_model.PersonSyncItem;
import services.net.view_model.UserSyncItem;

/**
 *
 * @author Agozie
 */
public class UserService extends DataService {
    public List<DisplayUser> getAllDisplayUsers()
    {
        return em.createQuery("SELECT new model.display.DisplayUser(a.id, a.department.id, a.person.id, a.person.lastName, a.person.firstName, a.phoneNumber, a.email,a.discriminator, a.department.name) FROM Users a").getResultList();
    }
    
    public List<Users> getAllUsers()
    {
         return em.createQuery("SELECT a FROM Users a", Users.class).getResultList();
    }
    
    public Users getUserById(int id)
    {
        try{
             Users user = (Users) em.createNamedQuery("Users.findById")
            .setParameter("id", id).getSingleResult();
             return user;
        }catch(Exception ex){return null;}
    }
    
    public Users getUserByEmail(String email)
    {
        try{
            Users user = (Users) em.createNamedQuery("Users.findByEmail")
                .setParameter("email", email.toUpperCase()).getSingleResult();
            return user;
        }
        catch(Exception ex){return null;}
    }
    
    public Users getUserByOnlineEntryId(int onlineEntryId)
    {
        try{
            Query q = em.createNamedQuery("Users.findByOnlineEntryId");
            q.setParameter("onlineEntryId", onlineEntryId);
            Users a = (Users)q.getSingleResult();
      
            return a;
        }catch(NoResultException ex){
            return null;
        }catch(Exception exc){
            exc.printStackTrace();
        }
        return null;
    }
    
    public boolean addDownloadedEntry(UserSyncItem item)
    {
        try{
            em.getTransaction().begin();
            
            Users existingRecord = getUserByOnlineEntryId(item.getId());
            Departments department = new DepartmentService().getDepartmentByOnlineEntryId(item.getDepartmentId());
            Persons person = new PersonService().getPersonByOnlineEntryId(item.getPersonId());
            if(person == null){
                // modify date
                Date birthDate = DateHelper.StringToDate(item.getPerson().getDateOfBirth());
                item.getPerson().setDateOfBirth(birthDate.getTime() + "");
                
                // create person
                PersonService personService = new PersonService();
                PersonSyncItem temp = new PersonSyncItem(item.getPersonId(), item.getPerson().getFirstName(),
                item.getPerson().getLastName(), item.getPerson().getGender(), item.getPerson().getDateOfBirth());
                if(personService.addDownloadedEntry(temp))
                    person = personService.getPersonByOnlineEntryId(item.getPerson().getId());
            }
            
            if(existingRecord == null){
                Users newRecord = UserSyncItem.map(item, department, person);
                em.persist(newRecord);
            }
            else{
                existingRecord = UserSyncItem.map(existingRecord, item, item.getId(), department, person);
                em.merge(existingRecord);
            }
            
            em.getTransaction().commit();
        
            return true;
        }catch(Exception exc){
            exc.printStackTrace();
            System.out.println(exc.getMessage());
            return false;
        }
    }
    
    
    public void close(){
        em.close();
    }
}
