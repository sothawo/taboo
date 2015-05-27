/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.service.springboot;

import com.sothawo.taboo.common.BookmarkRepository;
import com.sothawo.taboo.repositories.InMemoryRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main application class.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@SpringBootApplication
public class Application {
// -------------------------- OTHER METHODS --------------------------

    @Bean
    public BookmarkRepository getRepository() {
        return new InMemoryRepository();
    }

// --------------------------- main() method ---------------------------

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
