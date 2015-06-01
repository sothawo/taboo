/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Component for defining the filter for the bookmarks to be shown.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@SpringComponent
public class BookmarkFilterComponent extends CustomComponent {
// --------------------------- CONSTRUCTORS ---------------------------

    public BookmarkFilterComponent() {
        VerticalLayout layout = new VerticalLayout();

        Label label = new Label("Filter definition");
        label.setSizeUndefined(); // shrink
        layout.addComponent(label);

        // set all to undefined sto shrink to needed space
        layout.setSizeUndefined();
        setSizeUndefined();

        setCompositionRoot(layout);
    }
}
