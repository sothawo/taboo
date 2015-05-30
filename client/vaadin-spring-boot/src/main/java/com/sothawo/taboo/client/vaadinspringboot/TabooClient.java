/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

/**
 * Implementations of this interface communicate with a taboo service.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public interface TabooClient {
    /**
     * stores a new Bookmark in the service.
     *
     * @param url
     *         the url of the bookmark
     * @param tags
     *         the tags for the bookmark
     */
    void storeNewBookmark(String url, String... tags);
}
