/*
 * Authorization.java
 *
 * Created on Aug 15, 2007, 3:40:51 AM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.badbuxtour.application;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;

import com.badbuxtour.pages.login.SignInPage;

/**
 * @author eeugene
 * 
 */
public class PageAccessHandler implements IAuthorizationStrategy {

    public boolean isActionAuthorized(Component component, Action action) {
	return true;
    }

    public boolean isInstantiationAuthorized(Class componentClass) {
	BBTSession session = (BBTSession) Session.get();
	boolean isSignedIn = session.isSignedIn();
	boolean isAdmin = session.getCurrentUser() != null ? session.getCurrentUser().isAdmin() : false;
	boolean isSuperAdmin = session.getCurrentUser() != null ? session.getCurrentUser().isSuperAdmin() : false;

	boolean pageRequiresLogin = componentClass.isAnnotationPresent(RequiresSignin.class);

	boolean pageRequiresAdmin = componentClass.isAnnotationPresent(RequiresAdmin.class);

	boolean pageRequiresSuperAdmin = componentClass.isAnnotationPresent(RequiresSuperAdmin.class);

	if (pageRequiresLogin && !isSignedIn) {
	    // bounce them to the login page - the login page will send them
	    // where they're going if they log in successfully
	    throw new RestartResponseAtInterceptPageException(SignInPage.class);
	}

	if (pageRequiresSuperAdmin && !isSuperAdmin)
	    return false;

	if (pageRequiresAdmin && !(isAdmin || isSuperAdmin))
	    return false;

	return true;
    }
}
