/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.data;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import model.Persons;
import services.net.view_model.PersonSyncItem;

/**
 *
 * @author E-book
 */
public class PersonService extends DataService {
    
    public Persons getPersonByOnlineEntryId(int onlineEntryId)
    {
        try{
            Query q = em.createNamedQuery("Persons.findByOnlineEntryId");
            q.setParameter("onlineEntryId", onlineEntryId);
            Persons a = (Persons)q.getSingleResult();
      
            return a;
        }catch(NoResultException ex){
            return null;
        }catch(Exception exc){
            exc.printStackTrace();
        }
        return null;
    }
    
    public boolean addDownloadedEntry(PersonSyncItem item)
    {
        try{
            em.getTransaction().begin();
            
            Persons existingRecord = getPersonByOnlineEntryId(item.getId());
            if(existingRecord == null){
                Persons newRecord = PersonSyncItem.map(item);
                em.persist(newRecord);
            }
            else{
                existingRecord = PersonSyncItem.map(existingRecord, item, item.getId());
                em.merge(existingRecord);
            }
            
            em.getTransaction().commit();
        
            return true;
        }catch(Exception exc){
            return false;
        }
    }
    
    public void close(){
        em.close();
    }
}
