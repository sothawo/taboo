/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import com.vaadin.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * REST Client for a taboo service.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@SpringComponent
public class TabooRestClient implements TabooClient {
// ------------------------------ FIELDS ------------------------------

    /** Logger */
    private final static Logger logger = LoggerFactory.getLogger(TabooClient.class);

    /** base url of the service, injected from confguration */
    @Value("${taboo.service.url}")
    private String tabooUrl;

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface TabooClient ---------------------

    /**
     * @param url
     *         the url of the bookmark
     * @param tags
     */
    @Override
    public void storeNewBookmark(String url, String... tags) {
        throw new UnsupportedOperationException("not implemented");
    }

// -------------------------- OTHER METHODS --------------------------

    @PostConstruct
    public void init() {
        logger.debug("taboo url: {}", tabooUrl);
    }
}
