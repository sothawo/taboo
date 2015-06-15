/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import java.util.Arrays;

/**
 * Component for defining the filter for the bookmarks to be shown.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@SpringComponent
public class BookmarkFilterComponent extends CustomComponent {
// ------------------------------ FIELDS ------------------------------

    /** TagList Component for selected tags */
    private final TagListComponent selectedTagList = new TagListComponent();
    /** TagLisComponent for available tags */
    private final TagListComponent availableTagList = new TagListComponent();

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * a vertical layout with two Panels: One for the selected tags, one for the available tags.
     */
    public BookmarkFilterComponent() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);

        layout.addComponent(createTagListPanel("selected tags", selectedTagList));
        selectedTagList.setTags(Arrays.asList("this", "are", "some", "tags", "that", "show", "up", "on", "the",
                "right", "side"));

        layout.addComponent(createTagListPanel("available tags", availableTagList));
        availableTagList.setTags(Arrays.asList("these", "are", "the", "tags", "that", "are", "available"));

        // set all to undefined to shrink to needed space
        layout.setSizeUndefined();
        setSizeUndefined();

        setCompositionRoot(layout);
    }

    /**
     * creates a Panle that contains the given TagLostComponent.
     * @param caption Panel caption
     * @param tagListComponent the component to be contained
     * @return Panel
     */
    private Panel createTagListPanel(String caption, TagListComponent tagListComponent) {
        Panel panel = new Panel(caption);
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.addComponent(tagListComponent);
        panel.setContent(layout);
        return panel;
    }
}
