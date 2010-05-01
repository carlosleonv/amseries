package com.badbuxtour.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badbuxtour.dao.Club;
import com.badbuxtour.dao.Tournament;
import com.google.appengine.api.datastore.Key;

public class TournamentJpaController extends GenericJpaController<Tournament> {

    private Logger logger = LoggerFactory.getLogger(TournamentJpaController.class);

    public TournamentJpaController() {
	super(Tournament.class);
    }

    public List<Tournament> findByIdTournament(String idTournament) throws Exception {
	Map<String, Object> params = new HashMap<String, Object>(1);
	params.put("idTournament", idTournament);
	return findBy("findByIdTournament", params);
    }

    public void createTournament(Key clubKey, Tournament tournament) throws Exception {
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
		tournament.setClub(c);
	    }
	    em.persist(tournament);
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
