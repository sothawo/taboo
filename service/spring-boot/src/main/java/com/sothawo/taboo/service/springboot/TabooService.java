/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.service.springboot;

import com.sothawo.taboo.common.AlreadyExistsException;
import com.sothawo.taboo.common.Bookmark;
import com.sothawo.taboo.common.BookmarkRepository;
import com.sothawo.taboo.common.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Spring-Boot Service implementation for the taboo backend service.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@RestController // contains @ResponseBody
@RequestMapping("/taboo")
public class TabooService {
// ------------------------------ FIELDS ------------------------------

    private static final String OP_OR = "or";
    private static final String OP_AND = "and";

    /** the repository for the bookmarks, injected in constructor or explicitly set in no-arg-ctor */
    private BookmarkRepository repository;

// --------------------------- CONSTRUCTORS ---------------------------

    public TabooService() {
        repository = Application.getSpringMongoRepository();
    }

    public TabooService(BookmarkRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * ExceptionHandler for AlreadyExistsException. returns the exception's error message in the body with the 409
     * status code.
     *
     * @return HTTP CONFLICT Response Status and error message
     */
    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String alreadyExistsExceptionHandler(AlreadyExistsException e) {
        return e.getMessage();
    }

    /**
     * creates a new bookmark in the repository
     *
     * @param bookmark
     *         new bookmark to be created
     * @return the created bookmark
     * @throws IllegalArgumentException
     *         when bookmark has it's id set
     */
    @RequestMapping(value = "/bookmarks", method = RequestMethod.POST)
    public ResponseEntity<Bookmark> createBookmark(@RequestBody Bookmark bookmark, UriComponentsBuilder ucb) {
        if (null == bookmark) {
            throw new IllegalArgumentException("bookmark must not be null");
        }
        if (null != bookmark.getId()) {
            throw new IllegalArgumentException("id must not be set");
        }

        Bookmark createdBookmark = repository.createBookmark(bookmark);
        HttpHeaders headers = new HttpHeaders();
        URI locationUri = ucb
                .path("/taboo/bookmarks/")
                .path(String.valueOf(createdBookmark.getId()))
                .build().toUri();
        headers.setLocation(locationUri);
        return new ResponseEntity<>(createdBookmark, headers, HttpStatus.CREATED);
    }

    /**
     * deletes a bookmark from the repository
     *
     * @param id
     *         id of the bookmark to delete
     * @throws NotFoundException
     *         when no Bookmarks is found for the id
     */
    @RequestMapping(value = "/bookmarks/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookmarkById(@PathVariable(value = "id") String id) {
        repository.deleteBookmark(id);
    }

    /**
     * ExceptionHandler for IllegalArgumentException. returns the exception's error message in the body with the 412
     * status code.
     *
     * @return HTTP PRECONDITION_FAILED Response Status and error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public String exceptionHandlerIllegalArgumentException(IllegalArgumentException e) {
        return e.getMessage();
    }

    /**
     * ExceptionHandler for NotFoundException. returns the exception's error message in the body with the 404 status
     * code.
     *
     * @return HTTP NOT_FOUND Response Status and error message
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String exceptionHandlerNotFoundException(NotFoundException e) {
        return e.getMessage();
    }

    /**
     * gets all the bookmarks from the repository
     *
     * @param tags
     *         optional list of tags
     * @param op
     *         if "or", tags are combined with OR, otherwise with AND
     * @return all bookmarks
     */
    @RequestMapping(value = "/bookmarks", method = RequestMethod.GET)
    public Collection<Bookmark> findAllBookmarks(@RequestParam(value = "tag", required = false) List<String> tags,
                                                 @RequestParam(value = "op", defaultValue = OP_AND) String op) {
        return (null != tags)
                ? repository.findBookmarksWithTags(tags, !OP_OR.equalsIgnoreCase(op))
                : repository.findAllBookmarks();
    }

    /**
     * return all tags from the repository.
     *
     * @return collection of tags
     */
    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public Collection<String> findAlltags() {
        return repository.findAllTags();
    }

    /**
     * returns the bookmark with the given id.
     *
     * @param id
     *         id of the bookmark
     * @return Bookmark
     * @throws NotFoundException
     *         when no Bookmarks is found for the id
     */
    @RequestMapping(value = "/bookmarks/{id}", method = RequestMethod.GET)
    public Bookmark findBookmarkById(@PathVariable(value = "id") String id) {
        return repository.findBookmarkById(id);
    }
}
