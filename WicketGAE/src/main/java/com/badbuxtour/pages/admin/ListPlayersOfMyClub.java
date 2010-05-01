/*
 * ListPage.java
 *
 * Created on November 22, 2009, 9:07 PM
 */
package com.badbuxtour.pages.admin;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.badbuxtour.application.Application;
import com.badbuxtour.application.BBTSession;
import com.badbuxtour.application.RequiresAdmin;
import com.badbuxtour.application.RequiresSignin;
import com.badbuxtour.controller.ClubJpaController;
import com.badbuxtour.controller.PlayerJpaController;
import com.badbuxtour.dao.Player;
import com.google.appengine.api.datastore.Key;

@RequiresSignin
@RequiresAdmin
public class ListPlayersOfMyClub extends Panel {

    private static final long serialVersionUID = 1L;
    private static final int ROWS_PER_PAGE = 15;

    private Key getPlayerId() {
	return ((BBTSession) Session.get()).getCurrentUser().getPlayerId();
    }

    public ListPlayersOfMyClub(String id) {
	super(id);

	final Player p = getPlayerController().findEntity(getPlayerId());
	
	// create a Data Provider
	IDataProvider itemDataProvider = new IDataProvider<Player>() {

	    public Iterator iterator(int first, int count) {
		try {
		    return getClubController().playersOfMyClub(p).iterator();
		} catch (Exception e) {
		    throw (new WicketRuntimeException(e));
		}
	    }

	    public int size() {
		try {
		    return getClubController().playersOfMyClub(p).size();
		} catch (Exception e) {
		    throw (new WicketRuntimeException(e));
		}
	    }

	    public IModel model(final Player object) {
		return new LoadableDetachableModel() {
		    @Override
		    protected Player load() {
			return (Player) object;
		    }
		};
	    }

	    public void detach() {
	    }
	};

	DataView dataView = new DataView<Player>("rows", itemDataProvider, ROWS_PER_PAGE) {

	    @Override
	    protected void populateItem(Item<Player> repItem) {
		Player player = (Player) repItem.getModelObject();
		repItem.setModel(new CompoundPropertyModel<Player>(player));
		repItem.add(new Label("lastName"));
		repItem.add(new Label("firstName"));
		repItem.add(new Label("licenceNumber"));
		Link lk = new Link("activateLink", repItem.getModel()) {

		    @Override
		    public void onClick() {
			try {
			    Player p = (Player) getModelObject();
			    p.setIsActive((p.getIsActive() + 1) % 1);
			    getPlayerController().edit(p);
			} catch (Exception ex) {
			    Logger.getLogger(ListPlayersOfMyClub.class.getName()).log(Level.SEVERE, null, ex);
			}
		    }
		};
		lk.add(new Label("activateLabel", (player.getIsActive().equals(1) ? "désactiver" : "activer")));
		repItem.add(lk);
	    }
	};
	add(dataView);
	PagingNavigator pager = new PagingNavigator("pager", dataView);
	add(pager);

    }

    private PlayerJpaController getPlayerController() {
	return ((Application) getApplication()).getPlayerController();
    }

    private ClubJpaController getClubController() {
	return ((Application) getApplication()).getClubController();
    }
}
