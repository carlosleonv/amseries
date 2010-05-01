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
package com.badbuxtour.pages.club;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;

import com.badbuxtour.application.Application;
import com.badbuxtour.application.RequiresSignin;
import com.badbuxtour.application.RequiresSuperAdmin;
import com.badbuxtour.application.UserErrorCode;

/**
 * 
 * @author eeugene
 */
@RequiresSignin
@RequiresSuperAdmin
public final class CreateClubPanel extends Panel {

    private RequiredTextField idClub;
    private RequiredTextField name;
    private TextField phone;
    private TextField emailAddress;
    private TextField webSite;

    public String getWebSite() {
	return webSite.getModelObject() != null ? webSite.getModelObject().toString() : null;
    }

    public String getIdClub() {
	return idClub.getModelObject().toString();
    }

    public String getName() {
	return name.getModelObject().toString();
    }

    public String getPhone() {
	return phone.getModelObject() != null ? phone.getModelObject().toString() : null;
    }

    public String getEmailAddress() {
	return emailAddress.getModelObject() != null ? emailAddress.getModelObject().toString() : null;
    }

    public CreateClubPanel(String id) {
	super(id);
	// Feedback
	add(new FeedbackPanel("feedback"));
	add(new CreateClubForm("createClubForm"));
    }

    public final class CreateClubForm extends Form {

	/** El-cheapo model for form. */
	private final ValueMap properties = new ValueMap();

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            id of the form component
	 */
	public CreateClubForm(final String id) {
	    super(id);
	    add(idClub = new RequiredTextField("club-idClub", new PropertyModel(properties, "idClub")));
	    add(name = new RequiredTextField("club-name", new PropertyModel(properties, "name")));
	    add(phone = new TextField("club-phone", new PropertyModel(properties, "phone1")));
	    add(emailAddress = new TextField("club-email", new PropertyModel(properties, "emailAddress")));
	    add(webSite = new TextField("club-webSite", new PropertyModel(properties, "webSite")));
	}

	/**
	 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
	 */
	@Override
	public final void onSubmit() {
	    try {
		UserErrorCode result = ((Application) getApplication()).getClubController().createClub(getIdClub(),
			getName(), getPhone(), getEmailAddress(), getWebSite());

		if (result == null) {
		    info("Club créé");
		    clearForm();
		} else {
		    final String errmsg = result.toString();
		    error(errmsg);
		}
	    } catch (Exception ex) {
		throw (new WicketRuntimeException(ex));
	    }
	}

	private void clearForm() {
	    properties.clear();
	}
    }
}
