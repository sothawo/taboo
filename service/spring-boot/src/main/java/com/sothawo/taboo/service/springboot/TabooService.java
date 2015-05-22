/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.service.springboot;

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
     * ExceptionHandler for NotFoundException. just returns an empty body with the 404 status code.
     *
     * @return HTTP NOT_FOUND Error Response
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void notFound() {
    }

// --------------------------- main() method ---------------------------

    public static void main(String[] args) {
        SpringApplication.run(TabooService.class, args);
    }
}
