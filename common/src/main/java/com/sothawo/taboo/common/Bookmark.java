/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.common;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

/**
 * The bookmark POJO. Tags when added are converted to lowercase and duplicate tags are removed. A Tag has a unique id,
 * which is assigned by the repository, a title, which normally is taken from the website's title, a URL, which is
 * unique and a colleciton of tags.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public class Bookmark {
// ------------------------------ FIELDS ------------------------------

    /** the id of the bookmark */
    private String id;
    /** the title of a bookmark */
    private String title = "";
    /** the URL the bookmark points to as String */
    private String url = "";
    /** the tags of the bookmark */
    private Collection<String> tags = new HashSet<>();

// --------------------- GETTER / SETTER METHODS ---------------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bookmark bookmark = (Bookmark) o;

        return !(url != null ? !url.equals(bookmark.url) : bookmark.url != null);
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * adds the given tag in lowercase to the internal collection, if it is not already present.
     *
     * @param tag
     *         new tag
     * @throws NullPointerException
     *         when tga is null
     */
    public void addTag(String tag) {
        tags.add(Objects.requireNonNull(tag).toLowerCase());
    }

    /**
     * returns an unmodifiable view of the tags collection.
     *
     * @return unmodifiable collection
     */
    public Collection<String> getTags() {
        return Collections.unmodifiableCollection(tags);
    }

    public void setUrl(String url) {
        this.url = Objects.requireNonNull(url);
    }
}
