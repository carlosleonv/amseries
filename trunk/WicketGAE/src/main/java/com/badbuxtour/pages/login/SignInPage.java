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

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import com.badbuxtour.application.Application;
import com.badbuxtour.application.BasePage;
import com.badbuxtour.application.UserErrorCode;


/**
 * Simple example of a sign in page. It extends SignInPage, a base class which
 * provide standard functionality for typical log-in pages
 *
 * @author eeugene
 */
public final class SignInPage extends BasePage {

    public SignInPage(PageParameters params) {
        this();
        info(params.getString("message"));
    }
    
    public SignInPage() {
        //The real logic is in these two panels below
        add(new SignInPanel("signInPanel") {

            public UserErrorCode signIn(String emailAddress, String password) {
                UserErrorCode result = ((Application)getApplication()).getPlayerController().authenticate(emailAddress, password);
                return result;
            }
        });

        //This will display strings passed to the error() method of
        //this component or its subcomponents, to display messages like
        //unknown user, etc.
        add (new FeedbackPanel("feedback"));        
    }
}
