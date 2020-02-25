/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.SqlResultSetMapping;
import model.Clients;
import model.Persons;
import model.display.DisplayClient;
/**
 *
 * @author Agozie
 */
public class ClientService extends DataService {
    
     public List<DisplayClient> getAllClients()
    {       
         Query q =  em.createNativeQuery("SELECT a.id, a.phone, a.businessName, a.address, a.email, p.firstName, p.lastName, a.uId, p.dateOfBirth, p.gender FROM Clients a LEFT JOIN Persons p ON p.id = a.personId");
        // return em.createQuery("SELECT DISTINCT c FROM Clients c LEFT JOIN c.personId p").getResultList();                                                                                                                                                 
        @SuppressWarnings("unchecked")
       List<Object[]> list = q.getResultList();
        
        return map(DisplayClient.class, list);
    }
     
     public Clients getClientById(int id)
     {
          try{
            Clients a = (Clients) em.createNamedQuery("Clients.findById").
            setParameter("id", id).getSingleResult();
            return a;
        }catch(NoResultException ex){
            return null; 
        }catch(Exception exc){
            exc.printStackTrace();
        }
        return null; 
     }
     
  public static <T> List<T> map(Class<T> type, List<Object[]> records){
   List<T> result = new LinkedList<>();
   for(Object[] record : records){
      result.add(map(type, record));
   }
   return result;
}
     
   public static <T> T map(Class<T> type, Object[] tuple){
   List<Class<?>> tupleTypes = new ArrayList<>();
   for(Object field : tuple){
       if (null != field)
      tupleTypes.add(field.getClass());
       else
          tupleTypes.add(String.class);
   }
   try {
      Constructor<T> ctor = type.getConstructor(tupleTypes.toArray(new Class<?>[tuple.length]));
      return ctor.newInstance(tuple);
   } catch (Exception e) {
      throw new RuntimeException(e);
   }
 }
   
   public boolean updateClient(Clients client, int id){
      try{
          em.getTransaction().begin();
          Clients updateClient = (Clients) em.createNamedQuery("Clients.findById").setParameter("id", id).getSingleResult();
          
          
          if (client.getPersonId() == null)
          updateClient.setPersonId(null);
          else if(updateClient.getPersonId() != null && client.getPersonId() != null){
              Persons updatePerson = (Persons) em.createNamedQuery("Persons.findById").setParameter("id", updateClient.getPersonId().getId()).getSingleResult();
              updatePerson.setFirstName(client.getPersonId().getFirstName());
              updatePerson.setLastName(client.getPersonId().getLastName());
              updatePerson.setDateOfBirth(client.getPersonId().getDateOfBirth());
              updatePerson.setGender(client.getPersonId().getGender());
          }
          else {
              Persons updatePerson = new Persons();
              updatePerson.setFirstName(client.getPersonId().getFirstName());
              updatePerson.setLastName(client.getPersonId().getLastName());
              updatePerson.setDateOfBirth(client.getPersonId().getDateOfBirth());
              updatePerson.setGender(client.getPersonId().getGender());
              em.persist(updatePerson);
             //em.getTransaction().commit();
              updateClient.setPersonId(updatePerson);
          }
          updateClient.setAddress(client.getAddress());
          updateClient.setBusinessName(client.getBusinessName());
          updateClient.setEmail(client.getEmail());
          updateClient.setPhone(client.getPhone());
          updateClient.setAddress(client.getAddress());
          em.persist(updateClient);
          em.getTransaction().commit();
          em.close();
          
          return true;
      }catch(Exception exc){
          exc.printStackTrace();
      return false;} 
   }
}
