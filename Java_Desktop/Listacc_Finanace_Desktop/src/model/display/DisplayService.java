/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.display;

import model.Projects;
import model.Services;

/**
 *
 * @author Agozie
 */
public class DisplayService extends Services {
    
    private String ProjectName;
    private int projectsId;

    public int getProjectsId() {
        return projectsId;
    }

    public void setProjectsId(int projectsId) {
        this.projectsId = projectsId;
    }

    public DisplayService() {
    }

    public DisplayService(Integer id) {
        super(id);
    }

    public DisplayService(Integer id, String name, String description, double amount, String projectName, int projectId, int fixedAmount ) {
        super(id, amount, fixedAmount);
        this.setDescription(description);
        this.setName(name);
        this.setProjectsId(projectId);
        this.setProjectName(projectName);
        
        
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String ProjectName) {
        this.ProjectName = ProjectName;
    }
    
}
