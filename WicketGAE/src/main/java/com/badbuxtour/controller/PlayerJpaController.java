/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.badbuxtour.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.wicket.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badbuxtour.application.BBTSession;
import com.badbuxtour.application.UserErrorCode;
import com.badbuxtour.dao.Club;
import com.badbuxtour.dao.Player;
import com.badbuxtour.dao.TournamentHasPlayer;
import com.badbuxtour.pojo.UserDTO;
import com.badbuxtour.pojo.UserUtil;
import com.google.appengine.api.datastore.Key;

/**
 * 
 * @author eeugene
 */
public class PlayerJpaController extends GenericJpaController<Player> {

    private Logger logger = LoggerFactory.getLogger(Player.class);

    public PlayerJpaController() {
	super(Player.class);
    }

    /**
     * Log the user in.
     * 
     * @param emailAddress
     *            their email address
     * @param password
     *            the password they entered in the login form
     */
    public UserErrorCode authenticate(String emailAddress, String password) {
	Player p = null;
	try {
	    List<Player> existing = findByMail(emailAddress);
	    if (existing.size() > 0)
		p = existing.get(0);
	} catch (Exception ex) {
	    ex.printStackTrace();
	    return UserErrorCode.EXCEPTION;
	}
	if (p == null) {
	    return UserErrorCode.UNKNOWN_USER;
	}
	if (!UserUtil.matchPassword(password, p.getPassword())) {
	    // Well, if you wanted to be secure you'd return unknown user here
	    return UserErrorCode.INCORRECT_PASSWORD;
	}
	if (p.getIsActive() == 0) {
	    return UserErrorCode.INACTIVE;
	}
	onAuthenticate(p);
	return null;
    }

    protected void onAuthenticate(Player p) {
	UserDTO user = new UserDTO(p.getKey(), p.getLastName(), p.getFirstName(), p.getMail(), p.getClub() != null ? p
		.getClub().getKey() : null, p.getIsAdmin() == 1 ? true : false, p.getIsSuperAdmin() == 1 ? true : false);
	BBTSession sess = (BBTSession) Session.get();
	sess.setCurrentUser(user);
    }

    public UserErrorCode createUser(String lastName, String firstName, String gender, Date birthday,
	    String emailAddress, String password, String repeatPassword, String licence, Key clubKey, boolean admin,
	    boolean superAdmin) throws Exception {
	if (password.length() < 5) {
	    return UserErrorCode.BAD_PASSWORD;
	}
	if (!emailAddress.contains("@")) {
	    return UserErrorCode.BAD_EMAIL;
	}
	if (!password.equals(repeatPassword)) {
	    return UserErrorCode.NON_MATCHING_PASSWORD;
	}

	List<Player> existing = findByMail(emailAddress);
	if (existing.size() > 0) {
	    return UserErrorCode.USER_EXISTS;
	}

	Player p = new Player();
	p.setLastName(lastName);
	p.setFirstName(firstName);
	p.setMail(emailAddress);
	p.setIsAdmin(new Integer(admin ? 1 : 0));
	p.setIsSuperAdmin(new Integer(superAdmin ? 1 : 0));
	p.setBirthday(birthday);
	p.setGender(gender);
	p.setLicenceNumber(licence);
	p.setIsActive(new Integer(1));
	p.setPassword(UserUtil.encryptPassword(password));
	// p.setClub(club);
	createPlayer(clubKey, p);
	// create(p);
	return null;
    }

    public List<Player> findByClub(Key club) throws Exception {
	Map<String, Object> params = new HashMap<String, Object>(1);
	params.put("club", club);
	return findBy("findByClub", params);
    }

    public List<Player> findByMail(String mail) throws Exception {
	Map<String, Object> params = new HashMap<String, Object>(1);
	params.put("mail", mail);
	return findBy("findByMail", params);
    }

    public void createPlayer(Key clubKey, Player player) throws Exception {
	EntityManager em = null;
	try {
	    em = getEntityManager();
	    em.getTransaction().begin();

	    if (clubKey != null) {
		Club c = em.find(Club.class, clubKey);
		if (c == null) {
		    String message = "Club " + clubKey + " not found!";
		    logger.error(message);
		    throw new RuntimeException(message);
		}
		c.getPlayerCollection().add(player);
	    } else {
		em.persist(player);
	    }
	    em.getTransaction().commit();
	} catch (Exception e) {
	    throw (e);
	} finally {
	    if (em.getTransaction().isActive()) {
		em.getTransaction().rollback();
	    }
	    em.close();
	}
    }

    public void addTournament(Key tournamentKey, Key playerKey) throws Exception {
	EntityManager em = null;
	try {
	    em = getEntityManager();
	    em.getTransaction().begin();

	    if (playerKey != null) {
		Player p = em.find(Player.class, playerKey);
		if (p == null) {
		    throw new RuntimeException("Player " + playerKey + " not found!");
		}
		p.getTournaments().add(tournamentKey);
		em.merge(p);
	    }
	    em.getTransaction().commit();
	} catch (Exception e) {
	    throw (e);
	} finally {
	    if (em.getTransaction().isActive()) {
		em.getTransaction().rollback();
	    }
	    em.close();
	}
    }
}
