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

    /** the id of the bookmark */
    private int id;
    /** the URL the bookmark points to as String */
    private String url = "";
    /** the tags of the bookmark */
    private Collection<String> tags = new ArrayList();

// --------------------- GETTER / SETTER METHODS ---------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public String getUrl() {
        return url;
    }

// -------------------------- OTHER METHODS --------------------------

    public void setTags(Collection<String> tags) {
        this.tags = Objects.requireNonNull(tags);
    }

    public void setUrl(String url) {
        this.url = Objects.requireNonNull(url);
        ;
    }
}