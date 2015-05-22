/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.common;

import java.util.Collection;

/**
 * Implementations of this class store and retrieve Bookmarks.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public interface BookmarkRepository {
// -------------------------- OTHER METHODS --------------------------

    /**
     * returns all bookmarks in the repository.
     *
     * @return the bookmarks
     */
    Collection<Bookmark> findAllBookmarks();

    /**
     * returns the bookmark for the given id.
     *
     * @param id
     *         id of the bookmark
     * @return the bookmark
     * @throws NotFoundException
     *         if no bookmark is found for the given id
     */
    Bookmark findBookmarkById(int id);

    /**
     * returns all bookmarks that have at least one of the given tags.
     *
     * @param tags
     *         the tags to be searched
     * @return the bookmarks
     */
    Collection<Bookmark> findBookmarksWithAnyTag(Collection<String> tags);

    /**
     * returns all bookmarks that have all of the given tags.
     *
     * @param tags
     *         the tags to be searched
     * @return the bookmarks
     */
    Collection<Bookmark> finpdBookmarksWithAllTags(Collection<String> tags);
}
