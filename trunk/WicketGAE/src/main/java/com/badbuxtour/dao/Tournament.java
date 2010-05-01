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
import javax.persistence.Table;

import com.google.appengine.api.datastore.Key;

/**
 * 
 * @author eeugene
 */
@Entity
@Table(name = "tournament")
@NamedQueries( {
	@NamedQuery(name = "Tournament.findAll", query = "SELECT t FROM Tournament t"),
	@NamedQuery(name = "Tournament.findByIdTournament", query = "SELECT t FROM Tournament t WHERE t.idTournament = :idTournament"),
	@NamedQuery(name = "Tournament.findByName", query = "SELECT t FROM Tournament t WHERE t.name = :name"),
	@NamedQuery(name = "Tournament.findByBeginDate", query = "SELECT t FROM Tournament t WHERE t.beginDate = :beginDate"),
	@NamedQuery(name = "Tournament.findByEndDate", query = "SELECT t FROM Tournament t WHERE t.endDate = :endDate"),
	@NamedQuery(name = "Tournament.findByHasSingle", query = "SELECT t FROM Tournament t WHERE t.hasSingle = :hasSingle"),
	@NamedQuery(name = "Tournament.findByHasDouble", query = "SELECT t FROM Tournament t WHERE t.hasDouble = :hasDouble"),
	@NamedQuery(name = "Tournament.findByHasMixtedDouble", query = "SELECT t FROM Tournament t WHERE t.hasMixtedDouble = :hasMixtedDouble"),
	@NamedQuery(name = "Tournament.findByIsVeteran", query = "SELECT t FROM Tournament t WHERE t.isVeteran = :isVeteran"),
	@NamedQuery(name = "Tournament.findByIsYoung", query = "SELECT t FROM Tournament t WHERE t.isYoung = :isYoung"),
	@NamedQuery(name = "Tournament.findByRegistrationEndDate", query = "SELECT t FROM Tournament t WHERE t.registrationEndDate = :registrationEndDate"),
	@NamedQuery(name = "Tournament.findByHasSinglePool", query = "SELECT t FROM Tournament t WHERE t.hasSinglePool = :hasSinglePool"),
	@NamedQuery(name = "Tournament.findByHasDoublePool", query = "SELECT t FROM Tournament t WHERE t.hasDoublePool = :hasDoublePool"),
	@NamedQuery(name = "Tournament.findByHasMixedDoublePool", query = "SELECT t FROM Tournament t WHERE t.hasMixedDoublePool = :hasMixedDoublePool"),
	@NamedQuery(name = "Tournament.findByOneBoardRate", query = "SELECT t FROM Tournament t WHERE t.oneBoardRate = :oneBoardRate"),
	@NamedQuery(name = "Tournament.findByTwoBoardsRate", query = "SELECT t FROM Tournament t WHERE t.twoBoardsRate = :twoBoardsRate"),
	@NamedQuery(name = "Tournament.findByThreeBoardsRate", query = "SELECT t FROM Tournament t WHERE t.threeBoardsRate = :threeBoardsRate") })
public class Tournament implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    private String idTournament;

    private String name;

    private Date beginDate;

    private Date endDate;

    private Integer hasSingle;

    private Integer hasDouble;

    private Integer hasMixtedDouble;

    private Integer isVeteran;

    private Integer isYoung;

    private Date registrationEndDate;

    private Integer hasSinglePool;

    private Integer hasDoublePool;

    private Integer hasMixedDoublePool;

    private String description;

    private String oneBoardRate;

    private String twoBoardsRate;

    private String threeBoardsRate;

    // @ManyToMany
    // private Collection<Rank> rankCollection;

    @ManyToOne(fetch = FetchType.LAZY)
    private Club club;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tournament")
    private List<TournamentHasPlayer> tournamentHasPlayerCollection = new ArrayList<TournamentHasPlayer>();

    public Tournament() {
    }

    public Key getKey() {
	return key;
    }

    public void setKey(Key key) {
	this.key = key;
    }

    public String getIdTournament() {
	return idTournament;
    }

    public void setIdTournament(String idTournament) {
	this.idTournament = idTournament;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Date getBeginDate() {
	return beginDate;
    }

    public void setBeginDate(Date beginDate) {
	this.beginDate = beginDate;
    }

    public Date getEndDate() {
	return endDate;
    }

    public void setEndDate(Date endDate) {
	this.endDate = endDate;
    }

    public Integer getHasSingle() {
	return hasSingle;
    }

    public void setHasSingle(Integer hasSingle) {
	this.hasSingle = hasSingle;
    }

    public Integer getHasDouble() {
	return hasDouble;
    }

    public void setHasDouble(Integer hasDouble) {
	this.hasDouble = hasDouble;
    }

    public Integer getHasMixtedDouble() {
	return hasMixtedDouble;
    }

    public void setHasMixtedDouble(Integer hasMixtedDouble) {
	this.hasMixtedDouble = hasMixtedDouble;
    }

    public Integer getIsVeteran() {
	return isVeteran;
    }

    public void setIsVeteran(Integer isVeteran) {
	this.isVeteran = isVeteran;
    }

    public Integer getIsYoung() {
	return isYoung;
    }

    public void setIsYoung(Integer isYoung) {
	this.isYoung = isYoung;
    }

    public Date getRegistrationEndDate() {
	return registrationEndDate;
    }

    public void setRegistrationEndDate(Date registrationEndDate) {
	this.registrationEndDate = registrationEndDate;
    }

    public Integer getHasSinglePool() {
	return hasSinglePool;
    }

    public void setHasSinglePool(Integer hasSinglePool) {
	this.hasSinglePool = hasSinglePool;
    }

    public Integer getHasDoublePool() {
	return hasDoublePool;
    }

    public void setHasDoublePool(Integer hasDoublePool) {
	this.hasDoublePool = hasDoublePool;
    }

    public Integer getHasMixedDoublePool() {
	return hasMixedDoublePool;
    }

    public void setHasMixedDoublePool(Integer hasMixedDoublePool) {
	this.hasMixedDoublePool = hasMixedDoublePool;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getOneBoardRate() {
	return oneBoardRate;
    }

    public void setOneBoardRate(String oneBoardRate) {
	this.oneBoardRate = oneBoardRate;
    }

    public String getTwoBoardsRate() {
	return twoBoardsRate;
    }

    public void setTwoBoardsRate(String twoBoardsRate) {
	this.twoBoardsRate = twoBoardsRate;
    }

    public String getThreeBoardsRate() {
	return threeBoardsRate;
    }

    public void setThreeBoardsRate(String threeBoardsRate) {
	this.threeBoardsRate = threeBoardsRate;
    }

    public Club getClub() {
	return club;
    }

    public void setClub(Club club) {
	this.club = club;
    }

    public List<TournamentHasPlayer> getTournamentHasPlayerCollection() {
	return tournamentHasPlayerCollection;
    }

    public void setTournamentHasPlayerCollection(List<TournamentHasPlayer> tournamentHasPlayerCollection) {
	this.tournamentHasPlayerCollection = tournamentHasPlayerCollection;
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
	if (!(object instanceof Tournament)) {
	    return false;
	}
	Tournament other = (Tournament) object;
	if ((this.key == null && other.key != null) || (this.key != null && !this.key.equals(other.key))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.badbuxtour.model.Tournament[idTournament=" + idTournament + "]";
    }

}
