/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.badbuxtour.dao;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.google.appengine.api.datastore.Key;

/**
 * 
 * @author eeugene
 */
@Entity
@Table(name = "tournament_has_player")
@NamedQueries( {
	@NamedQuery(name = "TournamentHasPlayer.findAll", query = "SELECT t FROM TournamentHasPlayer t"),
	@NamedQuery(name = "TournamentHasPlayer.findBySingleRank", query = "SELECT t FROM TournamentHasPlayer t WHERE t.singleRank = :singleRank"),
	@NamedQuery(name = "TournamentHasPlayer.findByDoubleRank", query = "SELECT t FROM TournamentHasPlayer t WHERE t.doubleRank = :doubleRank"),
	@NamedQuery(name = "TournamentHasPlayer.findByMixtedDoubleRank", query = "SELECT t FROM TournamentHasPlayer t WHERE t.mixtedDoubleRank = :mixtedDoubleRank"),
	@NamedQuery(name = "TournamentHasPlayer.findByDoublePartner", query = "SELECT t FROM TournamentHasPlayer t WHERE t.doublePartner = :doublePartner"),
	@NamedQuery(name = "TournamentHasPlayer.findByMixtedDoublePartner", query = "SELECT t FROM TournamentHasPlayer t WHERE t.mixtedDoublePartner = :mixtedDoublePartner") })
public class TournamentHasPlayer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    private String singleRank;

    private String doubleRank;

    private String mixtedDoubleRank;

    private String doublePartner;

    private String mixtedDoublePartner;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tournament tournament;

//    @ManyToOne(fetch = FetchType.LAZY)
    @Basic
    private Key player;

    public TournamentHasPlayer() {
    }

    public String getSingleRank() {
	return singleRank;
    }

    public void setSingleRank(String singleRank) {
	this.singleRank = singleRank;
    }

    public String getDoubleRank() {
	return doubleRank;
    }

    public void setDoubleRank(String doubleRank) {
	this.doubleRank = doubleRank;
    }

    public String getMixtedDoubleRank() {
	return mixtedDoubleRank;
    }

    public void setMixtedDoubleRank(String mixtedDoubleRank) {
	this.mixtedDoubleRank = mixtedDoubleRank;
    }

    public String getDoublePartner() {
	return doublePartner;
    }

    public void setDoublePartner(String doublePartner) {
	this.doublePartner = doublePartner;
    }

    public String getMixtedDoublePartner() {
	return mixtedDoublePartner;
    }

    public void setMixtedDoublePartner(String mixtedDoublePartner) {
	this.mixtedDoublePartner = mixtedDoublePartner;
    }

    public Tournament getTournament() {
	return tournament;
    }

    public void setTournament(Tournament tournament) {
	this.tournament = tournament;
    }

    public Key getPlayer() {
	return player;
    }

    public void setPlayer(Key player) {
	this.player = player;
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
	if (!(object instanceof TournamentHasPlayer)) {
	    return false;
	}
	TournamentHasPlayer other = (TournamentHasPlayer) object;
	if ((this.key == null && other.key != null) || (this.key != null && !this.key.equals(other.key))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.badbuxtour.model.TournamentHasPlayer[key=" + key + "]";
    }

}
