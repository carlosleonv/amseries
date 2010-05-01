/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.badbuxtour.dao;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
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

import com.google.appengine.api.datastore.Key;

/**
 *
 * @author eeugene
 */
@Entity
//@Table(name = "rank")
@NamedQueries({
    @NamedQuery(name = "Rank.findAll", query = "SELECT r FROM Rank r"),
    @NamedQuery(name = "Rank.findByIdRank", query = "SELECT r FROM Rank r WHERE r.idRank = :idRank"),
    @NamedQuery(name = "Rank.findByName", query = "SELECT r FROM Rank r WHERE r.name = :name")})
public class Rank implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

	private Integer idRank;
    
    private String name;
    
    @OneToMany
    private Collection<Player> playerCollection;
    
    @OneToMany
    private Collection<Player> playerCollection1;
    
    @OneToMany
    private Collection<Player> playerCollection2;


    public Key getKey() {
		return key;
	}

    public Integer getIdRank() {
        return idRank;
    }

    public void setIdRank(Integer idRank) {
        this.idRank = idRank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Player> getPlayerCollection() {
        return playerCollection;
    }

    public void setPlayerCollection(Collection<Player> playerCollection) {
        this.playerCollection = playerCollection;
    }

    public Collection<Player> getPlayerCollection1() {
        return playerCollection1;
    }

    public void setPlayerCollection1(Collection<Player> playerCollection1) {
        this.playerCollection1 = playerCollection1;
    }

    public Collection<Player> getPlayerCollection2() {
        return playerCollection2;
    }

    public void setPlayerCollection2(Collection<Player> playerCollection2) {
        this.playerCollection2 = playerCollection2;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRank != null ? idRank.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rank)) {
            return false;
        }
        Rank other = (Rank) object;
        if ((this.idRank == null && other.idRank != null) || (this.idRank != null && !this.idRank.equals(other.idRank))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.badbuxtour.model.Rank[idRank=" + idRank + "]";
    }

}
