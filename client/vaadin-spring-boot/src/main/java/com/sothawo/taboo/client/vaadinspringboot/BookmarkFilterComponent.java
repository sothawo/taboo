/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import com.google.common.collect.Sets;
import com.sothawo.taboo.common.Bookmark;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
    /** the taboo service */
    @Autowired
    private TabooClient taboo;
    /** the component for showing the bookmarks */
    @Autowired
    private BookmarkTableComponent bookmarkTableComponent;

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

// -------------------------- OTHER METHODS --------------------------

    /**
     * adds a given to to the selection.
     *
     * @param tag
     *         the tag to add
     */
    private void addTagToSelection(String tag) {
        logger.info("add tag {}", tag);

        // first get the set of selected tags
        final Set<String> selectedTags = new HashSet<>();
        selectedTags.addAll(selectedTagList.getTags());
        selectedTags.add(tag);
        setSelectedTags(selectedTags);
    }

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
        setSelectedTags(Collections.EMPTY_LIST);
        availableTagList.setTags(taboo.getTags());
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
        setSelectedTags(tags);
    }

    /**
     * sets the selected tags list, loads the bookmarks, forwards them to the display and sets the available tags list.
     *
     * @param tags
     *         the selected tags
     */
    public void setSelectedTags(Collection<String> tags) {
        Set<String> selectedTags = new HashSet<>();
        selectedTags.addAll(tags);
        selectedTagList.setTags(selectedTags);
        loadBookmarksForSelectedTags();
    }

    /**
     * load the bookmarks for the selected tags and adjusts the list of available tags.
     */
    public void loadBookmarksForSelectedTags() {
        Set<String> selectedTags = new HashSet<>(selectedTagList.getTags());
        // collect the tags from the bookmarks, remove the tags that are already selected and set in the available
        // tags list get the bookmarks and set the set of selected tags
        Collection<Bookmark> bookmarks = taboo.getBookmarks(selectedTags);
        bookmarkTableComponent.setBookmarks(bookmarks);
        availableTagList.setTags(Sets.difference(
                bookmarks.stream().flatMap(bookmark -> bookmark.getTags().stream()).collect(Collectors.toSet()),
                selectedTags));
    }
}
