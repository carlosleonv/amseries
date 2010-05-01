package com.badbuxtour.test.controller;

import java.util.List;

import org.junit.Test;

import com.badbuxtour.controller.PlayerJpaController;
import com.badbuxtour.dao.Club;
import com.badbuxtour.dao.Player;
import com.badbuxtour.dao.Tournament;
import com.badbuxtour.dao.TournamentHasPlayer;

public class TournamentTestCase extends BaseTestCase {

    private void insertEmptyTournament() {
	Tournament t = new Tournament();
	try {
	    tournamentController.create(t);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Test
    public void testInsertTournament() {
	int count = tournamentController.count();
	assertEquals("Test testInsertTournament error", 0, count);
	insertEmptyTournament();
	count = tournamentController.count();
	assertEquals("Test testInsertTournament error", 1, count);
    }

    @Test
    public void testCreateTournamentWithClub() {
	testInsertClub();
	try {
	    tournamentController.createTournament(getClubInstance().getKey(), getMockTournament());
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	assertNotNull(getClubInstance());
	assertNotNull(getTournamentInstance());
	assertNotNull(getTournamentInstance().getClub());
	assertEquals("Test addClubToTournament : inconsistent data", getTournamentInstance().getClub().getIdClub(),
		getClubInstance().getIdClub());
    }

    @Test
    public void testGetTournamentsForClub() {
	testCreateTournamentWithClub();
	Club club = getClubInstance();
	assertNotNull(club.getTournamentCollection());
	assertEquals("Test testGetTournamentsForClub error", 1, club.getTournamentCollection().size());
	assertEquals("Test testGetTournamentsForClub : inconsistent data", club.getTournamentCollection().get(0)
		.getIdTournament(), getTournamentInstance().getIdTournament());
    }

    @Test
    public void testSubscribePlayerForTournament() {
	testInsertPlayer();
	testCreateTournamentWithClub();
	assertEquals(1, countEntity("Club"));
	assertEquals(1, countEntity("Player"));

	try {
	    tournamentHasPlayerController.createTournamentHasPlayer(getPlayerInstance().getKey(),
		    getTournamentInstance().getKey(), new TournamentHasPlayer());
	    playerController.addTournament(getTournamentInstance().getKey(), getPlayerInstance().getKey());
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	TournamentHasPlayer thp = null;
	try {
	    List<TournamentHasPlayer> toupla = tournamentHasPlayerController.findBy("findAll", null);
	    if (toupla.size() > 1) {
		throw new Exception("Multiple TournamentHasPlayer found");
	    }
	    thp = toupla.get(0);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	assertNotNull(thp);
	assertNotNull(thp.getTournament());
	assertNotNull(thp.getPlayer());
	assertEquals("Bad player fetched", thp.getPlayer(), getPlayerInstance().getKey());
	assertEquals("Bad tournament fetched", getTournamentInstance().getKey(), thp.getTournament().getKey());
	assertEquals(1, getTournamentInstance().getTournamentHasPlayerCollection().size());
	assertEquals(1, getPlayerInstance().getTournaments().size());
    }
}
