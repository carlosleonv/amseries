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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.google.appengine.api.datastore.Key;

/**
 * 
 * @author eeugene
 */
@Entity
// @Table(name = "town")
@NamedQueries( { @NamedQuery(name = "Town.findAll", query = "SELECT t FROM Town t"),
	@NamedQuery(name = "Town.findByIdTown", query = "SELECT t FROM Town t WHERE t.idTown = :idTown"),
	@NamedQuery(name = "Town.findByName", query = "SELECT t FROM Town t WHERE t.name = :name"),
	@NamedQuery(name = "Town.findByCode", query = "SELECT t FROM Town t WHERE t.code = :code"),
	@NamedQuery(name = "Town.findByDepartment", query = "SELECT t FROM Town t WHERE t.department = :department"),
	@NamedQuery(name = "Town.findByInsee", query = "SELECT t FROM Town t WHERE t.insee = :insee") })
public class Town implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    private String idTown;

    private String name;

    private String code;

    private String department;

    private String insee;

    @OneToMany(mappedBy = "town", cascade = CascadeType.ALL)
    private List<Club> clubCollection = new ArrayList<Club>();

    public Key getKey() {
	return key;
    }

    public String getIdTown() {
	return idTown;
    }

    public void setIdTown(String idTown) {
	this.idTown = idTown;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getDepartment() {
	return department;
    }

    public void setDepartment(String department) {
	this.department = department;
    }

    public String getInsee() {
	return insee;
    }

    public void setInsee(String insee) {
	this.insee = insee;
    }

    public List<Club> getClubCollection() {
	return clubCollection;
    }

    public void setClubCollection(List<Club> clubCollection) {
	this.clubCollection = clubCollection;
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
	if (!(object instanceof Town)) {
	    return false;
	}
	Town other = (Town) object;
	if ((this.key == null && other.key != null) || (this.key != null && !this.key.equals(other.key))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.badbuxtour.model.Town[idTown=" + idTown + "]";
    }

}
