/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.repositories.springmongo;

import com.sothawo.taboo.common.Bookmark;
import com.sothawo.taboo.common.BookmarkRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * BookmarkRepository implementation with a mongodb database accessed by spring-data-mongo.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@Component
public class SpringMongoRepository implements BookmarkRepository {
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
    @Override
    public Bookmark createBookmark(Bookmark bookmark) {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * returns all bookmarks in the repository.
     *
     * @return the bookmarks
     */
    @Override
    public Collection<Bookmark> findAllBookmarks() {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * returns the bookmark for the given id.
     *
     * @param id
     *         id of the bookmark
     * @return the bookmark
     * @throws NotFoundException
     *         if no bookmark is found for the given id
     */
    @Override
    public Bookmark findBookmarkById(String id) {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * returns all bookmarks that have all of the given tags.
     *
     * @param tags
     *         the tags to be searched
     * @param opAnd
     *         if true, the tags are to be combined using AND, otherwise OR
     * @return the bookmarks
     */
    @Override
    public Collection<Bookmark> findBookmarksWithTags(Collection<String> tags, boolean opAnd) {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * removes all bookmarks from the repository.
     */
    @Override
    public void purge() {
//        throw new UnsupportedOperationException("not implemented");
    }
}
