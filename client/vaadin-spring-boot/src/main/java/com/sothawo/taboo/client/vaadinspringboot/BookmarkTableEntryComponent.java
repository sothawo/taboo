/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import com.sothawo.taboo.common.Bookmark;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Component to display a Bookmark in the Bookmarks table..
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public class BookmarkTableEntryComponent extends CustomComponent {
// ------------------------------ FIELDS ------------------------------

    private final Bookmark bookmark;

// --------------------------- CONSTRUCTORS ---------------------------

    public BookmarkTableEntryComponent(Bookmark bookmark) {
        this.bookmark = bookmark;
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        layout.addComponent(new Label(bookmark.getUrl()));

        setCompositionRoot(layout);
    }
}
