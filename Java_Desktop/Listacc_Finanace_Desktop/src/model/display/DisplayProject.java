/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.display;

import model.Departments;
import model.Projects;

/**
 *
 * @author Agozie
 */
public class DisplayProject extends Projects{

    public DisplayProject() {
    }

    public DisplayProject(Integer id) {
        super(id);
    }

    public DisplayProject(Integer id, String name, String description, String departmentName, Departments departmentID) {
        super(id, name);
        setDescription(description);
        setDepartmentName(departmentName);
        setDepartment(departmentID);
    }
    
    public DisplayProject(Integer id, String description, String name, Departments departmentID) {
        super(id, name);
        setDescription(description);
        setDepartment(departmentID);
    }
    
    private String DepartmentName;

    public String getDepartmentName() {
        return DepartmentName;
    }

    private void setDepartmentName(String DepartmentName) {
        this.DepartmentName = DepartmentName;
    }
    
    
}
