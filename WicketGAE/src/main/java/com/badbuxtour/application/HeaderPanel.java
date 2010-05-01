/*
 * HeaderPanel.java
 *
 * Created on 17 janvier 2010, 14:01
 */
 
package com.badbuxtour.application;

import com.badbuxtour.pages.login.ChangePasswordPage;
import com.badbuxtour.pages.login.RegisterPage;
import com.badbuxtour.pages.login.SignInPage;
import com.badbuxtour.pages.login.SignOut;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/** 
 *
 * @author eeugene
 * @version 
 */

public class HeaderPanel extends Panel {

    /**
     * Construct.
     * @param componentName name of the component
     * @param exampleTitle title of the example
     */

    public HeaderPanel(String componentName)
    {
        super(componentName);
        //A login link to take users to the login form
        BookmarkablePageLink loginLink = new BookmarkablePageLink("login-link",
                SignInPage.class);
        add(loginLink);

        //A login link to take users to the login form
        BookmarkablePageLink registerLink = new BookmarkablePageLink("register-link",
                RegisterPage.class);
        add(registerLink);

        //Now find out if there is a user logged in
        BBTSession sess = (BBTSession) Session.get();
        IModel mdl = null;
        if (sess.isSignedIn()) {
            mdl = new PropertyModel(sess, "currentUser");
        }

        //Add a label in the upper left with the user's name
        add(new Label("current-user", mdl));

        //Add a link for changing the password.  It will take the user to
        //the login page if not logged in
        BookmarkablePageLink pwLink =
                new BookmarkablePageLink("change-password-link",
                ChangePasswordPage.class);
        add (pwLink);

        //Create a logout link, only to be shown if the user is logged in
        BookmarkablePageLink logoutLink =
                new BookmarkablePageLink("logout-link", SignOut.class);
        add (logoutLink);
        //Only show login or logout depending on whether the user is
        //logged in
        loginLink.setVisible (!sess.isSignedIn());
        registerLink.setVisible (!sess.isSignedIn());
        logoutLink.setVisible (sess.isSignedIn());
        pwLink.setVisible (sess.isSignedIn());
    }

}
