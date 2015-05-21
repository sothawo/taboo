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
    /** the URL the bookmark points to as String */
    private String url = "";

    /** the tags of the bookmark */
    private Collection tags = new ArrayList();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = Objects.requireNonNull(url);;
    }

    public Collection getTags() {
        return tags;
    }

    public void setTags(Collection tags) {
        this.tags = Objects.requireNonNull(tags);
    }
}
