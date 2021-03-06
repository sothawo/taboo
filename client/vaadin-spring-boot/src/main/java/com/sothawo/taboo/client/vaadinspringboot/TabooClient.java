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
     * deletes the given bookmark.
     *
     * @param bookmark
     *         the bookmark to delete
     */
    void deleteBookmark(final Bookmark bookmark);

    /**
     * retrieves all bookmarks with the given tags and containing the search string. when the list of tags is empty, all
     * bookmarks are returned.
     *
     * @param tags
     *         the tags of the bookmarks
     * @param search
     *         the string to search for
     * @return list of bookmarks
     */
    Collection<Bookmark> getBookmarks(final Collection<String> tags, final String search);

    /**
     * get all the tags available in the service.
     *
     * @return
     */
    Collection<String> getTags();

    /**
     * loads the title for an URL from the Service.
     *
     * @param url
     *         the url for which the title shall be loaded
     * @return a bookmark with an url that may be updated and a title
     */
    Bookmark loadTitle(final String url);

    /**
     * stores a new Bookmark in the service.
     *
     * @param bookmark
     *         the bookmark to store
     */
    void storeNewBookmark(final Bookmark bookmark);

    /**
     * updates the given bookmark in the service.
     *
     * @param bookmark
     *         the bookmark to update
     */
    void updateBookmark(Bookmark bookmark);
}
