/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.repositories.springmongo;

import com.sothawo.taboo.common.Bookmark;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;

/**
 * The interface for the mongo repository to be created by Spring.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public interface MongoBookmarkRepository extends MongoRepository<MongoBookmark, String> {
// -------------------------- OTHER METHODS --------------------------

    /**
     * find all bookmarks with a title that contains the given string.
     *
     * @param s
     *         the string to be searched in the title.
     * @return Collection of found bookmarks
     */
    Collection<MongoBookmark> findByLowerTitleContaining(String s);

    /**
     * find an entry for a given url, the should at most be one
     *
     * @param url
     *         the url to search
     * @return the MongoBookmark if found
     */
    MongoBookmark findByUrl(String url);
}
