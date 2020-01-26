/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Agozie
 */
@Entity
@Table(name = "AspNetUserTokens")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AspNetUserTokens.findAll", query = "SELECT a FROM AspNetUserTokens a"),
    @NamedQuery(name = "AspNetUserTokens.findByUserId", query = "SELECT a FROM AspNetUserTokens a WHERE a.aspNetUserTokensPK.userId = :userId"),
    @NamedQuery(name = "AspNetUserTokens.findByLoginProvider", query = "SELECT a FROM AspNetUserTokens a WHERE a.aspNetUserTokensPK.loginProvider = :loginProvider"),
    @NamedQuery(name = "AspNetUserTokens.findByName", query = "SELECT a FROM AspNetUserTokens a WHERE a.aspNetUserTokensPK.name = :name"),
    @NamedQuery(name = "AspNetUserTokens.findByValue", query = "SELECT a FROM AspNetUserTokens a WHERE a.value = :value")})
public class AspNetUserTokens implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AspNetUserTokensPK aspNetUserTokensPK;
    @Column(name = "Value")
    private String value;
    @JoinColumn(name = "UserId", referencedColumnName = "Id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Users users;

    public AspNetUserTokens() {
    }

    public AspNetUserTokens(AspNetUserTokensPK aspNetUserTokensPK) {
        this.aspNetUserTokensPK = aspNetUserTokensPK;
    }

    public AspNetUserTokens(int userId, String loginProvider, String name) {
        this.aspNetUserTokensPK = new AspNetUserTokensPK(userId, loginProvider, name);
    }

    public AspNetUserTokensPK getAspNetUserTokensPK() {
        return aspNetUserTokensPK;
    }

    public void setAspNetUserTokensPK(AspNetUserTokensPK aspNetUserTokensPK) {
        this.aspNetUserTokensPK = aspNetUserTokensPK;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aspNetUserTokensPK != null ? aspNetUserTokensPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AspNetUserTokens)) {
            return false;
        }
        AspNetUserTokens other = (AspNetUserTokens) object;
        if ((this.aspNetUserTokensPK == null && other.aspNetUserTokensPK != null) || (this.aspNetUserTokensPK != null && !this.aspNetUserTokensPK.equals(other.aspNetUserTokensPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.AspNetUserTokens[ aspNetUserTokensPK=" + aspNetUserTokensPK + " ]";
    }
    
}
