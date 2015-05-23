/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.repositories;

import com.sothawo.taboo.common.AlreadyExistsException;
import com.sothawo.taboo.common.Bookmark;
import com.sothawo.taboo.common.BookmarkRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * InMemory BookmarkRepository implementation. Just offers functionality, no performance.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public class InMemoryRepository implements BookmarkRepository {
// ------------------------------ FIELDS ------------------------------

    /** id generator */
    private static final AtomicInteger nextId = new AtomicInteger(1);

    /** map for storing the bookmarks */
    private final Map<String, Bookmark> bookmarks = new HashMap<>();

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface BookmarkRepository ---------------------

    @Override
    public Bookmark createBookmark(Bookmark bookmark) {
        if (null == bookmark) {
            throw new IllegalArgumentException("bookmark is null");
        }
        if (null != bookmark.getId()) {
            throw new IllegalArgumentException("is is not null");
        }
        if (bookmarks.containsKey(bookmark.getUrl())) {
            throw new AlreadyExistsException("bookmark url " + bookmark.getUrl());
        }

        bookmark.setId(nextId.getAndIncrement());
        bookmarks.put(bookmark.getUrl(), bookmark);
        return bookmark;
    }

    @Override
    public Collection<Bookmark> findAllBookmarks() {
        return null;
    }

    @Override
    public Bookmark findBookmarkById(int id) {
        return null;
    }

    @Override
    public Collection<Bookmark> findBookmarksWithTags(Collection<String> tags, boolean opAnd) {
        return null;
    }
}
