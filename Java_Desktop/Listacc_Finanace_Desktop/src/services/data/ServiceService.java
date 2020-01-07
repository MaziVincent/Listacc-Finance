/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.data;

import java.util.List;
import model.display.DisplayService;

/**
 *
 * @author Agozie
 */
public class ServiceService extends DataService{
    
    public List<DisplayService> getAllServices()
    {
         return em.createQuery("SELECT new model.display.DisplayService(a.id, a.name, a.description, a.amount, a.projectId.name, a.projectId.id)FROM Services a").getResultList();
    }
}
