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
public class AspNetUserLoginsPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "LoginProvider")
    private String loginProvider;
    @Basic(optional = false)
    @Column(name = "ProviderKey")
    private String providerKey;

    public AspNetUserLoginsPK() {
    }

    public AspNetUserLoginsPK(String loginProvider, String providerKey) {
        this.loginProvider = loginProvider;
        this.providerKey = providerKey;
    }

    public String getLoginProvider() {
        return loginProvider;
    }

    public void setLoginProvider(String loginProvider) {
        this.loginProvider = loginProvider;
    }

    public String getProviderKey() {
        return providerKey;
    }

    public void setProviderKey(String providerKey) {
        this.providerKey = providerKey;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (loginProvider != null ? loginProvider.hashCode() : 0);
        hash += (providerKey != null ? providerKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AspNetUserLoginsPK)) {
            return false;
        }
        AspNetUserLoginsPK other = (AspNetUserLoginsPK) object;
        if ((this.loginProvider == null && other.loginProvider != null) || (this.loginProvider != null && !this.loginProvider.equals(other.loginProvider))) {
            return false;
        }
        if ((this.providerKey == null && other.providerKey != null) || (this.providerKey != null && !this.providerKey.equals(other.providerKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.AspNetUserLoginsPK[ loginProvider=" + loginProvider + ", providerKey=" + providerKey + " ]";
    }
    
}
