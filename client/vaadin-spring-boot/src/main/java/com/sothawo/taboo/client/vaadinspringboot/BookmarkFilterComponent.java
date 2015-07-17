/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import com.google.common.collect.Sets;
import com.sothawo.taboo.common.Bookmark;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
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
    /** TagListComponent for available tags */
    private final TagListComponent availableTagList = new TagListComponent();
    /** the taboo service */
    @Autowired
    private TabooClient taboo;
    /** the component for showing the bookmarks */
    @Autowired
    private BookmarkTableComponent bookmarkTableComponent;
    /** TextField to search in the bookmarks. */
    private TextField searchField;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * a vertical layout with two Panels: One for the selected tags, one for the available tags.
     */
    public BookmarkFilterComponent() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);

        Button clearSelection = new Button("clear selection");
        clearSelection.addClickListener(clickEvent -> clearSelection());
        clearSelection.setWidth("100%");
        layout.addComponent(clearSelection);

        layout.addComponent(createSearchPanel());

        layout.addComponent(createTagListPanel("selected tags", selectedTagList, null));
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

    /**
     * creates a Panel with a TextField to search in the bookmark's titles.
     *
     * @return Panel component
     */
    private Panel createSearchPanel() {
        Panel panel = new Panel("search...");
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        searchField = new TextField();
        searchField.addTextChangeListener(evt -> loadBookmarksForTagsAndSelection(selectedTagList.getTags(), evt
                .getText()));
        searchField.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.LAZY);
        searchField.setWidth("100%");

        layout.addComponent(searchField);

        panel.setContent(layout);
        return panel;
    }

    /**
     * loads the bookmarks for the given selection.
     */
    public void reloadBookmarks() {
        logger.debug("reloading bookmarks");
        loadBookmarksForTagsAndSelection(selectedTagList.getTags(), searchField.getValue());

    }

    /**
     * loads the bookmarks for the given tags and selection.
     *
     * @param tags
     *         the tags
     * @param search
     *         the search string
     */
    private void loadBookmarksForTagsAndSelection(final Collection<String> tags, final String search) {
        // when no tags are selected and no searchfield is set then load all the tags
        // do that in background, to show use UI.access() to trigger push
        if (Objects.requireNonNull(tags).isEmpty() && Objects.requireNonNull(search).isEmpty()) {
            CompletableFuture.supplyAsync(taboo::getTags)
                    .thenAccept(allTags -> getUI().access(() -> {
                        availableTagList.setTags(allTags);
                        setBookmarksToShow(Collections.EMPTY_LIST);
                    }));
        } else {
            CompletableFuture.supplyAsync(() -> taboo.getBookmarks(tags, search))
                    .thenAccept(bookmarks -> getUI().access(() -> setBookmarksToShow(bookmarks)));}
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

        // first get the set of selected tags, add the new one and set them
        final Set<String> selectedTags = new HashSet<>();
        selectedTags.addAll(selectedTagList.getTags());
        selectedTags.add(tag);
        selectedTagList.setTags(selectedTags);
        reloadBookmarks();
    }

    /**
     * initialize after wiring.
     */
    @PostConstruct
    public void initBean() {
        // wait with initialize until we are attached
        addAttachListener(attachEvent -> clearSelection());
    }

    /**
     * clears the selection criteria. resets the selected tags list to empty by calling #setSelectedTags() with an empty
     * list.
     */
    private void clearSelection() {
        searchField.setValue("");
        setSelectedTags(Collections.EMPTY_LIST);
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
        reloadBookmarks();
    }

    /**
     * sets the bookmarks in the bookmark table component and updates the tag selection listst so that the available
     * tags are the ones from the bookmarks without the already selected ones.
     *
     * @param bookmarks
     *         the bookmarks to show
     */
    private void setBookmarksToShow(Collection<Bookmark> bookmarks) {
        logger.debug("showing {} bookmark(s)", bookmarks.size());
        bookmarkTableComponent.setBookmarks(bookmarks);
        Set<String> selectedTags = new HashSet<>(selectedTagList.getTags());
        // collect the tags from the bookmarks, remove the tags that are already selected and set in the available
        // tags list get the bookmarks and set the set of selected tags
        if (bookmarks.size() > 0) {
            availableTagList.setTags(Sets.difference(
                    bookmarks.stream().flatMap(bookmark -> bookmark.getTags().stream()).collect(Collectors.toSet()),
                    selectedTags));
        }
    }
}
