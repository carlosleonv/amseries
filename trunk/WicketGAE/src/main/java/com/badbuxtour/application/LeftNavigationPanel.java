/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.badbuxtour.application;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

import com.badbuxtour.pages.admin.ListPlayersOfMyClub;
import com.badbuxtour.pages.club.CreateClubPanel;
import com.badbuxtour.pages.player.PlayerProfile;

/**
 * 
 * @author eeugene
 */
public final class LeftNavigationPanel extends Panel {

    class MenuItem implements Serializable {

	private String caption;
	private Class destination;
	private boolean adminOnly;
	private boolean superAdminOnly;

	public boolean isAdminOnly() {
	    return adminOnly;
	}

	public boolean isSuperAdminOnly() {
	    return superAdminOnly;
	}

	public void setAmdinOnly(boolean amdinOnly) {
	    this.adminOnly = amdinOnly;
	}

	public String getCaption() {
	    return caption;
	}

	public void setCaption(String caption) {
	    this.caption = caption;
	}

	public Class getDestination() {
	    return destination;
	}

	public void setDestination(Class destination) {
	    this.destination = destination;
	}

	public MenuItem(String caption, Class destination) {
	    this.caption = caption;
	    this.destination = destination;
	    this.adminOnly = this.destination.isAnnotationPresent(RequiresAdmin.class);
	    this.superAdminOnly = this.destination.isAnnotationPresent(RequiresSuperAdmin.class);
	}
    }

    public LeftNavigationPanel(String id) {
	super(id);
	RepeatingView rv = new RepeatingView("menu");
	add(rv);

	List<MenuItem> menuItems = new ArrayList<MenuItem>();
	menuItems.add(new MenuItem("PlayerProfile", PlayerProfile.class));
	menuItems.add(new MenuItem("PlayerProfile", PlayerProfile.class));
	menuItems.add(new MenuItem("PlayerProfile", PlayerProfile.class));
	menuItems.add(new MenuItem("PlayerProfile", PlayerProfile.class));
	menuItems.add(new MenuItem("ListPlayersOfMyClub", ListPlayersOfMyClub.class));
	menuItems.add(new MenuItem("CreateClubPanel", CreateClubPanel.class));

	BBTSession session = (BBTSession) Session.get();
	for (MenuItem i : menuItems) {
	    if (checkVisibility(session, i)) {
		WebMarkupContainer parent = new WebMarkupContainer(rv.newChildId());
		rv.add(parent);
		Link lk = new Link("link", new Model(i.getDestination())) {

		    @Override
		    public void onClick() {
			try {
			    Class classe = (Class) getModelObject();
			    Class[] types = { String.class };
			    Constructor constructeur = classe.getConstructor(types);
			    Object[] params = { "businessPanel" };
			    LeftNavigationPanel.this.getParent().addOrReplace(
				    (Component) constructeur.newInstance(params));
			} catch (Exception ex) {
			    Logger.getLogger(LeftNavigationPanel.class.getName()).log(Level.SEVERE, null, ex);
			}
		    }
		};
		lk.add(new Label("linkName", i.getCaption()));
		parent.add(lk);
	    }
	}
    }

    /**
     * @param session
     * @param i
     * @return
     */
    private boolean checkVisibility(BBTSession session, MenuItem i) {
	if (session.getCurrentUser() == null)
	    return false;
	// Super Admin shows everything
	if (session.getCurrentUser().isSuperAdmin())
	    return true;
	else if (session.getCurrentUser().isAdmin())
	    return !i.isSuperAdminOnly();
	else
	    return (!i.isAdminOnly() && !i.isSuperAdminOnly());
    }
}
