/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import com.sothawo.taboo.common.TagUtil;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * Component for entering new bookmark data.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@SpringComponent
public class EntryFormComponent extends CustomComponent {
// ------------------------------ FIELDS ------------------------------

    /** TextField that will bind to a 'bookmark' property */
    @PropertyId("bookmark") // not necessary when TextField is named like property
    private TextField bookmark = new TextField("bookmark");

    /** TextField that will bind to a 'tags' property */
    @PropertyId("tags")
    private TextField tags = new TextField("tags");

    /** the data object that gets the entered data */
    private EntryData data;

    /** the taboo service where new entries are stored */
    @Autowired
    private TabooClient taboo;

    /** the TableComponent */
    @Autowired
    private BookmarkTableComponent bookmarkTableComponent;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * creates a EntryForm. Layout is horizontal.
     */
    public EntryFormComponent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("100%");
        layout.setSpacing(true);

        layout.addComponent(bookmark);
        layout.addComponent(tags);
        bookmark.setWidth("100%");
        layout.setExpandRatio(bookmark, 2);
        tags.setWidth("100%");
        layout.setExpandRatio(tags, 1);

        data = new EntryData();
        BeanItem<EntryData> entryDataBeanItem = new BeanItem<>(data);
        FieldGroup binder = new FieldGroup(entryDataBeanItem);
        binder.bindMemberFields(this);

        Button buttonSave = new Button("Save");
        buttonSave.addClickListener(event -> {
            try {
                binder.commit();
                saveEntryData();
            } catch (FieldGroup.CommitException e) {
                ClientUI.handleException(e);
            }
        });
        layout.addComponent(buttonSave);
        layout.setComponentAlignment(buttonSave, Alignment.BOTTOM_CENTER);

        setCompositionRoot(layout);
    }

    /**
     * sends the entered data to the taboo service and then display all the bookmarks with the tags for the just eneterd
     * bookmark.
     */
    private void saveEntryData() {
        try {
            Collection<String> tags = TagUtil.split(data.getTags());
            taboo.storeNewBookmark(data.bookmark, tags);
            bookmarkTableComponent.showBookmarksWithTags(tags);
        } catch (Exception e) {
            ClientUI.handleException(e);
        }
    }

// -------------------------- INNER CLASSES --------------------------

    /**
     * Data object containing the new entered bookmark data.
     */
    public static class EntryData {
// ------------------------------ FIELDS ------------------------------

        /** the bookmark */
        private String bookmark = "";
        /** String containing the tags */
        private String tags = "";

// --------------------- GETTER / SETTER METHODS ---------------------

        public String getBookmark() {
            return bookmark;
        }

        public void setBookmark(String bookmark) {
            this.bookmark = bookmark;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }
    }
}
