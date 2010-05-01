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

import java.util.Date;

import org.apache.wicket.PageParameters;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import com.badbuxtour.application.Application;
import com.badbuxtour.application.BasePage;
import com.badbuxtour.application.UserErrorCode;
import com.google.appengine.api.datastore.Key;

/**
 * Simple example of a sign in page. It extends SignInPage, a base class which
 * provide standard functionality for typical log-in pages
 * 
 * @author eeugene
 */
public final class RegisterPage extends BasePage {

    public RegisterPage(PageParameters params) {
	this();
	info(params.getString("message"));
    }

    public RegisterPage() {
	add(new RegisterPanel("registerPanel") {

	    @Override
	    public UserErrorCode signIn(String lastName, String firstName, String gender, Date birthday, String email,
		    String password, String repeatPassword, String licence, Key club, boolean admin, boolean superAdmin) {
		UserErrorCode result;
		try {
		    result = ((Application) getApplication()).getPlayerController().createUser(lastName, firstName,
			    gender, birthday, email, password, repeatPassword, licence, club, admin, superAdmin);
		} catch (Exception ex) {
		    throw (new WicketRuntimeException(ex));
		}
		return result;
	    }
	});

	// Feedback
	add(new FeedbackPanel("feedback"));
    }
}
