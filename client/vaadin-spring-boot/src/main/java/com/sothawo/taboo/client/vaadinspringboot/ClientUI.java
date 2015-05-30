/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Main UI class.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@Theme("valo")
@SpringUI
public class ClientUI extends UI {
// ------------------------------ FIELDS ------------------------------

    private static final String TABOO = "taboo";

    @Autowired
    private EntryFormComponent entryFormComponent;

// -------------------------- OTHER METHODS --------------------------

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        getPage().setTitle(TABOO);

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);

        Label tabooLabel = new Label(TABOO);
        layout.addComponent(tabooLabel);
        layout.addComponent(createEntryComponent());

        Component bookmarkComponent = createBookmarkComponent();
        layout.addComponent(bookmarkComponent);
        bookmarkComponent.setHeight("100%");
        layout.setExpandRatio(bookmarkComponent, 1);
    }

    /**
     * creates the component for the existing bookmarks.
     * @return new Component
     */
    private Component createBookmarkComponent() {
        Panel panel = new Panel("bookmarks");
        return panel;
    }

    /**
     * creates the component where the user can add bookmarks with tags
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
