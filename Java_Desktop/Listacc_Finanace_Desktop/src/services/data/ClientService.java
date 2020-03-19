/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.data;

import helpers.DateHelper;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import model.Changes;
import model.Clients;
import model.Persons;
import model.display.DisplayClient;
import services.net.view_model.ClientSyncItem;
import services.net.view_model.PersonSyncItem;
import services.net.view_model.UploadResponseViewModel;
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
    
    public ClientSyncItem getClientByChange(Changes ch)
    {
        try{
            ClientSyncItem result = getClientInfoOnlyByChange(ch);
            if(result.getPersonId() != null)
                result = getClientAndPersonInfoByChange(ch);
            result.setChange(ch.getChanges());
            result.setChangeTimestamp(ch.getTimeStamp());
            result.setChangeUserOnlineEntryId(ch.getUser() != null ? ch.getUser().getOnlineEntryId(): null);
            return result;
        }catch(NoResultException ex){
            return null;
        }catch(Exception exc){
            exc.printStackTrace();
        }
        return null;
    }
    
    private ClientSyncItem getClientInfoOnlyByChange(Changes ch)
    {
        try{
            Query q = em.createQuery("Select new services.net.view_model.ClientSyncItem(a.id, a.businessName, "
                    + "a.phone, a.email, a.address, a.uId, a.uId2, a.amountReceivable, a.onlineEntryId, a.personId) "
                    + "FROM Clients a WHERE a.id = :id");
            q.setParameter("id", ch.getEntryId());
            ClientSyncItem result = (ClientSyncItem)q.getSingleResult();
            return result;
        }catch(NoResultException ex){
            return null;
        }catch(Exception exc){
            exc.printStackTrace();
        }
        return null;
    }
    
    private ClientSyncItem getClientAndPersonInfoByChange(Changes ch)
    {
        try{
            Query q = em.createQuery("Select new services.net.view_model.ClientSyncItem(a.id, a.businessName, "
                    + "a.phone, a.email, a.address, a.uId, a.uId2, a.amountReceivable, a.onlineEntryId, "
                    + "p.id, p.firstName, p.lastName, p.gender, p.dateOfBirth, p.onlineEntryId) "
                    + "FROM Clients a JOIN Persons p ON p.id = a.personId WHERE a.id = :id");
            q.setParameter("id", ch.getEntryId());
            ClientSyncItem result = (ClientSyncItem)q.getSingleResult();
            return result;
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
    
    public Clients getClientByOnlineEntryId(int onlineEntryId)
    {
        try{
            Query q = em.createNamedQuery("Clients.findByOnlineEntryId");
            q.setParameter("onlineEntryId", onlineEntryId);
            Clients a = (Clients)q.getSingleResult();
      
            return a;
        }catch(NoResultException ex){
            return null;
        }catch(Exception exc){
            exc.printStackTrace();
        }
        return null;
    }
    
    public boolean addDownloadedEntry(ClientSyncItem item)
    {
        try{
            em.getTransaction().begin();
            
            Clients existingRecord = getClientByOnlineEntryId(item.getId());
            Persons person = new PersonService().getPersonByOnlineEntryId(item.getPerson().getId());
            if(person == null){
                // modify date
                Date birthDate = DateHelper.StringToDate(item.getPerson().getDateOfBirth());
                item.getPerson().setDateOfBirth(birthDate.getTime() + "");
                
                // create person
                PersonService personService = new PersonService();
                PersonSyncItem temp = new PersonSyncItem(item.getPerson().getId(), item.getPerson().getFirstName(),
                item.getPerson().getLastName(), item.getPerson().getGender(), item.getPerson().getDateOfBirth());
                if(personService.addDownloadedEntry(temp))
                    person = personService.getPersonByOnlineEntryId(item.getPerson().getId());
            }
            if(existingRecord == null){
                Clients newRecord = ClientSyncItem.map(item, person);
                em.persist(newRecord);
            }
            else{
                existingRecord = ClientSyncItem.map(existingRecord, item, item.getId(), person);
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
            Query q = em.createNamedQuery("Clients.findById");
            q.setParameter("id", entry.getId());
            Clients a = (Clients)q.getSingleResult();
                
            a.setOnlineEntryId(entry.getOnlineEntryId()); // true
            em.merge(a);
            em.getTransaction().commit();
        
            return true;
        }
        catch(Exception exc){
            return false;
        }
    }
    
    public boolean updateClient(Clients client, int id){
        try{
            em.getTransaction().begin();
            Clients updateClient = (Clients) em.createNamedQuery("Clients.findById").setParameter("id", id).getSingleResult();


            if (client.getPerson() == null)
            updateClient.setPerson(null);
            else if(updateClient.getPerson() != null && client.getPerson() != null){
                Persons updatePerson = (Persons) em.createNamedQuery("Persons.findById").setParameter("id", updateClient.getPerson().getId()).getSingleResult();
                updatePerson.setFirstName(client.getPerson().getFirstName());
                updatePerson.setLastName(client.getPerson().getLastName());
                updatePerson.setDateOfBirth(client.getPerson().getDateOfBirth());
                updatePerson.setGender(client.getPerson().getGender());
            }
            else {
                Persons updatePerson = new Persons();
                updatePerson.setFirstName(client.getPerson().getFirstName());
                updatePerson.setLastName(client.getPerson().getLastName());
                updatePerson.setDateOfBirth(client.getPerson().getDateOfBirth());
                updatePerson.setGender(client.getPerson().getGender());
                em.persist(updatePerson);
               //em.getTransaction().commit();
                updateClient.setPerson(updatePerson);
            }
            updateClient.setAddress(client.getAddress());
            updateClient.setBusinessName(client.getBusinessName());
            updateClient.setEmail(client.getEmail());
            updateClient.setPhone(client.getPhone());
            updateClient.setAddress(client.getAddress());
            em.persist(updateClient);
            em.getTransaction().commit();
            em.close();

            // insert chage
            client.setId(id);
            new ChangeService().insertUpdateChange(client);
            
            return true;
        }catch(Exception exc){
            exc.printStackTrace();
            return false;
        } 
   }
    
    public void close(){
        em.close();
    }
}
