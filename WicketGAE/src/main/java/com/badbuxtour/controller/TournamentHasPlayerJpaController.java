package com.badbuxtour.controller;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badbuxtour.dao.Player;
import com.badbuxtour.dao.Tournament;
import com.badbuxtour.dao.TournamentHasPlayer;
import com.google.appengine.api.datastore.Key;

public class TournamentHasPlayerJpaController extends GenericJpaController<TournamentHasPlayer> {

    private Logger logger = LoggerFactory.getLogger(TournamentHasPlayerJpaController.class);

    public TournamentHasPlayerJpaController() {
	super(TournamentHasPlayer.class);
    }

    public void createTournamentHasPlayer(Key playerKey, Key tournamentKey, TournamentHasPlayer object) throws Exception {
	EntityManager em = null;
	try {
	    em = getEntityManager();
	    em.getTransaction().begin();

	    if (playerKey != null) {
//		Player p = em.find(Player.class, playerKey);
//		if (p == null) {
//		    String message = "Player " + playerKey + " not found!";
//		    logger.error(message);
//		    throw new RuntimeException(message);
//		}
//		p.getTournaments().add(tournamentKey);
		object.setPlayer(playerKey);
	    }
	    
	    if (tournamentKey != null) {
		Tournament t = em.find(Tournament.class, tournamentKey);
		if (t == null) {
		    String message = "Tournament " + tournamentKey + " not found!";
		    logger.error(message);
		    throw new RuntimeException(message);
		}
		object.setTournament(t);
	    }
	    em.persist(object);
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
