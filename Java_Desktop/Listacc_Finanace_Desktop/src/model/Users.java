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
@Table(name = "Users", catalog = "", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findById", query = "SELECT u FROM Users u WHERE u.id = :id"),
    @NamedQuery(name = "Users.findByUserName", query = "SELECT u FROM Users u WHERE u.userName = :userName"),
    @NamedQuery(name = "Users.findByNormalizedUserName", query = "SELECT u FROM Users u WHERE u.normalizedUserName = :normalizedUserName"),
    @NamedQuery(name = "Users.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email"),
    @NamedQuery(name = "Users.findByNormalizedEmail", query = "SELECT u FROM Users u WHERE u.normalizedEmail = :normalizedEmail"),
    @NamedQuery(name = "Users.findByEmailConfirmed", query = "SELECT u FROM Users u WHERE u.emailConfirmed = :emailConfirmed"),
    @NamedQuery(name = "Users.findByPasswordHash", query = "SELECT u FROM Users u WHERE u.passwordHash = :passwordHash"),
    @NamedQuery(name = "Users.findBySecurityStamp", query = "SELECT u FROM Users u WHERE u.securityStamp = :securityStamp"),
    @NamedQuery(name = "Users.findByConcurrencyStamp", query = "SELECT u FROM Users u WHERE u.concurrencyStamp = :concurrencyStamp"),
    @NamedQuery(name = "Users.findByPhoneNumber", query = "SELECT u FROM Users u WHERE u.phoneNumber = :phoneNumber"),
    @NamedQuery(name = "Users.findByPhoneNumberConfirmed", query = "SELECT u FROM Users u WHERE u.phoneNumberConfirmed = :phoneNumberConfirmed"),
    @NamedQuery(name = "Users.findByTwoFactorEnabled", query = "SELECT u FROM Users u WHERE u.twoFactorEnabled = :twoFactorEnabled"),
    @NamedQuery(name = "Users.findByLockoutEnd", query = "SELECT u FROM Users u WHERE u.lockoutEnd = :lockoutEnd"),
    @NamedQuery(name = "Users.findByLockoutEnabled", query = "SELECT u FROM Users u WHERE u.lockoutEnabled = :lockoutEnabled"),
    @NamedQuery(name = "Users.findByAccessFailedCount", query = "SELECT u FROM Users u WHERE u.accessFailedCount = :accessFailedCount"),
    @NamedQuery(name = "Users.findByPhone", query = "SELECT u FROM Users u WHERE u.phone = :phone"),
    @NamedQuery(name = "Users.findByAddress", query = "SELECT u FROM Users u WHERE u.address = :address"),
    @NamedQuery(name = "Users.findByPassword", query = "SELECT u FROM Users u WHERE u.password = :password")})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Column(name = "UserName")
    private String userName;
    @Column(name = "NormalizedUserName")
    private String normalizedUserName;
    @Column(name = "Email")
    private String email;
    @Column(name = "NormalizedEmail")
    private String normalizedEmail;
    @Basic(optional = false)
    @Column(name = "EmailConfirmed")
    private int emailConfirmed;
    @Column(name = "PasswordHash")
    private String passwordHash;
    @Column(name = "SecurityStamp")
    private String securityStamp;
    @Column(name = "ConcurrencyStamp")
    private String concurrencyStamp;
    @Column(name = "PhoneNumber")
    private String phoneNumber;
    @Basic(optional = false)
    @Column(name = "PhoneNumberConfirmed")
    private int phoneNumberConfirmed;
    @Basic(optional = false)
    @Column(name = "TwoFactorEnabled")
    private int twoFactorEnabled;
    @Column(name = "LockoutEnd")
    private String lockoutEnd;
    @Basic(optional = false)
    @Column(name = "LockoutEnabled")
    private int lockoutEnabled;
    @Basic(optional = false)
    @Column(name = "AccessFailedCount")
    private int accessFailedCount;
    @Basic(optional = false)
    @Column(name = "Phone")
    private int phone;
    @Column(name = "Address")
    private String address;
    @Column(name = "Password")
    private String password;
    @ManyToMany(mappedBy = "usersCollection")
    private Collection<AspNetRoles> aspNetRolesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userID")
    private Collection<Incomes> incomesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<AspNetUserLogins> aspNetUserLoginsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "users")
    private Collection<AspNetUserTokens> aspNetUserTokensCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<AspNetUserClaims> aspNetUserClaimsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "issuerId")
    private Collection<Expenditures> expendituresCollection;

    public Users() {
    }

    public Users(Integer id) {
        this.id = id;
    }

    public Users(Integer id, int emailConfirmed, int phoneNumberConfirmed, int twoFactorEnabled, int lockoutEnabled, int accessFailedCount, int phone) {
        this.id = id;
        this.emailConfirmed = emailConfirmed;
        this.phoneNumberConfirmed = phoneNumberConfirmed;
        this.twoFactorEnabled = twoFactorEnabled;
        this.lockoutEnabled = lockoutEnabled;
        this.accessFailedCount = accessFailedCount;
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNormalizedUserName() {
        return normalizedUserName;
    }

    public void setNormalizedUserName(String normalizedUserName) {
        this.normalizedUserName = normalizedUserName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNormalizedEmail() {
        return normalizedEmail;
    }

    public void setNormalizedEmail(String normalizedEmail) {
        this.normalizedEmail = normalizedEmail;
    }

    public int getEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(int emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSecurityStamp() {
        return securityStamp;
    }

    public void setSecurityStamp(String securityStamp) {
        this.securityStamp = securityStamp;
    }

    public String getConcurrencyStamp() {
        return concurrencyStamp;
    }

    public void setConcurrencyStamp(String concurrencyStamp) {
        this.concurrencyStamp = concurrencyStamp;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPhoneNumberConfirmed() {
        return phoneNumberConfirmed;
    }

    public void setPhoneNumberConfirmed(int phoneNumberConfirmed) {
        this.phoneNumberConfirmed = phoneNumberConfirmed;
    }

    public int getTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(int twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public String getLockoutEnd() {
        return lockoutEnd;
    }

    public void setLockoutEnd(String lockoutEnd) {
        this.lockoutEnd = lockoutEnd;
    }

    public int getLockoutEnabled() {
        return lockoutEnabled;
    }

    public void setLockoutEnabled(int lockoutEnabled) {
        this.lockoutEnabled = lockoutEnabled;
    }

    public int getAccessFailedCount() {
        return accessFailedCount;
    }

    public void setAccessFailedCount(int accessFailedCount) {
        this.accessFailedCount = accessFailedCount;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @XmlTransient
    public Collection<AspNetRoles> getAspNetRolesCollection() {
        return aspNetRolesCollection;
    }

    public void setAspNetRolesCollection(Collection<AspNetRoles> aspNetRolesCollection) {
        this.aspNetRolesCollection = aspNetRolesCollection;
    }

    @XmlTransient
    public Collection<Incomes> getIncomesCollection() {
        return incomesCollection;
    }

    public void setIncomesCollection(Collection<Incomes> incomesCollection) {
        this.incomesCollection = incomesCollection;
    }

    @XmlTransient
    public Collection<AspNetUserLogins> getAspNetUserLoginsCollection() {
        return aspNetUserLoginsCollection;
    }

    public void setAspNetUserLoginsCollection(Collection<AspNetUserLogins> aspNetUserLoginsCollection) {
        this.aspNetUserLoginsCollection = aspNetUserLoginsCollection;
    }

    @XmlTransient
    public Collection<AspNetUserTokens> getAspNetUserTokensCollection() {
        return aspNetUserTokensCollection;
    }

    public void setAspNetUserTokensCollection(Collection<AspNetUserTokens> aspNetUserTokensCollection) {
        this.aspNetUserTokensCollection = aspNetUserTokensCollection;
    }

    @XmlTransient
    public Collection<AspNetUserClaims> getAspNetUserClaimsCollection() {
        return aspNetUserClaimsCollection;
    }

    public void setAspNetUserClaimsCollection(Collection<AspNetUserClaims> aspNetUserClaimsCollection) {
        this.aspNetUserClaimsCollection = aspNetUserClaimsCollection;
    }

    @XmlTransient
    public Collection<Expenditures> getExpendituresCollection() {
        return expendituresCollection;
    }

    public void setExpendituresCollection(Collection<Expenditures> expendituresCollection) {
        this.expendituresCollection = expendituresCollection;
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
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Users[ id=" + id + " ]";
    }
    
}
