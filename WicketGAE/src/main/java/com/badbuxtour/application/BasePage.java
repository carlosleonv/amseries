/*
 * WicketExamplePage.java
 *
 * Created on 17 janvier 2010, 14:01
 */
 
package com.badbuxtour.application;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.markup.html.resources.StyleSheetReference;

/** 
 *
 * @author eeugene
 * @version 
 */

public class BasePage extends WebPage {

    /**
     * Constructor
     */
    public BasePage() {
        this(null);
    }
    
    /**
     * Construct.
     * @param model
     */
    public BasePage(IModel model) {
        super(model);
        add(new HeaderPanel("mainNavigation"));
        add(new StyleSheetReference("stylesheet", BasePage.class, "style.css"));
        LeftNavigationPanel lnp = new LeftNavigationPanel("leftNavigation");
        add(lnp);
        BBTSession sess = (BBTSession) Session.get();
        lnp.setVisible(sess.isSignedIn());
    }
}
