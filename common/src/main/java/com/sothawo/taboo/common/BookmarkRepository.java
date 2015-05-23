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
     * returns all bookmarks that have all of the given tags.
     *
     * @param tags
     *         the tags to be searched
     * @param opAnd
     *         if true, the tags are to be combined using AND, otherwise OR
     * @return the bookmarks
     */
    Collection<Bookmark> findBookmarksWithTags(Collection<String> tags, boolean opAnd);

    /**
     * creates a bookmark in the repository.
     *
     * @param bookmark
     *         the new bookmark. must not have the id set
     * @return the created bookmark with it's id
     * @throws IllegalArgumentException
     *         if the id is set in bookmark
     */
    Bookmark createBookmark(Bookmark bookmark);
}
