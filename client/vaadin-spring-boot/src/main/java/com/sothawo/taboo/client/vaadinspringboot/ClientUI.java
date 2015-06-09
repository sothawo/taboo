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

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.sothawo.taboo.common.BookmarkBuilder.aBookmark;

/**
 * Main UI class.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@Theme("taboo")
@SpringUI
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

        bookmarkTableComponent.setBookmarks(
                IntStream.rangeClosed(1, 50).mapToObj(i -> aBookmark().withUrl("http://www.sothawo.com/" + i).build())
                        .collect(Collectors.toList()));
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
//        bookmarkTableComponent.setWidth("80%");
        layout.setExpandRatio(bookmarkTableComponent, 1);

        layout.addComponent(bookmarkFilterComponent);
//        bookmarkFilterComponent.setWidth("20%");
//        layout.setExpandRatio(bookmarkFilterComponent, 1);

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
