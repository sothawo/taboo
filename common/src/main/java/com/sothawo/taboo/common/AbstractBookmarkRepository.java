/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.common;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract implementation of the BookmarkRepository interface that just implements some basic methods.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public abstract class AbstractBookmarkRepository implements BookmarkRepository {
// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface BookmarkRepository ---------------------

    /**
     * basic implementation that calls both methods {@link BookmarkRepository#findBookmarksWithTags(Collection,
     * boolean)}
     * and {@link BookmarkRepository#findBookmarksWithSearch(String)} and builds the intersection of both returned
     * collections. Deriving classes might implement a more performant search.
     *
     * @see BookmarkRepository#findBookmarksWithTagsAndSearch(Collection, boolean, String).
     */
    @Override
    public Collection<Bookmark> findBookmarksWithTagsAndSearch(Collection<String> tags, boolean opAnd, String s) {
        if(null == s) {
            return findBookmarksWithTags(tags, opAnd);
        } else if (null == tags) {
            return findBookmarksWithSearch(s);
        } else {
            Set<Bookmark> bookmarksWithTags = new HashSet<>(findBookmarksWithTags(tags, opAnd));
            Set<Bookmark> bookmarksWithString = new HashSet<>(findBookmarksWithSearch(s));
            return Sets.intersection(bookmarksWithTags, bookmarksWithString);
        }
    }
}
