/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.badbuxtour.pojo;

import java.io.Serializable;

import com.badbuxtour.dao.Player;
import com.google.appengine.api.datastore.Key;

/**
 * 
 * @author eeugene
 */
public class UserDTO implements Serializable {

    private Key playerId;
    private String lastName;
    private String firstName;
    private String mail;
    private Key idClub;
    private boolean isAdmin;
    private boolean isSuperAdmin;

    public UserDTO(Key playerId, String lastName, String firstName, String mail, Key idClub, boolean isAdmin,
	    boolean isSuperAdmin) {
	this.playerId = playerId;
	this.lastName = lastName;
	this.firstName = firstName;
	this.mail = mail;
	this.idClub = idClub;
	this.isAdmin = isAdmin;
	this.isSuperAdmin = isSuperAdmin;
    }

    public boolean isSuperAdmin() {
	return isSuperAdmin;
    }

    public void setSuperAdmin(boolean isSuperAdmin) {
	this.isSuperAdmin = isSuperAdmin;
    }

    public boolean isAdmin() {
	return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
	this.isAdmin = isAdmin;
    }

    public Key getIdClub() {
	return idClub;
    }

    public void setIdClub(Key idClub) {
	this.idClub = idClub;
    }

    public Key getPlayerId() {
	return playerId;
    }

    public void setPlayerId(Key playerId) {
	this.playerId = playerId;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getMail() {
	return mail;
    }

    public void setMail(String mail) {
	this.mail = mail;
    }

    @Override
    public String toString() {
	return firstName + " <" + mail + ">";
    }

    public Player getPlayer() {
	Player player = new Player();
	player.setKey(getPlayerId());
	player.setMail(getMail());
	return player;
    }

}
