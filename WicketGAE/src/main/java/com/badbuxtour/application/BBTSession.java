/*
 * ExampleSession.java
 * 
 * Created on Aug 15, 2007, 1:37:46 AM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.badbuxtour.application;

import com.badbuxtour.pojo.UserDTO;
import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebSession;

/**
 *
 * @author eeugene
 */
public final class BBTSession extends WebSession {

    private UserDTO user;

    BBTSession(WebApplication app, Request request) {
        super(app, request);
    }

    public void setCurrentUser(UserDTO user) {
        this.user = user;
    }

    public UserDTO getCurrentUser() {
        return this.user;
    }

    public boolean isSignedIn() {
        return user != null;
    }
}
