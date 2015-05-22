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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Collection;
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
     * @return all bookmarks
     */
    @RequestMapping(value = "/bookmark", method = RequestMethod.GET)
    public Collection<Bookmark> findAllBookmarks() {
        return repository.findAllBookmarks();
    }

    /**
     * returns the bookmark with the given id
     *
     * @param id
     *         id of the bookmark
     * @return Bookmark
     * @throws NotFoundException
     *         when no Bookmarks is found for the id
     */
    @RequestMapping(value = "/bookmark/{id}", method = RequestMethod.GET)
    public Bookmark findBookmarkById(@PathVariable(value = "id") Integer id) {
        return repository.findBookmarkById(id);
    }

    /**
     * ExceptionHandler for NotFoundException.
     * @return HTTP NOT_FOUND Error Response
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Error> notFound() {
        Error error = new Error();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

// --------------------------- main() method ---------------------------

    public static void main(String[] args) {
        SpringApplication.run(TabooService.class, args);
    }
}
