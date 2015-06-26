/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.repositories.springmongo;

import com.sothawo.taboo.common.Bookmark;
import com.sothawo.taboo.common.BookmarkBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;

import static com.sothawo.taboo.common.BookmarkBuilder.aBookmark;

/**
 * Bookmark class for the mongo db.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@Document(collection = SpringMongoRepository.COLLECTION_BOOKMARKS)
public class MongoBookmark {
// ------------------------------ FIELDS ------------------------------

    /** unique db id */
    @Id
    private String id;

    /** bookmark's url */
    private String url;

    /** bookmark's title */
    private String title;

    /** bookmark's title in lowercase for searching */
    @Indexed
    private String lowerTitle;

    /** bookmark's tags */
    @Indexed
    private Collection<String> tags = new LinkedHashSet<>();

// -------------------------- STATIC METHODS --------------------------

    /**
     * creates a MongoBookmark from a common Bookmark.
     *
     * @param bookmark
     *         the common bookmark
     * @return the MongoBookmark
     */
    public static MongoBookmark fromCommon(Bookmark bookmark) {
        MongoBookmark mongoBookmark = new MongoBookmark();
        mongoBookmark.setId(bookmark.getId());
        mongoBookmark.setUrl(bookmark.getUrl());
        String title = Optional.ofNullable(bookmark.getTitle()).orElse("");
        mongoBookmark.setTitle(title);
        mongoBookmark.setLowerTitle(title.toLowerCase());
        mongoBookmark.getTags().addAll(bookmark.getTags());
        return mongoBookmark;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLowerTitle() {
        return lowerTitle;
    }

    public void setLowerTitle(String lowerTitle) {
        this.lowerTitle = lowerTitle;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public void setTags(Collection<String> tags) {
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

// -------------------------- OTHER METHODS --------------------------

    /**
     * converts this object to a common bookmark.
     *
     * @return common Bookmark object
     */
    public Bookmark toCommon() {
        BookmarkBuilder bookmarkBuilder = aBookmark().withId(id).withUrl(url).withTitle(title);
        tags.stream().forEach(bookmarkBuilder::addTag);
        return bookmarkBuilder.build();
    }
}
