package com.badbuxtour.pages.login;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;

import com.badbuxtour.application.UserErrorCode;

/**
 * Reusable user sign in panel with username and password as well as support for
 * cookie persistence of the both. When the SignInPanel's form is submitted, the
 * abstract method signIn(String, String) is called, passing the username and
 * password submitted. The signIn() method should sign the user in and return
 * null if no error ocurred, or a descriptive String in the event that the sign
 * in fails.
 * 
 * @author Jonathan Locke
 * @author Juergen Donnerstag
 * @author Eelco Hillenius
 * @author Tim Boudreau
 */
public abstract class SignInPanel extends Panel {

    /** True if the panel should display a remember-me checkbox */
    private boolean includeRememberMe = true;
    /** Field for password. */
    private PasswordTextField password;
    /** True if the user should be remembered via form persistence (cookies) */
    private boolean rememberMe = true;
    /** Field for user name. */
    private TextField username;

    /**
     * @see org.apache.wicket.Component#Component(String)
     */
    public SignInPanel(final String id) {
	this(id, true);
    }

    /**
     * @param id
     *            See Component constructor
     * @param includeRememberMe
     *            True if form should include a remember-me checkbox
     * @see org.apache.wicket.Component#Component(String)
     */
    public SignInPanel(final String id, final boolean includeRememberMe) {
	super(id);
	this.includeRememberMe = includeRememberMe;
	add(new SignInForm("signInForm", includeRememberMe));
    }

    /**
     * Removes persisted form data for the signin panel (forget me)
     */
    public final void forgetMe() {
	// Remove persisted user data. Search for child component
	// of type SignInForm and remove its related persistence values.
	getPage().removePersistedFormData(SignInPanel.SignInForm.class, true);
    }

    /**
     * Convenience method to access the password.
     * 
     * @return The password
     */
    public String getPassword() {
	return password.getModelObject().toString();
    }

    /**
     * Get model object of the rememberMe checkbox
     * 
     * @return True if user should be remembered in the future
     */
    public boolean getRememberMe() {
	return rememberMe;
    }

    /**
     * Convenience method to access the username.
     * 
     * @return The user name
     */
    public String getUsername() {
	return username.getModelObject().toString();
    }

    /**
     * Convenience method set persistence for username and password.
     * 
     * @param enable
     *            Whether the fields should be persistent
     */
    public void setPersistent(boolean enable) {
	username.setPersistent(enable);
	password.setPersistent(enable);
    }

    /**
     * Set model object for rememberMe checkbox
     * 
     * @param rememberMe
     */
    public void setRememberMe(boolean rememberMe) {
	this.rememberMe = rememberMe;
	setPersistent(rememberMe);
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
    public abstract UserErrorCode signIn(final String username, final String password);

    /**
     * Sign in form.
     */
    public final class SignInForm extends Form {

	/** El-cheapo model for form. */
	private final ValueMap properties = new ValueMap();

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            id of the form component
	 */
	public SignInForm(final String id, final boolean includeRememberMe) {
	    super(id);

	    // Attach textfield components that edit properties map
	    // in lieu of a formal beans model
	    add(username = new TextField("username", new PropertyModel(properties, "username")));

	    add(password = new PasswordTextField("password", new PropertyModel(properties, "password")) {
		@Override
		protected boolean supportsPersistence() {
		    return true;
		}
	    });

	    // MarkupContainer row for remember me checkbox
	    WebMarkupContainer rememberMeRow = new WebMarkupContainer("rememberMeRow");

	    add(rememberMeRow);
	    // Add rememberMe checkbox
	    rememberMeRow.add(new CheckBox("rememberMe", new PropertyModel(SignInPanel.this, "rememberMe")));

	    // Make form values persistent
	    setPersistent(rememberMe);
	    // Show remember me checkbox?
	    rememberMeRow.setVisible(includeRememberMe);
	}

	/**
	 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
	 */
	@Override
	public final void onSubmit() {
	    UserErrorCode signInResult = signIn(getUsername(), getPassword());
	    if (signInResult == null) {
		// If login has been called because the user was not yet
		// logged in, than continue to the original destination,
		// otherwise to the Home page
		if (!continueToOriginalDestination()) {
		    // BBTSession sess = (BBTSession) getSession();
		    // User user = sess.getCurrentUser();
		    // String userName = user.getName();
		    // PageParameters urlParams = new PageParameters();
		    // urlParams.add ("message", "Welcome, " + userName + "!");
		    // setResponsePage(getApplication().getHomePage(),
		    // urlParams);
		    setResponsePage(getApplication().getHomePage());
		}
	    } else {
		// Try the component based localizer first. If not found try the
		// application localizer. Else use the default
		final String errmsg = signInResult.toString();
		// getLocalizer().getString("loginError", this,
		// "Unable to sign you in");
		error(errmsg);
	    }
	}
    }
}
