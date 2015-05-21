/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * The bookmark POJO.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public class Bookmark {
// ------------------------------ FIELDS ------------------------------

    /** the URL the bookmark points to as String */
    private String url = "";

    /** the tags of the bookmark */
    private Collection tags = new ArrayList();

// --------------------- GETTER / SETTER METHODS ---------------------

    public Collection getTags() {
        return tags;
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

        if (url != null ? !url.equals(bookmark.url) : bookmark.url != null) return false;
        return !(tags != null ? !tags.equals(bookmark.tags) : bookmark.tags != null);
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

// -------------------------- OTHER METHODS --------------------------

    public void setTags(Collection tags) {
        this.tags = Objects.requireNonNull(tags);
    }

    public void setUrl(String url) {
        this.url = Objects.requireNonNull(url);
        ;
    }
}
