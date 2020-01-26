/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "AspNetUserClaims")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AspNetUserClaims.findAll", query = "SELECT a FROM AspNetUserClaims a"),
    @NamedQuery(name = "AspNetUserClaims.findById", query = "SELECT a FROM AspNetUserClaims a WHERE a.id = :id"),
    @NamedQuery(name = "AspNetUserClaims.findByClaimType", query = "SELECT a FROM AspNetUserClaims a WHERE a.claimType = :claimType"),
    @NamedQuery(name = "AspNetUserClaims.findByClaimValue", query = "SELECT a FROM AspNetUserClaims a WHERE a.claimValue = :claimValue")})
public class AspNetUserClaims implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Column(name = "ClaimType")
    private String claimType;
    @Column(name = "ClaimValue")
    private String claimValue;
    @JoinColumn(name = "UserId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Users userId;

    public AspNetUserClaims() {
    }

    public AspNetUserClaims(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public String getClaimValue() {
        return claimValue;
    }

    public void setClaimValue(String claimValue) {
        this.claimValue = claimValue;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AspNetUserClaims)) {
            return false;
        }
        AspNetUserClaims other = (AspNetUserClaims) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.AspNetUserClaims[ id=" + id + " ]";
    }
    
}
