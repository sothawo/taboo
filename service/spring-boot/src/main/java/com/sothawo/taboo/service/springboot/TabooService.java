/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.service.springboot;

import com.sothawo.taboo.common.Bookmark;
import com.sothawo.taboo.common.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

// --------------------------- main() method ---------------------------

    public static void main(String[] args) {
        SpringApplication.run(TabooService.class, args);
    }
}
