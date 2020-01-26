/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.display;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import model.Clients;
import model.Persons;
@SqlResultSetMapping(
        name = "ClientMapping",
        entities = {
            @EntityResult(
                    entityClass = DisplayClient.class,
                    fields = {
                        @FieldResult(name = "id", column = "id"),
                        @FieldResult(name = "phone", column = "phone"),
                        @FieldResult(name = "businessName", column = "businessName"),
                        @FieldResult(name = "address", column = "address"),
                        @FieldResult(name = "email", column = "email"),
                        @FieldResult(name = "firstName", column = "firstName"),
                        @FieldResult(name = "lastName", column = "lastName"),
                        @FieldResult(name = "uId", column = "uId")
                    })})
/**
 *
 * @author Agozie
 */
@Entity

public class DisplayClient implements Serializable 
{
     @Id
    @Column(name = "Id")
    private Integer id;
    @Column(name = "BusinessName")
    private String businessName;
    @Column(name = "Phone")
    private String phone;
    @Column(name = "Email")
    private String email;
    @Column(name = "Address")
    private String address;
    @Column(name = "uId")
    private String uId;
     @Column(name = "firstName")
    private String firstName;
      @Column(name = "lastName")
    private String lastName;
   

    public DisplayClient() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUId() {
        return uId;
    }

    public void setUId(String uid) {
        this.uId = uid;
    }

    public DisplayClient(Integer id, String phone, String businessName, String address, String email, String  firstName, String lastName, String uid) {
        this.id = id;
        this.phone = phone;
        this.businessName = businessName;
        this.address = address;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.uId = uid;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int Id) {
        this.id = Id;
    }
        
    public String getName() {
        if(null == businessName)
            return getFirstName()+ " " + getLastName();
        else 
            return getBusinessName();
    }
    String name;
    public void setName(String name) {
        this.name = name;
    }
}
