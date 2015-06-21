/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import com.sothawo.taboo.common.Bookmark;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.dialogs.ConfirmDialog;

/**
 * Component to display a Bookmark in the Bookmarks table. On the right are buttons for edit and delete, the main area
 * has the bookmark url and the tags.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public class BookmarkTableEntryComponent extends CustomComponent {
// ------------------------------ FIELDS ------------------------------

    private final Bookmark bookmark;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * creates an entry component.
     *
     * @param bookmarkTableComponent
     *         the containe riof this object
     * @param bookmark
     *         the bookmark to show
     */
    public BookmarkTableEntryComponent(BookmarkTableComponent bookmarkTableComponent, Bookmark bookmark) {
        this.bookmark = bookmark;
        HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setWidth("100%");

        // vertical layout with the bookmarks elements
        VerticalLayout bookmarkLayout = new VerticalLayout();
        bookmarkLayout.addStyleName("bookmark-entry");

        Link link = new Link(bookmark.getUrl(), new ExternalResource(bookmark.getUrl()));
        link.setStyleName("bookmark-url");
        // Open the URL in a new window/tab
        link.setTargetName("_blank");
        bookmarkLayout.addComponent(link);

        Label tags = new Label(String.join(", ", bookmark.getTags()));
        tags.setStyleName("bookmark-tags");
        bookmarkLayout.addComponent(tags);

        bookmarkLayout.setWidth("100%");
        hLayout.addComponent(bookmarkLayout);

        // vertical layout with buttons
        VerticalLayout buttonLayout = new VerticalLayout();
        buttonLayout.setWidth("40px");


        // Button to edit
        Button button = new Button(FontAwesome.PENCIL_SQUARE_O);
        button.setWidth("100%");
        button.addStyleName(ValoTheme.BUTTON_TINY);
        buttonLayout.addComponent(button);

        // button to delete
        button = new Button(FontAwesome.TRASH_O);
        button.setWidth("100%");
        button.addStyleName(ValoTheme.BUTTON_TINY);
        buttonLayout.addComponent(button);
        button.addClickListener(clickEvent -> bookmarkTableComponent.deleteBookmark(bookmark));

        hLayout.addComponent(buttonLayout);
        hLayout.setExpandRatio(bookmarkLayout, 1.0f);

        setCompositionRoot(hLayout);
    }
}
