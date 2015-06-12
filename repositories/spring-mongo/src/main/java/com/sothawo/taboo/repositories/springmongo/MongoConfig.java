/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.repositories.springmongo;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Spring configuration class.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@Configuration
@ComponentScan
@PropertySource("classpath:/springmongo.properties")
@EnableMongoRepositories
public class MongoConfig extends AbstractMongoConfiguration {
// ------------------------------ FIELDS ------------------------------

    public static final String TEST_PROFILE = "test";
    private static final String PROP_BOOKMARKS_DB_PROD = "bookmarksDB.prod";
    private static final String PROP_BOOKMARKS_DB_TEST = "bookmarksDB.test";

    @Autowired
    private Environment env;

// -------------------------- OTHER METHODS --------------------------

    @Override
    protected String getDatabaseName() {
        return env.getProperty(env.acceptsProfiles(TEST_PROFILE) ? PROP_BOOKMARKS_DB_TEST : PROP_BOOKMARKS_DB_PROD);
    }

    /**
     * Mongo Client connecting to localhost on default port.
     *
     * @return Mongo Client
     * @throws Exception
     */
    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient();
    }
}
