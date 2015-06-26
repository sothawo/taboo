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
     * creates a bookmark in the repository.
     *
     * @param bookmark
     *         the new bookmark. must not have the id set
     * @return the created bookmark with it's id
     * @throws IllegalArgumentException
     *         if the id is set in bookmark
     * @throws AlreadyExistsException
     *         if a bookmark with the given url already exists
     */
    Bookmark createBookmark(Bookmark bookmark);

    /**
     * deletes the bookmark with the given id.
     *
     * @param id
     *         id of the bookmark to delete
     * @throws NotFoundException
     *         if no bookmark is found for the given id
     */
    void deleteBookmark(String id);

    /**
     * returns all bookmarks in the repository.
     *
     * @return the bookmarks
     */
    Collection<Bookmark> findAllBookmarks();

    /**
     * returns all tags that are stored in the repository.
     *
     * @return Collection of tags, may be emoty, not null
     */
    Collection<String> findAllTags();

    /**
     * returns the bookmark for the given id.
     *
     * @param id
     *         id of the bookmark
     * @return the bookmark
     * @throws NotFoundException
     *         if no bookmark is found for the given id
     */
    Bookmark findBookmarkById(String id);

    /**
     * returns all bookmarks that have all of the given tags.
     *
     * @param tags
     *         the tags to be searched
     * @param opAnd
     *         if true, the tags are to be combined using AND, otherwise OR
     * @return the found bookmarks
     */
    Collection<Bookmark> findBookmarksWithTags(Collection<String> tags, boolean opAnd);

    /**
     * returns the bookmarks where the title contains the given string.
     *
     * @param title
     *         the title substring to search
     * @return the found bookmarks
     */
    Collection<Bookmark> findBookmarksWithTitle(String title);

    /**
     * removes all bookmarks from the repository.
     */
    void purge();
}
