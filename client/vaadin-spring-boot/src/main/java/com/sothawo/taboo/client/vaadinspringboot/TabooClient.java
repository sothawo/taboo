/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import com.sothawo.taboo.common.Bookmark;

import java.util.Collection;

/**
 * Implementations of this interface communicate with a taboo service.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public interface TabooClient {
// -------------------------- OTHER METHODS --------------------------

    /**
     * retrieves all bookmarks with the given tags
     *
     * @param tags
     *         the tags of the bookmarks
     * @return list of bookmarks
     */
    Collection<Bookmark> getBookmarks(Collection<String> tags);

    /**
     * stores a new Bookmark in the service.
     *
     * @param url
     *         the url of the bookmark
     * @param tags
     *         the tags for the bookmark
     */
    void storeNewBookmark(String url, Collection<String> tags);
}
