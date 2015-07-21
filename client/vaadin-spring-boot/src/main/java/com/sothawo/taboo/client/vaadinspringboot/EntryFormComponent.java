/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import com.sothawo.taboo.common.Bookmark;
import com.sothawo.taboo.common.BookmarkBuilder;
import com.sothawo.taboo.common.TagUtil;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.teemu.VaadinIcons;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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

    private static final Logger logger = LoggerFactory.getLogger(EntryFormComponent.class);

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
        FieldGroup binder = new FieldGroup(new BeanItem<>(data));
        binder.bindMemberFields(this);

        VerticalLayout vLayout = new VerticalLayout();
        vLayout.setSpacing(true);

        HorizontalLayout hLayoutTop = new HorizontalLayout();
        hLayoutTop.setWidth("100%");
        hLayoutTop.setSpacing(true);

        hLayoutTop.addComponent(url);
        url.setWidth("100%");
        hLayoutTop.setExpandRatio(url, 1);

        // a button to load the title
        Button buttonLoad = new Button(VaadinIcons.DOWNLOAD_ALT);
        buttonLoad.addStyleName(ValoTheme.BUTTON_TINY);
        hLayoutTop.addComponent(buttonLoad);
        hLayoutTop.setComponentAlignment(buttonLoad, Alignment.BOTTOM_CENTER);
        buttonLoad.addClickListener(clickEvent -> downloadTitle());

        vLayout.addComponent(hLayoutTop);

        HorizontalLayout hLayoutBottom = new HorizontalLayout();
        hLayoutBottom.setWidth("100%");
        hLayoutBottom.setSpacing(true);

        hLayoutBottom.addComponent(title);

        hLayoutBottom.addComponent(tags);
        title.setWidth("100%");
        hLayoutBottom.setExpandRatio(title, 2);
        tags.setWidth("100%");
        hLayoutBottom.setExpandRatio(tags, 1);

        Button buttonSave = new Button("Save");
        buttonSave.addClickListener(event -> {
            try {
                binder.commit();
                saveEntryData();
                binder.clear();
                data.setId(null);
            } catch (FieldGroup.CommitException e) {
                ClientUI.handleException(e);
            }
        });
        hLayoutBottom.addComponent(buttonSave);
        hLayoutBottom.setComponentAlignment(buttonSave, Alignment.BOTTOM_CENTER);

        Button buttonClear = new Button("Clear");
        buttonClear.addClickListener(clickEvent -> {
            binder.clear();
            data.setId(null);
        });
        hLayoutBottom.addComponent(buttonClear);
        hLayoutBottom.setComponentAlignment(buttonClear, Alignment.BOTTOM_CENTER);

        vLayout.addComponent(hLayoutBottom);

        setCompositionRoot(vLayout);
    }

    /**
     * tries to download the title for the actual url.
     */
    private void downloadTitle() {
        String urlString = url.getValue();
        if (null != urlString && !urlString.isEmpty()) {
            if (!urlString.startsWith("http")) {
                urlString = "http://" + urlString;
            }
            final String finalUrl = urlString;
            logger.debug("loading title for url {}", finalUrl);
            CompletableFuture.supplyAsync(() -> {
                try {
                    String htmlTitle = Jsoup.connect(finalUrl).timeout(3000).get().title();
                    logger.debug("got title: {}", htmlTitle);
                    return htmlTitle;
                } catch (IOException e) {
                    UI.getCurrent().access(() -> ClientUI.handleException(e));
                    return "";
                }
            }).thenAccept(htmlTitle -> UI.getCurrent().access(() -> {
                // because the UI is annotated with @Push, after the call to UI.access() the server will
                // push the data to the client.
                title.setValue(htmlTitle);
                url.setValue(finalUrl);
            }));
        }
    }

    /**
     * sends the entered data to the taboo service and then display all the bookmarks with the tags for the just entered
     * bookmark.
     */
    private void saveEntryData() {
        try {
            Collection<String> tags = TagUtil.split(data.getTags());
            BookmarkBuilder bookmarkBuilder =
                    aBookmark().withId(data.getId()).withUrl(data.getUrl()).withTitle(data.getTitle());
            tags.stream().filter(tag -> !tag.isEmpty()).forEach(bookmarkBuilder::addTag);
            Bookmark bookmark = bookmarkBuilder.build();
            if(null == bookmark.getId()) {
                taboo.storeNewBookmark(bookmark);
            } else {
                taboo.updateBookmark(bookmark);
            }
            bookmarkFilterComponent.setSelectedTags(tags);
        } catch (Exception e) {
            ClientUI.handleException(e);
        }
    }

    /**
     * shows the bookmark data to be potentially edited.
     * @param bookmark
     */
    public void showBookmark(Bookmark bookmark) {
        getUI().access(() -> {
            data.setId(bookmark.getId());
            url.setValue(bookmark.getUrl());
            title.setValue(bookmark.getTitle());
            tags.setValue(bookmark.getTags().stream().collect(Collectors.joining(" ")));
        });
    }

// -------------------------- INNER CLASSES --------------------------

    /**
     * Data object containing the new entered bookmark data.
     */
    public static class EntryData {
// ------------------------------ FIELDS ------------------------------

        /** the id */
        private String id = null;

        /** the title */
        private String title = "";
        /** the bookmark */
        private String url = "";
        /** String containing the tags */
        private String tags = "";

// --------------------- GETTER / SETTER METHODS ---------------------

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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
