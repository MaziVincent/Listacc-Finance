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
@Table(name = "AspNetUserLogins", catalog = "", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AspNetUserLogins.findAll", query = "SELECT a FROM AspNetUserLogins a"),
    @NamedQuery(name = "AspNetUserLogins.findByLoginProvider", query = "SELECT a FROM AspNetUserLogins a WHERE a.aspNetUserLoginsPK.loginProvider = :loginProvider"),
    @NamedQuery(name = "AspNetUserLogins.findByProviderKey", query = "SELECT a FROM AspNetUserLogins a WHERE a.aspNetUserLoginsPK.providerKey = :providerKey"),
    @NamedQuery(name = "AspNetUserLogins.findByProviderDisplayName", query = "SELECT a FROM AspNetUserLogins a WHERE a.providerDisplayName = :providerDisplayName")})
public class AspNetUserLogins implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AspNetUserLoginsPK aspNetUserLoginsPK;
    @Column(name = "ProviderDisplayName")
    private String providerDisplayName;
    @JoinColumn(name = "UserId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Users userId;

    public AspNetUserLogins() {
    }

    public AspNetUserLogins(AspNetUserLoginsPK aspNetUserLoginsPK) {
        this.aspNetUserLoginsPK = aspNetUserLoginsPK;
    }

    public AspNetUserLogins(String loginProvider, String providerKey) {
        this.aspNetUserLoginsPK = new AspNetUserLoginsPK(loginProvider, providerKey);
    }

    public AspNetUserLoginsPK getAspNetUserLoginsPK() {
        return aspNetUserLoginsPK;
    }

    public void setAspNetUserLoginsPK(AspNetUserLoginsPK aspNetUserLoginsPK) {
        this.aspNetUserLoginsPK = aspNetUserLoginsPK;
    }

    public String getProviderDisplayName() {
        return providerDisplayName;
    }

    public void setProviderDisplayName(String providerDisplayName) {
        this.providerDisplayName = providerDisplayName;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aspNetUserLoginsPK != null ? aspNetUserLoginsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AspNetUserLogins)) {
            return false;
        }
        AspNetUserLogins other = (AspNetUserLogins) object;
        if ((this.aspNetUserLoginsPK == null && other.aspNetUserLoginsPK != null) || (this.aspNetUserLoginsPK != null && !this.aspNetUserLoginsPK.equals(other.aspNetUserLoginsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.AspNetUserLogins[ aspNetUserLoginsPK=" + aspNetUserLoginsPK + " ]";
    }
    
}
