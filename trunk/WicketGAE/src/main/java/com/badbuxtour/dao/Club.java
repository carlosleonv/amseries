/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.badbuxtour.dao;

import java.io.Serializable;
import java.util.ArrayList;
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
// @Table(name = "club")
@NamedQueries( { @NamedQuery(name = "Club.findAll", query = "SELECT c FROM Club c"),
	@NamedQuery(name = "Club.findByIdClub", query = "SELECT c FROM Club c WHERE c.idClub = :idClub"),
	@NamedQuery(name = "Club.findByName", query = "SELECT c FROM Club c WHERE c.name = :name"),
	@NamedQuery(name = "Club.findByPhone1", query = "SELECT c FROM Club c WHERE c.phone1 = :phone1"),
	@NamedQuery(name = "Club.findByPhone2", query = "SELECT c FROM Club c WHERE c.phone2 = :phone2"),
	@NamedQuery(name = "Club.findByWebsite", query = "SELECT c FROM Club c WHERE c.website = :website"),
	@NamedQuery(name = "Club.findByMail", query = "SELECT c FROM Club c WHERE c.mail = :mail") })
public class Club implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    private String idClub;

    private String name;

    private String phone;

    private String website;

    private String mail;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<Player> playerCollection = new ArrayList<Player>();

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<Tournament> tournamentCollection = new ArrayList<Tournament>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Town town;

    public Key getKey() {
	return key;
    }

    public String getIdClub() {
	return idClub;
    }

    public void setIdClub(String idClub) {
	this.idClub = idClub;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(String phone) {
	this.phone = phone;
    }

    public String getWebsite() {
	return website;
    }

    public void setWebsite(String website) {
	this.website = website;
    }

    public String getMail() {
	return mail;
    }

    public void setMail(String mail) {
	this.mail = mail;
    }

    public List<Player> getPlayerCollection() {
	return playerCollection;
    }

    public void setPlayerCollection(List<Player> playerCollection) {
	this.playerCollection = playerCollection;
    }

    public List<Tournament> getTournamentCollection() {
	return tournamentCollection;
    }

    public void setTournamentCollection(List<Tournament> tournamentCollection) {
	this.tournamentCollection = tournamentCollection;
    }

    public Town getTown() {
	return town;
    }

    public void setTown(Town town) {
	this.town = town;
    }

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (key != null ? key.hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object object) {
	// TODO: Warning - this method won't work in the case the id fields are
	// not set
	if (!(object instanceof Club)) {
	    return false;
	}
	Club other = (Club) object;
	if ((this.key == null && other.key != null) || (this.key != null && !this.key.equals(other.key))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.badbuxtour.model.Club[idClub=" + idClub + "]";
    }

}
