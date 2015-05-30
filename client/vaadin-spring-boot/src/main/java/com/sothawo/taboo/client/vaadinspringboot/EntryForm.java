/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Page;
import com.vaadin.ui.*;

/**
 * Component for entering new bookmark data.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public class EntryForm extends CustomComponent {
// ------------------------------ FIELDS ------------------------------

    /** TextField that will to a 'bookmark' property */
    @PropertyId("bookmark") // not necessary when TextField is named like property
    private TextField bookmark = new TextField("bookmark");

    /** TextField that will bind to a 'tags' property */
    @PropertyId("tags")
    private TextField tags = new TextField("tags");

    /** the data object that gets the entered data */
    private EntryData data;
// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * creates a EntryForm. Layout is horizontal
     */
    public EntryForm() {
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
                handleException(e);
            }
        });
        layout.addComponent(buttonSave);
        layout.setComponentAlignment(buttonSave, Alignment.BOTTOM_CENTER);

        setCompositionRoot(layout);
    }

    /**
     * shows the message of an exception in a notification
     *
     * @param e
     *         Exception
     */
    private void handleException(Exception e) {
        Notification notification = new Notification("Error", e.getMessage(), Notification.Type.ERROR_MESSAGE);
        notification.setDelayMsec(-1);
        notification.show(Page.getCurrent());
    }

    /**
     * sends the entered data to the taboo service.
     */
    private void saveEntryData() {
        handleException(new UnsupportedOperationException("not yet implemented."));
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
