/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.badbuxtour.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.google.appengine.api.datastore.Key;

/**
 * 
 * @author eeugene
 */
@Entity
// @Table(name = "player")
@NamedQueries( {
	@NamedQuery(name = "Player.findAll", query = "SELECT p FROM Player p"),
	@NamedQuery(name = "Player.findByIdPlayer", query = "SELECT p FROM Player p WHERE p.idPlayer = :idPlayer"),
	@NamedQuery(name = "Player.findByLastName", query = "SELECT p FROM Player p WHERE p.lastName = :lastName"),
	@NamedQuery(name = "Player.findByFirstName", query = "SELECT p FROM Player p WHERE p.firstName = :firstName"),
	@NamedQuery(name = "Player.findByBirthday", query = "SELECT p FROM Player p WHERE p.birthday = :birthday"),
	@NamedQuery(name = "Player.findByLicenceNumber", query = "SELECT p FROM Player p WHERE p.licenceNumber = :licenceNumber"),
	@NamedQuery(name = "Player.findByMail", query = "SELECT p FROM Player p WHERE p.mail = :mail"),
	@NamedQuery(name = "Player.findByIsAdmin", query = "SELECT p FROM Player p WHERE p.isAdmin = :isAdmin"),
	@NamedQuery(name = "Player.findByGender", query = "SELECT p FROM Player p WHERE p.gender = :gender"),
	@NamedQuery(name = "Player.findByIsSuperAdmin", query = "SELECT p FROM Player p WHERE p.isSuperAdmin = :isSuperAdmin"),
	@NamedQuery(name = "Player.findByIsActive", query = "SELECT p FROM Player p WHERE p.isActive = :isActive"),
	@NamedQuery(name = "Player.findByClub", query = "SELECT p FROM Player p WHERE p.club = :club"),
	@NamedQuery(name = "Player.findByPassword", query = "SELECT p FROM Player p WHERE p.password = :password") })
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    private String lastName;

    private String firstName;

    private Date birthday;

    private String licenceNumber;

    private String mail;

    private Integer isAdmin;

    private String gender;

    private Integer isSuperAdmin;

    private Integer isActive;

    private String password;

    // @ManyToOne (cascade=CascadeType.ALL)
    @ManyToOne(fetch = FetchType.LAZY)
    private Club club;

    // @OneToOne(fetch = FetchType.LAZY)
    // @Column(name = "club", insertable = false, updatable = false)
    // private Club clubAlias;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    private List<Key> tournaments = new ArrayList<Key>();

    public List<Key> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<Key> tournaments) {
        this.tournaments = tournaments;
    }

    public Club getClub() {
	return club;
    }

    public void setKey(Key key) {
	this.key = key;
    }

    public void setClub(Club idClub) {
	this.club = idClub;
    }

    public Key getKey() {
	return key;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public Date getBirthday() {
	return birthday;
    }

    public void setBirthday(Date birthday) {
	this.birthday = birthday;
    }

    public String getLicenceNumber() {
	return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
	this.licenceNumber = licenceNumber;
    }

    public String getMail() {
	return mail;
    }

    public void setMail(String mail) {
	this.mail = mail;
    }

    public Integer getIsAdmin() {
	return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
	this.isAdmin = isAdmin;
    }

    public String getGender() {
	return gender;
    }

    public void setGender(String gender) {
	this.gender = gender;
    }

    public Integer getIsSuperAdmin() {
	return isSuperAdmin;
    }

    public void setIsSuperAdmin(Integer isSuperAdmin) {
	this.isSuperAdmin = isSuperAdmin;
    }

    public Integer getIsActive() {
	return isActive;
    }

    public void setIsActive(Integer isActive) {
	this.isActive = isActive;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (key != null ? key.hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object object) {
	System.out
		.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! equals");
	// TODO: Warning - this method won't work in the case the id fields are
	// not set
	if (!(object instanceof Player)) {
	    return false;
	}
	Player other = (Player) object;
	if ((this.mail == null && other.mail != null) || (this.mail != null && !this.mail.equals(other.mail))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.badbuxtour.model.Player[key=" + key + ", mail=" + mail + "]";
    }

}
