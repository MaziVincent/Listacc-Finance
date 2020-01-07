/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.data;

import java.util.List;

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
}
