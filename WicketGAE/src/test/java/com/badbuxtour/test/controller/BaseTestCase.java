/**
 * 
 */
package com.badbuxtour.test.controller;

import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.badbuxtour.controller.ClubJpaController;
import com.badbuxtour.controller.PlayerJpaController;
import com.badbuxtour.controller.TournamentHasPlayerJpaController;
import com.badbuxtour.controller.TournamentJpaController;
import com.badbuxtour.controller.TownJpaController;
import com.badbuxtour.dao.Club;
import com.badbuxtour.dao.Player;
import com.badbuxtour.dao.Tournament;
import com.badbuxtour.dao.Town;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * @author eeugene
 * 
 */
public class BaseTestCase extends TestCase {

    protected PlayerJpaController playerController = new PlayerJpaController();

    protected ClubJpaController clubController = new ClubJpaController();

    protected TownJpaController townController = new TownJpaController();

    protected TournamentJpaController tournamentController = new TournamentJpaController();

    protected TournamentHasPlayerJpaController tournamentHasPlayerController = new TournamentHasPlayerJpaController();

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() {
	helper.setUp();
    }

    @After
    public void tearDown() {
	helper.tearDown();
    }

    private Player mockPlayer;
    private Club mockClub;
    private Tournament mockTournament;

    protected int countEntity(String entity) {
	DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
	return ds.prepare(new Query(entity)).countEntities();
    }
    
    protected Player getMockPlayer() {
	if (mockPlayer == null) {
	    mockPlayer = new Player();
	    mockPlayer.setFirstName("firstName");
	    mockPlayer.setLastName("lastName");
	    mockPlayer.setGender("M");
	    mockPlayer.setBirthday(new Date());
	    mockPlayer.setLicenceNumber("");
	    mockPlayer.setClub(null);
	    mockPlayer.setIsActive(1);
	    mockPlayer.setIsAdmin(0);
	    mockPlayer.setIsSuperAdmin(0);
	    mockPlayer.setMail("mail");
	    mockPlayer.setPassword("password");
	}
	return mockPlayer;
    }

    protected Club getMockClub() {
	if (mockClub == null) {
	    mockClub = new Club();
	    mockClub.setIdClub("ESBB");
	    mockClub.setName("buxerolles badminton club");
	}
	return mockClub;
    }

    protected Tournament getMockTournament() {
	if (mockTournament == null) {
	    mockTournament = new Tournament();
	    mockTournament.setIdTournament("ESBB1");
	    mockTournament.setName("Tournoi de bux");
	}
	return mockTournament;
    }

    protected Player getPlayerInstance() {
	if (mockPlayer != null) {
	    try {
		List<Player> players = playerController.findByMail(mockPlayer.getMail());
		if (players.size() > 1) {
		    throw new Exception("Multiple players found");
		}
		return players.get(0);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	return null;
    }

    protected Club getClubInstance() {
	if (mockClub != null) {
	    try {
		List<Club> clubs = clubController.findByIdClub(mockClub.getIdClub());
		if (clubs.size() > 1) {
		    throw new Exception("Multiple clubs found");
		}
		return clubs.get(0);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	return null;
    }

    protected Tournament getTournamentInstance() {
	if (mockTournament != null) {
	    try {
		List<Tournament> tours = tournamentController.findByIdTournament(mockTournament.getIdTournament());
		if (tours.size() > 1) {
		    throw new Exception("Multiple tours found");
		}
		return tours.get(0);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	return null;
    }

    protected void createTown() {
	Town t = new Town();
	try {
	    townController.create(t);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Test
    public void testInsertClub() {
	try {
	    clubController.create(getMockClub());
	} catch (Exception e) {
	    e.printStackTrace();
	}
	assertNotNull(getClubInstance());
	assertEquals("Test testInsertClub : inconsistent data", getClubInstance().getIdClub(), getMockClub()
		.getIdClub());
    }

    @Test
    public void testInsertPlayer() {
	try {
	    playerController.create(getMockPlayer());
	} catch (Exception e) {
	    e.printStackTrace();
	}
	assertNotNull(getPlayerInstance());
	assertEquals("Test testInsertPlayer : inconsistent data", getPlayerInstance().getMail(), getMockPlayer()
		.getMail());
    }

    @Test
    public void testCreatePlayerInClub() {
	testInsertClub();
	try {
	    Club c = getClubInstance();
	    playerController.createPlayer(c.getKey(), getMockPlayer());
	    List<Player> players = c.getPlayerCollection();
	    assertNotNull(players);
	    assertEquals("Test testOwnedRelationship error : multiple players fetched", 1, players.size());
	    assertEquals("Test testOwnedRelationship : inconsistent data", players.get(0).getMail(), getMockPlayer().getMail());
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
