/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.badbuxtour.pages.login;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;

import com.badbuxtour.application.Application;
import com.badbuxtour.application.SelectOption;
import com.badbuxtour.application.UserErrorCode;
import com.badbuxtour.controller.ClubJpaController;
import com.badbuxtour.controller.PlayerJpaController;
import com.badbuxtour.dao.Club;
import com.badbuxtour.dao.Player;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * Reusable user sign in panel with username and password as well as support for
 * cookie persistence of the both. When the SignInPanel's form is submitted, the
 * abstract method signIn(String, String) is called, passing the username and
 * password submitted. The signIn() method should sign the user in and return
 * null if no error ocurred, or a descriptive String in the event that the sign
 * in fails.
 * 
 * @author eeugene
 */
public abstract class RegisterPanel extends Panel {

    private RequiredTextField lastName;
    private RequiredTextField firstName;
    private RadioChoice gender;
    private DateTextField birthday;
    private PasswordTextField password;
    private PasswordTextField repeatPassword;
    private RequiredTextField emailAddress;
    private TextField licence;
    // private SelectOption club;
    private DropDownChoice ddc;
    private AjaxCheckBox noclub;
    private CheckBox admin;
    private CheckBox superAdmin;

    public boolean isAdmin() {
	return admin.getModelObject();
    }

    public boolean isSuperAdmin() {
	return superAdmin.getModelObject();
    }

    public Date getBirthday() {
	return birthday.getModelObject();
    }

    public Key getClub() {
	return ddc.getModelObject() != null ? (Key) KeyFactory.stringToKey((String) ((SelectOption) ddc
		.getModelObject()).getKey()) : null;
    }

    public String getGender() {
	return gender.getModelObject().toString();
    }

    public String getLicence() {
	return licence.getModelObject() != null ? licence.getModelObject().toString() : null;
    }

    public boolean isNoclub() {
	return noclub.getModelObject();
    }

    public String getLastName() {
	return lastName.getModelObject().toString();
    }

    public String getFirstName() {
	return firstName.getModelObject().toString();
    }

    public String getPassword() {
	return password.getModelObject().toString();
    }

    public String getRepeatPassword() {
	return repeatPassword.getModelObject().toString();
    }

    public String getEmailAddress() {
	return emailAddress.getModelObject().toString();
    }

    /**
     * @param id
     *            See Component constructor
     * @param includeRememberMe
     *            True if form should include a remember-me checkbox
     * @see org.apache.wicket.Component#Component(String)
     */
    public RegisterPanel(final String id) {
	super(id);

	// Add register form to page, passing feedback panel as
	// validation error handler
	add(new RegisterForm("registerForm"));
    }

    /**
     * Sign in user if possible.
     * 
     * @param username
     *            The username
     * @param password
     *            The password
     * @return True if signin was successful
     */
    public abstract UserErrorCode signIn(String lastName, String firstName, String gender, Date birthday,
	    String emailAddress, String password, String repeatPassword, String licence, Key club, boolean admin,
	    boolean superAdmin);

    /**
     * Sign in form.
     */
    public final class RegisterForm extends Form {

	/** El-cheapo model for form. */
	private final ValueMap properties = new ValueMap();

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            id of the form component
	 */
	public RegisterForm(final String id) {
	    super(id);
	    // Attach textfield components that edit properties map
	    // in lieu of a formal beans model
	    add(lastName = new RequiredTextField("register-lastname", new PropertyModel(properties, "lastName")));
	    add(firstName = new RequiredTextField("register-firstname", new PropertyModel(properties, "firstname")));
	    gender = new RadioChoice("register-gender", new PropertyModel(properties, "gender"),
		    new LoadableDetachableModel() {
			@Override
			protected Object load() {
			    return Arrays.asList("M", "F");
			}
		    });
	    add(gender.setRequired(true));
	    birthday = new DateTextField("register-birthday", new PropertyModel(properties, "birthday"));
	    add(birthday.setRequired(true));
	    add(licence = new TextField("register-licence", new PropertyModel(properties, "licence")));
	    add(emailAddress = new RequiredTextField("register-email", new PropertyModel(properties, "emailAddress")));
	    password = new PasswordTextField("register-password", new PropertyModel(properties, "password"));
	    add(password.setRequired(true));
	    repeatPassword = new PasswordTextField("repeat-password", new PropertyModel(properties, "password"));
	    add(repeatPassword.setRequired(true));
	    ddc = new DropDownChoice("register-club", new PropertyModel(properties, "club"),
		    new LoadableDetachableModel() {
			@Override
			protected Object load() {
			    return getClubController().fetchClubsForList();
			}
		    }, new ChoiceRenderer("value", "key"));
	    ddc.setOutputMarkupId(true);
	    add(ddc.setRequired(true));
	    add(noclub = new AjaxCheckBox("register-noclub", new PropertyModel(properties, "noclub")) {
		@Override
		protected void onUpdate(AjaxRequestTarget target) {
		    ddc.setRequired(!getConvertedInput());
		    target.addComponent(ddc);
		}
	    });
	    add(admin = new CheckBox("register-admin", new PropertyModel(properties, "admin")));
	    add(superAdmin = new CheckBox("register-superAdmin", new PropertyModel(properties, "superAdmin")));
	}

	/**
	 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
	 */
	@Override
	public final void onSubmit() {
	    String email = getEmailAddress();
	    String pword = getPassword();
	    String firstName = getFirstName();
	    Key clubKey = getClub();
	    UserErrorCode signInResult = signIn(getLastName(), getFirstName(), getGender(), getBirthday(), email,
		    pword, getRepeatPassword(), getLicence(), clubKey, isAdmin(), isSuperAdmin());

	    if (signInResult == null) {

		// add player to club
//		    if (club != null) {
//			List<Player> players = club.getPlayerCollection();
//			if (players != null) {
//			    List<Player> p;
//			    try {
//				p = getPlayerController().findByMail(email);
//				if (p != null && p.size() == 1) {
//				    players.add(p.get(0));
////				    getClubController().edit(club);
//				}
//			    } catch (Exception e) {
//				throw (new WicketRuntimeException(e));
//			    }
//			} else {
//			    try {
//				players = getPlayerController().findByMail(email);
//				club.setPlayerCollection(players);
////				getClubController().edit(club);
//			    } catch (Exception e) {
//				throw (new WicketRuntimeException(e));
//			    }
//			}
//		    }

		// signInResult = ((Application)
		// getApplication()).getPlayerController().authenticate(email,
		// pword);
		// assert signInResult == null : "Newly created user id " +
		// email
		// + "could not be logged in: " + signInResult;
		// If login has been called because the user was not yet
		// logged in, than continue to the original destination,
		// otherwise to the Home page
		// if (!continueToOriginalDestination()) {
		// Could construct a HomePage instance here, but it is better
		// to be in the habit of using PageParameters, as they can
		// also be used to construct links
		PageParameters urlParams = new PageParameters();
		urlParams.add("message",
			"Votre demande d'inscription a bien été enregistrée ! Votre compte est en attente de validation par "
				+ (isNoclub() ? " un responsable du site" : "votre responsable de club") + ".");
		setResponsePage(SignInPage.class, urlParams);
		// }
	    } else {
		// Try the component based localizer first. If not found try the
		// application localizer. Else use the default
		final String errmsg = signInResult.toString();// getLocalizer().getString("loginError",
		// this,
		// "Unable to sign you in");
		// Display it in the feedbackpanel in SignInPage
		error(errmsg);
	    }
	}

	private PlayerJpaController getPlayerController() {
	    return ((Application) getApplication()).getPlayerController();
	}

	private ClubJpaController getClubController() {
	    return ((Application) getApplication()).getClubController();
	}
    }
}
