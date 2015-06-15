/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

/**
 * Component for defining the filter for the bookmarks to be shown.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@SpringComponent
public class BookmarkFilterComponent extends CustomComponent {
// ------------------------------ FIELDS ------------------------------
    /** Logger */
    private final static Logger logger = LoggerFactory.getLogger(BookmarkFilterComponent.class);
    /** TagList Component for selected tags */
    private final TagListComponent selectedTagList = new TagListComponent();
    /** TagLisComponent for available tags */
    private final TagListComponent availableTagList = new TagListComponent();
    /** the taboo service where new entries are stored */
    @Autowired
    private TabooClient taboo;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * a vertical layout with two Panels: One for the selected tags, one for the available tags.
     */
    public BookmarkFilterComponent() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);

        Button clearSelection = new Button("clear");
        clearSelection.addClickListener(clickEvent -> resetTags());
        layout.addComponent(createTagListPanel("selected tags", selectedTagList, clearSelection));
        layout.addComponent(createTagListPanel("available tags", availableTagList, null));

        selectedTagList.setListener(this::removeTagFromSelection);
        availableTagList.setListener(this::addTagToSelection);

        setCompositionRoot(layout);
    }

    /**
     * adds a given to to the selection.
     *
     * @param tag
     *         the tag to add
     */
    private void addTagToSelection(String tag) {
        logger.info("add tag {}", tag);

        Set<String> tags = new HashSet<>();
        tags.addAll(selectedTagList.getTags());
        tags.add(tag);
        selectedTagList.setTags(tags);

        tags.clear();
        tags.addAll(availableTagList.getTags());
        tags.remove(tag);
        availableTagList.setTags(tags);
    }

    /**
     * creates a Panel that contains the given TagLostComponent.
     *
     * @param caption
     *         Panel caption
     * @param tagListComponent
     *         the component to be contained
     * @param additionalComponent
     *         if not null, this component is added after the taglist
     * @return Panel
     */
    private Panel createTagListPanel(String caption, TagListComponent tagListComponent, Component additionalComponent) {
        Panel panel = new Panel(caption);
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.addComponent(tagListComponent);
        if (null != additionalComponent) {
            layout.addComponent(additionalComponent);
            layout.setComponentAlignment(additionalComponent, Alignment.MIDDLE_CENTER);
        }
        panel.setContent(layout);
        return panel;
    }

    /**
     * removes the given tag from the selection.
     *
     * @param tag
     *         the tag to remove
     */
    private void removeTagFromSelection(String tag) {
        logger.info("remove tag {}", tag);

        Set<String> tags = new HashSet<>();
        tags.addAll(selectedTagList.getTags());
        tags.remove(tag);
        selectedTagList.setTags(tags);

        tags.clear();
        tags.addAll(availableTagList.getTags());
        tags.add(tag);
        availableTagList.setTags(tags);
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * initialize after wiring.
     */
    @PostConstruct
    public void initBean() {
        resetTags();
    }

    /**
     * resets the selected tags list to empty and the available to all that the server knows.
     */
    private void resetTags() {
        selectedTagList.setTags(null);
        availableTagList.setTags(taboo.getTags());
    }
}
