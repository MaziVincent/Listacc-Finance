/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.net.view_model;

import java.util.List;
import model.Departments;

/**
 *
 * @author E-book
 */
public class ChangeUpload<T> {
    private String token;
    private List<T> departmentList;
    
    public ChangeUpload(String token, List<T> departmentList){
        setToken(token);
        setDepartmentList(departmentList);
    }
    
    public String getToken() {
        return token;
    }

    public final void setToken(String token) {
        this.token = token;
    }
    
    public List<T> getDepartmentList() {
        return departmentList;
    } 

    public final void setDepartmentList(List<T> departmentList) {
        this.departmentList = departmentList;
    }
}
