/*
 * Application.java
 *
 * Created on 17 janvier 2010, 14:01
 */

package com.badbuxtour.application;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.SecondLevelCacheSessionStore;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.https.HttpsConfig;
import org.apache.wicket.protocol.https.HttpsRequestCycleProcessor;
import org.apache.wicket.request.IRequestCycleProcessor;
import org.apache.wicket.session.ISessionStore;
import org.apache.wicket.settings.Settings;

import com.badbuxtour.controller.ClubJpaController;
import com.badbuxtour.controller.PlayerJpaController;
import com.badbuxtour.dao.Club;
import com.badbuxtour.pages.login.SignInPage;
import com.example.gae.CachePageStore;

/**
 * 
 * @author eeugene
 * @version
 */

public class Application extends WebApplication {

    protected final transient PlayerJpaController playerController = new PlayerJpaController();
    protected final transient ClubJpaController clubController = new ClubJpaController();

    public ClubJpaController getClubController() {
	return clubController;
    }

    public PlayerJpaController getPlayerController() {
	return playerController;
    }

    public Application() {
	Club club = new Club();
	club.setIdClub("ESBB");
	club.setName("bux");
	try {
	    clubController.create(club);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	try {
	    List<Club> c = clubController.findByIdClub("ESBB");
	    playerController.createUser("m", "m", "M", new Date(), "m@", "mmmmm", "mmmmm", "", c.get(0).getKey(),
		    false, true);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public Class getHomePage() {
	return HomePage.class;
    }

    private int getSslPort() {
	// This will probably need to be changed depending on the server!
	// Glassfish uses 8181; JBoss's default is different, probably
	// others too
	return 8181;
    }

    @Override
    protected void init() {
	// Need this for ssl to work
	getRequestCycleSettings().setRenderStrategy(Settings.ONE_PASS_RENDER);
	// Might as well send them back to the sign in page
	getApplicationSettings().setAccessDeniedPage(SignInPage.class);
	// This will block users from accessing pages whose classes have our
	// you-must-login annotation
	getSecuritySettings().setAuthorizationStrategy(new PageAccessHandler());
	// required for GAE
	getResourceSettings().setResourcePollFrequency(null);
	// Get rid of those nasty wicket:::interface3 urls
	// mount("examples/", PackageName.forClass(HomePage.class));
	// mount("examples/login", PackageName.forClass(SignInPage.class));
    }

    @Override
    public Session newSession(Request request, Response response) {
	// create an instance of our custom session that knows what
	// user is logged in
	return new BBTSession(this, request);
    }

    @Override
    public IRequestCycleProcessor newRequestCycleProcessor() {
	// Provide our own request cycle processor which will look for
	// RequiresSSL annotations on pages it should show, and can
	// switch over to SSL when needed
	// return new SwitchToSslHandler(getSslPort());
	return new HttpsRequestCycleProcessor(new HttpsConfig(8080, getSslPort()));
    }

    @Override
    protected ISessionStore newSessionStore() {
	// return new HttpSessionStore(this);
	return new SecondLevelCacheSessionStore(this, new CachePageStore());
    }
}
