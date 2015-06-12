/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.repositories.springmongo;

import com.sothawo.taboo.common.AlreadyExistsException;
import com.sothawo.taboo.common.Bookmark;
import com.sothawo.taboo.common.BookmarkRepository;
import com.sothawo.taboo.common.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * BookmarkRepository implementation with a mongodb database accessed by spring-data-mongo.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@Component
public class SpringMongoRepository implements BookmarkRepository {

    /** name of the bookmarks collection */
    public static final String COLLECTION_BOOKMARKS = "bookmarks";

    /** the mongo template for custom queries */
    @Autowired
    private MongoOperations mongo;

    /** the backend repo created by Spring for simple operations */
    @Autowired
    private MongoBookmarkRepository mongoRepository;

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
        if (null == bookmark) {
            throw new IllegalArgumentException("bookmark is null");
        }
        if (null != bookmark.getId()) {
            throw new IllegalArgumentException("is is not null");
        }

        if (null != mongoRepository.findByUrl(bookmark.getUrl())) {
            throw new AlreadyExistsException("bookmark with url: " + bookmark.getUrl());
        }

        return mongoRepository.save(MongoBookmark.fromCommon(bookmark)).toCommon();
    }

    /**
     * returns all bookmarks in the repository.
     *
     * @return the bookmarks
     */
    @Override
    public Collection<Bookmark> findAllBookmarks() {
        return mongoRepository.findAll().stream().map(MongoBookmark::toCommon).collect(Collectors.toList());
    }

    /**
     * returns all tags that are stored in the repository.
     *
     * @return Collection of tags, may be emoty, not null
     */
    @Override
    public Collection<String> findAllTags() {
        // this is ugly, as the distinct method returns an untyped list, at the moment the only way to get the
        // distinct values from mongo
        //noinspection unchecked
        return mongo.getCollection(SpringMongoRepository.COLLECTION_BOOKMARKS).distinct("tags");
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
        MongoBookmark mongoBookmark = mongoRepository.findOne(id);
        if (null == mongoBookmark) {
            throw new NotFoundException("no bookmark with id " + id);
        }
        return mongoBookmark.toCommon();
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
        if (null == tags || tags.size() == 0) {
            return Collections.emptyList();
        }

        List<MongoBookmark> mongoBookmarks = opAnd
                ? mongo.find(query(where("tags").all(tags)), MongoBookmark.class)
                : mongo.find(query(where("tags").in(tags)), MongoBookmark.class);
        return mongoBookmarks.stream().map(MongoBookmark::toCommon).collect(Collectors.toList());
    }

    /**
     * removes all bookmarks from the repository.
     */
    @Override
    public void purge() {
        mongoRepository.deleteAll();
    }
}
