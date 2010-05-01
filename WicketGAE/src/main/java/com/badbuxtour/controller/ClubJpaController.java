/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.badbuxtour.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badbuxtour.application.BBTSession;
import com.badbuxtour.application.SelectOption;
import com.badbuxtour.application.UserErrorCode;
import com.badbuxtour.dao.Club;
import com.badbuxtour.dao.Player;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * 
 * @author eeugene
 */
public class ClubJpaController extends GenericJpaController<Club> {

    private Logger logger = LoggerFactory.getLogger(ClubJpaController.class);

    public ClubJpaController() {
	super(Club.class);
    }

    public List<SelectOption> fetchClubsForList() {
	List<SelectOption> result = new ArrayList<SelectOption>();
	for (Club c : findEntities()) {
	    result.add(new SelectOption(KeyFactory.keyToString(c.getKey()), c.getIdClub() + " - " + c.getName()));
	}
	return result;
    }

    public UserErrorCode createClub(String idClub, String name, String phone, String mail, String website)
	    throws Exception {
	if (mail != null && !mail.contains("@")) {
	    return UserErrorCode.BAD_EMAIL;
	}
	List<Club> existing = findByIdClub(idClub);
	if (existing.size() > 0) {
	    return UserErrorCode.CLUB_EXISTS;
	}
	Club c = new Club();
	c.setIdClub(idClub);
	c.setName(name);
	c.setMail(mail);
	c.setPhone(phone);
	c.setWebsite(website);
	create(c);
	return null;
    }

    public List<Club> findByIdClub(String idClub) throws Exception {
	Map<String, Object> params = new HashMap<String, Object>(1);
	params.put("idClub", idClub);
	return findBy("findByIdClub", params);
    }

    public List<Player> playersOfMyClub(Player p) {
	List<Player> players = new ArrayList<Player>();
	Key clubKey = getClub();
	if (clubKey != null) {
	    Club club = findEntity(clubKey);
	    if (club != null) {
		players = club.getPlayerCollection();
		players.get(0); // first access required, don't know why
		if (players.contains(p)) {
		    players.remove(p);
		}
	    }
	}
	return players;
    }

    private Key getClub() {
	return ((BBTSession) Session.get()).getCurrentUser().getIdClub();
    }

    // public List<Player> playersOfMyClub() {
    // EntityManager em = null;
    // try {
    // em = getEntityManager();
    // Query q = em.createQuery("select from " + Club.class.getName() +
    // " c JOIN c.playerCollection p where "
    // + "p.mail < :mail and p.mail > :mail");
    // q.setParameter("mail", ((BBTSession)
    // Session.get()).getCurrentUser().getMail());
    // List<Player> list = q.getResultList();
    // list.size();
    // return list;
    // } finally {
    // em.close();
    // }
    // }

}
