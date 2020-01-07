/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Agozie
 */
@Embeddable
public class AspNetUserTokensPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "UserId")
    private int userId;
    @Basic(optional = false)
    @Column(name = "LoginProvider")
    private String loginProvider;
    @Basic(optional = false)
    @Column(name = "Name")
    private String name;

    public AspNetUserTokensPK() {
    }

    public AspNetUserTokensPK(int userId, String loginProvider, String name) {
        this.userId = userId;
        this.loginProvider = loginProvider;
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLoginProvider() {
        return loginProvider;
    }

    public void setLoginProvider(String loginProvider) {
        this.loginProvider = loginProvider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userId;
        hash += (loginProvider != null ? loginProvider.hashCode() : 0);
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AspNetUserTokensPK)) {
            return false;
        }
        AspNetUserTokensPK other = (AspNetUserTokensPK) object;
        if (this.userId != other.userId) {
            return false;
        }
        if ((this.loginProvider == null && other.loginProvider != null) || (this.loginProvider != null && !this.loginProvider.equals(other.loginProvider))) {
            return false;
        }
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.AspNetUserTokensPK[ userId=" + userId + ", loginProvider=" + loginProvider + ", name=" + name + " ]";
    }
    
}
