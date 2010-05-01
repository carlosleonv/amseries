package com.example.gae;

import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.SecondLevelCacheSessionStore;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.session.ISessionStore;
import org.apache.wicket.util.lang.PackageName;

import com.example.openid.pages.MakotoOpenIdAwareSession;
import com.example.openid.pages.OpenIdRegistrationPage;

public class GaeWicketApplication extends WebApplication {
	@Override
	public Class<? extends Page> getHomePage() {
		return HomePage.class;
		// return com.example.openid.pages.OpenIdRegistrationPage.class;
	}

	@Override
	protected void init() {
		super.init();
		getResourceSettings().setResourcePollFrequency(null);
//		mount("openid", PackageName.forClass(OpenIdRegistrationPage.class));
	}

	@Override
	protected ISessionStore newSessionStore() {
		// return new HttpSessionStore(this);
		return new SecondLevelCacheSessionStore(this, new CachePageStore());
	}

	// public Session newSession(Request request, Response response) {
	// return new MakotoOpenIdAwareSession(request);
	// }

}
