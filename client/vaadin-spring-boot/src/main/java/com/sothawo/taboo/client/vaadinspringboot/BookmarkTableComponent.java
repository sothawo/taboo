/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import com.sothawo.taboo.common.Bookmark;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.Collection;
import java.util.Objects;

/**
 * Component for showing the bookmarks.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@SpringComponent
public class BookmarkTableComponent extends CustomComponent {
// ------------------------------ FIELDS ------------------------------

    /** the item id for the bookmark column in the table */
    private static final String BOOKMARK_ITEM_ID = "bookmark";

    /** the taboo service */
    @Autowired
    private TabooClient taboo;

    /** the filter definition component. */
    @Autowired
    private BookmarkFilterComponent bookmarkFilterComponent;

    /** the table for the bookmarks */
    private final Table table;

    /** Vaadin Container for the table */
    private final IndexedContainer container = new IndexedContainer();

// --------------------------- CONSTRUCTORS ---------------------------

    public BookmarkTableComponent() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        table = new Table();
        table.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);
        table.addStyleName("bookmarks-table");

        // one column with a custom component
        container.addContainerProperty("bookmark", BookmarkTableEntryComponent.class, null);
        table.setContainerDataSource(container);

        layout.addComponent(table);
        table.setWidth("100%");
        layout.setExpandRatio(table, 1);

        // set table length to table size and let the surrounding container scroll
        table.setPageLength(table.size());

        setCompositionRoot(layout);
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * delete the given bookmark after confirmation.
     *
     * @param bookmark
     */
    public void deleteBookmark(Bookmark bookmark) {
        ConfirmDialog.show(UI.getCurrent(), "Delete bookmark '" + bookmark.getUrl() + "'?",
                (ConfirmDialog.Listener) dialog -> {
                    if (dialog.isConfirmed()) {
                        try {
                            // Confirmed to continue, delete
                            taboo.deleteBookmark(bookmark);
                            // reload the remaining bookmarks
                            bookmarkFilterComponent.loadBookmarksForSelectedTags();
                        } catch (RuntimeException e) {
                            ClientUI.handleException(e);
                        }
                    }
                });
    }

    /**
     * sets the given bookmarks in the table
     *
     * @param bookmarks
     *         the bookmarks to show
     */
    public void setBookmarks(Collection<Bookmark> bookmarks) {
        Objects.requireNonNull(bookmarks);
        try {
            container.removeAllItems();
            bookmarks.stream().map((bookmark) -> new BookmarkTableEntryComponent(this, bookmark)).forEach(component ->
                    container.addItem(component)
                            .getItemProperty(BOOKMARK_ITEM_ID).setValue(component));
        } catch (Exception e) {
            ClientUI.handleException(e);
        }
    }
}
