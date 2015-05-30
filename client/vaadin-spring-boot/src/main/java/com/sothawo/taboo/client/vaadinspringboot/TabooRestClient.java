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
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;

/**
 * REST Client for a taboo service.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@SpringComponent
@EnableConfigurationProperties(TabooRestClientConfigurationProperties.class)
public class TabooRestClient implements TabooClient {
// ------------------------------ FIELDS ------------------------------

    /** Logger */
    private final static Logger logger = LoggerFactory.getLogger(TabooClient.class);

    @Autowired
    private TabooRestClientConfigurationProperties properties;

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
        tabooUrl = properties.getHost().getProtocol() + "://" + properties.getHost().getName() + ':'
                + properties.getHost().getPort() + '/' + properties.getContext();
        logger.debug("taboo url: {}", tabooUrl);
    }
}
