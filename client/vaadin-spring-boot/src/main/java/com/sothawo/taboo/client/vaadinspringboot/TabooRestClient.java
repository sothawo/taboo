/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import com.sothawo.taboo.common.Bookmark;
import com.sothawo.taboo.common.BookmarkBuilder;
import com.vaadin.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import java.util.Collection;

import static com.sothawo.taboo.common.BookmarkBuilder.aBookmark;

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
    public void storeNewBookmark(String url, Collection<String> tags) {
        BookmarkBuilder bookmarkBuilder = aBookmark().withUrl(url);
        tags.stream().filter(s -> !s.isEmpty()).forEach(bookmarkBuilder::addTag);
        Bookmark bookmark = bookmarkBuilder.build();
        RestTemplate rest = new RestTemplate();
        ResponseEntity<Bookmark> response =  rest.postForEntity(tabooUrl + "/bookmarks", bookmark, Bookmark.class);
    }

// -------------------------- OTHER METHODS --------------------------

    @PostConstruct
    public void init() {
        tabooUrl = properties.getHost().getProtocol() + "://" + properties.getHost().getName() + ':'
                + properties.getHost().getPort() + '/' + properties.getContext();
        logger.debug("taboo url: {}", tabooUrl);
    }
}
