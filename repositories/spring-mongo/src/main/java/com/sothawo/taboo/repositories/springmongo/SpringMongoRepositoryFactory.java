/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.repositories.springmongo;

import com.sothawo.taboo.common.BookmarkRepository;
import com.sothawo.taboo.common.BookmarkRepositoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Factory for SpringMongoRepository instances.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public class SpringMongoRepositoryFactory implements BookmarkRepositoryFactory {

    private final static Logger logger = LoggerFactory.getLogger(SpringMongoRepositoryFactory.class);

    /** Spring application context */
    private ApplicationContext context = null;

    /**
     * create a BookmarkRepository. the first string of the options is interpreted as specifying active Spring profiles
     * value.
     *
     * @param options
     *         options for creating the repository
     * @return the created Repository
     */
    @Override
    public BookmarkRepository createRepository(String[] options) {
        if (null == context) {
            // initialize the context
            if (null != options && options.length > 0) {
                System.getProperties().setProperty("spring.profiles.active", options[0]);
            }
            context = new AnnotationConfigApplicationContext(MongoConfig.class);
            logger.debug("created MongoConfig for database {}", context.getBean(MongoConfig.class).getDatabaseName());
        }
        return context.getBean(SpringMongoRepository.class);
    }
}
