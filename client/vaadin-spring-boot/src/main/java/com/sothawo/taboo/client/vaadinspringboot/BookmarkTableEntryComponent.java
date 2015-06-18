/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import com.sothawo.taboo.common.Bookmark;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
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
        layout.addStyleName("bookmark-entry");

        Link link = new Link(bookmark.getUrl(), new ExternalResource(bookmark.getUrl()));
        link.setStyleName("bookmark-url");
        // Open the URL in a new window/tab
        link.setTargetName("_blank");
        layout.addComponent(link);

        Label tags = new Label(String.join(", ", bookmark.getTags()));
        tags.setStyleName("bookmark-tags");
        layout.addComponent(tags);

        setCompositionRoot(layout);
    }
}
