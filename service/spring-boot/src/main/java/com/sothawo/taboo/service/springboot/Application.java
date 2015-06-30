/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.service.springboot;

import com.sothawo.taboo.common.BookmarkRepository;
import com.sothawo.taboo.repositories.InMemoryRepositoryFactory;
import com.sothawo.taboo.repositories.springmongo.SpringMongoRepositoryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class. Utility class with static methods.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@SpringBootApplication
public class Application {
// -------------------------- STATIC METHODS --------------------------

    /**
     * creates an InMemory Repository.
     *
     * @return Repository
     */
    public static BookmarkRepository getInMemoryRepository() {
        return new InMemoryRepositoryFactory().createRepository(null);
    }

    /**
     * creates a Spring Mongo Repository.
     *
     * @return Repository
     */
    public static BookmarkRepository getSpringMongoRepository() {
        BookmarkRepository repository = new SpringMongoRepositoryFactory().createRepository(null);
        return repository;
    }

// --------------------------- main() method ---------------------------

    /**
     * starts the application.
     *
     * @param args
     *         arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
