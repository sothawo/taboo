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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Spring-Boot Service implementation for the taboo backend service.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@SpringBootApplication
@RestController // contains @ResponseBody
@RequestMapping("/taboo")
public class TabooService {
// ------------------------------ FIELDS ------------------------------

    /** the repository for the bookmarks */
    private BookmarkRepository repository;

// --------------------------- CONSTRUCTORS ---------------------------

    @Autowired
    public TabooService(BookmarkRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

// -------------------------- OTHER METHODS --------------------------

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
    @ResponseStatus(HttpStatus.CREATED)
    public Bookmark createBookmark(@RequestBody Bookmark bookmark) {
        if (null == bookmark || null != bookmark.getId()) {
            throw new IllegalArgumentException("id must not be set");
        }
        return repository.createBookmark(bookmark);
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
                                                 @RequestParam(value = "op", defaultValue = "and") String op) {
        if (null != tags) {
            return repository.findBookmarksWithTags(tags, !"or".equalsIgnoreCase(op));
        } else {
            return repository.findAllBookmarks();
        }
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
    public Bookmark findBookmarkById(@PathVariable(value = "id") Integer id) {
        return repository.findBookmarkById(id);
    }

    /**
     * ExceptionHandler for IllegalArgumentException. returns the exception's error message in the body with the 412
     * status code.
     *
     * @return HTTP PRECONDITION_FAILED Response Status and error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public String illegalArgumentException(IllegalArgumentException e) {
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
    public String notFoundExceptionHandler(NotFoundException e) {
        return e.getMessage();
    }
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

// --------------------------- main() method ---------------------------

    public static void main(String[] args) {
        SpringApplication.run(TabooService.class, args);
    }
}
