/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import com.sothawo.taboo.common.BookmarkBuilder;
import com.sothawo.taboo.common.TagUtil;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

import static com.sothawo.taboo.common.BookmarkBuilder.aBookmark;

/**
 * Component for entering new bookmark data. The first row has the title and tags, the second the url and button to
 * save.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@SpringComponent
public class EntryFormComponent extends CustomComponent {
// ------------------------------ FIELDS ------------------------------

    /** TextField that will bind to a 'bookmark' property */
    @PropertyId("url")
    private TextField url = new TextField("bookmark");

    /** TextField that will bind to a 'title' property */
    @PropertyId("title")// not necessary when TextField is named like property
    private TextField title = new TextField("title");

    /** TextField that will bind to a 'tags' property */
    @PropertyId("tags")// not necessary when TextField is named like property
    private TextField tags = new TextField("tags");

    /** the data object that gets the entered data */
    private EntryData data;

    /** the taboo service where new entries are stored */
    @Autowired
    private TabooClient taboo;

    /** the TableComponent */
    @Autowired
    private BookmarkFilterComponent bookmarkFilterComponent;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * creates a EntryForm. Layout is horizontal.
     */
    public EntryFormComponent() {
        data = new EntryData();
        BeanItem<EntryData> entryDataBeanItem = new BeanItem<>(data);
        FieldGroup binder = new FieldGroup(entryDataBeanItem);
        binder.bindMemberFields(this);

        VerticalLayout vLayout = new VerticalLayout();
        vLayout.setSpacing(true);

        HorizontalLayout hLayoutTop = new HorizontalLayout();
        hLayoutTop.setWidth("100%");
        hLayoutTop.setSpacing(true);

        hLayoutTop.addComponent(title);
        hLayoutTop.addComponent(tags);
        title.setWidth("100%");
        hLayoutTop.setExpandRatio(title, 2);
        tags.setWidth("100%");
        hLayoutTop.setExpandRatio(tags, 1);

        vLayout.addComponent(hLayoutTop);

        HorizontalLayout hLayoutBottom = new HorizontalLayout();
        hLayoutBottom.setWidth("100%");
        hLayoutBottom.setSpacing(true);

        hLayoutBottom.addComponent(url);
        url.setWidth("100%");
        hLayoutBottom.setExpandRatio(url, 1);

        Button buttonSave = new Button("Save");
        buttonSave.addClickListener(event -> {
            try {
                binder.commit();
                saveEntryData();
            } catch (FieldGroup.CommitException e) {
                ClientUI.handleException(e);
            }
        });
        hLayoutBottom.addComponent(buttonSave);
        hLayoutBottom.setComponentAlignment(buttonSave, Alignment.BOTTOM_CENTER);

        vLayout.addComponent(hLayoutBottom);

        setCompositionRoot(vLayout);
    }

    /**
     * sends the entered data to the taboo service and then display all the bookmarks with the tags for the just eneterd
     * bookmark.
     */
    private void saveEntryData() {
        try {
            Collection<String> tags = TagUtil.split(data.getTags());
            BookmarkBuilder bookmarkBuilder = aBookmark().withUrl(data.getUrl()).withTitle(data.getTitle());
            tags.stream().filter(tag -> !tag.isEmpty()).forEach(bookmarkBuilder::addTag);
            taboo.storeNewBookmark(bookmarkBuilder.build());
            bookmarkFilterComponent.setSelectedTags(tags);
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

        /** the title */
        private String title = "";

        /** the bookmark */
        private String url = "";
        /** String containing the tags */
        private String tags = "";

// --------------------- GETTER / SETTER METHODS ---------------------

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
