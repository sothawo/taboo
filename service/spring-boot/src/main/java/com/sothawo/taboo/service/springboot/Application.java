/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.service.springboot;

import com.sothawo.taboo.common.BookmarkRepository;
import com.sothawo.taboo.repositories.InMemoryRepository;
import com.sothawo.taboo.repositories.InMemoryRepositoryFactory;
import com.sothawo.taboo.repositories.springmongo.SpringMongoRepository;
import com.sothawo.taboo.repositories.springmongo.SpringMongoRepositoryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Main application class.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@SpringBootApplication
public class Application {
// -------------------------- OTHER METHODS --------------------------

    public static BookmarkRepository getInMemoryRepository() {
        return new InMemoryRepositoryFactory().createRepository(null);
    }

    public static BookmarkRepository getSpringMongoRepository() {
        BookmarkRepository repository = new SpringMongoRepositoryFactory().createRepository(null);
        return repository;
    }

// --------------------------- main() method ---------------------------

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
