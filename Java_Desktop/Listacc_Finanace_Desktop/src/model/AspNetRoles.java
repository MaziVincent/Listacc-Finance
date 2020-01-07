/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Agozie
 */
@Entity
@Table(name = "AspNetRoles", catalog = "", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AspNetRoles.findAll", query = "SELECT a FROM AspNetRoles a"),
    @NamedQuery(name = "AspNetRoles.findById", query = "SELECT a FROM AspNetRoles a WHERE a.id = :id"),
    @NamedQuery(name = "AspNetRoles.findByName", query = "SELECT a FROM AspNetRoles a WHERE a.name = :name"),
    @NamedQuery(name = "AspNetRoles.findByNormalizedName", query = "SELECT a FROM AspNetRoles a WHERE a.normalizedName = :normalizedName"),
    @NamedQuery(name = "AspNetRoles.findByConcurrencyStamp", query = "SELECT a FROM AspNetRoles a WHERE a.concurrencyStamp = :concurrencyStamp")})
public class AspNetRoles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Column(name = "Name")
    private String name;
    @Column(name = "NormalizedName")
    private String normalizedName;
    @Column(name = "ConcurrencyStamp")
    private String concurrencyStamp;
    @JoinTable(name = "AspNetUserRoles", joinColumns = {
        @JoinColumn(name = "RoleId1", referencedColumnName = "Id"),
        @JoinColumn(name = "RoleId", referencedColumnName = "Id")}, inverseJoinColumns = {
        @JoinColumn(name = "UserId1", referencedColumnName = "Id"),
        @JoinColumn(name = "UserId", referencedColumnName = "Id")})
    @ManyToMany
    private Collection<Users> usersCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleId")
    private Collection<AspNetRoleClaims> aspNetRoleClaimsCollection;

    public AspNetRoles() {
    }

    public AspNetRoles(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public String getConcurrencyStamp() {
        return concurrencyStamp;
    }

    public void setConcurrencyStamp(String concurrencyStamp) {
        this.concurrencyStamp = concurrencyStamp;
    }

    @XmlTransient
    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }

    @XmlTransient
    public Collection<AspNetRoleClaims> getAspNetRoleClaimsCollection() {
        return aspNetRoleClaimsCollection;
    }

    public void setAspNetRoleClaimsCollection(Collection<AspNetRoleClaims> aspNetRoleClaimsCollection) {
        this.aspNetRoleClaimsCollection = aspNetRoleClaimsCollection;
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
        if (!(object instanceof AspNetRoles)) {
            return false;
        }
        AspNetRoles other = (AspNetRoles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.AspNetRoles[ id=" + id + " ]";
    }
    
}
