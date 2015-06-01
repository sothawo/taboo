/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * Component for showing the bookmarks.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@SpringComponent
public class BookmarkTableComponent extends CustomComponent {
    /** the table for the bookmarks */
    private final Table table;
// --------------------------- CONSTRUCTORS ---------------------------

    public BookmarkTableComponent() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        table = new Table();
        table.addStyleName("bookmarks-table");
        table.addContainerProperty("URL", String.class, null);
        for (int i = 1; i <= 50; i++) {
            table.addItem(new Object[]{"http://www.sotahwo.com/" + i}, i);
        }
        layout.addComponent(table);
        table.setWidth("100%");
        layout.setExpandRatio(table, 1);

        // set table length to table size and let the surrounding container scroll
        table.setPageLength(table.size());

        setCompositionRoot(layout);
    }
}
