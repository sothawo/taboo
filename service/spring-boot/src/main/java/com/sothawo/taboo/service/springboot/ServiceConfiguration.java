/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.service.springboot;

import com.sothawo.taboo.common.BookmarkRepository;
import com.sothawo.taboo.repositories.InMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@Configuration
public class ServiceConfiguration {
    @Bean
    public BookmarkRepository getRepository() {
        return new InMemoryRepository();
    }
}
