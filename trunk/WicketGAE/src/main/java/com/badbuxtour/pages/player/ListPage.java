/*
 * ListPage.java
 *
 * Created on November 22, 2009, 9:07 PM
 */
package com.badbuxtour.pages.player;

import java.util.Iterator;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.badbuxtour.application.Application;
import com.badbuxtour.application.BasePage;
import com.badbuxtour.application.RequiresSignin;
import com.badbuxtour.controller.PlayerJpaController;
import com.badbuxtour.dao.Player;

@RequiresSignin
public class ListPage extends BasePage {

    private static final long serialVersionUID = 1L;
    private static final int ROWS_PER_PAGE = 5;

    public ListPage() {

        // create a Data Provider
        IDataProvider itemDataProvider = new IDataProvider<Player>() {

            public Iterator iterator(int first, int count) {
                return ((Application)getApplication()).getPlayerController().findEntities(count, first).iterator();
            }

            public int size() {
                return ((Application)getApplication()).getPlayerController().findEntities().size();
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
//                repItem.add(ItemDetails.link("details", player));
                repItem.add(new Label("id"));
                repItem.add(new Label("login"));
                repItem.add(new Label("lastname"));
                repItem.add(new Label("firstname"));
            }
        };
        add(dataView);
        PagingNavigator pager = new PagingNavigator("pager", dataView);
        add(pager);

    }

    private int TestDatabase() {
        PlayerJpaController itemCont = new PlayerJpaController();
        int i = itemCont.findEntities().size();
        System.out.println(" returned "
                + Integer.valueOf(i) + " Player entities");
        return i;
    }
}
