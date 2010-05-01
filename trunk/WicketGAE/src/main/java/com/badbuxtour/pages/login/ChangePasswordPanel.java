/*
 * ChangePasswordPanel.java
 *
 * Created on Aug 11, 2007, 9:12:12 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.badbuxtour.pages.login;

import org.apache.wicket.PageParameters;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.badbuxtour.application.Application;
import com.badbuxtour.application.BBTSession;
import com.badbuxtour.application.HomePage;
import com.badbuxtour.application.RequiresSignin;
import com.badbuxtour.application.UserErrorCode;
import com.badbuxtour.pojo.UserDTO;

/**
 *
 * @author Tim Boudreau
 */
@RequiresSignin
public class ChangePasswordPanel extends Panel {

    public ChangePasswordPanel (String id) {
        super (id);
        add (new F("form"));
    }

    private class F extends Form {
        TextField nameField;
        PasswordTextField old;
        PasswordTextField nue;
        PasswordTextField repeat;
        F(String id) {
            super (id);
            BBTSession sess = (BBTSession) getSession();
            UserDTO user = sess.getCurrentUser();
            assert user != null; //we have are requiring authentication
            
            this.email = user.getMail();
            add (old = new PasswordTextField("old", 
                    new PropertyModel(this, "oldPw")));
            
            add (nue = new PasswordTextField("new", 
                    new PropertyModel(this, "newPw")));
            
            add (repeat = new PasswordTextField("repeat-new", 
                    new PropertyModel(this, "repeatNewPw")));
            
            add (new FeedbackPanel("feedback"));
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getNewPw() {
            return newPw;
        }

        public void setNewPw(String newPw) {
            this.newPw = newPw;
        }

        public String getOldPw() {
            return oldPw;
        }

        public void setOldPw(String oldPw) {
            this.oldPw = oldPw;
        }

        public String getRepeatNewPw() {
            return repeatNewPw;
        }

        public void setRepeatNewPw(String repeateNewPw) {
            this.repeatNewPw = repeateNewPw;
        }

        String email;
        String oldPw;
        String newPw;
        String repeatNewPw;

        @Override
        public void onSubmit() {
            String pw = oldPw;
            String eml = email;
            String nuPass = newPw;
            String repeatPass = repeatNewPw;
            if (!nuPass.equals(repeatPass)) {
                error ("Passwords do not match");
                return;
            }
            if (nuPass.length() < 5) {
                error ("Password too short");                
                return;
            }
            Application app = (Application) getApplication();
            
            UserErrorCode res = ((Application)getApplication()).getPlayerController().authenticate(eml, pw);
            if (res != null) {
                error (res.toString());
                return;
            }
            BBTSession sess = (BBTSession) Session.get();
            UserDTO newUser = sess.getCurrentUser();
            assert newUser != null;
//            newUser.setPassword(nuPass);
            PageParameters pp = new PageParameters();
            pp.put ("message", "Your password has been changed.");            
            setResponsePage(HomePage.class, pp);
        }
    }
}
