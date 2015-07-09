/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Main UI class. Annotated as @Push, so that afetr UI.access() calls, the content is pushed to the client
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@Theme("taboo")
@SpringUI
@Push
public class ClientUI extends UI {
// ------------------------------ FIELDS ------------------------------

    private static final String TABOO = "taboo";

    /** componente for entering a new bookmark. */
    @Autowired
    private EntryFormComponent entryFormComponent;

    /** component for defining the fileter for the bookmarks to display. */
    @Autowired
    private BookmarkFilterComponent bookmarkFilterComponent;

    /** component for showing the bookmarks */
    @Autowired
    private BookmarkTableComponent bookmarkTableComponent;

// -------------------------- STATIC METHODS --------------------------

    /**
     * shows the message of an exception in a notification
     *
     * @param e
     *         Exception
     */
    public static void handleException(Exception e) {
        Notification notification = new Notification("Error", e.getMessage(), Notification.Type.ERROR_MESSAGE);
        notification.setDelayMsec(-1);
        notification.show(Page.getCurrent());
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        getPage().setTitle(TABOO);

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);

        layout.addComponent(createEntryComponent());

        Component bookmarkComponent = createBookmarkComponent();
        layout.addComponent(bookmarkComponent);
        bookmarkComponent.setHeight("100%");
        layout.setExpandRatio(bookmarkComponent, 1);
    }

    /**
     * creates the component for the bookmarks. Contains a BookmarkTableComponent and a BookmarkFilterComponent.
     *
     * @return new Component
     */
    private Component createBookmarkComponent() {
        Panel panel = new Panel("bookmarks");
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("100%");
        layout.setSpacing(true);
        layout.setMargin(true);

        layout.addComponent(bookmarkTableComponent);
        layout.setExpandRatio(bookmarkTableComponent, 3);

        layout.addComponent(bookmarkFilterComponent);
        layout.setExpandRatio(bookmarkFilterComponent, 1);

        panel.setContent(layout);
        return panel;
    }

    /**
     * creates the component where the user can add bookmarks with tags
     *
     * @return new Component
     */
    private Component createEntryComponent() {
        Panel panel = new Panel("new bookmark");
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.addComponent(entryFormComponent);
        panel.setContent(layout);
        return panel;
    }
}
