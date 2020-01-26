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
import model.display.DisplayClient;
/**
 *
 * @author Agozie
 */
public class ClientService extends DataService {
    
     public List<DisplayClient> getAllClients()
    {       
         Query q =  em.createNativeQuery("SELECT a.id, a.phone, a.businessName, a.address, a.email, p.firstName, p.lastName, a.uId FROM Clients a LEFT JOIN Persons p ON p.id = a.personId");
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
}
